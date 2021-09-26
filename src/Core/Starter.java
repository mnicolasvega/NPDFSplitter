package Core;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

public class Starter
{
	// ===================================================================
	
	private final static String SW_NAME = "MinPDFSplitter",
								SW_AUTHOR = "M. Nicolás Vega",
								SW_VERSION = "0.1";
	
	// ===================================================================

	private static final String	CMD_EXIT_1	= "exit",
								CMD_EXIT_2	= "quit",
								CMD_EXIT_3	= "e",
								CMD_EXIT_4	= "q",
								CMD_HELP_1	= "?",
								CMD_HELP_2	= "help",
								CMD_ORIGIN	= "origin",
								CMD_DESTINY	= "destiny",
								CMD_FILE	= "partname",
								CMD_OW		= "overwrite",
								CMD_PARTS	= "parts",
								CMD_SPLIT	= "split",
								CMD_ABOUT	= "about";
	
	private static final String	CMD_S_ORIGIN	= CMD_ORIGIN + " \"",
								CMD_S_DESTINY	= CMD_DESTINY + " \"",
								CMD_S_OW		= CMD_OW + " ",
								CMD_S_PARTS		= CMD_PARTS + " ",
								CMD_S_FILE		= CMD_FILE + " \"";
	
	private static final String NOTIFICATION_TEXT	= "-> ",
								NOTIFICATION_SPACE	= "   ";

	// ===================================================================
	
	private static String				doc_file,
										folder,
										new_name;
	private static boolean				overwrite = true;
	private static PDDocument			pdf;
	private static List<Pair<Integer>>	split;

	// ===================================================================
	
	public static void main(String[] args)
	{
		Scanner s = new Scanner(System.in);
		String	read,
				cmd;
		File	file;
		PDFUtils pu = new PDFUtils( );
		
		System.out.println("Welcome to \"" + SW_NAME + "\", use \"help\" to read the list of available commands.");
		
		while ((read = s.nextLine()) != null)
		{
			cmd = read.toLowerCase();
			
			if (cmd.equalsIgnoreCase(CMD_HELP_1) || cmd.equalsIgnoreCase(CMD_HELP_2))
			{
				System.out.println("Help Menu:");
				
				System.out.printf(NOTIFICATION_TEXT + "'%s', '%s':\n" + NOTIFICATION_SPACE +
									"opens this menu.\n",
									CMD_HELP_1, CMD_HELP_2);
				
				System.out.printf(NOTIFICATION_TEXT + "'%s', '%s', '%s, '%s':\n" + NOTIFICATION_SPACE +
									"closes the program.\n",
									CMD_EXIT_1, CMD_EXIT_2, CMD_EXIT_3, CMD_EXIT_4);
				
				System.out.printf(NOTIFICATION_TEXT + "'%s <file-location>':\n" + NOTIFICATION_SPACE +
									"sets the PDF file location to read it.\n",
									CMD_ORIGIN);
				
				System.out.printf(NOTIFICATION_TEXT + "'%s <directory-location>':\n" + NOTIFICATION_SPACE +
									"sets the destiny directory to write the PDF segments.\n",
									CMD_DESTINY);
				
				System.out.printf(NOTIFICATION_TEXT + "'%s <segment-name>':\n" + NOTIFICATION_SPACE +
									"sets the PDF segments name.\n",
									CMD_FILE);
				
				System.out.printf(NOTIFICATION_TEXT + "'%s <yes/no>':\n" + NOTIFICATION_SPACE +
									"enables/diseables overwriting old PDF segments.\n",
									CMD_OW);
				
				System.out.printf(NOTIFICATION_TEXT + "'%s <segments>':\n" + NOTIFICATION_SPACE +
									"specifies the segments to divide the PDF file.\n" +  NOTIFICATION_SPACE +
									"example format: s, 2, 3-5, n-e\n" +  NOTIFICATION_SPACE +
									"valid keys: 's' to specify the first page | 'n' to continue from last page | 'e' to specify the last page.\n",
									CMD_PARTS);
				
				System.out.printf(NOTIFICATION_TEXT + "'%s':\n" + NOTIFICATION_SPACE +
									"splits the desired PDF into the specified segments.\n",
									CMD_SPLIT);
			}
			else if (cmd.length() > CMD_S_ORIGIN.length() &&
					 cmd.substring(0, CMD_S_ORIGIN.length()).equals(CMD_S_ORIGIN))
			{
				cmd = cmd.substring(CMD_S_ORIGIN.length(), cmd.length() - 1);
				
				System.out.println(NOTIFICATION_TEXT + "Loading PDF");
				
				file = new File(cmd);
				if (file.exists() && !file.isDirectory())
				{
					doc_file = cmd;
					try
					{
						pdf = Loader.loadPDF( new File(doc_file) );
						System.out.println(NOTIFICATION_TEXT + "PDF loaded succesfully.");
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
			else if (cmd.length() > CMD_S_DESTINY.length() &&
					 cmd.substring(0, CMD_S_DESTINY.length()).equals(CMD_S_DESTINY))
			{
				cmd = cmd.substring(CMD_S_DESTINY.length(), cmd.length() - 1);
				
				file = new File(cmd);
				if (file.exists() && file.isDirectory())
				{
					folder = cmd;
					System.out.println(NOTIFICATION_TEXT + "Destiny directory succesfully updated. (New: '" + folder + "')");
				}
			}
			else if (cmd.length() > CMD_S_OW.length() &&
					 cmd.substring(0, CMD_S_OW.length()).equals(CMD_S_OW))
			{
				cmd = cmd.substring(CMD_S_OW.length());
				
				if (cmd.equals("yes") || cmd.equals("y"))
					overwrite = true;
				else if (cmd.equals("no") || cmd.equals("n"))
					overwrite = false;

				System.out.println(NOTIFICATION_TEXT + "Overwriting: " + overwrite);
			}
			else if (cmd.length() > CMD_S_PARTS.length() &&
					 cmd.substring(0, CMD_S_PARTS.length()).equals(CMD_S_PARTS))
			{
				cmd = cmd.substring(CMD_S_PARTS.length());
				split = pu.generateSplitList(cmd, pdf);
				System.out.println(NOTIFICATION_TEXT + "Segments to split:");
				System.out.println(NOTIFICATION_SPACE + pu.splitTextList(split) );
			}
			else if (cmd.length() > CMD_S_FILE.length() &&
					 cmd.substring(0, CMD_S_FILE.length()).equals(CMD_S_FILE))
			{
				cmd = cmd.substring(CMD_S_FILE.length(), cmd.length() - 1);
				new_name = cmd;
				
				System.out.println(NOTIFICATION_TEXT + "Subfiles name saved succesfully. (New: '" + new_name + "')");
			}
			else if (cmd.equals(CMD_SPLIT))
			{
				System.out.println(NOTIFICATION_TEXT + "Splitting document.");
				pu.splitAndSave(split, pdf, folder, new_name, overwrite);
				System.out.println(NOTIFICATION_TEXT + "Document succesfully splited.");
			}
			else if (cmd.equals(CMD_EXIT_1) || cmd.equals(CMD_EXIT_2) || cmd.equals(CMD_EXIT_3) || cmd.equals(CMD_EXIT_4))
			{
				break;
			}
			else if (cmd.equals(CMD_ABOUT))
			{
				System.out.println("Software developed by M. Nicolás Vega.");
			}
		}
	}

	// ===================================================================
	
	private String[] oneCut( String src )
	{
		String[] sz = new String[2];
		
		int p = src.indexOf(" ");
		sz[0] = src.substring(0, p);
		
		return sz;
	}

	// ===================================================================
}
