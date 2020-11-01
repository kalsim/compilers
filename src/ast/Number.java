package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * the Number class defines an integer. Evaluating the
 * Number will simply return the integer value.
 *
 * @author Montek Kalsi
 * @version 10/29/19
 */
public class Number extends Expression
{
    private int value;

    /**
     * Constructs a Number object and assigns its value
     * @param val the int value passed in
     */
    public Number(int val)
    {
        value = val;
    }

    /**
     * Outputs the value of the number
     * @return the int stored
     */
    public int getValue()
    {
        return value;
    }

    /**
     * It outputs the value of the number
     * @param env the environment passed in
     * @return outputs the value of the number
     */
    public int eval(Environment env)
    {
        return value;
    }

    /**
     * Compiles the number by storing it in $v0.
     * @param e The emitter that emits statements to the file
     */
    public void compile(Emitter e)
    {
        e.emit("li $v0," + value + "\t #sets $v0 to " + value);
    }
}
