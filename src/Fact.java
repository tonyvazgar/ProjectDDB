import java.util.Vector;

public class Fact implements Comparable
{
    String functor;
    Vector<String> arguments;
    int arity;
    boolean isLegal;

    public Fact(String elNombre)
    {
        isLegal = true;
        if(isLegalAtom(elNombre))
        {
            if (!startsWithUppercase(elNombre))
            {
                functor = elNombre;
                arguments = 	new Vector<String>();
                arity = 0;
            }//end if
            else
            {
                isLegal = false;
                System.out.println("The fact name " + elNombre + " must be legal.");
            }//end else
        }//end if
        else
        {
            isLegal = false;
            System.out.println("The fact name " + elNombre + " must be legal.");
        }//end else
    }//end constructor



    private boolean isLegalAtom(String anArgument)
    {
        boolean legal;
        int i;
        String theCharacter;

        i = 0;
        legal = true;
        while (i < anArgument.length())
        {
            theCharacter = anArgument.substring(i, i+1);
            if(
                    (theCharacter.equals("-")) ||
                            (theCharacter.equals(" ")) ||
                            (theCharacter.equals("=")) ||
                            (theCharacter.equals("!")) ||
                            (theCharacter.equals("'")) ||
                            (theCharacter.equals("$")) ||
                            (theCharacter.equals("%")) ||
                            (theCharacter.equals("&")) ||
                            (theCharacter.equals("/")) ||
                            (theCharacter.equals("?")) ||
                            (theCharacter.equals("?")) ||
                            (theCharacter.equals("?")) ||
                            (theCharacter.equals("@")) ||
                            (theCharacter.equals("#")) ||
                            (theCharacter.equals("^")) ||
                            (theCharacter.equals("*")) ||
                            (theCharacter.equals("+")) ||
                            (theCharacter.equals("[")) ||
                            (theCharacter.equals("]")) ||
                            (theCharacter.equals("{")) ||
                            (theCharacter.equals("}")) ||
                            (theCharacter.equals(".")) ||
                            (theCharacter.equals(",")) ||
                            (theCharacter.equals(";")) ||
                            (theCharacter.equals("<")) ||
                            (theCharacter.equals(">")) ||
                            (theCharacter.equals(":"))
                    )
            {
                legal = false;
            }//end if
            i = i + 1;
        }//end while
        return legal;
    }//end isLegalAtom



    private boolean startsWithUppercase(String anArgument)
    {
        String firstLetter;

        firstLetter = anArgument.substring(0,1);
        if(
                (firstLetter.equals("A")) ||
                        (firstLetter.equals("B")) ||
                        (firstLetter.equals("C")) ||
                        (firstLetter.equals("D")) ||
                        (firstLetter.equals("E")) ||
                        (firstLetter.equals("F")) ||
                        (firstLetter.equals("G")) ||
                        (firstLetter.equals("H")) ||
                        (firstLetter.equals("I")) ||
                        (firstLetter.equals("J")) ||
                        (firstLetter.equals("K")) ||
                        (firstLetter.equals("L")) ||
                        (firstLetter.equals("M")) ||
                        (firstLetter.equals("N")) ||
                        (firstLetter.equals("?")) ||
                        (firstLetter.equals("O")) ||
                        (firstLetter.equals("P")) ||
                        (firstLetter.equals("Q")) ||
                        (firstLetter.equals("R")) ||
                        (firstLetter.equals("S")) ||
                        (firstLetter.equals("T")) ||
                        (firstLetter.equals("U")) ||
                        (firstLetter.equals("V")) ||
                        (firstLetter.equals("W")) ||
                        (firstLetter.equals("X")) ||
                        (firstLetter.equals("Y")) ||
                        (firstLetter.equals("Z"))
                )
        {
            return true;
        }
        else
            return false;
        //end if

    }//end startsWithUppercase

    public void addArgument(int integerArgument)
    {
        String theArgument;
        Integer integerObject;

        integerObject = new Integer(integerArgument);
        theArgument = integerObject.toString();
        arguments.add(theArgument);
        arity = arity + 1;
    }//end addArgument


    public void addArgument(String anArgument)
    {
        if(!startsWithUppercase(anArgument))
        {
            if(isLegalAtom(anArgument))
            {
                arguments.add(anArgument);
                arity = arity + 1;
            }//end if
            else
            {
                isLegal = false;
                System.out.println("The argument " + anArgument + " must be legal.");
            }//end else
        }//end if
        else
        {
            isLegal = false;
            System.out.println("The argument " + anArgument + " must be legal.");
        }//end else
    }//end addArgument


    public void addStringArgument(String anArgument)
    {
        String theArgument;
        char comillas;

        comillas = '"';
        theArgument = comillas + anArgument + comillas;
        arguments.add(theArgument);
        arity = arity + 1;
    }//end addStringArgument



    public void addFactArgument(Fact anArgument)
    {
        String theArgument;

        theArgument = anArgument.toString();
        theArgument = theArgument.substring(0,theArgument.length()-1);
        arguments.add(theArgument);
        arity = arity + 1;
    }//end addArgument


    public int compareTo(Object anObject)
    {
        Fact otherFact;
        String stringThisFact;
        String stringOtherfact;

        otherFact = (Fact) anObject;
        stringThisFact = toString();
        stringOtherfact = otherFact.toString();
        return stringThisFact.compareTo(stringOtherfact);
    }//end compareTo


    public String toString()
    {
        String cadenaDeArgumentos;
        int i;

        if (isLegal)
        {
            if(arity !=0)
            {
                cadenaDeArgumentos="";
                i=0;
                while (i<arity)
                {
                    cadenaDeArgumentos = cadenaDeArgumentos + arguments.get(i) + ",";
                    i = i + 1;
                }//end while
                cadenaDeArgumentos = cadenaDeArgumentos.substring(0,cadenaDeArgumentos.length()-1);
                return functor + "(" + cadenaDeArgumentos + ").";
            }//end if
            else
                return functor + ".";
        }//end if
        else
            return "";
        //end if then else
    }//end toString

}//end class Fact
