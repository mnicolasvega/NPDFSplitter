package Core;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

public class CMDLine
{
	// ===================================================================
	
	private final static String SW_NAME		= "NPDFSplitter",
								SW_AUTHOR	= "M. Nicolás Vega",
								SW_VERSION	= "0.1",
								SW_WEBSITE	= "https://github.com/mnicolasvega";
	
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
		Scanner 	s		= new Scanner(System.in);
		String		read;
		String[]	cmd		= new String[2];
		File		file;
		boolean		run		= true;
		PDFUtils	pu		= new PDFUtils( );
		
		System.out.println("Welcome to " + SW_NAME + " " + SW_VERSION + ", use \"" + CMD_HELP_2 + "\" to read the list of available commands.");
		
		while (run && (read = s.nextLine()) != null)
		{
			cmd = spaceCut(read);
			
			if (cmd[0].equalsIgnoreCase(CMD_HELP_1) || cmd[0].equalsIgnoreCase(CMD_HELP_2))
			{
				cmd_help();
			}
			else if (cmd[0].equalsIgnoreCase(CMD_ORIGIN))
			{
				cmd_origin( removeQMarks(cmd[1]) );
			}
			else if (cmd[0].equalsIgnoreCase(CMD_DESTINY))
			{
				cmd_destiny( removeQMarks(cmd[1]) );
			}
			else if (cmd[0].equalsIgnoreCase(CMD_OW))
			{				
				cmd_overwrite(cmd[1]);
			}
			else if (cmd[0].equalsIgnoreCase(CMD_PARTS))
			{				
				cmd_parts(cmd[1], pu);
			}
			else if (cmd[0].equalsIgnoreCase(CMD_FILE))
			{				
				cmd_file( removeQMarks(cmd[1]) );
			}
			else if (cmd[0].equalsIgnoreCase(CMD_SPLIT))
			{
				cmd_split(pu);
			}
			else if (cmd[0].equalsIgnoreCase(CMD_EXIT_1) || cmd[0].equalsIgnoreCase(CMD_EXIT_2) ||
					 cmd[0].equalsIgnoreCase(CMD_EXIT_3) || cmd[0].equalsIgnoreCase(CMD_EXIT_4))
			{
				run = false;
			}
			else if (cmd[0].equalsIgnoreCase(CMD_ABOUT))
			{
				cmd_about();
			}
		}
	}

	// ===================================================================
	
	private static void cmd_help()
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
	
	// ===================================================================
	
	private static void cmd_origin(String args)
	{
		System.out.println(NOTIFICATION_TEXT + "Loading PDF");
		
		File file = new File(args);
		if (file.exists() && !file.isDirectory())
		{
			doc_file = args;
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
	
	// ===================================================================
	
	private static void cmd_destiny(String args)
	{
		File file = new File(args);
		if (file.exists() && file.isDirectory())
		{
			folder = args;
			System.out.println(NOTIFICATION_TEXT + "Destiny directory succesfully updated. (New: '" + folder + "')");
		}
	}
	
	// ===================================================================
	
	private static void cmd_overwrite(String args)
	{
		if (args.equals("yes") || args.equals("y"))
			overwrite = true;
		else if (args.equals("no") || args.equals("n"))
			overwrite = false;

		System.out.println(NOTIFICATION_TEXT + "Overwriting: " + overwrite);
	}
	
	// ===================================================================
	
	private static void cmd_parts(String args, PDFUtils pu)
	{
		split = pu.generateSplitList(args, pdf);
		System.out.println(NOTIFICATION_TEXT + "Segments to split:");
		System.out.println(NOTIFICATION_SPACE + pu.splitTextList(split) );
	}
	
	// ===================================================================
	
	private static void cmd_file(String args)
	{
		new_name = args;
		
		System.out.println(NOTIFICATION_TEXT + "Subfiles name saved succesfully. (New: '" + new_name + "')");
	}
	
	// ===================================================================
	
	private static void cmd_split(PDFUtils pu)
	{
		System.out.println(NOTIFICATION_TEXT + "Splitting document.");
		pu.splitAndSave(split, pdf, folder, new_name, overwrite);
		System.out.println(NOTIFICATION_TEXT + "Document succesfully splited.");
	}
	
	// ===================================================================
	
	private static void cmd_about( )
	{
		System.out.println("\"" + SW_NAME + " " + SW_VERSION + "\" was developed by " + SW_AUTHOR);
		System.out.println("Contact: " + SW_WEBSITE);
	}
	
	// ===================================================================
	
	private static String removeQMarks( String input )
	{
		String sz = input;
		
		if (sz.length() > 2 && sz.startsWith("\"") && sz.endsWith("\""))
			sz = input.substring(1, input.length() - 1);
		else
			sz = "";
		
		return sz;
	}

	// ===================================================================
	
	private static String[] spaceCut( String src )
	{
		String[] sz = new String[2];
		
		sz = src.split(" ", 2);
		
		return sz;
	}

	// ===================================================================
}
