import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.sound.midi.*; // package for all midi classes
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JPanel;

import java.awt.Color;

import javax.swing.JSlider;
import javax.swing.JTextField;

import java.awt.Label;
import java.awt.Panel;
import java.awt.Font;
import java.awt.FlowLayout;

import javax.swing.JCheckBox;
import javax.swing.JEditorPane;
import javax.swing.SwingConstants;
import javax.swing.JScrollBar;
import javax.swing.JLabel;

public class Window {

	private JFrame frame;
	private static ArrayList<String> fileList = new ArrayList<String>();
	private static MidiRep midiFile = null; 
	private static DistributionInfo di = new DistributionInfo();
	private static String fileName = "/Users/matthewward/Documents/344/egb.mid";
	private static JButton fileChooserBtn;
	private static JButton togglePlayBtn;
	private static JCheckBox parseCheck;
	private static JCheckBox generateCheck;
	private static JSlider slider;
	private static JLabel lblNotesToGenerate;
	private static boolean playing = false;
	private JTextPane outputWindow;
	private JTextField numNotes;

	/**
	 * Launch the application.
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		  if (!fileList.contains("/Users/matthewward/Documents/344/egb.mid"))
		  {
			  fileList.add("/Users/matthewward/Documents/344/egb.mid");
		  }	  
		
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
		frame.setBounds(100, 100, 450, 348);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		fileChooserBtn = new JButton("Midi Files...");
		fileChooserBtn.setBounds(6, 141, 116, 29);
		frame.getContentPane().add(fileChooserBtn);

		togglePlayBtn = new JButton("Start");
		togglePlayBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!playing) {
					try {
						if (generateCheck.isSelected()) {
							  if (parseCheck.isSelected()) {
								  for (String file : fileList) {
									  midiFile = openToMidiRep(file);
									  di = Parser.run(midiFile.getMidiFile(), di);
								  }
								  midiFile = Creator.createMidi(di, slider.getValue(), Integer.parseInt(numNotes.getText()));	
							  } else {
								  midiFile = Creator.createMidi(null, slider.getValue(), Integer.parseInt(numNotes.getText()));
							  }
						  } else {
							  if (parseCheck.isSelected()) {
								  for (String file : fileList) {
									  midiFile = openToMidiRep(file);
									  di = Parser.run(midiFile.getMidiFile(), di);
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
		togglePlayBtn.setBounds(340, 141, 104, 29);
		frame.getContentPane().add(togglePlayBtn);
		togglePlayBtn.setBackground(Color.GRAY);
		
		outputWindow = new JTextPane();
		outputWindow.setBounds(6, 182, 438, 138);
		outputWindow.setText(fileList.toString());
		frame.getContentPane().add(outputWindow);

		parseCheck = new JCheckBox("Parse Midi File(s)");
		parseCheck.setSelected(true);
		parseCheck.setBounds(6, 6, 150, 23);
		frame.getContentPane().add(parseCheck);

		generateCheck = new JCheckBox("Generate Midi File");
		generateCheck.setSelected(true);
		generateCheck.setBounds(6, 34, 150, 23);
		frame.getContentPane().add(generateCheck);
		
		slider = new JSlider(0x00, 0x10, 0x08);
		slider.setBounds(6, 101, 438, 29);
		frame.getContentPane().add(slider);
		
		numNotes = new JTextField();
		numNotes.setText("200");
		numNotes.setBounds(373, 44, 71, 28);
		frame.getContentPane().add(numNotes);
		numNotes.setColumns(10);
		
		JLabel lblTempo = new JLabel("Tempo");
		lblTempo.setBounds(191, 78, 61, 16);
		frame.getContentPane().add(lblTempo);
		
		lblNotesToGenerate = new JLabel("Number of Notes to Generate");
		lblNotesToGenerate.setBounds(168, 50, 193, 16);
		frame.getContentPane().add(lblNotesToGenerate);
		
		fileChooserBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String fileName = fileChooser();
				
				if (!fileList.contains(fileName))
				  {
					  fileList.add(fileName);
				  }
				
				outputWindow.setText(fileList.toString());
			}
		});
	}
	
	public static void playPause() {
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
