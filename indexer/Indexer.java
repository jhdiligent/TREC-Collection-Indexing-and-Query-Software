package indexer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;


public class Indexer{
	private Map<Integer,String>	idTermMap;
	private TermIdInvertedListMap timap;
	public Map<Integer, String> getIdTermMap() {
		return idTermMap;
	}
	public TermIdInvertedListMap getTIMap() {
		return timap;
	}
	public void run(String path,String spath) throws IOException {
//		Runtime run=Runtime.getRuntime();
		
		/**
		 * Check number of input parameter
		 */
		if(path==null) {
			System.out.println("Please put dirname of collections as first parameter");
			return;
		}
		//documentProcessor		
		System.out.println("Checking validation of input parameter...");
		DocProcessor docProcessor=new DocProcessor(path);
		if (docProcessor.getDirectory()==null) {
			return;
		}
		//Build stoplist
		StopList stoplist=new StopList();
		stoplist.build(new File(spath));
		//tokenizer
		long startTime=System.currentTimeMillis();
		System.out.println("Tokeninzing...");
		Tokenizer tokenizer = new Tokenizer(docProcessor.getFileList(), docProcessor.getDirectory(),stoplist);
		tokenizer.tokenizingFileList();
		long endTime=System.currentTimeMillis();
		System.out.println((endTime-startTime)/1000.0+"s");
		
		//store map		
//		startTime=System.currentTimeMillis();
//		System.out.println("Preparing termId-term map...");
//		JSONArray idTermMap=tokenizer.jsonWriter();
//		JsonWriter jw=new JsonWriter();
//		jw.write(idTermMap, "id_term_map.txt");
//		endTime=System.currentTimeMillis();
//		System.out.println((endTime-startTime)/1000.0+"s");
//		idTermMap=tokenizer.getMap();
//		long totalmemory=run.freeMemory();
		//indexer
		startTime=System.currentTimeMillis();
		
		List<List<Tuple>> tuples=tokenizer.getTuples();
		System.out.println("There are "+tuples.size()+" documents");
		System.out.println("Building inverted list...");
		TermIdInvertedListMap timap1 = new TermIdInvertedListMap();
		TermIdInvertedListMap timap2 = new TermIdInvertedListMap();
		timap=timap1;
		int i=0,length=tuples.size();
//		System.out.print("Size of each storage is "+length/2);
		for(;i<length;i++) {
			//INDEXING
			timap.buildOneDoc(tuples.get(i));
			if(i==length/2) {
				endTime=System.currentTimeMillis();
				System.out.println("Indexing part1 takes "+(endTime-startTime)/1000.0+"s");
				//update timap				
				timap=timap2;
				startTime=System.currentTimeMillis();
			}
			tuples.set(i, null);
//			System.out.println("Memory usage"+(totalmemory-run.freeMemory()));
		}
		tuples=null;
		endTime=System.currentTimeMillis();
		System.out.println("Indexing part2 takes "+(endTime-startTime)/1000.0+"s");

		//merge two list and store
		startTime=System.currentTimeMillis();
		System.out.print("Merging maps...");
		timap1.merge(timap2);
		timap2=null;
		timap=timap1;
		timap1=null;
		System.out.println("Number of tokens: "+timap.getMap().keySet().size());
//			timap1.jsonWriter("invertedList.txt");
		endTime=System.currentTimeMillis();
		System.out.println("output takes "+(endTime-startTime)/1000.0+"s");
		System.out.println("-----Indexer-Completed-----");
	}
}
