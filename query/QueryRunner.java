package query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import indexer.Indexer;
import indexer.TermIdInvertedListMap;


public class QueryRunner {
	private Map<Integer,String>	idTermMap;
	private TermIdInvertedListMap timap;
	/**
	 * StopList building, StemClasses Reading,and query
	 * @param args input params
	 * @throws IOException
	 */
	public void run(String args[]) throws IOException {
		File[] files;
		/**
		 * Vertify validation of input params
		 */
		if((files=vertifyInputParams(args))==null)
			return;
		
//		File file = new File("stoplist.txt");
//		if(!file.exists()||!file.isFile()) {
//			System.out.println("Can't find stoplist to build queries");
//		}
		/**
		 * Build StopList from stoplist.txt
		 */
		StopList stoplist=buildStopList(files[1]);

		/**
		 * Get inverted list
		 */
		Indexer indexer = new Indexer();
		indexer.run(args[2],args[1]);
		timap=indexer.getTIMap();
		if(timap==null) {
			System.out.println("timap is empty");
		}
		/**
		 * Query and write result to query_result.txt
		 */
		query(files[0],stoplist);
		
	}
	/**
	 * Vertify validation of input params
	 * @param args path of files
	 * @return files
	 */
	public File[] vertifyInputParams(String args[]) {
		if(args.length!=3) {
			System.out.println("Please put enough parameters");
			return null;
		}
		if(!args[0].endsWith(".txt")) {
				System.out.println("Please put queries as first parameter");
		}
		if(!args[1].endsWith("stoplist.txt")) {
			System.out.println("Please put stoplist as second parameter");
		}
		File[] files=new File[2];
		for(int i=0;i<2;i++) {
			files[i]=new File(args[i]);
			if(!files[i].exists()) {
				System.out.println("argument "+i+" doesn't exist");
			    return null;
			}
			if(!files[i].isFile()) {
				System.out.println("At least ont file path isn't point to a file");
				return null;
		}


			
		}
		return files;
	}
	/**
	 * Build StopList from stoplist.txt
	 * @param file stoplist.txt
	 * @return built stoplist
	 * @throws IOException
	 */
	public StopList buildStopList(File file) throws IOException {
		FileReader fileReader=new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
	    String word;
	    StopList stoplist=new StopList();
	    System.out.println("Building StopList...");
	    while((word=bufferedReader.readLine())!=null) {
	    	stoplist.addWord(word);
	    }
	    bufferedReader.close();
	    fileReader.close();
	    return stoplist;
	}

	/**
	 * Query and write results to query_result.txt
	 * @param file query_desc.51-100.short.txt
	 * @param stoplist stoplist
	 * @param stemclasses stemclasses
	 * @throws IOException
	 */
	public void query(File file,StopList stoplist) throws IOException {
		FileReader fileReader=new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
	    String line;	
	    List<Query> queryList = new LinkedList<Query>();

		PrintWriter writer = new PrintWriter("query_result.txt", "UTF-8");
	    while((line=bufferedReader.readLine())!=null) {
	    	if(line.equals(""))continue;
	    	Query q = new Query(line);
	    	q.setTokens(stoplist);
	    	q.queryES(writer,timap);
	    	queryList.add(q);
	    }		
	    System.out.println("Run "+queryList.size()+" queries");
	    writer.close();	    
		bufferedReader.close();
		fileReader.close();
	}
}
