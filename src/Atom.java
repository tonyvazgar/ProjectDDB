import java.util.Vector;

/**
 * Created by gerardoayala on 12/14/17.
 */
public class Atom
{
    private String predicate;
    private Vector<String> terms;
    //

    public Atom()
    {
        predicate = "";
        terms = new Vector<String>();
    }//end constructor


    public void setPredicate(String aPredicate)
    {
        predicate = aPredicate;
    }//end setPredicate


    public void addTerm(String aTerm)
    {
        terms.add(aTerm);
    }//end addTerm


    public void addAtomTerm(String aTerm)
    {
        terms.add(aTerm);
    }//end addAtomTerm


    public void addStringTerm(String aTerm)
    {
        String term;
        //
        term = "\"" + aTerm + "\"";
        terms.add(term);
    }//end addStringTerm


    public void addIntTerm(int aNumericalTerm)
    {
        String term;
        Integer integer;
        //
        integer = new Integer(aNumericalTerm);
        term = integer.toString();
        terms.add(term);
    }//end addIntTerm


    public String getPredicate()
    {
        return predicate;
    }//end getPredicate


    public int getArity()
    {
        return terms.size();
    }//end getPredicate


    public String getTermAt(int index)
    {
        String term;
        //
        if ( (terms.size() == 0) || (index >= terms.size()) || (index < 0))
        {
            term = null;
        }//end if
        else
        {
            term = terms.get(index);
        }//end else
        return term;
    }//end getTermAt


    public String toString()
    {
        String string;
        int i;
        //
        string = predicate + "(";
        i = 0;
        while(i < terms.size())
        {
            string = string + terms.get(i) + ",";
            i = i + 1;
        }//end while
        string = string.substring(0,string.length()-1);
        string = string + ")";
        return string;
    }//end toString



}//end class