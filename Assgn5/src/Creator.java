import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Iterator;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;
import javax.sound.midi.Track;
import javax.swing.JTextPane;


public class Creator {	
	
	public static MidiRep createMidi(DistributionInfo di, float tempo, int numberOfNotes, int inst, String outputTrackName, String key, JTextPane jtp) {
		Random rand = new Random();
        updateMarkov(di);
        int root = 0;
        int majorThird = 0;
        int perfectFifth = 0;
		
		try
		{
	//****  Create a new MIDI sequence with 24 ticks per beat  ****
			Sequence s = new Sequence(Sequence.PPQ,24);

	//****  Obtain a MIDI track from the sequence  ****
			Track t = s.createTrack();

	//****  General MIDI sysex -- turn on General MIDI sound set  ****
			byte[] b = {(byte)0xF0, 0x7E, 0x7F, 0x09, 0x01, (byte)0xF7};
			SysexMessage sm = new SysexMessage();
			sm.setMessage(b, 6);
			MidiEvent me = new MidiEvent(sm,(long)0);
			t.add(me);

	//****  set tempo (meta event)  ****
			MetaMessage mt = new MetaMessage();
			
			byte[] bt = {(byte) (0x00 + tempo), (byte)0xA1, 0x20};
			mt.setMessage(0x51 ,bt, 3);
			me = new MidiEvent(mt,(long)0);
			t.add(me);

	//****  set track name (meta event)  ****
			mt = new MetaMessage();
			String TrackName = new String(outputTrackName);
			mt.setMessage(0x03 ,TrackName.getBytes(), TrackName.length());
			me = new MidiEvent(mt,(long)0);
			t.add(me);

	//****  set omni on  ****
			ShortMessage mm = new ShortMessage();
			mm.setMessage(0xB0, 0x7D,0x00);
			me = new MidiEvent(mm,(long)0);
			t.add(me);

	//****  set poly on  ****
			mm = new ShortMessage();
			mm.setMessage(0xB0, 0x7F,0x00);
			me = new MidiEvent(mm,(long)0);
			t.add(me);

	//****  (change instrument type, channel instrument)  ****
			
			mm = new ShortMessage();
			mm.setMessage(ShortMessage.PROGRAM_CHANGE, 0, inst, 0); 
			me = new MidiEvent(mm,(long)0);
			t.add(me);
			
			int n = numberOfNotes;
			ArrayList<Integer> noteList = new ArrayList<Integer>();
			
			int keyToAdd = di.getInitialKey(key);
			root = keyToAdd % 12;
			majorThird = root + 4;
			perfectFifth = root + 7;
			
			if (keyToAdd == -1)
			{
				jtp.setText(jtp.getText() + '\n' + "Could not get inital key");
				return null;
			}
			noteList.add(keyToAdd);
			for (int i = 0; i < n - 1; i++) {
				keyToAdd = di.getNextKey(noteList.get(noteList.size() - 1), key);
				if (keyToAdd == -1) {
					jtp.setText(jtp.getText() + '\n' + "Could not get next key.");
					return null;
				}
				noteList.add(keyToAdd);
			}
			
			Iterator<Integer> itr = noteList.iterator();
			int i = 0;
			int delay = 8;
			int count = 0;
			int odd = 0;
			int vel = 127;
			while(itr.hasNext())
            {
//				odd++;
//				if (odd % 4 == 1){
//					vel = 120;
//				} else if (odd % 2 == 1) {
//					vel = 95;
//				} else {
//					vel = 70;
//				}
				
                int note;
                ShortMessage noteOnMsg;

//                ShortMessage noteOffMsg = new ShortMessage();
//                //Signal/Channel/Pitch/Velocity
//                noteOffMsg.setMessage(ShortMessage.NOTE_OFF,0, note, 127);

                count++;
                if (count % 3 == 0) {
                	note = itr.next();
                    noteOnMsg = new ShortMessage();
                    noteOnMsg.setMessage(ShortMessage.NOTE_ON, 0, note, vel);
                    t.add(new MidiEvent(noteOnMsg,i));
                	
                	note = itr.next();
                    noteOnMsg = new ShortMessage();
                    noteOnMsg.setMessage(ShortMessage.NOTE_ON, 0, note, vel);
                    t.add(new MidiEvent(noteOnMsg,i));
                    
                    note = itr.next();
                    noteOnMsg = new ShortMessage();
                    noteOnMsg.setMessage(ShortMessage.NOTE_ON, 0, note, vel);
                    t.add(new MidiEvent(noteOnMsg,i));
                    
                } else if (count % 12 == 0) {
                	note = root;
                	noteOnMsg = new ShortMessage();
                    noteOnMsg.setMessage(ShortMessage.NOTE_ON, 0, note, vel);
                    t.add(new MidiEvent(noteOnMsg,i));
                	
                    note = majorThird;
                    noteOnMsg = new ShortMessage();
                    noteOnMsg.setMessage(ShortMessage.NOTE_ON, 0, note, vel);
                    t.add(new MidiEvent(noteOnMsg,i));
                    
                    note = perfectFifth;
                    noteOnMsg = new ShortMessage();
                    noteOnMsg.setMessage(ShortMessage.NOTE_ON, 0, note, vel);
                    t.add(new MidiEvent(noteOnMsg,i));
                } else {
                	note = itr.next();
                    noteOnMsg = new ShortMessage();
                    noteOnMsg.setMessage(ShortMessage.NOTE_ON, 0, note, vel);
                    t.add(new MidiEvent(noteOnMsg,i));
                }
                
	             i = i + delay;

            }


	//****  set end of track (meta event) 19 ticks later  ****
			mt = new MetaMessage();
	        byte[] bet = {}; // empty array
			mt.setMessage(0x2F,bet,0);
			me = new MidiEvent(mt, (long)140);
			t.add(me);

	//****  write the MIDI sequence to a MIDI file  ****
			File f = new File("midifile.mid");
			MidiSystem.write(s, 1, f);
			
			return new MidiRep(s, f);
		} //try
			catch(Exception e)
		{
			System.out.println("Exception caught " + e.toString());
			e.printStackTrace();
			System.exit(1);
		} //catch
		return null;
	}
	
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
}
