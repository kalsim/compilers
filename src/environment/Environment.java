package environment;

import java.util.Hashtable;
import java.util.List;

import ast.ProcedureDeclaration;
import ast.Statement;

import scanner.ScanErrorException;

/**
 * The Environment class maps variable names to values
 * in a Hashtable map. Procedure names are also mapped to ProcedureDeclaration
 * objects. Environments can have parent Environments.
 *
 * @author Montek Kalsi
 * @version 11/12/19
 */
public class Environment
{
    private Hashtable<String,Integer> table;
    private Hashtable<String,ProcedureDeclaration> procedures;
    private Environment parent;

    /**
     * Default constructor for an Environment which initializes table,
     * procedures, and parent
     */
    public Environment()
    {
        table = new Hashtable<String,Integer>();
        procedures = new Hashtable<String,ProcedureDeclaration>();
        parent = null;
    }

    /**
     * Constructor for an environment with a parent p
     * @param p the parent environment passed in
     */
    public Environment(Environment p)
    {
        parent = p;
        table = new Hashtable<String,Integer>();
        procedures = new Hashtable<String,ProcedureDeclaration>();
    }

    /**
     * If there is a parent and variable isn't in the current
     * Environment, then its value is set in the parent.
     * Otherwise, it is declared here in this Environment.
     * @param variable the passed in variable
     * @param value the passed in value
     */
    public void setVariable(String variable, int value)
    {
        if (parent!=null && !table.contains(variable))
            parent.setVariable(variable, value);
        else
            declareVariable(variable,value);
    }

    /**
     * setProcedure calls itself until it reaches the global Environment
     * with a null parent. It then adds the procedure
     * to the procedures instance variable Hashtable.
     * @param variable id of the procedure
     * @param decl declaration associated with the procedure
     */
    public void setProcedure(String variable, ProcedureDeclaration decl)
    {
        if (parent != null)
        {
            parent.setProcedure(variable, decl);
        }
        else
        {
            procedures.put(variable,decl);
        }
    }

    /**
     * If the variable is in this Environment, then its corresponding
     * value is returned. Otherwise, getVariable is called on the
     * parent.
     * @param variable the variable's value being checked
     * @return the correspondng variable's value
     */
    public int getVariable(String variable)
    {
        if (table.keySet().contains(variable))
            return table.get(variable);
        else
            return parent.getVariable(variable);
    }

    /**
     * Set the parent environment.
     * @param p the replacement environment.
     */
    public void setParent(Environment p)
    {
        parent = p;
    }
    /**
     * Adds a variable to store its value in the Hashtable
     * in the Environment.
     * @param variable the added variable
     * @param value the corresponding added value
     */
    public void declareVariable(String variable, int value)
    {
        table.put(variable,value);
    }

    /**
     * Returns the listed procedure in the global Environment.
     * @param variable the passed in variable
     * @return the listed ProcedureDeclaration
     */
    public ProcedureDeclaration getProcedure(String variable)
    {
        if (parent != null)
            return parent.getProcedure(variable);
        return procedures.get(variable);
    }

}