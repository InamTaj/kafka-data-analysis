package inam.java.models;

import java.util.Arrays;

public class SensorInput {
	private int id;
	private long ts;
	private int t;
	private int[] v = new int[3];
	private int[] i = new int[3];

	public SensorInput(int id, long ts, int t, int[] v, int[] i) {
		this.id = id;
		this.ts = ts;
		this.t = t;
		this.v = v;
		this.i = i;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public long getTs() {
		return ts;
	}

	public void setTs(long ts) {
		this.ts = ts;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	public int[] getV() {
		return v;
	}

	public void setV(int[] v) {
		this.v = v;
	}

	public int[] getI() {
		return i;
	}

	public void setI(int[] i) {
		this.i = i;
	}

	@Override
	public String toString() {
		return "{" +
				"id=" + id +
				", ts=" + ts +
				", t=" + t +
				", v=" + Arrays.toString(v) +
				", i=" + Arrays.toString(i) +
				'}';
	}
}
