import java.util.ArrayList;

public class Key {
	private int key;
	private int value;
	private ArrayList<Long> times;
	private int inst;
	
	public Key(int key, long time, int instr) {
		this.key = key;
		value = 1;
		times = new ArrayList<Long>();
		this.inst = inst;
		times.add(time);
	}
	
	public boolean equals(Object x) {
		return (x instanceof Key && ((Key)x).getKey() == key); 
	}
	
	public void inc(Long time) {
		value++;
		times.add(time);
	}
	
	public int getKey() { return key; }
	public int getValue() { return value; }
	public ArrayList<Long> getTimes() { return times; }
	public int getInst() { return inst; }
	
	public void setKey(int key) { this.key = key; }
	public void setValue(int value) { this.value = value; }
	public void setInst(int inst) { this.inst = inst; }
	
	public String toString() {
		String ret = "";
		ret += "(key: " + key + ", value: " + value + ")";
		return ret;
	}
}