package reading;
import java.awt.event.*;   
import java.io.*;   
import javax.swing.*;   

public class Reading{   
	private JTextArea ta;   
	private JProgressBar pb;   
	int val;   

	public Reading(){   
		final JFileChooser fc = new JFileChooser(".");   
		fc.setMultiSelectionEnabled(false);   
		ta = new JTextArea();   
		pb = new JProgressBar(JProgressBar.HORIZONTAL, 0, 100);   
		pb.setStringPainted(true);   
		final JButton
		open = new JButton("open"),
		read = new JButton("read");   
		ActionListener l = new ActionListener(){   
			File selectedFile;   

			public void actionPerformed(ActionEvent e){   
				JButton button = (JButton)e.getSource();   
				if(button == open){   
					if(fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){   
						selectedFile = fc.getSelectedFile();   
					}   
				}   
				if(button == read)   
					readFile(selectedFile);   
			}   
		};   
		open.addActionListener(l);   
		read.addActionListener(l);   
		JPanel north = new JPanel();   
		north.add(open);   
		north.add(read);   
		JPanel south = new JPanel();   
		south.add(pb);   
		JFrame f = new JFrame();   
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
		f.getContentPane().add(north, "North");   
		f.getContentPane().add(new JScrollPane(ta));   
		f.getContentPane().add(south, "South");   
		f.setSize(400,400);   
		f.setLocation(200,200);   
		f.setVisible(true);   
	}   

	private void readFile(final File f){   
		ta.setText("");   
		// read in a separate thread to allow gui to be responsive to user   
		new Thread(new Runnable(){   
			public void run(){   
				BufferedReader br = null;   
				try{   
					br = new BufferedReader(   
							new InputStreamReader(   
									new FileInputStream(f)));   
					long size = f.length(), len = 0;   
					String line = null;   
					while((line = br.readLine()) != null){   
						ta.append(line + "\n");   
						len += line.length() + 2;       // "\n" length = 2   
						val = (int)((100 * len)/size);   
						pb.setValue(val);   
						// slow down the action to allow   
						// demonstration using small files   
						Thread.sleep(100);   
					}   
					br.close();   
				}   
				catch(InterruptedException ie){   
					System.err.println("interrupted sleep: " + ie.getMessage());   
				}   
				catch(IOException ioe){   
					System.err.println("read: " + ioe.getMessage());   
				}   
			}   
		}).start();   
	}   

	public static void main(String[] args){   
		new Reading();   
	}   
}  
