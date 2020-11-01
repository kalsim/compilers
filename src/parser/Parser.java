package parser;
import ast.*;
import ast.Number;
import environment.Environment;
import scanner.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import emitter.Emitter;

/**
 * The parser class models a basic parser, accepting languages with the following grammar:
 *
 * program → PROCEDURE id ( maybeparms ) ; stmt program | stmt .
 * maybeparms → parms | ε
 * parms → parms , id | id
 * stmt → WRITELN ( expr ) ; | BEGIN stmts END ; | id := expr ;
 * | IF cond THEN stmt | WHILE cond DO stmt
 * stmts → stmts stmt | ε
 * expr → expr + term | expr - term | term
 * term → term * factor | term / factor | factor
 * factor → ( expr ) | - factor | num | id ( maybeargs ) | id
 * maybeargs → args | ε
 * args → args , expr | expr
 * cond → expr relop expr
 * relop → = | <> | < | > | <= | >=
 *
 * As this is a recursive descent parser, there is a recursive method for every terminal
 * with form parseX. Each method simply returns its corresponding
 * Program, Statement, or Expression, with each specific class's exec/eval methods
 * either executing the statement or evaluating the expression.
 *
 * @author Montek Kalsi
 * @version 12/5/19
 */
public class Parser
{
    private Scanner scanner;
    private String currentToken;

    /**
     * Constructor for a Parser which takes in a Scanner, using it to initialize the
     * scanner instance variable. currentToken is defined to be the next Token in the scanner
     * and a HashMap for variables is initialized where the key is a String and the value is an
     * integer.
     * @param scannerinput The given scanner
     * @throws scanner.ScanErrorException if other methods have errors in scanning or parsing the
     * input
     * @throws java.io.IOException if there is a basic error
     */
    public Parser(Scanner scannerinput) throws scanner.ScanErrorException, java.io.IOException
    {
        scanner = scannerinput;
        currentToken = scanner.nextToken();
    }

    /**
     * Eat takes in an expected token and updates curToken to the
     * next token if the expected token matches the current one.
     * otherwise, it throws an illegal argument exception.
     * A scan error exception is thrown if the scanner is
     * at the end of the file.
     *
     * @param expected the expected token
     * @throws ScanErrorException if the scanner reaches the end
     */
    private void eat(String expected) throws scanner.ScanErrorException, java.io.IOException
    {
        if (currentToken.equals(expected))
        {
            currentToken = scanner.nextToken();
        }
        else
        {
            throw new IllegalArgumentException("the token equals " + currentToken + " and it " +
                    "should " +
                    "equal " + expected);
        }
    }

    /**
     * Eats the current token, a number, and returns the value. It
     * removes parentheses and evaluates negatives as well.
     * @return the token in Number form
     * @throws ScanErrorException if the scanner is at the end
     */
    private Number parseNumber() throws scanner.ScanErrorException, java.io.IOException
    {
        int num = Integer.parseInt(currentToken);
        eat(currentToken);
        return new Number(num);
    }

    /**
     * parseFactor recursively parses a factor by the grammar
     * 	 stmt -> WRITELN ( factor ) ;
     * 	 factor -> ( expr )
     *   factor -> - factor
     * 	 factor -> num
     * and outputs the value of the factor.
     *
     * @return the Expression of the factor
     * @throws IllegalArgumentException if the tokens do not match
     * @throws ScanErrorException if the scanner reaches the end
     */
    private Expression parseFactor() throws ScanErrorException, IOException
    {
        if(Scanner.isDigit(currentToken.charAt(0)))
            return parseNumber();
        else if(Scanner.isLetter(currentToken.charAt(0)))
        {
            String n = currentToken;
            eat(n);
            if(currentToken.equals("("))
            {
                eat("(");
                List<Expression> params = new ArrayList<Expression>();
                while(!currentToken.equals(")"))
                {
                    Expression param = parseExpression();
                    params.add(param);
                    if(!currentToken.equals(")"))
                    {
                        eat(",");
                    }
                }
                ProcedureCall call = new ProcedureCall(n,params);
                eat(")");
                return call;
            }
            return new Variable(n);
        }
        else if(currentToken.equals("("))
        {
            eat(currentToken);
            Expression expr = parseExpression();
            eat(")");
            return expr;
        }
        else if (currentToken.equals("-"))
        {
            eat(currentToken);
            Number num = (Number)parseFactor();
            return new Number(-num.getValue());
        }
        else
        {
            Variable num = new Variable(currentToken);
            eat(currentToken);
            return num;
        }
    }

    /**
     * parseTerm parses the current term according to the grammar rule
     * for it. Operators include *, /, and mod. After parsing
     * the current term , its integer value is returned. Before returning the
     * expression, parseFactor is called as parentheses and negatives are
     * of higher precedence than multiplication and division.
     * @return an expression for the current term
     * @throws scanner.ScanErrorException if an invalid statement or expression is found
     * @throws java.io.IOException if there is a basic error
     */
    public Expression parseTerm() throws scanner.ScanErrorException, java.io.IOException
    {
        Expression num = parseFactor();
        while(currentToken.equals("*")||currentToken.equals("/")||currentToken.equals("mod"))
        {
            if (currentToken.equals("*"))
            {
                eat("*");
                num = new BinOp("*",num,parseFactor());
            }
            else if(currentToken.equals("mod"))
            {
                eat("mod");
                num = new BinOp("%",num,parseFactor());
            }
            else
            {
                eat("/");
                num = new BinOp("/",num,parseFactor());
            }
        }
        return num;

    }

    /**
     * parseExpression parses the current expression using the grammar
     * for expressions explained above. Operators for addition and
     * subtraction are included. After parsing the current expression, it's
     * returned. parseTerm is also performed before the addition and subtraction
     * due to precedence.
     * @return the current expression
     * @throws scanner.ScanErrorException if an invalid statement or expression is found
     * @throws java.io.IOException if there is a basic error
     */
    public Expression parseExpression() throws scanner.ScanErrorException, java.io.IOException
    {
        Expression num = parseTerm();
        while(currentToken.equals("+")||currentToken.equals("-"))
        {
            if (currentToken.equals("+"))
            {
                eat("+");
                num = new BinOp("+",num,parseTerm());
            }
            else
            {
                eat("-");
                num = new BinOp("-",num,parseTerm());
            }
        }
        return num;
    }

    /**
     * parseStatement is used to parse statements in the language. This
     * includes WRITELN to print expressions to the console, BEGIN to signal
     * the start of a function, END for the end, and if statements.
     * It returns a statement corresponding to the type of statement which
     * was parsed.
     * @throws scanner.ScanErrorException if an invalid statement or expression is found
     * @throws java.io.IOException if there is a basic error
     * @return a statement corresponding to the parsed statement.
     */
    private Statement parseStatement() throws ScanErrorException, IOException
    {
        if(currentToken.equals("WRITELN"))
        {
            eat("WRITELN");
            eat("(");
            Expression num = parseExpression();
            eat(")");
            eat(";");
            return new Writeln(num);
        }
        else if(currentToken.equalsIgnoreCase("IF"))
        {
            eat("IF");
            Expression exp1 = parseExpression();
            String operand = currentToken;
            eat(operand);
            Expression exp2 = parseExpression();
            eat("THEN");
            Statement stmt = parseStatement();
            Statement ifstmt = new If(new Condition(operand,exp1,exp2),stmt);
            return ifstmt;
        }
        else if(currentToken.equalsIgnoreCase("WHILE"))
        {
            eat("WHILE");
            Expression exp1 = parseExpression();
            String operand = currentToken;
            eat(operand);
            Expression exp2 = parseExpression();
            eat("DO");
            Statement stmt = parseStatement();
            Statement whlstmt = new While(new Condition(operand,exp1,exp2),stmt);
            return whlstmt;
        }
        else if(currentToken.equals("BEGIN"))
        {
            eat("BEGIN");
            ArrayList<Statement> stmts = new ArrayList<Statement>();
            while(!currentToken.equals("END"))
            {
                stmts.add(parseStatement());
            }
            Block block = new Block(stmts);
            eat("END");
            eat(";");
            return block;
        }
        else
        {
            String key = currentToken;
            eat(key);
            eat(":=");
            Expression val = parseExpression();
            eat(";");
            return new Assignment(key,val);
        }
    }

    /**
     * parseProgram parses the entire program by parsing all Procedures
     * and then a Statement representing the main method part of the program. It
     * returns a program with a list of all the procedure declarations including
     * the main method statement to be run.
     * @return a program with a all the procedure declarations and the main method
     * @throws ScanErrorException if the current token does not equal the given token
     * @throws java.io.IOException if there is any basic error
     */
    public Program parseProgram() throws ScanErrorException, IOException
    {
        Program p = new Program();
        ArrayList<String> vars = new ArrayList<String>();
        ArrayList<String> locals = new ArrayList<String>();
        while (currentToken.equals("VAR"))
        {
            eat("VAR");
            while (!currentToken.equals(";"))
            {
                vars.add(currentToken);
                eat(currentToken);
                if (currentToken.equals(","))
                    eat(",");
            }
            eat(";");
        }
        List<String> params = new ArrayList<String>();
        ArrayList<ProcedureDeclaration> procs = new ArrayList<ProcedureDeclaration>();
        while (currentToken.equals("PROCEDURE"))
        {
            eat("PROCEDURE");
            String s = currentToken;
            eat(s);
            eat("(");
            while (!currentToken.equals(")"))
            {
                params.add(currentToken);
                eat(currentToken);
                if (currentToken.equals(","))
                    eat(",");
            }
            eat(")");
            eat(";");
            if (currentToken.equals("VAR"))
            {
                eat("VAR");
                while (!currentToken.equals(";"))
                {
                    locals.add(currentToken);
                    eat(currentToken);
                    if (currentToken.equals(","))
                        eat(",");
                }
                eat(";");
            }
            Statement exec = parseStatement();
            procs.add(new ProcedureDeclaration(s, exec, params, locals));
        }
        Statement stmt = parseStatement();
        p.setStatement(stmt);
        p.setVariables(vars);
        p.setProcedure(procs);
        return p;
    }

    /**
     * The main method to test the parser which creates a parser to parse
     * a file passed in as an input stream. parseProgram is then called.
     * @param args The main method arguments
     * @throws FileNotFoundException if the file is not found
     * @throws scanner.ScanErrorException if an invalid statement or expression is found
     * @throws java.io.IOException if there is a basic error
     */
    public static void main(String[] args) throws FileNotFoundException,scanner.ScanErrorException,
            java.io.IOException
    {
        Parser parser = new Parser(new Scanner(new FileInputStream(new File(
                "/Users/montek.kalsi/IdeaProjects/boolingtime/src/parser/parserTest10.txt"))));
        parser.parseProgram().compile("output.asm");
    }

}
