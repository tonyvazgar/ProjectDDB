import it.unical.mat.wrapper.DLVInvocation;
import it.unical.mat.wrapper.FactHandler;
import it.unical.mat.wrapper.FactResult;
import java.util.Vector;

public class Answer implements FactHandler
{
    Vector<FactResult> answerSet;


    public Answer()
    {
        answerSet = new Vector<FactResult>();
    }//end constructor


    public void handleResult(DLVInvocation obsd, FactResult factResult)
    {
        if (!answerSet.contains(factResult))
            answerSet.add(factResult);
        //end if
    }//end  handleResult



    public Vector<FactResult> getAnswerSet()
    {
        return answerSet;
    }//end getAnswerSet

}//end Answer
