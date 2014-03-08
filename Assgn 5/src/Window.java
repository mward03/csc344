import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.midi.*; // package for all midi classes
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Window {

	private JFrame frame;
	private static ArrayList<String> fileList = new ArrayList<String>();
	private static MidiRep midiFile = null; 
	private static DistributionInfo di = new DistributionInfo();
	private static String fileName = "/Users/matthewward/Documents/344/egb.mid";
	private static JButton fileChooserBtn;
	private static JButton playButton;
	private static JCheckBox parseCheck;
	private static JCheckBox generateCheck;
	private static JSlider slider;
	private static JLabel lblNotesToGenerate;
	private static boolean playing = false;
	private static JTextPane selectedPanel;
	private static JTextField numNotes;
	private static Sequencer sequencer;
	private static JTextField midiNameInput;
	private static JTextField midiNoteBox;
	private static JScrollPane op;
	private static JTextPane outputPanel;
	private static JTextPane playQueuePanel;
	/**
	 * Launch the application.
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String defaultPath = "C:\\Users\\Matthew\\SkyDrive\\School\\Classes\\344\\Assgn 5\\egb.mid";
		fileList.add(defaultPath);
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Window() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1309, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		fileChooserBtn = new JButton("Midi Files...");
		fileChooserBtn.setBounds(6, 288, 116, 29);
		frame.getContentPane().add(fileChooserBtn);

		playButton = new JButton("Play");
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!playing) {
					try {
						if (generateCheck.isSelected()) {
							  if (parseCheck.isSelected()) {
								  for (String file : fileList) {
									  midiFile = openToMidiRep(file);
									  di = Parser.run(midiFile.getMidiFile(), di, outputPanel);
								  }
								  midiFile = Creator.createMidi(di, slider.getValue(), Integer.parseInt(numNotes.getText()), Integer.parseInt(midiNoteBox.getText()), midiNameInput.getText());	
							  } else {
								  midiFile = Creator.createMidi(null, slider.getValue(), Integer.parseInt(numNotes.getText()), Integer.parseInt(midiNoteBox.getText()), midiNameInput.getText());
							  }
						  } else {
							  if (parseCheck.isSelected()) {
								  for (String file : fileList) {
									  midiFile = openToMidiRep(file);
									  di = Parser.run(midiFile.getMidiFile(), di, outputPanel);
								  }
							  } else {
								  midiFile = openToMidiRep(fileName);
							  }
						  }
					} catch (Exception e1) {
						e1.printStackTrace();
					}
						
				    if (midiFile != null)
					  playPause();
				}
			}
		});
		playButton.setBounds(470, 288, 104, 29);
		frame.getContentPane().add(playButton);
		
		selectedPanel = new JTextPane();
		selectedPanel.setEditable(false);
		selectedPanel.setBounds(6, 328, 283, 122);
		frame.getContentPane().add(selectedPanel);

		parseCheck = new JCheckBox("Parse Midi File(s)");
		parseCheck.setSelected(true);
		parseCheck.setBounds(6, 6, 150, 23);
		frame.getContentPane().add(parseCheck);

		generateCheck = new JCheckBox("Generate Midi File");
		generateCheck.setSelected(true);
		generateCheck.setBounds(6, 34, 150, 23);
		frame.getContentPane().add(generateCheck);
		
		slider = new JSlider(0x00, 0x10, 0x08);
		slider.setBounds(52, 248, 522, 29);
		frame.getContentPane().add(slider);
		
		numNotes = new JTextField();
		numNotes.setHorizontalAlignment(SwingConstants.RIGHT);
		numNotes.setText("200");
		numNotes.setBounds(470, 62, 71, 20);
		frame.getContentPane().add(numNotes);
		numNotes.setColumns(10);
		
		JLabel lblTempo = new JLabel("Tempo");
		lblTempo.setBounds(16, 250, 61, 16);
		frame.getContentPane().add(lblTempo);
		
		lblNotesToGenerate = new JLabel("Number of Notes to Generate");
		lblNotesToGenerate.setBounds(312, 64, 150, 16);
		frame.getContentPane().add(lblNotesToGenerate);
		
		JLabel lblOutputFilename = new JLabel("Output Filename");
		lblOutputFilename.setBounds(16, 64, 150, 16);
		frame.getContentPane().add(lblOutputFilename);
		
		midiNameInput = new JTextField();
		midiNameInput.setText("midiOuputFile");
		midiNameInput.setBounds(138, 64, 132, 20);
		frame.getContentPane().add(midiNameInput);
		midiNameInput.setColumns(10);
		
		midiNoteBox = new JTextField();
		midiNoteBox.setText("90");
		midiNoteBox.setHorizontalAlignment(SwingConstants.TRAILING);
		midiNoteBox.setBounds(470, 35, 71, 20);
		frame.getContentPane().add(midiNoteBox);
		midiNoteBox.setColumns(10);
		
		JButton clearFilesButton = new JButton("Clear Files");
		clearFilesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fileList.removeAll(fileList);
				selectedPanel.setText("");
				
			}
		});
		clearFilesButton.setBounds(132, 288, 104, 29);
		frame.getContentPane().add(clearFilesButton);
		
		playQueuePanel = new JTextPane();
		playQueuePanel.setEditable(false);
		playQueuePanel.setBounds(299, 328, 275, 122);
		frame.getContentPane().add(playQueuePanel);
		
		outputPanel = new JTextPane();
		outputPanel.setBounds(584, 6, 692, 439);
		outputPanel.setEditable(false);

		selectedPanel.setText(fileList.toString());
		
		op = new JScrollPane(outputPanel);
		op.setBounds(584, 6, 692, 439);
		frame.getContentPane().add(op);
		
		JLabel lblMidiNote = new JLabel("Midi Note");
		lblMidiNote.setBounds(312, 37, 150, 16);
		frame.getContentPane().add(lblMidiNote);
		
		fileChooserBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileName = fileChooser();
				
				if (!fileList.contains(fileName))
				  {
					  fileList.add(fileName);
				  }
				
				selectedPanel.setText(fileList.toString());
			}
		});
	}
	
	public static void playPause() {
		  try {
//		      if (sequencer != MidiSystem.getSequencer()) {
				  sequencer = MidiSystem.getSequencer();
				  sequencer.open();
		    	  sequencer.setSequence(midiFile.getSequence());
		    	  sequencer.start();
//		      } else {
//		    	  sequencer.stop();
//		    	  sequencer = null;
//		      }
		} catch (MidiUnavailableException e) {
			  e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			  e.printStackTrace();
		}
	  }

	  public static MidiRep openToMidiRep(String location) {
		  Sequence sequence = null;
		  File file = new File(fileList.get(0));
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
	  
	  public static String fileChooser() {
		  JFileChooser chooser = new JFileChooser();
		    FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "MID files", "mid");
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(null);
		    if(returnVal == JFileChooser.APPROVE_OPTION)
		       return chooser.getSelectedFile().toString();
		    return null;
	  }
}
