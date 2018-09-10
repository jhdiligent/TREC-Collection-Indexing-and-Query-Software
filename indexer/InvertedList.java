package indexer;

import java.util.ArrayList;
import java.util.List;

public class InvertedList{
	List<DocTF> docTFs;
	
	public InvertedList() {
		docTFs=new ArrayList<DocTF>();
	}
	
	public void addDocTF(DocTF doctf) {
		docTFs.add(doctf);
	}
//	public JSONArray jsonWriter() {
//		JSONArray oj=new JSONArray();
//		DocTF docTF;
//		for(int i=0,length=docTFs.size();i<length;i++) {
//			docTF=docTFs.get(i);
//			oj.put(docTF.jsonWriter());
//		}
//		
//		return oj;
//	}
	public void print() {
		System.out.println("------InvertedList----");
		for(DocTF d : docTFs) {
			d.print();
		}
		System.out.println("-----end-----");
	}
	
	public int size() {
		return docTFs.size();
	}
	
	public DocTF get(int index) {
		return docTFs.get(index);
	}
	
	public void addAll(InvertedList invertedList) {
		for(int i=0,length=invertedList.size();i<length;i++) {
			docTFs.add(invertedList.get(i));
		}		
	}
	public List<Integer> getDocIds(){
		List<Integer> l=new ArrayList<>();
		for(int i=0,length=docTFs.size();i<length;i++) {
			DocTF doc=docTFs.get(i);
			l.add(doc.getDocId());
		}
		return l;
	}
}
