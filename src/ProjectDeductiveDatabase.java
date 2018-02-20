import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import it.unical.mat.dlv.program.Term;
import it.unical.mat.wrapper.FactResult;


public class ProjectDeductiveDatabase {
	static DeductiveDatabase database;
	static Vector<FactResult>  answerSet;



	public static Vector<Dato> getDatos(Vector<FactResult>  answerSet) {
		Vector<Dato> datos;
		Dato dato;
		FactResult factResult;
		int i;
		int j;
        List<Term> lista;
        String predicate;
        StringTokenizer tokenizer;
		//
		datos = new Vector<Dato>();
		i = 0;
		while(i < answerSet.size()) {
			factResult = answerSet.get(i);
			lista = factResult.attributes();
            if(lista.size() == 0)       //predicado sin atributos (como iAmHungry)
                predicate = factResult.toString();
            else {
                tokenizer = new StringTokenizer(factResult.toString(), "(");
                predicate = tokenizer.nextToken();
            }//end else
			dato = new Dato(predicate);
            if(lista.size() != 0) {
                j = 0;
                while(j < lista.size()) {
                    dato.addArgument(lista.get(j).toString());
                    j = j + 1;
                }//end while
            }//end if
            datos.add(dato);
			i = i + 1;
		}//end while

		return datos;
	}//end getDatos




	public static void main(String args[]) {
        Vector<Dato> datos;
        int i;
        Dato dato;
        //
		database = new DeductiveDatabase("/Users/tony/Documents/dlv/dlv.bin");
        database.load("/Users/tony/Documents/dlv/introductionFacts.txt");
        database.load("/Users/tony/Documents/dlv/introductionRules.txt");
		answerSet = database.getAnswerSet();
        //database.show();

		Fact fact;

		fact = new Fact("man");
		fact.addArgument("gerardo");
		database.addFact(fact);


		fact = new Fact("likes");
		fact.addArgument("maria");
		fact.addArgument("gerardo");
		database.addNegationFact(fact);

		answerSet = database.getAnswerSet();
        //database.show();

        fact = new Fact("man");
        fact.addArgument("gerardo");
        //database.removeFact(fact);

		fact = new Fact("man");
		fact.addArgument("gerardo");
		System.out.println(database.isTrue(fact));

        fact = new Fact("likes");
        fact.addArgument("maria");
        fact.addArgument("gerardo");
        database.removeNegationFact(fact);

        answerSet = database.getAnswerSet();
        database.show();

        datos = getDatos(answerSet);

        /*
        dato = datos.get(8);
        System.out.println(dato.getPredicate());
        i = 0;
        while( i < dato.getArity()) {
            System.out.println(dato.getArgument(i));
            i = i + 1;
        }//end while
        */

        //Lista de los nobodyLikesHim
        int him = 0;
        while(him < datos.size()){
            String elDato = datos.get(him).getPredicate();
            if(elDato.equals("nobodyLikesHim")){
                System.out.println("Pobrecito " + datos.get(him).getArgument(0));
            }
            him = him + 1;
        }

        //Lista de los que quieren una cita
        for(Dato elDato:datos){
            if(elDato.getPredicate().equals("dateProposal")){
                System.out.println();
                System.out.print("Proponemos una cita de ");
                System.out.print(elDato.getArgument(0));
                System.out.print(" con ");
                System.out.print(elDato.getArgument(1));

                System.out.println();
                System.out.print("Proponemos una cita de ");
                System.out.print("Tony");
                System.out.print(" con ");
                System.out.print("Alina");
            }
        }
	}//end main

}//end ProjectDeductiveDatabase




