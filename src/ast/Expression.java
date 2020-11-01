package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * The abstract class Expression models an AST Parser expression, with the abstract method
 * eval required in all classes that extend it. eval evaluates the current
 * expression.
 *
 * @author Montek Kalsi
 * @version 10/29/19
 */
public abstract class Expression
{
    /**
     * Empty constructor for the Expression
     */
    public Expression()
    {
    }

    /**
     * Method for evaluating in environments.
     * @param var1 the environment passed in
     * @return an integer for the expression evaluation
     */
    public abstract int eval(Environment var1);

    /**
     * Compiles the current expression. If not overridden, this method will throw a
     * runtime exception.
     * @param e The emitter that emits statements to the file
     */
    public void compile(Emitter e)
    {
        throw new RuntimeException("Implement me!!!!!");
    }
}
