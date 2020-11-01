package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * The abstract class Statement has an abstract method exec that
 * is required in all classes that extend this Statement, which has
 * the purpose of executing itself (the current statement).
 *
 * @author Montek Kalsi
 * @version 10/29/19
 */
public abstract class Statement
{
    /**
     * Empty constructor for a statement object
     */
    public Statement()
    {
    }

    /**
     * An abstract method to execute the Statement using the passed in
     * environment var1
     *
     * @param env the environment passed in
     */
    public abstract void exec(Environment env);

    /**
     * Compiles the current statement. If not overridden, this method will throw a
     * runtime exception.
     * @param e The emitter that emits statements to the file
     */
    public void compile(Emitter e)
    {
        throw new RuntimeException("Implement me!!!!!");
    }
}
