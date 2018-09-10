package indexer;

public class TrieNode {
	TrieNode[] characters;
	
	boolean isStopWord;
	
	TrieNode(){
		characters=new TrieNode[27];
		isStopWord=false;
	}
	
	
}
