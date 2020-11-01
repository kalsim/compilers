package scanner;

import java.io.*;

/**
 * scanner.scanner is a simple scanner for Compilers and Interpreters (2014-2015) lab exercise 1
 * @author Montek Kalsi
 * @version 9/8/19
 *  
 * Usage:
 * The scanner.scanner object acts as an input reader, determining the lexemes in
 * given input strings, and outputting a string of tokens for a parser.
 */
public class Scanner
{
    // the input stream of characters
    private BufferedReader in;

    // the character currently being examined
    private char currentChar;

    // determines whether the input stream has reached the end of the file
    private boolean eof;

    /**
     * scanner.scanner constructor for construction of a scanner that
     * uses an InputStream object for input.  
     * Usage: 
     * FileInputStream inStream = new FileInputStream(new File(<file name>);
     * scanner.scanner lex = new scanner.scanner(inStream);
     * @param inStream the input stream to use
     */
    public Scanner(InputStream inStream)
    {
        in = new BufferedReader(new InputStreamReader(inStream));
        eof = false;
        getNextChar();
    }
    /**
     * scanner.scanner constructor for constructing a scanner that
     * scans a given input string.  It sets the end-of-file flag an then reads
     * the first character of the input string into the instance field currentChar.
     * Usage: scanner.scanner lex = new scanner.scanner(input_string);
     * @param inString the string to scan
     */
    public Scanner(String inString)
    {
        in = new BufferedReader(new StringReader(inString));
        eof = false;
        getNextChar();
    }

    /**
     * Sets instance field currentChar to the value read from the input stream using the read method.
     * If -1 is returned, the stream is at the end of the file, changing eof to true.
     * If an IO exception is thrown, the system exits with status 1.
     */
    private void getNextChar()
    {
        int n = 0;
        try
        {
            n = in.read();
        }
        catch (IOException e)
        {
            System.exit(-1);
            e.printStackTrace();
        }
        if (n == -1)
            eof=true;
        else
        {
            currentChar = (char) (n);
            if (currentChar == '.')
                eof = true;
        }
    }

    /**
     * eat takes in the expected character, comparing it against the current one. If equivalent,
     * the IO stream advances to the next character; otherwise, a scanner.ScanErrorException is thrown
     * with the strings included in the reason.
     *
     * @param expected   the character compared with currentChar
     */
    private void eat(char expected) throws ScanErrorException
    {
        if (expected==currentChar)
        {
            getNextChar();
        }
        else
        {
            String s = "Illegal character. Expected " + currentChar + " and found " + expected;
            throw(new ScanErrorException(s));
        }
    }

    /**
     * Determines whether the input has reached the end of the file using
     * variable eof.
     *
     * @return true if the end of file has been reached.
     */
    public boolean hasNext()
    {
        return !eof;
    }

    /**
     * Based on the type of currentChar, either a number, identifier, or operand
     * is scanned.
     *
     * @return the next token as a String
     */
    public String nextToken() throws ScanErrorException
    {
        if (eof)
            return "END";
        while(hasNext() && isWhitespace(currentChar))
        {
            eat(currentChar);
        }
        if(isDigit(currentChar))
        {
            return scanNumber();
        }
        if(isLetter(currentChar))
        {
            return scanIdentifier();
        }
        return scanOperand();
    }

    /**
     * Checks whether d is a character defined by regular expression 0-9.
     *
     * @param d the input character checked
     * @return true if d falls within the range 0-9
     */
    public static boolean isDigit(char d)
    {
        return (d >= '0' && d <= '9');
    }

    /**
     * Determines whether l is within regular expression a-z or A-Z.
     *
     * @param l the character being examined
     * @return true if l falls within the regular expression
     */
    public static boolean isLetter(char l)
    {
        return ((l >= 'a' && l <= 'z') || (l >= 'A' && l <= 'Z'));
    }

    /**
     * Validates if c is whitespace.
     *
     * @param c the character being examined
     * @return true if c is whitespace
     */
    public static boolean isWhitespace(char c)
    {
        return (c == ' ' || c == '\n' || c == '\r' || c == '\t');
    }

    /**
     * If the character in the input stream begins a number token,
     * the characters following are concatenated into an output String returned
     * as the found lexeme.
     *
     * @return the lexeme of a number
     * @throws ScanErrorException if a lexeme isn't found
     */
    private String scanNumber() throws ScanErrorException
    {
        if (!isDigit(currentChar))
        {
            throw (new ScanErrorException("No number is found"));
        }
        String num = "";
        while (hasNext() && isDigit(currentChar))
        {
            num += currentChar;
            eat(currentChar);
        }
        return num;
    }

    /**
     * Continues along the input stream while concatenating characters in
     * an output String that compose an identifier.
     *
     * @return the lexeme of an identifier
     * @throws ScanErrorException if a lexeme isn't found
     */
    private String scanIdentifier() throws ScanErrorException
    {
        if (!isLetter(currentChar))
        {
            throw (new ScanErrorException("No identifier is found"));
        }
        String identifier="";
        while(hasNext() && (isLetter(currentChar) || isDigit(currentChar)))
        {
            identifier+=currentChar;
            eat(currentChar);
        }
        return identifier;
    }

    /**
     * Determines if currentChar matches with any of the valid operands.
     * If so, it is returned.
     *
     * @return the string of the operand token
     * @throws ScanErrorException if no operand is found
     */
    private String scanOperand() throws ScanErrorException
    {
        char[] operands = {'+','-','*','/','%','=','(',')','.',':',';','>','<', '$', '^', '@', ','};
        int ind=0;
        while(ind < operands.length && currentChar!=operands[ind])
        {
            ind++;
        }
        //if (currentChar!=operands[ind])
        char c = currentChar;
        eat(currentChar);
        if (!eof && ind>=operands.length)
        {
            throw (new ScanErrorException(c + " caused an exception"));
        }
        String operand="";
        if ((c=='<' || c=='>' || c==':' || c=='=') && (currentChar=='=' || currentChar=='>' || currentChar=='<'))
        {
            operand = "" + c + currentChar;
            eat(currentChar);
        }
        else
            operand=""+c;
        return operand;
        //return String.valueOf(c);
    }
}
