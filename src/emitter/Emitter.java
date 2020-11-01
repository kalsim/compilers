package emitter;
import java.io.*;

import ast.ProcedureDeclaration;

/**
 * Emitter helps output MIPS code to a different file.
 *
 * @author Montek Kalsi
 * @version 1/16/20
 */
public class Emitter
{
	private PrintWriter out;
	private int start = 1;
	private int s = 1;
	private int e = 1;
	private int height = 0;
	private ProcedureDeclaration pro;

	/**
	 * Emitter object constructor with output file passed in
	 * @param o the name of the output file.
	 */
	public Emitter(String o)
	{
		try
		{
			out = new PrintWriter(new FileWriter(o), true);
		}
		catch(IOException e)
		{
			throw new RuntimeException(e);
		}
	}

	/**
	 * prints one line of code to file (with non-labels indented)
	 * @param code the MIPS code.
	 */
	public void emit(String code)
	{
		if (!code.endsWith(":"))
			code = "\t" + code;
		out.println(code);
	}

	/**
	 * Pushes the register value onto a stack.
	 * @param reg the register pushed
	 */
	public void emitPush(String reg)
	{
		emit("subu $sp, $sp, 4" + "  #stores register " + reg +" onto the stack.");
		emit("sw " + reg +", ($sp)");
		height++;
	}

	/**
	 * Pops value off stack and onto register.
	 * @param reg the register used to store popped value
	 */
	public void emitPop(String reg)
	{
		emit("lw " + reg +", ($sp)" + "  #removes register " + reg + " from the stack.");
		emit("addu $sp, $sp, 4");
		height--;
	}
	/**
	 * Closes the file.
	 */
	public void close()
	{
		out.close();
	}

	/**
	 * Outputs next label number for ifs.
	 * @return the next label
	 */
	public int nextLabelID()
	{
		int r = start;
		start++;
		return r;
	}

	/**
	 * Gets next label number for loops.
	 * @return next loop label number
	 */
	public int next()
	{
		int r = s;
		s++;
		return r;
	}

	/**
	 * Outputs next label number for loop ends.
	 * @return the next label number
	 */
	public int endnext()
	{
		int r = e;
		e++;
		return r;
	}

	/**
	 * current procedure context
	 * @param proc the ProcedureDeclaration set to current procedure.
	 */
	public void setProc(ProcedureDeclaration proc)
	{
		pro = proc;
		height = 0;
	}
	/**
	 * Clears current procedure context
	 * @postcondition current ProcedureDeclaration is null.
	 */
	public void clearProc()
	{
		pro = null;
	}

	/**
	 * Checks if var is part of the current ProcedureDeclaration object.
	 * @param var the variable name
	 * @return true if the variable is a local one
	 */
	public boolean isLocalVariable(String var)
	{
		if (pro != null)
		{
			if (var.equals(pro.getName()))
				return true;
			if (pro.getArgs().contains(var) || pro.getVars().contains(var))
				return true;
		}
		return false;
	}

}