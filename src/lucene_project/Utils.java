package lucene_project;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Utils
{
	public static String readFile(String pathname) throws IOException
	{

	    File file = new File(pathname);
	    StringBuilder fileContents = new StringBuilder((int)file.length());        

	    try (Scanner scanner = new Scanner(file)) {
	        while(scanner.hasNextLine()) {
	            fileContents.append(scanner.nextLine() + System.lineSeparator());
	        }
	        return fileContents.toString();
	    }
	}
}
