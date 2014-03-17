import java.io.File;
import javax.sound.midi.Sequence;


public class MidiRep {
	private Sequence s;
	private File f;
	
	public MidiRep(Sequence s, File f) {
		this.s = s;
		this.f = f;
	}
	
	public Sequence getSequence() { return s; }
	public File getMidiFile() { return f; }
}
