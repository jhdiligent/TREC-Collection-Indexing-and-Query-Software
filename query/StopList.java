package query;

import java.io.File;

public class StopList {
	StopListTrieTree tree;
	
	public StopList(){
		tree=new StopListTrieTree();
	}
	
	public void addWord(String word) {
		tree.addWord(word);
	}
	
	public boolean contains(String word) {
		return tree.inStopList(word);
	}

}
