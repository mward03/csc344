import java.util.ArrayList;
import java.util.Random;

public class DistributionInfo {
	public ArrayList<Key> keyList;
	public float[][] markov;
	int total;
	
	public DistributionInfo() {
		keyList = new ArrayList<Key>();
		markov = new float[128][128];
		total = -1;
	}
	
	public void setTotal(int i) { total = i; }

	public void print(boolean m, boolean k) {
		//print walk matrix
		if (m) {
			for (int i = 0; i < 128; i++) {
				for (int j = 0; j < 128; j++) {
					System.out.print(markov[i][j] + " ");
				}
				System.out.println();
			}
		}
		
		//print sorted list
		System.out.println();
		if (k) {    
			for (Key key : keyList)
				System.out.println(key);
		}
	}
	
	public int getInitialKey() {
		if (total == -1) System.exit(-1);
		
		float curr = 0.0f;
		Random rand = new Random();
		float target =  rand.nextFloat();
		
		for (Key k : keyList) {
			curr += (k.getValue() / (float)total); 
			if (target <= curr) return k.getKey();
		}
		
		return -1;
	}

	public int getNextKey(int note) {
		float curr = 0.0f;
		Random rand = new Random();
		float target =  rand.nextFloat();
		
		for (int i = 0; i < 128; i++) {
			curr += markov[note][i];
			if (target <= curr) return i;
		}
		
		return -1;
	}
}