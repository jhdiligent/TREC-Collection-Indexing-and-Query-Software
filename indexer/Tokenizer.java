package indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class Tokenizer {
	private String[] fileList;
	private String directory;
	private List<List<Tuple>> tuples;
	private Map<Integer,String> termId_termMap;
	StopList stoplist;
	
	public Tokenizer(String[] fileList,String directory,StopList stoplist) {
		this.fileList=fileList;
		this.directory=directory;
		tuples=new Vector<List<Tuple>>();
		termId_termMap = new HashMap<Integer,String>();
		this.stoplist=stoplist;
	}
	
	public void tokenizingFileList() {
		
		for(int i=0;i<fileList.length;i++) {
			String filename=fileList[i];
			tokenizingSingleFile(filename);
		}
	}
	
	public void tokenizingSingleFile(String filename) {
		parseFile(directory,filename);	
	}
	
	private void parseFile(String path,String filename) {		
		try {			
			File file = new File(path,filename);
			FileReader fileReader = new FileReader(file);		    
			BufferedReader bufferedReader = new BufferedReader(fileReader);
		    String line;

			String docStart="<DOC>";
			String docEnd="</DOC>";
			String docnoSign="<DOCNO>";
			String textStart="<TEXT>";
			String textEnd="</TEXT>";
			boolean docStarted =false;
			boolean readStarted=false;
			Document doc = null;
			int position=0;
			List<Tuple> curTup = null;
		    while((line=bufferedReader.readLine())!=null) {		    	
		    	//analysis the line
		    	if(!docStarted) {
			    	if(line.equals(docStart)) {
			    		curTup=new Vector<Tuple>();
			    		position=0;
			    		docStarted=true;		
			    	}
		    	}
		    	else{//in a document
		    		if(line.equals(docEnd)) {//doc ends
		    			if(curTup.size()>0)
		    				tuples.add(curTup);
		    			doc.length=position;
		    			docStarted=false;
		    			continue;
		    		}
		    		if(readStarted) {
		    			if(line.equals(textEnd)) {
		    				readStarted=false;
		    				continue;
		    			}
		    			if(line!=null) {		
			    			position=tokenizingALine(line,position,doc.getDocId(),curTup);
		    			}
		    			continue;
		    		}
		    		if(line.startsWith(docnoSign)) {
		    			String docnos[] =line.split(" ");
		    			doc=new Document(docnos[1]);	    			
		    		}
		    		else if(line.equals(textStart)){
		    			readStarted=true;
		    		}
		    	}	    	
		    } 
		    bufferedReader.close();
		    fileReader.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	public int tokenizingALine(String line,int position,int docId,List<Tuple> curTup) {
		String newline=line.replace("''", "")
		           .replace(";", "")
		           .replace("\"", "")
		           .replace("``", "")
		           .replace("\\]", "")
		           .replace("\\[", "")
		           .replace("\\{", "")
		           .replace("\\}", "")
		           .replace("\\|", "")
		           .replace("\\", "")
		           .replace(",", "")
		           .replace("-"," ")
		           .replace("_", "");
		String[] preTokens=newline.split(" ");
		for(int i=0;i<preTokens.length;i++) {
			String preToken=preTokens[i];
			if(preToken.length()==0||preToken.equals(""))continue;
			preToken=preToken.toLowerCase();
			//skip the date format
			if(preToken.charAt(0)!='$')
				preToken=preToken.replace(".","");
			if(preToken.endsWith("."))
				preToken=preToken.substring(0, preToken.length()-2);
			if(preToken.length()==0||preToken.equals(""))continue;
			//Stoplist
			if(stoplist.contains(preToken))continue;			
			//stemmer
			position++;
			int termId=preToken.hashCode();
			Tuple tuple=new Tuple(termId,docId,position);		
			curTup.add(tuple);
			termId_termMap.put(termId, preToken);
		}
		return position;
	}
	
	public List<List<Tuple>> getTuples() {
		return tuples;
	}
	
	public Map<Integer,String> getMap(){
		return termId_termMap;
	}
	
//	public void printTuples() {
//		System.out.println("-------------Tuples------------");
//		System.out.println("   TermId    docId    position");
//		for(Tuple tuple: tuples) {
//			System.out.println("   "+tuple.getTermId()+"    "+tuple.getDocId()+"    "+tuple.getPosition());
//		}
//	}
	public void printMap() {
		System.out.println("------termId-term-Map-------");
		for(int key:termId_termMap.keySet()) {
			System.out.println("id: "+key+"   term: "+termId_termMap.get(key));
		}
	}
//	public JSONArray jsonWriter() {
//		JSONArray arr=new JSONArray();
//		for(int termid:termId_termMap.keySet()) {
//			JSONObject oj=new JSONObject();
//			oj.put("termId", termid);
//			oj.put("term", termId_termMap.get(termid));
//			arr.put(oj);
//		}
//		return arr;
//	}
}
