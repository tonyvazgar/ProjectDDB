

import it.unical.mat.wrapper.DLVInputProgram;
import it.unical.mat.wrapper.DLVInputProgramImpl;
import it.unical.mat.wrapper.DLVInvocation;
import it.unical.mat.wrapper.DLVWrapper;
import it.unical.mat.wrapper.FactResult;
import it.unical.mat.dlv.program.Rule;
import java.util.Vector;


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




    // determines if a predicate is true
    public boolean isTrue(Fact aPredicate)
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
                factString = aFact.toString() + ".";
                if(factString.equals(aPredicate.toString()))
                {
                    isTrue = true;
                    break;
                }//end if
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



	////////////////////////////////////////////




    public void show()
    {
        FactResult factResult;
        int i;
        //
        System.out.println(" == ANSWER SET ====");
        i = 0;
        while( i < answerSet.size())
        {
            factResult = answerSet.get(i);
            System.out.println(factResult);
            i = i + 1;
        }//end while
        System.out.println("================");
    }//end presentaAnswerSet

	/////////////////////////////////////////////////////////////



}//end class DeductiveDatabase
