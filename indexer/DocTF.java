package indexer;

import java.util.List;

public class DocTF {
	private int docId;
	private double tf;
	private List<Integer> positions;
	private int termId;
	
	public DocTF(int frequency,int docId,List<Integer> positions,int termId) {
		this.positions=positions;
		this.docId=docId;
		tf=1+(Math.log(frequency*1.0)/Math.log(2.0));
		this.termId=termId;
	}
	public int size() {
		return positions.size();
	}
	public int getDocId() {
		return docId;
	}
	public double getTF() {
		return tf;
	}
	public int getTermId() {
		return termId;
	}
//	public JSONObject jsonWriter() {
//		JSONObject doctf=new JSONObject();
//		doctf.put("docId", docId);
//		doctf.put("TF", tf);
//		JSONArray ps=new JSONArray();
//		for(int p:positions) {
//			ps.put(p);
//		}
//		doctf.put("positions", ps);
//		
//		return doctf;
//	}
	public void print() {
		System.out.println("-------------");
		System.out.println("docId: "+docId);
		System.out.println("TF: "+tf);
		int i=0;
		for(int p : positions) {
			i++;
			System.out.println("postion"+i+" : "+p);
		}
		System.out.println("-------------");
	}
}
