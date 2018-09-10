package indexer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class StopList {
	StopListTrieTree tree;
	
	StopList(){
		tree=new StopListTrieTree();
	}
	
	public void addWord(String word) {
		tree.addWord(word);
	}
	
	public boolean contains(String word) {
		return tree.inStopList(word);
	}
	
	public void build(File file) throws IOException {
		FileReader fileReader=new FileReader(file);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
	    String word;
	    System.out.println("Building StopList...");
	    while((word=bufferedReader.readLine())!=null) {
	    	addWord(word);
	    }
	    bufferedReader.close();
	    fileReader.close();
	    
	}
}
