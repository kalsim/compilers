package ast;

import java.util.List;

import emitter.Emitter;
import environment.Environment;

/**
 * The ProcedureDeclaration class is used for declaring procedures prior
 * to the program beginning to store them in the environment's map. Once
 * stored, they can  be used for each ProcedureCall in statements afterwards.
 *
 * @author Montek Kalsi
 * @version 1/14/20
 */
public class ProcedureDeclaration extends Statement
{
    private String id;
    private Statement stmt;
    private Environment env;
    private List<String> args;
    private List<String> vars;

    /**
     * Instantiates a ProcedureDeclaration object storing its name as
     * a key, a main statement,and a list of arguments.
     * @param s the ProcedureDeclaration's name
     * @param st the ProcedureDeclaration main statement
     * @param a the ProcedureDeclaration's list of arguments
     */
    public ProcedureDeclaration(String s, Statement st, List<String> a, List<String> locals)
    {
        id = s;
        stmt = st;
        args = a;
        vars = locals;
    }

    /**
     * Outputs the name id.
     * @return the id.
     */
    public String getName()
    {
        return id;
    }

    /**
     * Outputs the ProcedureDeclaration's arguments.
     * @return the list of arguments.
     */
    public List<String> getArgs()
    {
        return args;
    }

    /**
     * Outputs the variables.
     * @return the list of variables
     */
    public List<String> getVars()
    {
        return vars;
    }

    /**
     * Outputs the main statement of the ProcedureDeclaration.
     * @return the main statement.
     */
    public Statement getStmt()
    {
        return stmt;
    }

    /**
     * Outputs the local environment.
     * @return the environment env
     */
    public Environment getEnv()
    {
        return env;
    }

    /**
     * exec sets the procedure in the passed in environment's HashMap.
     * @param e the environment passed in to be used
     */
    public void exec(Environment e)
    {
        e.setProcedure(id, this);
        env = new Environment(e);
        for (String s : vars)
            env.declareVariable(s, 0);
    }

    /**
     * Converts the ProcedureDeclaration into MIPS, pushing the
     * local variables onto the stack.
     * @param e the Emitter for printing code in MIPS
     */
    public void compile(Emitter e)
    {
        e.emit("proc" + id + ":");
        e.emitPush("$v0");
        e.setProc(this);
        e.emitPush("$ra");
        e.emit("li $t7, 0");
        for (int i = 0; i <= vars.size() - 1; i++)
            e.emitPush("$t7");
        stmt.compile(e);
        for (int i = 0; i <= args.size() - 1; i++)
            e.emitPop("$t7");
        e.emitPop("$ra");
        e.emitPop("$v0");
        for (int i = 0; i < args.size(); i++)
            e.emitPop("$t0");
        e.emit("jr $ra");
        e.clearProc();
    }
}
