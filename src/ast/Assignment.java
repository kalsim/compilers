package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * Class for an Assignment object consisting of a variable and the expression that
 * the variable is assigned to. The object executes itself
 * by inputting the variable and corresponding value into the environment.
 *
 * @author Montek Kalsi
 * @version 10/29/19
 */
public class Assignment extends Statement
{
    private String variable;
    private Expression expr;

    /**
     * Constructor for assigning instance variables
     * @param var the variable
     * @param exp the expression
     */
    public Assignment(String var, Expression exp)
    {
        variable = var;
        expr = exp;
    }

    /**
     * Executes the assignment by adding the variable with the corresponding
     * value to the environment env
     *
     * @param env the environment to add the variable
     */
    public void exec(Environment env)
    {
        env.setVariable(variable, expr.eval(env));
    }

    /**
     * Compiles this assignment by  compiling the expression and then storing the value of
     * $v0 with the value of the expression into the word corresponding with the given
     * variable
     * @param e The emitter that emits statements to the file
     */
    public void compile(Emitter e)
    {
        expr.compile(e);
        e.emit("sw $v0, " + variable + " \t #assigns the value of the expression(in $v0) to " + variable);
    }
}
