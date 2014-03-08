import java.util.ArrayList;
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
			 	out += key.toString();
		}
		
		outputPanel.setText(out);
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