package indexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class TermIdInvertedListMap {
	Map<Integer,InvertedList> tiMap;
	
	public TermIdInvertedListMap() {
		tiMap = new HashMap<Integer,InvertedList>(200);
	}
	
	public TermIdInvertedListMap(Map<Integer,InvertedList> map) {
		tiMap=map;
	}
	
	public InvertedList getInvertedList(int termId) {
		return tiMap.get(termId);
	}
	
	public void buildOneDoc(List<Tuple> tuples) {
		if(tuples.size()==0)return;
		int curDocId=tuples.get(0).getDocId();
		Map<Integer,List<Integer>> map = new HashMap<Integer,List<Integer>>();
		Tuple tuple=null;
		List<Integer> list=null;
		for(int i=0,length=tuples.size();i<length;i++) {			
			tuple=tuples.get(i);
			list=map.getOrDefault(tuple.getTermId(), new ArrayList<Integer>());	
			list.add(tuple.getPosition());
			map.put(tuple.getTermId(), list);
		}
		addOneDocToTiMap(map,curDocId);
	}
	public void addOneDocToTiMap(Map<Integer,List<Integer>> map, int curDocId) {
		List<Integer> l;
		DocTF doctf;
		InvertedList il;
		
	    Set<Entry<Integer, List<Integer>>> entrySet = map.entrySet();
	    Iterator<Entry<Integer, List<Integer>>> iter = entrySet.iterator();
	    Entry<Integer, List<Integer>> entry;
	    int key;
	    while (iter.hasNext())
	    {
	        entry= iter.next();
	        l=entry.getValue(); 
	        key=entry.getKey();
	        doctf=new DocTF(l.size(),curDocId, l,key);	       
	        il=tiMap.getOrDefault(key, new InvertedList());
			il.addDocTF(doctf);
			tiMap.put(key, il);
	    }
	}
//	/**
//	 * tuples->docMap
//	 * @param tuples
//	 */
//	public void build(List<Tuple> tuples) {
//		Map<Integer,List<Integer>> map = null;
//		int curDocId=-1;
//		Tuple tuple;
//		List<Integer> list;
//		
//		for(int i=0,length=tuples.size();i<length;i++) {
//			tuple=tuples.get(i);
//			if(curDocId==-1) {
//				map=new HashMap<Integer,List<Integer>>();
//				curDocId=tuple.getDocId();
//			}
//			else if(tuple.getDocId()!=curDocId) {
//				updateMap(map,curDocId);
//				curDocId=tuple.getDocId();
//				map=new HashMap<Integer,List<Integer>>();
//			}
//			list=map.getOrDefault(tuple.getTermId(), new ArrayList<Integer>());	
//			list.add(tuple.getPosition());
//			map.put(tuple.getTermId(), list);
//			if(i==tuples.size()-1) {
//				updateMap(map,curDocId);				
//			}
//		}
//	}
	
//	int i=0;
//	public void buildBigMap(List<Tuple> tuples) {
//		ExecutorService pool = Executors.newFixedThreadPool(3); 
//		int len=tuples.size();
//		while(i<len) {
//			int curDocId=tuples.get(i).getDocId();
//			
//			Map<Integer,List<Integer>> map=buildSmallMap(tuples,pool,len);
//			
//			Set<Entry<Integer, List<Integer>>> entrySet = map.entrySet();
//		    Iterator<Entry<Integer, List<Integer>>> iter = entrySet.iterator();
//		    Entry<Integer, List<Integer>> entry;
//		    List<Integer> l;
//		    int key;
//		    InvertedList il;
//		    while (iter.hasNext())
//		    {
//				entry= iter.next();
//				l=entry.getValue();
//				key=entry.getKey();
//				DocTF doctf=new DocTF(l.size(),curDocId, l,key);				
//				il=tiMap.getOrDefault(key, new InvertedList());
//				il.addDocTF(doctf);
//				tiMap.put(key, il);					
//		    }
//		}
//		
//		pool.shutdown();
//	}
	/**
//	 * Multi-thread build Small Map
//	 * @param tuples
//	 * @return
//	 */
//	public Map<Integer,List<Integer>> buildSmallMap(List<Tuple> tuples,ExecutorService pool,int len) {
//		Map<Integer,List<Integer>> map = new ConcurrentHashMap<Integer,List<Integer>>();
//		if(i==len)return map;
//		int curDocId=tuples.get(i).getDocId();	
//		
//		for(;i<len&&tuples.get(i).getDocId()==curDocId;i++) {
//			Tuple t=tuples.get(i);
//			pool.execute(new Runnable() {
//				@Override
//				public void run() {					
//					List<Integer> l=map.getOrDefault(t.getTermId(), new Vector<Integer>());
//					l.add(t.getPosition());
//					map.put(t.getTermId(), l);
//				}				
//			});	
//		}
//		return map;
//	}
	
	
//	private void updateMap(Map<Integer,List<Integer>> map,int curDocId) {
//		List<Integer> l;
//		DocTF doctf;
//		InvertedList il;
//		
//	    Set<Entry<Integer, List<Integer>>> entrySet = map.entrySet();
//	    Iterator<Entry<Integer, List<Integer>>> iter = entrySet.iterator();
//	    Entry<Integer, List<Integer>> entry;
//	    int key;
//	    while (iter.hasNext())
//	    {
//	        entry= iter.next();
//	        l=entry.getValue();
//	        key=entry.getKey();
//	        doctf=new DocTF(l.size(),curDocId, l,key);	        
//	        il=tiMap.getOrDefault(key, new InvertedList());
//			il.addDocTF(doctf);
//			tiMap.put(key, il);
//	    }
//		
//	}
	public void sort() {
		Map<Integer,InvertedList> sortedMap = new TreeMap<Integer,InvertedList>(tiMap);
		tiMap=sortedMap;
	}
	public Map<Integer,InvertedList> getMap() {
		return tiMap;
	}
//	public void jsonWriter(String filename) throws IOException {
//		JSONObject oj;
//	    Set<Entry<Integer, InvertedList>> entrySet = tiMap.entrySet();
//	    Iterator<Entry<Integer, InvertedList>> iter = entrySet.iterator();
//	    Entry<Integer, InvertedList> entry;
//	    FileWriter fw = new FileWriter(filename, false); 
//	    BufferedWriter bw = new BufferedWriter(fw);
//	    while (iter.hasNext())
//	    {
//	    	entry= iter.next();
//			oj=new JSONObject();
//			oj.put("termId",entry.getKey());			
//			oj.put("InvertedList",entry.getValue().jsonWriter());
//			bw.write("\"termId\":");
//			bw.write(entry.getKey().toString());
//			bw.write("|");
//			bw.write("\"InvertedList\":");
//			bw.write(entry.getValue().jsonWriter().toString());
//			bw.newLine();
//			bw.flush();
//	    }
//	    bw.close();  
//	    fw.close();
//	}
	public void print() {
		System.out.println("------InvertedList-Map----");
		for(int termId : tiMap.keySet()) {
			System.out.println("termId: "+termId);
			tiMap.get(termId).print();
		}
		
	}
	
	public void merge(TermIdInvertedListMap map2) {
		sort();
		map2.sort();
		TreeMap<Integer,InvertedList> treeMap2=(TreeMap<Integer, InvertedList>) map2.getMap();
		TreeMap<Integer,InvertedList> treeMap1 = (TreeMap<Integer, InvertedList>) tiMap;
		
	    Set<Entry<Integer, InvertedList>> entrySet = treeMap2.entrySet();
	    Iterator<Entry<Integer, InvertedList>> iter = entrySet.iterator();
	    Entry<Integer, InvertedList> entry;

	    while (iter.hasNext()) {
	    	entry=iter.next();
	    	int key=entry.getKey();
	    	if(treeMap1.containsKey(key))
	    		treeMap1.get(key).addAll(entry.getValue());
	    	else
	    		treeMap1.put(key, entry.getValue());
	    	iter.remove();
	    }
	}
}
