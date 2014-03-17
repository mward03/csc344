import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

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

	public void print(boolean m, boolean k, JTextPane outputPanel) {
		//print walk matrix
		String out = outputPanel.getText();
		if (m) {
			for (int i = 0; i < 128; i++) {
				for (int j = 0; j < 128; j++) {
					out += Float.toString(markov[i][j]) + " ";
					//System.out.print(markov[i][j] + " ");
				}
				out += '\n';
				//System.out.println();
			}
		}
		
		//print sorted list
		//System.out.println();
		out += '\n';
		if (k) {    
			for (Key key : keyList)
				//System.out.println(key);
			 	out += (key.toString() + '\n');
		}
		
		outputPanel.setText(out);
	}
	
	public int getInitialKey(String key) {
		if (key != "All") {
			
			int[] notes = null;
			if (key != "All") {
				Global g = new Global();
				HashMap<String, int[]> notesInKey = g.getKeys();
				notes = notesInKey.get(key);
			}
			
			float curr = 0.0f;
			Random rand = new Random();
			float target =  rand.nextFloat();
			int found = 0;
			
			ArrayList<Key> temp = new ArrayList<Key>();
			
			for (Key k : keyList) {
				for (int n : notes) {
					if (k.getKey() % 12 == n) {
						found = 1;
					}
				}
				if (found == 1) temp.add(k);
				found = 0;
			}
			
			for (Key k : temp) {
				curr += (k.getValue() / (float)total); 
				if (target <= curr) return k.getKey();
			}

			
		} else {
		
			float curr = 0.0f;
			Random rand = new Random();
			float target =  rand.nextFloat();
			
			for (Key k : keyList) {
				curr += (k.getValue() / (float)total); 
				if (target <= curr) return k.getKey();
			}
		}
		
		return -1;
	}

	public int getNextKey(int note, String key) {
		if (key == "All") {
			float curr = 0.0f;
			Random rand = new Random();
			float target =  rand.nextFloat();

			for (int i = 0; i < 128; i++) {
				curr += markov[note][i];
				if (target <= curr) return i;
			}

			return -1;
		}
		
		
		int[] notes = null;
		Global g = new Global();
		HashMap<String, int[]> notesInKey = g.getKeys();
		notes = notesInKey.get(key);
		
		float curr = 0.0f;
		Random rand = new Random();
		float target =  rand.nextFloat();
		int found = 0;
		
		float[] poss = markov[note];
		for (int i = 0; i < 128; i++) {
			for (int n : notes) {
				if ( n != i % 12 ) {
					found = 1;
				}
			}
			if (found == 0) {
				poss[i] = 0.0f;
			}
			found = 0;
		}
		
		poss = updateArr(poss);
		
		for (int i = 0; i < 128; i++) {
			curr += poss[i];
			for (int n : notes) {
				if ( n == i % 12 ) {
					if (target <= curr) return i;
				}
			}
		}
		
		return -1;
	}
	
	private float[] updateArr(float[] arr) {
		int count = 0;
		for (int j = 0; j < 128; j++) {
			count += arr[j];
		}
		for (int j = 0; j < 128 && count > 0; j++) {
			arr[j] /= count;
		}
		return arr;
	}
}