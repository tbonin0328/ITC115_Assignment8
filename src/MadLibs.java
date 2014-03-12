import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * @author thomasbonin
 * ITC 115 -- Assignment 8, MadLibs
 */
public class MadLibs 
{
	private static final String CUE_START = "<";
	private static final String CUE_END = ">";
	private static String yourFile = null;
	
	private Scanner in = new Scanner(System.in);
	
	/**
	 * @param fileName: the file that's being passed into the method, can be any text file.
	 * @return: a new file to be read from is returned if it's an actual file.
	 * This method retrieves the file specified by the method as long as it is a file.
	 */
	public Scanner getFileReader(String fileName)
	{
		File file = new File(fileName);
		try 
		{
			return new Scanner(file);
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println(fileName);
			System.out.println("Not a valid file name! Please try again.");
			return (getFileReader("input/" + getFile()));	
		}				
	}
	
	/**
	 * @param fileName: the file that's being passed into the method, can be any text file.
	 * @return: a new file to be written to is returned if it's an actual file.
	 * This method retrieves a file and sets it up to written to.
	 */
	public PrintWriter getFileWriter(String fileName)
	{
		FileOutputStream file = null;
		
		try
		{
			file = new FileOutputStream(fileName);
		}
		
		catch (FileNotFoundException e)
		{
			System.out.println(e.getMessage());
			return null; //System.exit(-1); let's someone decide instead of automatic exit?
		}
		return new PrintWriter(file);
	}
		
	/**
	 * This method (the play method) provides an introduction and then gives the user three options to choose from:
	 * create, view, or quit. The method uses a while loop set to true that loops as long as the user doesn't choose
	 * to quit. It uses if statements within this while loop to direct the system to process different methods according
	 * to user choices of either "create" or "view". Both "create" and "view" use a getFile() method to retrieve the 
	 * file chosen by the user. Both "create" and "view" use getFileReader to get the chosen file. Only the "create"
	 * function uses getFileWriter because "view" doesn't need it.
	 * 
	 * When the user chooses "create", a while loop processes each line of the chosen file through the
	 * createMadLib method, as long as there's a next line, and prints each line to the chosen file.
	 * 
	 * When the user chooses "view", a while loop is used to pull up each line of the file and print it
	 * to the console.
	 * 
	 * The "create" function ends by closing the writer and the reader. The "view" function ends by 
	 * closing the reader. 
	 */
	public void play()
	{
		printString("Welcome to the game of Mad Libs.");
		printString("You will help create a story by providing words and phrases.");
		
		while (true)
		{
			String command = getString("(C)reate, (V)iew, or (Q)uit");
	
			if (command.toLowerCase().equals("q")) break;
	
			if (command.toLowerCase().equals("c"))
			{	
				String file = getFile();
				Scanner fileIn = getFileReader("input/" + file);
				PrintWriter writer = getFileWriter("output/" + file);
				
				while(fileIn.hasNextLine())
				{
					String line = fileIn.nextLine();
					
					line = createMadLib(line);
					
					writer.println(line);
				}
				writer.close();
				fileIn.close();
			}
	
			if (command.toLowerCase().equals("v"))
			{
				String file = getFile();
				Scanner fileIn = getFileReader("output/" + file);
				
				while(fileIn.hasNextLine())
				{
					String line = fileIn.nextLine();
					System.out.println(line);
				}
				
				fileIn.close();
			}
		}
		System.out.println("Goodbye :(");
	}

	/**
	 * @return: This method returns the file name entered by the user.
	 * It uses the getString() method to prompt the user to enter the file name.
	 * It then stores the file as yourFile. The method the creates a new file
	 * using the file name and checks to make sure it's a file. If it's not a
	 * file, it prompts the user to enter the file name again until the file name
	 * is correct, and then it returns the file.
	 */
	private String getFile() 
	{
		yourFile = getString("Enter file name: ");
		return yourFile;
	}
	
	/**
	 * @param string: this string is one line submitted for processing by the method.
	 * This method: 1) initiates several local variables: 
	 * 		-- int z: a variable that uses the getCueCount method to store the number
	 * 		of cues in the string.
	 * 		-- Startpoint, the starting point for each iteration of the indexOf() method
	 * 		used in a line.
	 * 		-- int x: the variable used to store the index of the "<".
	 * 		-- int y: the variable used to to store the index of the ">".
	 * 		-- String answer: the variable used to store the madlib answer entered by the 
	 * 		user.
	 * 2) gets the index of "<" using the indexOf method; stores it as x.
	 * 3) gets the index of ">" using x as the starting index; stores is as y.
	 * 4) Creates a string called thisCue using x+1 as the starting index and
	 * y as the ending index. "thisCue" is the madlib prompt without the greater 
	 * than/less than signs around it.
	 * 5) Creates a string called cueReplace using x as the starting index and y+1
	 * as the ending index.
	 * 7) 
	 * @return 
	 */
	private String createMadLib(String string) 
	{
		int z = getCueCount(string);
		int Startpoint = 0;
		int x = 0;
		int y = 0;
		String answer = "";
		String thisCue = "";
		String cueStart = "";
		String cueReplace = "";
		
		if (z != 0)
		{
			for (int i = 0; i < z; i++)
			{
				x = string.indexOf(CUE_START, Startpoint); //gets index of next "<";
				y = string.indexOf(CUE_END, x); //gets index of ">" after "<";
				thisCue = string.substring(x+1,y);
				cueStart = thisCue.substring(0,1);
				cueReplace = string.substring(x, y+1);
				answer = getAnswer(thisCue, cueStart);
				string = string.replace(cueReplace, answer);
				Startpoint = x;
			}
		}
		return string;
	}
	
	/**
	 * @param thisCue: the madlib prompt with the greater than/less than signs around it.
	 * @param cueStart: the first letter of thisCue (the word itself).
	 * @return: the prompt that the user sees that asks for the word for the mablib.
	 * This method uses the starting letter of the cue and and the cue itself to figure out if
	 * cue starts with a vowel. If it does it changes the article of the prompt to "an"
	 * and returns the prompt. Otherwise, it just returns the prompt.
	 */
	public String getAnswer(String thisCue, String cueStart)
	{
		String prompt = "";
		
		while(true)
		{
			if (cueStart.toLowerCase().matches("[aeiou]"))
			{
				prompt = getString("Please type an " + thisCue + ": ");
				break;
			}
			else
			{
				prompt = getString("Please type a " + thisCue + ": ");
				break;
			}
		}
		return prompt;
	}
	
	public int getCueCount(String string)
	{
		int counter = 0;
		for( int i = 0; i < string.length(); i++ ) 
		{
		    if(string.charAt(i) == '<' ) 
		    {
		        counter++;
		    } 
		}
		return counter;
	}
	
	public void printString(String text)
	{
		System.out.println(text);
	}
	
	public String getString(String prompt)
	{
		System.out.println(prompt);
		return in.nextLine();
	}
			
	public static void main(String[] args)
	{
		MadLibs program = new MadLibs();
		program.play();
	}
}
