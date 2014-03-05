import java.util.ArrayList;

public class Key {
	private int key;
	private int value;
	private ArrayList<Long> times;
	
	public Key(int key, long time) {
		this.key = key;
		value = 1;
		times = new ArrayList<Long>();
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
	
	public void setKey(int key) { this.key = key; }
	public void setValue(int value) { this.value = value; }
	
	public String toString() {
		String ret = "";
		ret += "(key: " + key + ", value: " + value + ")";
//		for (Long l : times) 
//			ret += (" " + l);
//		ret += ('\n' + '\n');
		return ret;
	}
}