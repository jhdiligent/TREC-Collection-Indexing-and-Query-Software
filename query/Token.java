package query;

public class Token {
	private int termId;
	private double IDF;
	private double qw;
	public Token(int termId,int n) {
		this.termId=termId;
		int N=84613;
		IDF= Math.log(N*1.0/n)/Math.log(2.0);
	}
	public double getIDF() {
		return IDF;
	}
	public void setQW(double qw) {
		this.qw=qw;
	}
	public int getTermId() {
		return termId;
	}
	public double getQW() {
		return qw;
	}
}
