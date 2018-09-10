package query;

public class Doc {
	private double cos;
	private int docId;
	public Doc(double cos,int docID) {
		this.cos=cos;
		this.docId=docID;
	}
	public double getCos() {
		return cos;
	}
	public int getDocId() {
		return docId;
	}
	public String getDocNo() {
		String res=String.format("AP89%04d-%04d", docId/10000,docId%10000);
		return res;
	}
}
