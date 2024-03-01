import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

public class Searchengine {
	public static void print(String result) {
		if(result == null)
			System.out.println("The value is not found");
		else
			System.out.println("Found value is: " + result);
	}
	
	public static void display(DictionaryInterface<String, String> dataBase) {
		Iterator<String> keyIterator = dataBase.getKeyIterator();
		Iterator<String> valueIterator = dataBase.getValueIterator();
		while (keyIterator.hasNext()) {
			System.out.println("Key: " + keyIterator.next() + " Value: " + valueIterator.next());
		}
	}
	private static int search(String[] splitted, String word) {
		int count = 0;
		int i;
        for( i = 0; i < splitted.length ; i++) {
            if (word.equals(splitted[i])) ++count;
        }
        
        return count;
    }
	
	
	public static void main(String[] args)
			throws FileNotFoundException
	{
		
		 
		DictionaryInterface<String, String> dataBase = new HashedDictionary<String, String>();
		int i=0,f=0,v=0;
		/*File highscore = new File("src/resources/sport/001.txt");
		Scanner scHigh = new Scanner(highscore);*/
		//System.out.println(scHigh.next());
		String DELIMITERS = "[-+=" +
		        " " +        //space
		        "\r\n " +    //carriage return line fit
				"1234567890" + //numbers
				"’'\"" +       // apostrophe
				"(){}<>\\[\\]" + // brackets
				":" +        // colon
				"," +        // comma
				"‒–—―" +     // dashes
				"…" +        // ellipsis
				"!" +        // exclamation mark
				"." +        // full stop/period
				"«»" +       // guillemets
				"-‐" +       // hyphen
				"?" +        // question mark
				"‘’“”" +     // quotation marks
				";" +        // semicolon
				"/" +        // slash/stroke
				"⁄" +        // solidus
				"␠" +        // space?   
				"·" +        // interpunct
				"&" +        // ampersand
				"@" +        // at sign
				"*" +        // asterisk
				"\\" +       // backslash
				"•" +        // bullet
				"^" +        // caret
				"¤¢$€£¥₩₪" + // currency
				"†‡" +       // dagger
				"°" +        // degree
				"¡" +        // inverted exclamation point
				"¿" +        // inverted question mark
				"¬" +        // negation
				"#" +        // number sign (hashtag)
				"№" +        // numero sign ()
				"%‰‱" +      // percent and related signs
				"¶" +        // pilcrow
				"′" +        // prime
				"§" +        // section sign
				"~" +        // tilde/swung dash
				"¨" +        // umlaut/diaeresis
				"_" +        // underscore/understrike
				"|¦" +       // vertical/pipe/broken bar
				"⁂" +        // asterism
				"☞" +        // index/fist
				"∴" +        // therefore sign
				"‽" +        // interrobang
				"※" +          // reference mark
		        "]";
				

	String[] stopWord = null;
	String[] searchFile = null;
	long indexTimeTotal = 0;
	//Reading Stop words
	Path pathstop= Path.of("src/resources/stop_words_en.txt");
	try {
		stopWord = Files.readString(pathstop).split(DELIMITERS);
	} catch (IOException e1) {
		System.out.println("Error!");
		e1.printStackTrace();
	}
	List<String> stopWordList = Arrays.asList(stopWord);
	 /*
	//Reading search words
	Path pathsear= Path.of("src/resources/search.txt");
	try {
		searchFile = Files.readString(pathsear).split("\n");
	} catch (IOException e1) {
		System.out.println("Error!");
		e1.printStackTrace();
	}
	*/
	//indexing hashTable
	
	for(f=1;f<=511;f++)
	{
		Boolean filerepeat=false;
		String[] splitted = null;
		String value = null;
		try {
			if(f<=9)
			{
				Path path1= Path.of("src/resources/sport/00"+f+".txt");
				splitted = Files.readString(path1).split(DELIMITERS);
				System.out.println("File: src/resources/sport/00"+f+".txt Loaded!");
			}
			else if (f<=99)
			{
				Path path1= Path.of("src/resources/sport/0"+f+".txt");
				splitted = Files.readString(path1).split(DELIMITERS);
				System.out.println("File: src/resources/sport/0"+f+".txt Loaded!");
			}
			else
			{
				Path path1= Path.of("src/resources/sport/"+f+".txt");
				splitted = Files.readString(path1).split(DELIMITERS);
				System.out.println("File: src/resources/sport/"+f+".txt Loaded!");
			}
			
			
			
		} catch (IOException e) {
			System.out.println("Error!");
			e.printStackTrace();
		}
		
		for(i=0;i<splitted.length;i++)
		{
			if(!stopWordList.contains(splitted[i].toLowerCase()))
			{
				if(f<=9)
				{
					if(dataBase.contains(splitted[i].toLowerCase()))
						value=dataBase.remove(splitted[i].toLowerCase())+"00"+f+".txt"+"@";				
					else 
						value="00"+f+".txt"+"@";
				}
				else if (f<=99)
				{
					if(dataBase.contains(splitted[i].toLowerCase()))
						value=dataBase.remove(splitted[i].toLowerCase())+"0"+f+".txt"+"@";				
					else 
						value="0"+f+".txt"+"@";
				}
				else
				{
					if(dataBase.contains(splitted[i].toLowerCase()))
						value=dataBase.remove(splitted[i].toLowerCase())+f+".txt"+"@";				
					else 
						value=f+".txt"+"@";
				}
				
				long indexingTimeStart=System.nanoTime();
				dataBase.add(splitted[i].toLowerCase(), value);
				long indexingTimeEnd=System.nanoTime();
				indexTimeTotal = indexTimeTotal + indexingTimeEnd - indexingTimeStart;
				value=null;
			}
			
		}
		
	}
	/*
	 //measuring search time
	long searTime = 0 ;
	long avgSearchTime = 0 ;
	long minSearchTime = 0 ;
	long maxSearchTime = 0 ; 
	long sumSearchTime = 0 ;
	System.out.println("Total Search words count: "+ searchFile.length);
	for(int j=0;j<searchFile.length;j++)
	{
		long searchTimeStart=System.nanoTime();
		dataBase.contains(searchFile[j]);
		long searchTimeEnd=System.nanoTime();
		searTime = searchTimeEnd - searchTimeStart;
		if(searTime>maxSearchTime)
			maxSearchTime = searTime;
		if(searTime<minSearchTime)
			minSearchTime = searTime;
		if(minSearchTime==0)
			minSearchTime = searTime;
		sumSearchTime =sumSearchTime + searTime;
		
	}
	
	avgSearchTime = sumSearchTime / searchFile.length;
	*/
	
	//search with special relevance score
		String[] searchWords = null;
		Scanner sc = new Scanner(System.in);  
	    System.out.println("What do you want to search?\n");
	    String searchSentence = null;
	    while(searchSentence==null || searchSentence.split(DELIMITERS).length!=3 )
	    {
	    	 System.out.println("Please Enter 3 words as requested\n");
	    	 searchSentence = sc.nextLine();
	    	 String[] ch=searchSentence.split(DELIMITERS);
	    	 if(ch.length!=0)
	    	 {
	    		 if((dataBase.contains(ch[0].toLowerCase()))==false)
		    		 searchSentence=" ";
		    	 else if(ch.length>0)
		    	 {
		    		 if((dataBase.contains(ch[1].toLowerCase()))==false)
		    			 searchSentence=" ";
		    	 }
		    	 else if(ch.length>1)
		    	 {
		    		 if((dataBase.contains(ch[2].toLowerCase()))==false)
		    			 searchSentence=" ";
		    		  }
		    		 
	    	 }
	    	
	
	    	
	    }
		sc.close();
		System.out.println("You Are going to search:\n"+searchSentence );
		searchWords = searchSentence.split(DELIMITERS);
		
		Integer first=0,second=0,third=0;
		String at="@";
		Float score1 = 0f ;
		Float score2 = 0f ;
		Float score3 = 0f ;
		String file1 = null;
		String file2 = null;
		String file3 = null;
		
		String[] filesContains1stWord;
		String[] filesContains2ndWord;
		String[] filesContains3rdWord;
		
		filesContains1stWord = dataBase.getValue(searchWords[0].toLowerCase()).split(at);
		filesContains2ndWord = dataBase.getValue(searchWords[1].toLowerCase()).split(at);
		filesContains3rdWord = dataBase.getValue(searchWords[2].toLowerCase()).split(at);
		
		
		for(i=0;i<filesContains1stWord.length;i++)
		{
			
			if(i>0)
			{
				
				if(!filesContains1stWord[i].equals(filesContains1stWord[i-1]))
				{
					
					if(search(filesContains2ndWord, filesContains1stWord[i])!=0 || search(filesContains3rdWord, filesContains1stWord[i])!=0)
					{
						String filetemp = filesContains1stWord[i];
						Float dif1=0f,dif2=0f,dif3=0f, difTotal =0f, sum=0f;
						Float scoretemp= 0f;
						dif1 = (float) Math.abs(search(filesContains1stWord, filesContains1stWord[i])-search(filesContains2ndWord, filesContains1stWord[i]));
						dif2 = (float) Math.abs(search(filesContains1stWord, filesContains1stWord[i])-search(filesContains3rdWord, filesContains1stWord[i]));
						dif3 = (float) Math.abs(search(filesContains3rdWord, filesContains1stWord[i])-search(filesContains2ndWord, filesContains1stWord[i]));
						difTotal = dif1 + dif2 + dif3;
						if(difTotal==0)
							difTotal++;
						sum=(float) (search(filesContains1stWord, filesContains1stWord[i])+search(filesContains2ndWord, filesContains1stWord[i])+search(filesContains3rdWord, filesContains1stWord[i]));
						scoretemp= (float) (sum / difTotal);
						if(scoretemp>score1)
						{
							score3=score2;
							score2=score1;
							score1=scoretemp;
							file3=file2;
							file2=file1;
							file1=filetemp;
							
						}
						else if(scoretemp>score2)
						{
							score3=score2;
							score2=scoretemp;
							file3=file2;
							file2=filetemp;
						}
						else if(scoretemp>score3)
						{
							score3=scoretemp;
							file3=filetemp;
						}
						
							
					}					
				}
			}
			else
			{
				
				if(search(filesContains2ndWord, filesContains1stWord[i])!=0&&search(filesContains3rdWord, filesContains1stWord[i])!=0)
				{
					String filetemp = filesContains1stWord[i];
					Float dif1=0f,dif2=0f,dif3=0f, difTotal =0f, sum=0f;
					Float scoretemp= 0f;
					dif1 = (float) Math.abs(search(filesContains1stWord, filesContains1stWord[i])-search(filesContains2ndWord, filesContains1stWord[i]));
					dif2 = (float) Math.abs(search(filesContains1stWord, filesContains1stWord[i])-search(filesContains3rdWord, filesContains1stWord[i]));
					dif3 = (float) Math.abs(search(filesContains3rdWord, filesContains1stWord[i])-search(filesContains2ndWord, filesContains1stWord[i]));
					difTotal = dif1 + dif2 + dif3;
					if(difTotal==0)
						difTotal++;
					sum=(float) (search(filesContains1stWord, filesContains1stWord[i])+search(filesContains2ndWord, filesContains1stWord[i])+search(filesContains3rdWord, filesContains1stWord[i]));
					scoretemp= (float) (sum / difTotal);
					if(scoretemp>score1)
					{
						score3=score2;
						score2=score1;
						score1=scoretemp;
						file3=file2;
						file2=file1;
						file1=filetemp;
						
					}
					else if(scoretemp>score2)
					{
						score3=score2;
						score2=scoretemp;
						file3=file2;
						file2=filetemp;
					}
					else if(scoretemp>score3)
					{
						score3=scoretemp;
						file3=filetemp;
					}
					
				}
				
				
			}
			
			
		}
		if(score1!=0)
		{
			if(score2!=0)
			{
				if(score3!=0)
				{
					System.out.println("Three most relevent files are:\n1- "+ file1 + " with relevency score of : " + score1+"\n2- " + file2 + " with relevency score of : " + score2+"\n3- " + file3 + " with relevency score of : " + score3);
				}
				else
					System.out.println("Olny 2 relevent file found:\n1- "+ file1 + " with relevency score of : " + score1+"\n2- " + file2 + " with relevency score of : " + score2);
			}
			else
				System.out.println("Olny 1 relevent file found:\n1- "+ file1 + " with relevency score of : " + score1);
			
				
		}
		else
			System.out.println("Relevent file not found!");
		
		
	
		/*
		System.out.println("Collision Count is: "+dataBase.getCollisionCount());
		System.out.println("Indexing time is: "+ indexTimeTotal);
		System.out.println("Avg. Search Time: "+ avgSearchTime);
		System.out.println("Min. Search Time: "+ minSearchTime);
		System.out.println("Max. Search Time: "+ maxSearchTime);
		*/

	}
}
