package query;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import indexer.DocTF;
import indexer.InvertedList;
import indexer.TermIdInvertedListMap;



public class Query {
	int Num=-1;
	private String queryContent;
	Map<Integer,Integer> tokens;
	
	public Query(String line){
		queryContent=line;
		tokens=new HashMap<Integer,Integer>();
	}
	
	public void setTokens(StopList stoplist) {
		String query=queryContent.toLowerCase().replace(",", "").replace(".", "").replace("\"", "").replace("(","").replace(")", "").replace("-", " ");		
		String words[]=query.split(" ",-1);	
		int count=0;
		for(int i=0;i<words.length;i++) {
			String word=words[i];
			if(word.equals(""))continue;
			count++;
			if(Num==-1) {
				Num=Integer.valueOf(words[i]);
				continue;
			}
			if(count<=4) {
				continue;
			}
			
			if(stoplist.contains(word))continue;
			int wh=word.hashCode();
			int f=tokens.getOrDefault(wh, 0);
			f++;
			tokens.put(wh,f);
		}
	}
	
	
	public void queryES(PrintWriter writer,TermIdInvertedListMap timap) {
		if(timap==null) {
			System.out.println("Empty timap!");
			return;
		}
		writer.println("Query "+Num);
//		printTokens();
		int termId;
		InvertedList il;
		// docId, docTFs contain docId
		List<Token> tokenlist = new ArrayList<>();
		Map<Integer,List<DocTF>> docIds=new HashMap<Integer,List<DocTF>>();
		int numTokens=tokens.size();
		Set<Entry<Integer, Integer>> entryset= tokens.entrySet();
		Iterator<Entry<Integer, Integer>> iter=entryset.iterator();
		Entry<Integer,Integer>  entry;
		while(iter.hasNext()) {
			entry=iter.next();
			termId=entry.getKey().hashCode();
			il=timap.getInvertedList(termId);
			if(il==null) continue;
			Token token = new Token(termId,il.size());
			tokenlist.add(token);
			for(int j=0,len=il.size();j<len;j++) {
				DocTF key=il.get(j);
				List<DocTF> doc=docIds.getOrDefault(key.getDocId(), new ArrayList<DocTF>());
				doc.add(key);
				docIds.put(key.getDocId(), doc);
			}			
		}
		//build map from tokenlist 
		Map<Integer,Token> idTokenMap = new HashMap<Integer,Token>();
		for(int i=0,length=tokenlist.size();i<length;i++) {
			Token token=tokenlist.get(i);
			idTokenMap.put(token.getTermId(), token);
		}
		/**
		 * Calculate weight Wi,q
		 */
		for(int i=0,length=tokenlist.size();i<length;i++) {
			Token temp=tokenlist.get(i);
			double tf=getTF(tokens.get(temp.getTermId()));
			double weight=getW(tf,temp.getIDF());
			temp.setQW(weight);
		}
		/**
		 * Documnent with many terms
		 */
		List<List<DocTF>> docSet=getDocSetWithManyTerms(docIds,numTokens-1,numTokens);
		/**
		 * Calculate weight i,j cosine and build maxHeap
		 */
		List<DocTF> doc;
		Queue<Doc> pq = new PriorityQueue<Doc>(new Comparator<Doc>() {
			@Override
			public int compare(Doc o1, Doc o2) {
				// TODO Auto-generated method stub
				double v1=o1.getCos(),v2=o2.getCos();
				return v1>v2?-1:(v1<v2?1:0);
			}
		});
		double s3=0;
		Set<Entry<Integer,Token>> entryset2=idTokenMap.entrySet();
		Iterator<Entry<Integer,Token>> itr=entryset2.iterator();
		Entry<Integer,Token> ent;
		while(itr.hasNext()) {
			ent=itr.next();
			double wq=ent.getValue().getQW();
			s3+=wq*wq;
		}
		
		for(int i=0,length=docSet.size();i<length;i++) {
			doc = docSet.get(i);
			double cos=getCos(doc,idTokenMap,s3);
			if(cos<0.5)continue;
			Doc newdoc=new Doc(cos,doc.get(0).getDocId());
			pq.offer(newdoc);
		}
		int ans=0;
		while(ans<50&&!pq.isEmpty()) {
			writer.println(pq.poll().getDocNo());
			ans++;
		}
	}
	private double getCos(List<DocTF> doc,Map<Integer,Token> idTokenMap,double s3) {
		
		double s1=0;
		double s2=0;
		double wj;
		double wq;
		DocTF doctf;
		for(int j=0,len=doc.size();j<len;j++) {
			doctf=doc.get(j);
			Token token=idTokenMap.get(doctf.getTermId());
			wj=getW(doctf.getTF(),token.getIDF());
			wq=token.getQW();
			s1+=wj*wq;
			s2+=wj*wj;
		}
		double cos=s1/(Math.sqrt(s2)*Math.sqrt(s3));		
		return cos;
	}
	private double getW(double tf,double idf) {
		return tf*idf;
	}
	private double getIDF(int n) {
		int N=84613;
		return Math.log(N*1.0/n)/Math.log(2.0);
	}
	private double getTF(int f) {
		return 1+Math.log(f*1.0)/Math.log(2.0);
	}
//	public void printTokens() {
//		for(Integer token:tokens.keySet()) {
//			System.out.print(token+" ");
//		}
//		System.out.println("");
//	}
	public List<List<DocTF>> getDocSetWithManyTerms(Map<Integer,List<DocTF>> docIds,int p,int numTokens) {
		Set<Entry<Integer,List<DocTF>>> entryset= docIds.entrySet();
		Iterator<Entry<Integer,List<DocTF>>> iter;
		Entry<Integer,List<DocTF>>  entry;
		List<List<DocTF>> docs;
		do {
			docs=new ArrayList<>();
			iter=entryset.iterator();
			while(iter.hasNext()) {
				entry=iter.next();
				if(entry.getValue().size()>=p)
					docs.add(entry.getValue());
			}
			p--;
		}while(docs.size()<=200);		
		return docs;
	}
}
