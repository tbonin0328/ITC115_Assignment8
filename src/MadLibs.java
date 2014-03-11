import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Scanner;

public class MadLibs 
{
	private static final String ARTICLE_A = " a ";
	private static final String ARTICLE_B = " an ";
	private static final String CUE_START = "<";
	private static final String CUE_END = ">";
	private static String yourFile = null;
	
	private Scanner in = new Scanner(System.in);
	
	public Scanner getFileReader(String fileName)
	{
		File file = new File(fileName);
		
		try 
		{
			return new Scanner(file);
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println(e.getMessage());
			System.exit(-1);
			return null;
		}
	}
	
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
	//PrintWriter writer = new PrintWriter(file);
	}
		
	public void play()
	{
		printString("Welcome to the game of Mad Libs.");
		printString("You will help create a story by providing words and phrases.");
		
		while (true)
		{
			String command = getString("(C)reate, (V)iew, (Q)uit");
	
			if (command.toLowerCase().equals("q")) break;
	
			if (command.toLowerCase().equals("c"))
			{
				String file = getFile();
				PrintWriter writer = getFileWriter("output/" + file);
				Scanner fileIn = getFileReader("input/" + file);
				
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

		
		
		//loop through the file and display its contents
		
	private String getFile() 
	{
		yourFile = getString("Enter file name: ");
		
		return yourFile;
	}
	
	/**
	 * @param line 
	 * @param string: this string is one line submitted for processing by the method.
	 * This method: 1) established an index start point of 0;
	 * 2) gets the index of "<" using the start point as the starting index.
	 * 3) stores the index of "<" as int x;
	 * 4) gets the index of ">" using x as the starting index.
	 * 5) Creates a string called thisCue using x+1 as the starting index and
	 * y as the ending index.
	 * 6) Creates a string called article using x-2 as the starting index and x
	 * as the ending index.
	 * 7) 
	 * @return 
	 */
	private String createMadLib(String string) 
	{
		int x = 0;
		int y = 0;
		int z = getCueCount(string);
		String answer = "";
		//String article = "";
		int Startpoint = 0;
		
			if (z != 0)
			{
				for (int i = 0; i < z; i++)
				{
					x = string.indexOf(CUE_START, Startpoint); //gets index of next "<";
					y = string.indexOf(CUE_END, x); //gets index of ">" after "<";
					String thisCue = string.substring(x+1,y);
					String cueStart = thisCue.substring(0,1);
					String cueReplace = string.substring(x, y+1);
					answer = getAnswer(thisCue, cueStart);
					string = string.replace(cueReplace, answer);
					Startpoint = x;
					//System.out.println(string);
					//System.out.println(x + ", " + y + ", " + thisCue + ", " + cueReplace);
				}
			}
		return string;
	}
	
	public void viewMadLib(String string)
	{
		Scanner fileIn = getFileReader(yourFile);
		//loop through the file and display its contents
		while(fileIn.hasNextLine())
		{
			String line = fileIn.nextLine();
			System.out.println(line);
		}
		
		fileIn.close();
		}
	
	private String replaceCue(String stringA, String thisCue, String articleC, String cueReplace) 
	{
		String cueStart = thisCue.substring(0,1);
		String answer = "";
		String answerStart = "";
		
		answer = getAnswer(thisCue, cueStart);
		answerStart = answer.substring(0,1);
		
		return (changeString(stringA, answerStart, articleC, cueReplace, answer));
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
		String answer = "";
		
		while(true)
		{
			if (cueStart.toLowerCase().matches("[aeiou]"))
			{
				answer = getString("Please type an " + thisCue + ": ");
				break;
			}
			else
			{
				answer = getString("Please type a " + thisCue + ": ");
				break;
			}
		}
		return answer;
	}
	
	public String changeString(String stringA, String answerStart, String articleC, String replaceCue, String answer)
	{
		String stringB = "";
		
		while(true)
		{
			if (answerStart.matches("[aeiou]"))
			{
				stringB = stringA.replace(ARTICLE_A, ARTICLE_B);
				stringB = stringB.replace(replaceCue, answer);
				break;
			}
			else
			{
				stringB = stringA.replace(replaceCue, answer);
				break;
			}
		}
		return stringB;
	}
	
	public String getArticle(int x, String string)
	{
		String article = "";
		if(x >= 2)
		{
			article = string.substring(x-2, x);
		}
		else
		{
		article = "the";
		}
		return article;
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
