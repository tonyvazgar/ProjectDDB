
import it.unical.mat.wrapper.FactResult;
import it.unical.mat.dlv.program.Term;
import java.util.List;
import java.util.Vector;

/**
 * Created by gerardoayala on 12/14/17.
 */
public class AnswerSet extends Vector<Atom>
{

    public AnswerSet(Vector<FactResult> results)
    {
        int i;
        int j;
        FactResult factResult;
        List<Term> attributes;
        Atom atom;
        String predicate;
        int index;
        //
        i = 0;
        while (i < results.size())
        {
            factResult = results.get(i);
            if(factResult.toString().contains("("))
            {
                index = factResult.toString().indexOf('(');
                predicate = factResult.toString().substring(0,index);
            }//end if
            else
            {
                predicate = factResult.toString();
            }//end else
            atom = new Atom();
            atom.setPredicate(predicate);
            attributes = factResult.attributes();
            if(attributes.size() > 0)
            {
                j = 0;
                while(j < attributes.size())
                {
                    atom.addTerm(attributes.get(j).toString());
                    j = j + 1;
                }//end while
            }//end if
            add(atom);
            i = i + 1;
        }//end while
    }//end constructor
}//end AnswerSet
