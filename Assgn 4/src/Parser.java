import java.io.File;
import java.util.ArrayList;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class Parser {
    public static final int NOTE_ON = 0x90;
    public static final int NOTE_OFF = 0x80;
    public static final String[] NOTE_NAMES = {"C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"};

    public static DistributionInfo run(File midi, DistributionInfo di) throws Exception {
    	
        Sequence sequence = MidiSystem.getSequence(midi);
        long time;
        int temp;
        int total = 0;
        
        int prev = -1;
        int trackNumber = 0;

        for (Track track :  sequence.getTracks()) {
            trackNumber++;
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
//                        int note = key % 12;
//                        String noteName = NOTE_NAMES[note];
                        //System.out.println("Key: " + key + ", note: " + note + " note name: " + noteName + " instrument: ");

                        // Generate Sorted list of notes
                        time = System.currentTimeMillis();
                        temp = contains(di.keyList, key);
                        if (temp != -1) {
                        	di.keyList.get(temp).inc(System.currentTimeMillis());
                        } else {
                        	di.keyList.add(new Key(key, System.currentTimeMillis()));
                        }
                        total++;
                        
                        //if (total >= 10) break;
                        
                        //determine walk
                        if (prev == -1) {
                        	prev = key;
                        } else {
                        	di.markov[prev][key] += 1;
                        	prev = key;
                        }
                        
                    }
                }
            }
        }
        
        updateMarkov(di);
        
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
        //di.print(true, true);
        di.print(true, true);
        
        return di;
    }

    // Turns matrix from number of instances to percentages
	private static void updateMarkov(DistributionInfo di) {
		for (int i = 0; i < 128; i++) {
			int count = 0;
			for (int j = 0; j < 128; j++) {
				count += di.markov[i][j];
			}
			for (int j = 0; j < 128 && count > 0; j++) {
				di.markov[i][j] /= count;
			}
		}
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