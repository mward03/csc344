import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

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
import javax.swing.JComboBox;

public class Window {

	private JFrame frame;
	private static ArrayList<String> fileList = new ArrayList<String>();
	private static MidiRep midiFile = null; 
	private static DistributionInfo di = new DistributionInfo();
	private static JButton fileChooserBtn;
	private static JButton playButton;
	private static JButton genButton;
	private static JSlider slider;
	private static JLabel lblNotesToGenerate;
	private static JTextPane selectedPanel;
	private static JTextField numNotes;
	private static Sequencer sequencer;
	private static JTextField midiNameInput;
	private static JTextField midiNoteBox;
	private static JScrollPane op;
	private static JTextPane outputPanel;
	private static JComboBox comboBox;
	private static JButton derButton;
	/**
	 * Launch the application.
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
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
				playPause();
			}
		});
		playButton.setBounds(470, 288, 104, 29);
		frame.getContentPane().add(playButton);

		derButton = new JButton("Derivative Song");
		derButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if (fileList.size() <= 0) return;
            	if (sequencer != null) sequencer.stop();
            	sequencer = null;
            	
				try {
                    for (String file : fileList) {
                        midiFile = openToMidiRep(file);
                    }
				} catch (Exception e1) {
					e1.printStackTrace();
				}

			    if (midiFile != null)
				  playPause();
            }
        });
		derButton.setBounds(223, 288, 150, 29);
        frame.getContentPane().add(derButton);
		
        genButton = new JButton("Generate");
        genButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if (fileList.size() <= 0) return;
            	if (sequencer != null) sequencer.stop();
            	sequencer = null;
            	
                try {
                    for (String file : fileList) {
                        midiFile = openToMidiRep(file);
                        di = Parser.run(midiFile.getMidiFile(), di, outputPanel);
                    }
                    midiFile = Creator.createMidi(di, (slider.getMaximum() - slider.getValue()), Integer.parseInt(numNotes.getText()), Integer.parseInt(midiNoteBox.getText()), midiNameInput.getText(), (String)comboBox.getSelectedItem(), outputPanel);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                if (midiFile != null)
                    playPause();
            }
        });
        genButton.setBounds(370, 288, 104, 29);
        frame.getContentPane().add(genButton);
		
		selectedPanel = new JTextPane();
		selectedPanel.setEditable(false);
		selectedPanel.setBounds(6, 328, 568, 114);
		frame.getContentPane().add(selectedPanel);
		
		slider = new JSlider(0x00, 0x10, 0x08);
		slider.setBounds(52, 248, 522, 29);
		frame.getContentPane().add(slider);
		
		numNotes = new JTextField();
		numNotes.setHorizontalAlignment(SwingConstants.RIGHT);
		numNotes.setText("200");
		numNotes.setBounds(229, 62, 132, 20);
		frame.getContentPane().add(numNotes);
		numNotes.setColumns(10);
		
		JLabel lblTempo = new JLabel("Tempo");
		lblTempo.setBounds(16, 250, 61, 16);
		frame.getContentPane().add(lblTempo);
		
		lblNotesToGenerate = new JLabel("Number of Notes to Generate");
		lblNotesToGenerate.setBounds(16, 64, 184, 16);
		frame.getContentPane().add(lblNotesToGenerate);
		
		JLabel lblOutputFilename = new JLabel("Output Filename");
		lblOutputFilename.setBounds(16, 6, 150, 16);
		frame.getContentPane().add(lblOutputFilename);
		
		midiNameInput = new JTextField();
		midiNameInput.setText("midiOuputFile");
		midiNameInput.setBounds(229, 4, 132, 20);
		frame.getContentPane().add(midiNameInput);
		midiNameInput.setColumns(10);
		
		midiNoteBox = new JTextField();
		midiNoteBox.setText("90");
		midiNoteBox.setHorizontalAlignment(SwingConstants.TRAILING);
		midiNoteBox.setBounds(229, 36, 132, 20);
		frame.getContentPane().add(midiNoteBox);
		midiNoteBox.setColumns(10);
		
		JButton clearFilesButton = new JButton("Clear Files");
		clearFilesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fileList.removeAll(fileList);
				selectedPanel.setText("");
				
			}
		});
		clearFilesButton.setBounds(122, 288, 104, 29);
		frame.getContentPane().add(clearFilesButton);
		
		outputPanel = new JTextPane();
		outputPanel.setBounds(584, 6, 692, 439);
		outputPanel.setEditable(false);

		selectedPanel.setText(fileList.toString());
		
		op = new JScrollPane(outputPanel);
		op.setBounds(584, 6, 692, 439);
		frame.getContentPane().add(op);
		
		JLabel lblMidiNote = new JLabel("Midi Note");
		lblMidiNote.setBounds(16, 37, 150, 16);
		frame.getContentPane().add(lblMidiNote);
		
		comboBox = new JComboBox();
		comboBox.setBounds(229, 94, 132, 27);
		frame.getContentPane().add(comboBox);
		Global temp = new Global();
		HashMap<String, int[]> hm = temp.getKeys();
		Set set = hm.keySet();
		Iterator i = set.iterator();
		comboBox.addItem("All");
	     // Display elements
	     while(i.hasNext()) {
	       comboBox.addItem(i.next().toString());
	     }
	     comboBox.setSelectedItem("All");
		
		JLabel lblNewLabel = new JLabel("Key");
		lblNewLabel.setBounds(16, 98, 61, 16);
		frame.getContentPane().add(lblNewLabel);
		
		fileChooserBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fileChooser();
				
				if (fileList.size() == 0) selectedPanel.setText("No midi files selected.");
				
				String ret = "";
				for (String s : fileList)
					ret += (s + '\n');
				selectedPanel.setText(ret);
			}
		});
	}
	
	public static void playPause() {
		  try {
		  if (sequencer == null && midiFile == null) {
			  return;
		  } else if (sequencer == null) {
				  sequencer = MidiSystem.getSequencer();
				  sequencer.open();
		    	  sequencer.setSequence(midiFile.getSequence());
		    	  sequencer.start();
		    	  playButton.setText("Pause");
		      } else if (sequencer.isRunning()){
		    	  playButton.setText("Play");
		    	  sequencer.stop();
		      } else {
		    	  playButton.setText("Pause");
		    	  sequencer.start();
		      }
		} catch (MidiUnavailableException e) {
			  e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			  e.printStackTrace();
		}
		  
		fileList.remove(fileList);
		fileList.add(midiNameInput.getText());
		selectedPanel.setText(fileList.toString());
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
		    chooser.setCurrentDirectory(new File("."));
		    chooser.setMultiSelectionEnabled(true);
		    FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "MID files", "mid");
		    chooser.setFileFilter(filter);
		    int returnVal = chooser.showOpenDialog(null);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	for (File f : chooser.getSelectedFiles())
		    		if (!fileList.contains(f.toString())) 
		    			fileList.add(f.toString());
		    }
		    return null;
	  }
}
