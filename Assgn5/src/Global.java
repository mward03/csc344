import java.util.HashMap;


public class Global {
	public int[] aMajor = {0, 2, 4, 5, 7, 9, 11};
	public int[] bMajor = {2, 4, 6, 7, 9, 11};
	public int[] cMajor = {0, 2, 3, 5, 7, 8, 10, 12};
	public int[] dMajor = {5, 7, 9, 10, 0, 2};
	public int[] eMajor = {7, 9, 11, 0, 2, 4, 6};
	public int[] fMajor = {7, 8, 0, 1, 3, 5, 7};
	public int[] gMajor = {10, 0, 2, 3, 5, 7, 9};
	
	public int[] aMinor = {0, 2, 3, 5, 7, 8, 10};
	public int[] bMinor = {2, 4, 5, 7, 9, 10};
	public int[] cMinor = {3, 5, 6, 10, 11, 1};
	public int[] dMinor = {5, 7, 8, 10, 0, 1};
	public int[] eMinor = {07, 9, 10, 0, 2, 3, 5};
	public int[] fMinor = {8, 11, 1, 3, 4, 6};
	public int[] gMinor = {10, 0, 1, 3, 5, 6, 7};	
	
	HashMap<String, int[]> hm = new HashMap<String, int[]>();
	
	public Global() {
		hm = new HashMap<String, int[]>();

		hm.put("aMajor", aMajor);
		hm.put("bMajor", bMajor);
		hm.put("cMajor", cMajor);
		hm.put("dMajor", dMajor);
		hm.put("eMajor", eMajor);
		hm.put("fMajor", fMajor);
		hm.put("gMajor", gMajor);
		
		hm.put("aMinor", aMinor);
		hm.put("bMinor", bMinor);
		hm.put("cMinor", cMinor);
		hm.put("dMinor", dMinor);
		hm.put("eMinor", eMinor);
		hm.put("fMinor", fMinor);
		hm.put("gMinor", gMinor);
	}
	
	public HashMap<String, int[]> getKeys() { return hm; }
}
