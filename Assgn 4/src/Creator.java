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


public class Creator {
	public static MidiRep createMidi(DistributionInfo di, float tempo, int numberOfNotes) {
		boolean derivative = false;
		if (di != null) derivative = true;
		
		try
		{
	//****  Create a new MIDI sequence with 24 ticks per beat  ****
			Sequence s = new Sequence(javax.sound.midi.Sequence.PPQ,24);

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
			String TrackName = new String("midifile track");
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
			mm.setMessage(ShortMessage.PROGRAM_CHANGE, 1, 11, 0); 
			me = new MidiEvent(mm,(long)0);
			t.add(me);
			
			Random rand = new Random();
			int n = numberOfNotes;
			ArrayList<Integer> noteList = new ArrayList<Integer>();
			if (derivative) {
				noteList.add(di.getInitialKey());
			} else {
				noteList.add(rand.nextInt(127) + 1);
			}
			for (int i = 0; i < n - 1; i++) {
				if (derivative) {
					noteList.add(di.getNextKey(noteList.get(noteList.size() - 1)));
				} else {
					noteList.add(rand.nextInt(127) + 1);
				}
			}
			
			Iterator<Integer> itr = noteList.iterator();
			int i = 0;
			int delay = 10;
			while(itr.hasNext())
            {
                int note = itr.next();

                ShortMessage noteOnMsg = new ShortMessage();
                //Signal/Channel/Pitch/Velocity
                noteOnMsg.setMessage(ShortMessage.NOTE_ON, 0, note, 127);

                ShortMessage noteOffMsg = new ShortMessage();
                //Signal/Channel/Pitch/Velocity
                noteOffMsg.setMessage(ShortMessage.NOTE_OFF,0, note, 127);

////			//change instrument of track t
////			try
////            {
////                 ShortMessage instrumentChange = new ShortMessage();
//// 
////                 instrumentChange.setMessage(ShortMessage.PROGRAM_CHANGE, 1, 60, 0);
//// 
////                 //MidiEvent instrumentChange = new MidiEvent(ShortMessage.PROGRAM_CHANGE,drumPatch.getBank(),drumPatch.getProgram());
////                 t.add(new MidiEvent(instrumentChange,0));
////            }
////            catch(Exception e)
////            {
////                //Handle
////            }
                
                if (rand.nextInt(5) == 2)
                	delay = 0;
                
                t.add(new MidiEvent(noteOnMsg,i));
                i = i + delay;
                t.add(new MidiEvent(noteOffMsg,i));
                i = i + delay;
                
                delay = 10;
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
}
