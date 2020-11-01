package ast;

import emitter.Emitter;
import environment.Environment;

/**
 * Class for the If statement that allows the parser to evaluate an if statement. It
 * initializes the instance variables for an if statement, such as the condition, the statement
 * for then, and the statement for else.
 *
 * @author Montek Kalsi
 * @version 10/29/19
 *
 */
public class If extends Statement
{
    private Condition condition;
    private Statement statement;
    private Statement elses;

    /**
     * Constructor for an if loop which initializes the condition and the statement when
     * the condition is true. This is an alternate constructor for the case that there
     * is no else, so "elses" is set to null
     * @param condition The condition that will be evaluated. If true, then statement will be run
     * @param stmts The statement that will be executed if the condition is true
     */
    public If(Condition condition, Statement stmts)
    {
        this.condition = condition;
        statement = stmts;
        elses = null;
    }

    /**
     * Constructor for an if loop which initializes the condition, the statement for the case
     * that the condition is true, and the condition that the statement is false.
     * @param condition The condition that will be evaluated. If true, then statement will be run;
     * else, the "elses" statement will be run
     * @param stmts The statement that will be executed if the condition is true
     * @param elses The statement that will be executed if the condition is false
     */
    public If(Condition condition, Statement stmts, Statement elses)
    {
        this.condition = condition;
        statement = stmts;
        this.elses = elses;
    }

    /**
     * Evaluates the if statement by running the then statement if the condition is true. If
     * it is false, it runs the "elses" statement if the if statement has an ELSE
     * @param env The given environment
     * @throws scanner.ScanErrorException if the parser encounters an invalid statement
     * or expression
     * @throws java.io.IOException if there is any other error
     */
    public void exec(Environment env)
    {
        if (elses != null)
        {
            if (condition.eval(env) == 1)
            {
                statement.exec(env);
            }
            else
            {
                elses.exec(env);
            }
        }
        else
        {
            if (condition.eval(env) == 1)
            {
                statement.exec(env);
            }
        }
    }

    /**
     * Compiles the if statement by compiling the condition and then statements.
     * @param e The emitter that emits statements to the file
     */
    public void compile(Emitter e)
    {
        int id = e.nextLabelID();
        String label = "endif" + id;
        String finish = "finish" + id;
        condition.compile(e, label);
        statement.compile(e);
        if (elses!=null)
        {
            e.emit("j " + finish);
            e.emit(label + ":");
            elses.compile(e);
            e.emit(finish + ":" );
        }
        else
        {
            e.emit(label + ":");
        }
    }
}