import it.unical.mat.wrapper.DLVInputProgram;
import it.unical.mat.wrapper.DLVInputProgramImpl;
import it.unical.mat.wrapper.DLVInvocation;
import it.unical.mat.wrapper.DLVWrapper;
import it.unical.mat.wrapper.FactResult;
import it.unical.mat.dlv.program.Rule;
import java.util.Collections;
import java.util.Vector;
import java.io.*;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * @author Gerardo Ayala
 * @version 4.0 Build December 2017.
 */
public class DeductiveDatabase
{
	// the answer set
	private Vector<FactResult> answerSet;

	// the program in DLV
	private DLVInputProgram inputProgram;

	// lists of new facts and rules
	private Vector<String> newFactsList;

	// list of facts to remove
	private Vector<String> factsToRemoveList;

	// the path of the DLV executable 
	private String dlvPath;

	// the path of the program
	//private String programFilePath;

	// a string that helps to tokenize 
	// the arguments of a fact
	private String argumentsString;

	// file management attributes
	private boolean isReadingOnly;
	private File file;
	private OutputStream outputStream;
	private FileReader fileReader;
	private BufferedReader bufferedReader;
	private boolean eof;


	// constructor
	public DeductiveDatabase(String aDlvPath)
	{
		dlvPath = aDlvPath;
		inputProgram = new DLVInputProgramImpl();
		newFactsList = new Vector<String>();
		factsToRemoveList = new Vector<String>();
	}//end constructor


	// to indicate a file where we have facts to include
	public void load(String aFilePath)
	{
		inputProgram.addFile(aFilePath);
		//programFilePath = aFilePath;
	}//end load



	// get the new added facts
	public Vector<String> getNewFactsList()
	{
		return newFactsList;
	}//end getNewFactsList



	// get the answer set
	public Vector<FactResult> getAnswerSet()
	{
		final DLVInvocation invocation;
		final Answer factHandler;
		//--------------------

		factHandler = new Answer();
		invocation=DLVWrapper.getInstance().createInvocation(dlvPath);
		try
		{
			invocation.setInputProgram(inputProgram);
			invocation.setNumberOfModels(1);
			invocation.subscribe(factHandler);
			invocation.run();
			invocation.waitUntilExecutionFinishes();
		}//end try
		catch(Exception e)
		{
			System.out.println("Ha habido una excepci?n:" + e);
		}//end catch
		answerSet = factHandler.getAnswerSet();
		return answerSet;
	}//end getAnswerSet


	private boolean comparePredicates(String aPredicate, String otherPredicate)
	{
		String p1, p2;
		boolean areEqual;
		StringTokenizer tokenizer;
		String token;
		//------------
		areEqual = false;
		p1 = "";
		p2 = "";

		if(aPredicate.equals(otherPredicate))
			areEqual = true;
			//end if
		else
		{
			tokenizer = new StringTokenizer(aPredicate);
			while(tokenizer.hasMoreElements())
			{
				token = tokenizer.nextToken();
				p1 = p1 + token;
			}//end while

			tokenizer = new StringTokenizer(otherPredicate);
			while(tokenizer.hasMoreElements())
			{
				token = tokenizer.nextToken();
				p2 = p2 + token;
			}//end while

			if(p1.equals(p2))
				areEqual = true;
			//end if
		}//end else

		return areEqual;
	}//end comparePredicates



	// determines if a predicate is true
	public boolean isTrue(String aPredicate)
	{
		String factString;
		int i;
		FactResult aFact;
		boolean isTrue;
		//--------------

		isTrue = false;
		if(answerSet!=null)
		{
			i = 0;
			while( (i<answerSet.size()) && !isTrue)
			{
				aFact = answerSet.elementAt(i);
				factString = aFact.toString();
				isTrue = comparePredicates(factString,aPredicate);
				//end if
				i = i + 1;
			}//end while
		}//end if
		return isTrue;
	}//end isTrue






	public void addFact(Fact aFact)
	{
		Rule newExpression;
		//-------------------

		if(aFact.isLegal)
		{
			newExpression = new Rule(aFact.toString());
			inputProgram.addExpression(newExpression);
			answerSet = getAnswerSet();
			if(!newFactsList.contains(aFact.toString()))
				newFactsList.add(aFact.toString());
			//end if
		}//end if
	}//end addFact



	// to add a negation fact (-fact)
	public void addNegationFact(Fact aFact)
	{
		Rule newExpression;
		//-------------------

		if(aFact.isLegal)
		{
			newExpression = new Rule("-"+ aFact.toString());
			inputProgram.addExpression(newExpression);
			answerSet = getAnswerSet();
			if(!newFactsList.contains("-"+ aFact.toString()))
				newFactsList.add("-"+ aFact.toString());
			//end if
		}//end if
	}//end addNegationFact




	// to remove a fact from the database
	public void removeFact(Fact aFact)
	{
		Rule anExpression;
		//------------------

		anExpression = new Rule(aFact.toString());
		inputProgram.removeExpression(anExpression);
		answerSet = getAnswerSet();
		if(newFactsList.contains(aFact.toString()))
		{
			newFactsList.remove(aFact.toString());
		}//end if
		factsToRemoveList.add(aFact.toString());
	}//end removeFact





	// to remove a negation fact (-fact)
	public void removeNegationFact(Fact aFact)
	{
		Rule anExpression;
		//----------------

		anExpression = new Rule("-" + aFact.toString());
		inputProgram.removeExpression(anExpression);
		answerSet = getAnswerSet();
		if(newFactsList.contains("-" + aFact.toString()))
		{
			newFactsList.remove("-" + aFact.toString());
		}//end if
		factsToRemoveList.add("-" + aFact.toString());
	}//end removeNegationFact


	// to get the name of a predicate
	private String getFunctor(String aFact)
	{
		int index;
		//------------

		if(aFact.contains("("))
		{
			index = aFact.indexOf('(');
			return aFact.substring(0,index);
		}//end if
		else
			return aFact.substring(0,aFact.length()-1);

	}//end getFunctor



	private String firstCharAsString(String aString)
	{
		return aString.substring(0,1);
	}//end getFirstCharAsString



	private String getNextArgument()
	{
		int posicionDeSiguienteComa;
		int posicionDeSiguienteParentesisCerrado;
		int posicionTerminoDelArgumento;
		String anArgument;
		//--------------------

		anArgument = null;
		if(argumentsString.length()!=0)
		{
			if(firstCharAsString(argumentsString).equals(","))
				argumentsString = argumentsString.substring(1,argumentsString.length());
			//end if
			if(firstCharAsString(argumentsString).equals(" "))
				argumentsString = argumentsString.substring(1,argumentsString.length());
			//end if
			//definimos el final del argumento
			posicionDeSiguienteParentesisCerrado = argumentsString.indexOf(")");
			posicionDeSiguienteComa = argumentsString.indexOf(",");
			// si ya no hay m?s argumentos
			if(posicionDeSiguienteComa == -1)
			{
				posicionTerminoDelArgumento = argumentsString.indexOf(")");
				anArgument = argumentsString.substring(0,posicionTerminoDelArgumento);
			}//end if
			else
			// hay m?s argumentos
			{
				posicionTerminoDelArgumento = argumentsString.indexOf(",");
				anArgument = argumentsString.substring(0,posicionTerminoDelArgumento);
			}//end else
		}//end if
		return anArgument;
	}//end getNextArgument


	// get all the facts of the answer set
	// as a Vector of String objects
	public Vector<Fact> getFactList(String functor)
	{
		Vector<Fact> factList;
		int i;
		String theFactString;
		Fact theFact;
		String anArgument;
		String comillas;
		char caracterComillas;
		int argumentsPosition;
		int longitudDelArgumento;
		//-------------------------

		factList = new Vector<Fact>();
		caracterComillas = '"';
		comillas = new String();
		comillas = "" + caracterComillas;

		i = 0;
		answerSet = getAnswerSet();
		while(i<answerSet.size())
		{
			theFactString = answerSet.get(i).toString();
			if (functor.equals(getFunctor(theFactString)))
			{
				theFact = new Fact(functor);
				argumentsPosition = theFactString.indexOf("(") + 1;
				argumentsString = theFactString.substring(argumentsPosition,theFactString.length());

				anArgument = getNextArgument();
				while(anArgument != null)
				{
					// incluye el argumento
					longitudDelArgumento = anArgument.length();
					//checa si el argumento es una cadena

					if(firstCharAsString(anArgument).equals(comillas))
					{
						anArgument = anArgument.substring(1,anArgument.length()-1);
						theFact.addStringArgument(anArgument);
					}//end if
					else
					{
						theFact.addArgument(anArgument);
					}//end if then else

					// Despu?s de incluir el argumento redefine el resto de argumentos
					argumentsString = argumentsString.substring(longitudDelArgumento, argumentsString.length());
					if(argumentsString.equals(")"))
					{
						argumentsString = "";
					}//end if
					anArgument = getNextArgument();
				}//end while
				argumentsString = null;
				factList.add(theFact);
			}//end if
			i = i + 1;
		}//end while
		Collections.sort(factList);
		return factList;
	}//end getFactList




	// construye y abre el archivo
	private void create(String aFilePath)
	{
		isReadingOnly = false;
		try
		{
			file = new File(aFilePath);
			if (file.createNewFile())
			{
				/// si no existe el archivo, lo crea
			}//end if
			// se ABRE el archivo al crearse el flujo de salida
			outputStream = new FileOutputStream(file);
		}//en try
		catch(IOException excepcion)
		{
			System.out.println(excepcion);
		}//en catch
	}//end create




	// abre el archivo si ya existe
	public void open(String aFilePath)
	{
		String linea;
		FileInputStream inputStream;
		int numeroDeLineas;
		//-------------------

		isReadingOnly = true;
		file = new File(aFilePath);
		try
		{
			// se ABRE el archivo al crearse el flujo de entrada
			inputStream = new FileInputStream(file);
			// creo el lector del archivo que permitir? leer CARACTERES
			// (lecturas de 2 bytes)


			fileReader = new FileReader(aFilePath);
			// creo el buffer de lectura,
			// que es quien LEERA el archivo
			bufferedReader = new BufferedReader(fileReader);
			// inicio mi EOF (end Of File) como false
			eof=false;

			// Hasta ahora
			// YA abrimos el archivo!!!!
			// y ya podemos leerlo

			// determino cu?ntas l?neas tiene el archivo
			// leyendo todo el archivo, l?nea por l?nea

			// Inicio mi contador
			numeroDeLineas = 0;
			// mientras no sea el fin de archivo
			while(!eof)
			{
				// leo una l?nea el archivo
				linea = leeLinea();
				// incremento mi contador de l?neas
				numeroDeLineas = numeroDeLineas + 1;
			}//end while
			// ahora me encuentro AL FINAL DEL ARCHIVO
			// por ello...
			// re-inicio mi fileReader y tambi?n mi bufferedReader
			// para regresar al INICIO del archivo
			try
			{
				// creo otra vez el lector del archivo
				fileReader = new FileReader(aFilePath);
				// creo otra vez el buffer de lectura
				bufferedReader = new BufferedReader(fileReader);
				// regreso a false el indicador de fin de archivo
				eof=false;
			}//en try
			catch(FileNotFoundException excepcion)
			{
				System.out.println(excepcion);
			}//en catch

		}//en try
		catch(FileNotFoundException excepcion)
		{
			System.out.println("NO se encontr? el archivo!!!");
		}//en catch
	}//end open



	// to read a line from a text file
	private String leeLinea()
	{
		// caracter separador de nueva linea
		char delimitador;
		// caracter separador de "return" o salto
		char salto;

		String linea;
		char caracter;
		int entero;

		// caracter separador de nueva linea
		delimitador = '\n';
		// caracter separador de "return" o salto
		salto = '\r';

		// si no hay nada en el reng?n del archivo
		// el m?todo regresa la cadena "nil"
		// inicio con estos valores ya que linea + caracter = "nil"
		// en la primera iteraci?n del ciclo
		linea = "%%";
		caracter = '%';

		// inicio el ciclo
		do
		{
			try
			{
				// si el caracter no es salto de rengl?n
				if (caracter != salto)
				{
					// concateno el caracter a la linea
					linea = linea + caracter;
				}//endif

				// leo 2 bytes (UN CARACTER) del archivo
				// y debo indicar que los interpreto como un char
				caracter = (char) bufferedReader.read();

				// interpreto el caracter le?do como int
				// para checar si es el n?mero que indica fin de archivo
				entero = (int) caracter;
				//el n?mero 65535 indica FIN DE ARCHIVO
				// (64 x 1024) - 1
				// o sea, 16 bits en "1"
				// que son los siguientes 2 bytes: "11111111 11111111"
				if (entero == 65535)
				{
					eof = true;
				}//end if
			}//end try
			catch(Exception excepcion)
			{
				eof = true;
			}//end catch
		}//end do
		// continuar? el ciclo mientras
		// el caracter no sea el delimitador
		// y no lleguemos al fin de archivo (End Of File)
		while ((caracter!=delimitador) && (eof == false));

		// la l?nea tiene 4 bytes m?s, 3 del "nil" y uno del \r
		// Si la l?nea tiene 4 o m?s caracteres, es que tiene datos
		if ((linea.length() >= 4))
		{
			// hay contenido, por lo que le quito el "nil" inicial
			// elimino los 3 primerso caracteres.
			// selecciono una subcadena de la linea
			// de la cuarta posici?n (3) en adelante.
			linea = linea.substring(3);
		}//end if
		// regreso la linea de caracteres le?dos
		return linea;
	}//end read



	private String readString()
	{
		return leeLinea();
	}//end readString



	private void escribeLinea(String cadena)
	{
		// se escribir? un arreglo de BYTES
		byte arregloSalida[];

		// se obtienen los bytes de la cadena (String)
		arregloSalida = cadena.getBytes();
		// se escribe el arreglo, byte por byte
		for (int i=0; i<arregloSalida.length; i++)
		{
			try
			{
				// escribimos el i?simo byte
				outputStream.write(arregloSalida[i]);
			}//end try
			catch(Exception excepcion)
			{
				System.out.println(excepcion);
			}//end catch
		}//end for
	}//end escribeLinea



	private void writeString(String cadena)
	{
		cadena = cadena+"\n";;
		escribeLinea(cadena);
		eof = false;
	}//end writeString


	private void close()
	{
		try
		{
			// si solamente leemos, cerramos lo referente a la lectura
			if (isReadingOnly)
			{
				// cierro el fileReader y el bufferedReader
				bufferedReader.close();
				fileReader.close();
			}//end if
			else
				// cerramos lo referente a la escritura
				outputStream.close();
			//end else
		}//end try
		catch(IOException excepcion)
		{
			System.out.println(excepcion);
		}//end catch
	}//end close




}//end class DeductiveDatabase
