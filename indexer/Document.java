package indexer;

public class Document {
	private String docNo;
	private int docId;
	public int length;
	
	public Document(String docNo) {
		this.docNo=docNo;
		docId=getDocId();
	}
	public Document(int docId) {
		this.docId=docId;
		docNo=getDocNo();
	}
	
	public String getDocNo() {
		if(docNo!=null)return docNo;
		int left=docId/10000;
		String res="AP89"+(left<1000?("0"+left):left)+"-0"+docId%10000;
		return res;
	}
	public int getDocId() {
		if(docId>0)return docId;
		int res=Integer.parseInt(docNo.substring(4, 8))*10000
				+Integer.parseInt(docNo.substring(9, 13));
		return res;
	}
}
