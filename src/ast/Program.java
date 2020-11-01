package ast;

import java.util.ArrayList;

import emitter.Emitter;
import environment.Environment;

/**
 * A Program object stores a list of procedures and
 * the Statement which comes right after.
 *
 * @author Montek Kalsi
 * @version 1/14/20
 */
public class Program extends Statement
{
    private ArrayList<ProcedureDeclaration> procedures;
    private ArrayList<String> variables;
    private Statement stmt;

    /**
     * Constructor for Program objects which instantiates
     * the list of procedures.
     */
    public Program()
    {
        procedures = new ArrayList<ProcedureDeclaration>();
    }

    /**
     * Sets the Statement
     *
     * @param stmt the Statement used
     */
    public void setStatement(Statement stmt)
    {
        this.stmt = stmt;
    }

    /**
     * Adds a Procedure
     *
     * @param pro the Procedure added
     */
    public void addProcedure(ProcedureDeclaration pro)
    {
        procedures.add(pro);
    }

    /**
     * Makes the list of Procedures into the list passed in
     *
     * @param procs the list of Procedures
     */
    public void setProcedure(ArrayList<ProcedureDeclaration> procs)
    {
        procedures = procs;
    }

    /**
     * variable list to passed in list
     *
     * @param var the new set of variables
     */
    public void setVariables(ArrayList<String> var)
    {
        variables = var;
    }

    /**
     * Executes the Procedures within the list,
     * then executing the Statement afterward.
     *
     * @param env the environment passed in
     */
    public void exec(Environment env)
    {
        for(ProcedureDeclaration procedure : procedures)
        {
            procedure.exec(env);
        }
        stmt.exec(env);
    }

    /**
     * The compile method to convert into MIPS code.
     * @param file the file passed in
     */
    public void compile(String file)
    {
        Emitter emt = new Emitter(file);
        emt.emit(".data");
        emt.emit("newline: .asciiz \"\\n\" ");
        for (String s : variables)
            emt.emit(s + ": .word 0");
        emt.emit(".text");
        emt.emit(".globl main");
        emt.emit("main:");
        stmt.compile(emt);
        emt.emit("li $v0,10");
        emt.emit("syscall");
        for(ProcedureDeclaration proc : procedures)
            proc.compile(emt);
        emt.close();
    }
}