package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * The Condition object contains an expression, a relative operator, and another
 * expression. The object defines a boolean expression which
 * can evaluate itself.
 * 	cond -> expr relop expr
 * 	relop -> = | <> | < | > | <= | >=
 *
 * @author Montek Kalsi
 * @version 10/29/19
 */
public class Condition extends Expression
{
    private String operator;
    private Expression expr1;
    private Expression expr2;

    /**
     * Constructor for the condition setting instance variables.
     * @param e1
     * @param relOperator
     * @param e2
     */
    public Condition(String relOperator, Expression e1, Expression e2)
    {
        expr1 = e1;
        expr2 = e2;
        operator = relOperator;
    }

    /**
     * Evaluates this condition object. returns 1 if the boolean expression is
     * true, 0 otherwise. The grammar is in the class comments.
     * @param env the environment where it is evaluated.
     * @return 1 if the expression is true
     */
    public int eval(Environment env)
    {
        int value1 = expr1.eval(env);
        int value2 = expr2.eval(env);
        boolean stmt;

        if (this.operator.equals("="))
            stmt = value1 == value2;
        else if (this.operator.equals("<>"))
            stmt = value1 != value2;
        else if (this.operator.equals("<"))
            stmt = value1 < value2;
        else if (this.operator.equals(">"))
            stmt = value1 > value2;
        else if (this.operator.equals("<="))
            stmt = value1 <= value2;
        else
            stmt = value1 >= value2;

        if (stmt)
            return 1;
        return 0;
    }

    /**
     * Compiles the condition by first compiling the two expressions and storing them in $t0 and
     * $v0. Then, a branch statement is emitted corresponding to the operator. For example, beq
     * is emitted if the operator is =, <>, and so on.
     * @param e The emitter that emits statements to the file
     * @param target The target label that the branch statement goes to when false
     */
    public void compile(Emitter e , String target)
    {
        expr1.compile(e);
        e.emitPush("$v0");
        expr2.compile(e);
        e.emitPop("$t0");
        if (operator.equals("="))
        {
            e.emit("bne $t0, $v0, " + target+ "\t#the following expression will run"
                    + " if $t0 is equal to $v0");
        }
        else if (operator.equals("<>"))
        {
            e.emit("beq $t0, $v0, " + target + "\t#the following expression will run"
                    + " if $t0 is not equal to $v0");
        }
        else if (operator.equals(">"))
        {
            e.emit("ble $t0, $v0, " + target + "\t#the following expression will run"
                    + " if $t0 is greater than $v0");
        }
        else if (operator.equals("<"))
        {
            e.emit("bge $t0, $v0, " + target + "\t#the following expression will run"
                    + " if $t0 is less than $v0");
        }
        else if (operator.equals("<="))
        {
            e.emit("bgt $t0, $v0, " + target + "\t#the following expression will run"
                    + " if $t0 is less than or equal to $v0");
        }
        else
        {
            e.emit("blt $t0, $v0, " + target + "\t#the following expression will run"
                    + " if $t0 is greater than or equal to $v0");
        }

    }
}
