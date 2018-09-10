package indexer;

public class StopListTrieTree {
	TrieNode root;
	
	public StopListTrieTree() {
		root=new TrieNode();
	}
	
	public void addWord(String word) {
		char[] cs=word.toCharArray();
		TrieNode cur=root;
		for(char c: cs) {
			int index=c-'a';
			if(c=='\''){
				index=26;
			}
			if(cur.characters[index]==null) {
				cur.characters[index]=new TrieNode();
			}
			cur=cur.characters[index];
		}
		cur.isStopWord=true;
	}
	
	public void build(String[] words) {
		for(String word : words) {
			addWord(word);
		}
	}
	
	public boolean inStopList(String word) {
		if(word==null)return false;
		char[] cs=word.toCharArray();
		TrieNode cur=root;
		for(char c: cs) {
			int index=c-'a';
			if(c=='\'')index=26;
			if(index<0||index>26)return false;
			if(cur.characters[index]==null) {
				return false;
			}
			cur=cur.characters[index];
		}
		return cur.isStopWord;	
	}
}
