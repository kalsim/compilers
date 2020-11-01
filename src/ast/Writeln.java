package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * Writeln is a class that extends Statement, initializing its instance variable
 * Expression exp. In the exec method, it prints the given expression.
 *
 * @author Montek Kalsi
 * @version 10/29/19
 */
public class Writeln extends Statement
{
    private Expression exp;

    /**
     * Sets exp to e passed in
     * @param e the Expression passed in
     */
    public Writeln(Expression e)
    {
        exp = e;
    }

    /**
     * Executes by printing the evaluated Expression
     * @param env the Environment passed in
     */
    public void exec(Environment env)
    {
        System.out.println(exp.eval(env));
    }

    /**
     * Compiles the Writeln statement by compiling the given expression.
     * Then, compile emits code to print the value and a new line.
     * @param e The emitter that emits statements to the file
     */
    public void compile(Emitter e)
    {
        exp.compile(e);
        e.emit("move $a0,$v0\t#moves the number from $v0 to $a0 to be printed");
        e.emit("li $v0, 1");
        e.emit("syscall\t#prints the number that was in $v0");
        e.emit("la $a0, newline\t#loads the new line address in $a0 to be printed");
        e.emit("li $v0, 4");
        e.emit("syscall\t#prints out a new line");
    }
}
