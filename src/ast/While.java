package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * Objects of the while loop class execute the Statement object
 * while the condition object (cond) is true.
 *
 * @author Montek Kalsi
 * @version 10/29/19
 */
public class While extends Statement
{
    private Condition con;
    private Statement s;

    /**
     * The while constructor which passes in the condition and statement.
     *
     * @param condition the while condition
     * @param statement the Statement object passed in
     */
    public While(Condition condition, Statement statement)
    {
        con = condition;
        s = statement;
    }

    /**
     * Executes by evaluating the statement in env
     * @param env the Environment
     */
    public void exec(Environment env)
    {
        while (con.eval(env)==1)
        {
            s.exec(env);
        }
    }

    /**
     * Converts the code for a While loop to MIPS.
     * @param e the Emitter that prints MIPS code.
     */
    public void compile(Emitter e)
    {
        String temp = "loop" + e.next();
        String etemp = "endloop" + e.endnext();
        e.emit(temp + ":");
        con.compile(e, etemp);
        s.compile(e);
        e.emit("j " + temp);
        e.emit(etemp + ":");
    }
}
