

import java.util.Vector;
import it.unical.mat.wrapper.FactResult;


public class ProjectDeductiveDatabase
{
	static DeductiveDatabase database;
	static Vector<FactResult>  answerSet;


	public static void main(String args[])
	{
		database = new DeductiveDatabase("/Users/tony/Documents/dlv/dlv.bin");
        database.load("/Users/tony/Documents/dlv/introductionFacts.txt");
        database.load("/Users/tony/Documents/dlv/introductionRules.txt");
		answerSet = database.getAnswerSet();
        database.show();

		Fact fact;

		fact = new Fact("man");
		fact.addArgument("gerardo");
		database.addFact(fact);


		fact = new Fact("likes");
		fact.addArgument("maria");
		fact.addArgument("gerardo");
		database.addNegationFact(fact);

		answerSet = database.getAnswerSet();
        database.show();

        fact = new Fact("man");
        fact.addArgument("gerardo");
        database.removeFact(fact);

		fact = new Fact("man");
		fact.addArgument("gerardo");
		System.out.println(database.isTrue(fact));

        fact = new Fact("likes");
        fact.addArgument("maria");
        fact.addArgument("gerardo");
        database.removeNegationFact(fact);

        answerSet = database.getAnswerSet();
        database.show();

	}//end main

}//end ProjectDeductiveDatabase




