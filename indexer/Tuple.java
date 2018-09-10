package indexer;

public class Tuple {
	private int termId;
	private int docId;
	private int position;
	public Tuple(int termId,int docId, int position) {
		this.termId=termId;
		this.docId=docId;
		this.position=position;
	}
	public int getTermId() {
		return termId;
	}
	public int getDocId() {
		return docId;
	}
	public int getPosition() {
		return position;
	}
	public void print() {
		System.out.println("   "+termId+"    "+docId+"    "+position);
	}
}
