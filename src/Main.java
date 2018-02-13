import java.util.Vector;
import it.unical.mat.wrapper.FactResult;


public class Main {
    static DeductiveDatabase database;
    static Vector<FactResult>  results;
    static AnswerSet answerSet;


    public static void presentaAnswerSet(Vector<FactResult> anAnswerSet) {
        FactResult factResult;
        int i;
        //
        System.out.println(" == ANSWER SET ====");
        i = 0;
        while( i < anAnswerSet.size())
        {
            factResult = anAnswerSet.get(i);
            System.out.println(factResult);
            i = i + 1;
        }//end while
        System.out.println("================");
    }//end presentaAnswerSet



    public static void main(String args[])  {

        database = new DeductiveDatabase("/Users/tony/Documents/dlv/dlv.bin");
        database.load("/Users/tony/Documents/dlv/introductionFacts.txt");
        database.load("/Users/tony/Documents/dlv/introductionRules.txt");
        results = database.getAnswerSet();
        answerSet = new AnswerSet(results);
        System.out.println(answerSet);

    }//end main

}//end ProjectDeductiveDatabase
