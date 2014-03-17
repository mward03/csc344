import java.io.File;
import java.util.ArrayList;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class Parser {
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    public static DistributionInfo run(File midi, DistributionInfo di, JTextPane outputPanel) throws Exception { 	
        Sequence sequence = MidiSystem.getSequence(midi);
        int total = 0;
        int temp;
        ArrayList<Key> buffer = new ArrayList<Key>();
        Key tempKey;
       
//        int trackNumber = 0;

        for (Track track :  sequence.getTracks()) {
//            trackNumber++;
//            System.out.println("Track " + trackNumber + ": size = " + track.size());
//            System.out.println();
            for (int i=0; i < track.size(); i++) { 
                MidiEvent event = track.get(i);
//                System.out.println("@" + event.getTick() + " ");
                MidiMessage message = event.getMessage();
                if (message instanceof ShortMessage) {
                    ShortMessage sm = (ShortMessage) message;
//                    System.out.println("Channel: " + sm.getChannel() + " ");
                    if (sm.getCommand() == NOTE_ON) {
                        int key = sm.getData1();

                        tempKey = new Key(key, System.currentTimeMillis(), sm.getData2());
                        
                        // Generate Sorted list of notes
                        temp = contains(di.keyList, key);
                        if (temp != -1) {
                        	di.keyList.get(temp).inc(System.currentTimeMillis());
                        } else {
                        	di.keyList.add(tempKey);
                        }
                        total++;
                        
                        
                        int found = 0;
                        Key used = null;
                        //determine walk
                        for (Key k : buffer) {
                        	if (k.getInst() == tempKey.getInst()) {
                        		di.markov[k.getKey()][tempKey.getKey()] += 1;
                        		used = k;
                        		found = 1;
                        		break;
	                		} 
                        }
                        if (found == 0) {
                        	buffer.add(tempKey);
                        } else {
                        	buffer.remove(used);
                        	buffer.add(tempKey);
                        }
                        found = 0;
                        
                    }
                }
            }
        }     
        
        //Sort list of notes
        ArrayList<Key> ret = new ArrayList<Key>(di.keyList.size());
        int max = di.keyList.size();
        int m = -1;
        int ndx = 0;
        
        for (int i = 0; i < max; i++) {
        	for (Key k : di.keyList) {
        		if (k.getValue() > m) {
        			m = k.getValue();
        			ndx = di.keyList.indexOf(k);
        		}
        	}
        	ret.add(di.keyList.get(ndx));
        	di.keyList.remove(ndx);
        	ndx = 0;
        	m = -1;
        }
        di.keyList = ret;
        di.setTotal(total);
        
        //print distribution info (markov, keyList)
    	di.print(true, true, outputPanel);
        
        return di;
    }

	private static int contains(ArrayList<Key> keyList, int key) {
		int ndx = 0;
		for (Key k : keyList) {
			if (k.getKey() == key)
				return ndx;
			ndx++;
		}
		return -1;
	}
}