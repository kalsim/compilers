package ast;
import java.util.List;
import environment.Environment;
import scanner.ScanErrorException;
import emitter.Emitter;

/**
 * The Block class contains a list of statements, extending the Statement class. It starts
 * with a BEGIN and an end of END. The execute method for the Block
 * executes every statement within the block.
 *
 * @author Montek Kalsi
 * @version 10/29/19
 */
public class Block extends Statement
{
    private List<Statement> stmts;

    /**
     * The constructor for the Block class that initializes the list of statements
     * @param stmts the given list of statements
     */
    public Block(List<Statement> stmts)
    {
        this.stmts = stmts;
    }

    /**
     * Executes the Block statement by executing every statement contained within the block
     * @param env The given environment
     * @throws scanner.ScanErrorException if the parser encounters an invalid statement
     * or expression
     * @throws java.io.IOException if there is any other basic error
     */
    public void exec(Environment env)
    {
        for (Statement stmt:stmts)
        {
            stmt.exec(env);
        }
    }
    @Override
    /**
     * Compiles the block statement by compiling all of the statements within it
     * @param e The emitter that emits statements to the file
     */
    public void compile(Emitter e)
    {
        for (Statement stmt:stmts)
        {
            stmt.compile(e);
        }
    }
}
