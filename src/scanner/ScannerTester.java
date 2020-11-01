package scanner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ScannerTester
{
    public static void main(String args[])
    {
        Scanner s = null;
        try
        {
            File f = new File("/Users/montek.kalsi/IdeaProjects/boolingtime/src/scanner/ScannerTest.txt");
            s = new Scanner(new FileInputStream(f));
        }
        catch (FileNotFoundException e)
        {
            //e.printStackTrace();
        }
        while (s.hasNext())
        {
            try
            {
                System.out.println(s.nextToken());
            }
            catch (ScanErrorException e)
            {
                e.printStackTrace();
            }
        }
        System.out.println("p");
    }
}
