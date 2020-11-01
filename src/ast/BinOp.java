package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * The BinOp class consists of an expression, an operator, and
 * another expression. It evaluates the arithmetic
 * expressions represented by exp1 op exp2 and then
 * outputs the equivalent value.
 *
 * @author Montek Kalsi
 * @version 10/29/19
 */
public class BinOp extends Expression
{
    private String op;
    private Expression exp1;
    private Expression exp2;

    /**
     * Constructor for BinOp assigning instance variables
     * for operations.
     * @param op the operator
     * @param exp1 the first expression
     * @param exp2 the second expression
     */
    public BinOp(String op, Expression exp1, Expression exp2)
    {
        this.op = op;
        this.exp1 = exp1;
        this.exp2 = exp2;
    }

    /**
     * Evaluates the following arithmetic expressions and returns
     * the equivalent values.
     * 		exp1 - exp2
     * 		exp1 + exp2
     * 		exp1 / exp2
     * 		exp1 * exp2
     * 		var = exp2 (for which the method returns 0)
     * @param env the environment where they are evaluated
     */
    public int eval(Environment env)
    {
        if (op == "-")
            return exp1.eval(env) - exp2.eval(env);
        else if (op == "+")
            return exp1.eval(env) + exp2.eval(env);
        else if (op == "/")
            return exp1.eval(env) / exp2.eval(env);
        else if (op == "*")
            return exp1.eval(env) * exp2.eval(env);
        else
        {
            env.setVariable(((Variable)exp1).getName(), exp2.eval(env));
            return 0;
        }
    }

    /**
     * Compiles the binop expression by compiling the two expressions. Then, the emitter
     * emits code corresponding to the operation given, like using addu
     * to add two numbers together.
     * @param e The emitter that emits statements to the file
     */
    public void compile(Emitter e)
    {
        if (op.equals("+"))
        {
            exp1.compile(e);
            e.emitPush("$v0");
            exp2.compile(e);
            e.emitPop("$t0");
            e.emit("addu $v0, $v0, $t0\t#adds $t0(exp1) and $v0(exp2)");
        }
        if (op.equals("-"))
        {
            exp1.compile(e);
            e.emitPush("$v0");
            exp2.compile(e);
            e.emitPop("$t0");
            e.emit("subu $v0, $v0, $t0\t#subtracts $t0(exp1) and $v0(exp2)");
        }
        if (op.equals("*"))
        {
            exp1.compile(e);
            e.emitPush("$v0");
            exp2.compile(e);
            e.emitPop("$t0");
            e.emit("mult $t0, $v0");
            e.emit("mflo $v0\t#multiplies $t0(exp1) and $v0(exp2)");
        }
        if (op.equals("/"))
        {
            exp1.compile(e);
            e.emitPush("$v0");
            exp2.compile(e);
            e.emitPop("$t0");
            e.emit("div $t0, $v0");
            e.emit("mflo $v0\t#divides $t0(exp1) and $v0(exp2)");
        }

    }
}