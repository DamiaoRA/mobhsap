package sethe.tripbuilder.test;

public class TimeQ {
	private long t1;
	private long t2;
	private String queryName;
	private long count;

	public TimeQ(long t1, long t2, String queryName, long count) {
		this.t1 = t1;
		this.t2 = t2;
		this.queryName = queryName;
		this.count = count;
	}

	public long getT1() {
		return t1;
	}
	public void setT1(long t1) {
		this.t1 = t1;
	}
	public long getT2() {
		return t2;
	}
	public void setT2(long t2) {
		this.t2 = t2;
	}
	public String getQueryName() {
		return queryName;
	}
	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}
	
	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public String toString() {
		return (t2-t1) + "";
	}
}