package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * The Variable class represents a variable in an ast parser which
 * contains an evaluate method that returns the variable's value and a constructor that
 * initializes the variable's name.
 *
 * @author Montek Kalsi
 * @version 10/29/19
 */
public class Variable extends Expression
{
    private String name;

    /**
     * The constructor with name n passed in
     * @param n the name
     */
    public Variable(String n)
    {
        name = n;
    }

    /**
     * Outputs the name stored
     * @return the name as a string
     */
    public String getName()
    {
        return name;
    }

    /**
     * Evaluates in Environment env passed in and outputs name
     *
     * @param env the Environment
     */
    public int eval(Environment env)
    {
        return env.getVariable(name);
    }

    /**
     * Compiles the variable by storing the value of the given word in $v0.
     * The  the address of the variable is loaded into $t0. It then is loaded into
     * $v0.
     * @param e The emitter that emits statements to the file
     */
    public void compile (Emitter e)
    {
        e.emit("la $t0, " + name);
        e.emit("lw $v0, ($t0) \t#loads value of " + name + " into $v0");
    }
}
