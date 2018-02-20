import java.util.Vector;

public class Dato
{
    String predicate;
    Vector<String> arguments;


    public Dato(String aName)
    {
        predicate = aName;
        arguments = new Vector<String>();
    }//end constructor



    public void addArgument(String anArgument)
    {
        arguments.add(anArgument);
    }//end addArgument


    public String getPredicate()
    {
        return predicate;
    }//end getPredicate


    public int getArity()
    {
        return arguments.size();
    }//end getPredicate


    public String getArgument(int i)
    {
        return arguments.get(i);
    }//end getPredicate


}//end Dato