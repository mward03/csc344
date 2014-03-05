import java.io.File;
import java.io.IOException;

import javax.sound.midi.*; // package for all midi classes

public class Driver
{
  public static void main(String argv[]) throws Exception {
	  
	  // Toggle for functionality
	  boolean generate = true;
	  boolean parse = true;
	  boolean play = true;
	  
	  MidiRep midiFile = null;  
	  DistributionInfo di;
	  
	  //String fileName = "/Users/matthewward/Documents/344/egb.mid";
	  String fileName = "/Users/matthewward/Documents/344/tfaf.mid";
	  
	  if (generate) {
		  if (parse) {
			  midiFile = openToMidiRep(fileName);
			  di = Parser.run(midiFile.getMidiFile());
			  midiFile = Creator.createMidi(di);	
		  } else {
			  midiFile = Creator.createMidi(null);
		  }
	  } else {
		  if (parse) {
			  midiFile = openToMidiRep(fileName);
			  di = Parser.run(midiFile.getMidiFile());
		  } else {
			  midiFile = openToMidiRep(fileName);
		  }
	  }
		
	  if (play && midiFile != null)
		  play(midiFile);
  }
  
  public static void play(MidiRep midiFile) {
	  Sequencer sequencer;
	  try {
		  sequencer = MidiSystem.getSequencer();
	      sequencer.open();
	      sequencer.setSequence(midiFile.getSequence());
	      sequencer.start();
	} catch (MidiUnavailableException e) {
		  e.printStackTrace();
	} catch (InvalidMidiDataException e) {
		  e.printStackTrace();
	}
  }

  public static MidiRep openToMidiRep(String location) {

	  	File file = new File(location);
	  	Sequence sequence = null;
	  	
	  	try {
	  		sequence = MidiSystem.getSequence(file);
	  		//MidiSystem.write(sequence, 1, file);
	    } catch (IOException e) {
	    	e.printStackTrace();
			System.exit(1);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
			System.exit(1);
		}
	  	
	    return new MidiRep(sequence, file);
  }
}