package Core;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

public class PDFUtils
{
	// ===================================================================
	
	public void splitAndSave( List<Pair<Integer>> recortes, PDDocument pdf, String destiny, String name, boolean overwrite )
	{
		List<PDDocument> sub_pdfs = new LinkedList<PDDocument>();
		int i = 1;
		
		for (Pair<Integer> p : recortes)
		{
			Splitter splitter = new Splitter();
			splitter.setStartPage( p.getKey() );
			splitter.setEndPage( p.getValue() );
			splitter.setSplitAtPage( p.getValue() - p.getKey() + 1 );
			List<PDDocument> pages;
			try
			{
				pages = splitter.split(pdf);
				sub_pdfs.add( pages.get(0) );
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		File f;
		String	fdir,
				page;
		
		i = 0;
		// Saving each page as an individual document
		for (PDDocument spdf : sub_pdfs)
		{
			try
			{
				if (recortes.get(i).getKey() == recortes.get(i).getValue())
					page = "" + recortes.get(i).getKey();
				else
					page = "" + recortes.get(i).getKey() + "-" + recortes.get(i).getValue();
				
				fdir = destiny + "\\" + name + " " + page + ".pdf";
				
				f = new File(fdir);
				if (f.exists())
				{
					if (overwrite)
					{
						f.delete();
						System.out.printf("Saving segment '%s'.\n", page);
						spdf.save(fdir);
					}
					else
					{
						System.out.printf("Segment '%s' could not be saved, as a file with the same name already exists.\n", page);
					}
				}
				else
				{
					System.out.printf("Saving segment '%s'.\n", page);
					spdf.save(fdir);
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			i ++;
		}
	}

	// ===================================================================
	
	public String splitTextList( List<Pair<Integer>> list )
	{
		String res = "";
		int start, end;
		boolean first = true;
		
		for (Pair<Integer> p : list)
		{
			start = p.getKey();
			end = p.getValue();
			
			if (!first)
				res += ", ";
			else
				first = false;
			
			if (start == end)
				res += start;
			else
				res += start + "-" + end;
		}
		
		return res;
	}

	// ===================================================================
	
	public List<Pair<Integer>> generateSplitList( String text, PDDocument doc )
	{		
		List<Pair<Integer>> lista = new LinkedList<Pair<Integer>>( );
		String parsed = removeSpaces(text);
		String[] intervals = parsed.split(",");
		String[] subinterval;
		int start, end = 0;
		
		for (int i = 0; i < intervals.length; i ++)
		{
			if (intervals[i].indexOf('-') > -1)
			{
				subinterval = intervals[i].split("-");
				
				if (subinterval[0].equalsIgnoreCase("s"))
					start = 1;
				else if (subinterval[0].equalsIgnoreCase("n"))
					start = (end + 1);
				else
					start = Integer.parseInt(subinterval[0]);
				
				if (subinterval[1].equalsIgnoreCase("e"))
					end = doc.getNumberOfPages();
				else
					end = Integer.parseInt(subinterval[1]);
			}
			else
			{
				if (intervals[i].equalsIgnoreCase("s"))
					start = 1;
				else if (intervals[i].equalsIgnoreCase("n"))
					start = (end + 1);
				else if (intervals[i].equalsIgnoreCase("e"))
					start = doc.getNumberOfPages();
				else
					start = Integer.parseInt(intervals[i]);
				
				end = start;
			}
			
			lista.add( new Pair<Integer>( start, end ) );
		}
		
		return lista;
	}

	// ===================================================================
	
	private String removeSpaces( String text )
	{
		String result = "";
		char c;
		
		for (int i = 0; i < text.length(); i ++)
		{
			c = text.charAt(i);
			if (c != ' ' && c != '\t' && c != '\n' && c != '\r')
				result = result + c;
		}
		
		return result;
	}
	
	// ===================================================================
}
