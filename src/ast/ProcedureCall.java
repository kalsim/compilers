package ast;

import java.util.List;

import emitter.Emitter;
import environment.Environment;

/**
 * A ProcedureCall can keep track of its String id and a list of
 * the names of the arguments passed to the call.
 *
 * @author Montek Kalsi
 * @version 1/10/20
 */
public class ProcedureCall extends Expression
{
    private String id;
    private List<Expression> args;

    /**
     * Constructor for a ProcedureCall which stores a string
     * identifier and a list of expressions for arguments.
     * @param id the string identifier
     * @param arguments the list of arguments
     */
    public ProcedureCall(String id,List<Expression> arguments)
    {
        args = arguments;
        this.id = id;
    }

    /**
     * This method helps evaluate a ProcedureCall. It creates a new
     * environment to track local variables. ProcedureDeclaration takes
     * the names of the arguments. The statement associated with it is
     * executed to return the procedure variable's value.
     *
     * @param env  the parent environment for the procedure being executed.
     * @return the integer procedure variable's value
     */
    public int eval(Environment env)
    {
        Environment newEnvironment = new Environment(env);
        env.declareVariable(id, 0);
        ProcedureDeclaration declaration = env.getProcedure(id);
        declaration.getEnv().setParent(newEnvironment);
        List<String> argVars = declaration.getArgs();
        if (argVars!=null && args!=null)
        {
            if(argVars.size() != args.size())
                throw new IllegalArgumentException("parameters don't match the arguments");
            for (int i=0; i<argVars.size(); i++)
                newEnvironment.declareVariable(argVars.get(i), args.get(i).eval(env));
        }
        declaration.getStmt().exec(declaration.getEnv());
        return newEnvironment.getVariable(id);
    }

    /**
     * Compiles the ProcedureCall and jumps to the
     * correct ProcedureDeclaration label in MIPS.
     * @param e the Emitter used to print the MIPS code.
     */
    public void compile(Emitter e)
    {
        for (int i = args.size() - 1; i >= 0; i--)
        {
            args.get(i).compile(e);
            e.emitPush("$v0");
        }
        e.emit("jal proc"+id);
    }
}