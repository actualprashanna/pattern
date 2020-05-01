
import java.awt.EventQueue;
import java.awt.Font;
import javax.swing.JFrame;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.util.Map;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTextArea;
import javax.swing.JList;

/**
 * 
 * @author Prashanna Nepal
 *
 */
public class MainFrame {
	public ByteScanner s;
	public FileScanner b;
	public Pattern p;
	private JFrame byteFrame = new JFrame();
	private JList<String> displayPattern = new JList<String>();
	private JTextField fileReader = new JTextField();
	private JTextArea finalResult = new JTextArea();
	private JScrollPane scrlResult = new JScrollPane();
	private JLabel locationLabel = new JLabel();
	private JLabel patternLabel = new JLabel();
	private JLabel resultLabel = new JLabel();
	private JMenuBar menuRow = new JMenuBar();
	private Font font;
	private Map attributes;

/**
* Launch the application.
*/
public static void main(String[] args) {
EventQueue.invokeLater(new Runnable() {
public void run() {
	try {
		MainFrame frame = new MainFrame();
		frame.byteFrame.setVisible(true);
		}
		catch (Exception error) {
		error.printStackTrace();
		}
	}
});
}

/**
* Create the application.
*/
public MainFrame() {
	initialize();
}

/**
* Initialize the contents of the frame.
*/
@SuppressWarnings("unchecked")
private void initialize() {
	
	menuRow.setBounds(0, 0, 700, 20);

	JMenu File = new JMenu("File");
	menuRow.add(File);

	JMenu help = new JMenu("Help");
	menuRow.add(help);
	
	JMenuItem openFile = new JMenuItem("Open File/Directory");
	File.add(openFile);
	openFile.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Select File/Directory");
		chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

		try {
		if (chooser.showOpenDialog(byteFrame) == JFileChooser.APPROVE_OPTION) {
		fileReader.setText(chooser.getSelectedFile().toString());
		}
		}
		catch (Exception ex) {
		// TODO: handle exception
		JOptionPane.showMessageDialog(null,"Issue found during file selection.");
		}
		ByteScanner.scanResult = "";
		try {
			String location = fileReader.getText();
			finalResult.setText(ByteScanner.scanFileFolder(location));
		} catch (Exception error) {
			error.printStackTrace();
		}
		}
		});
	
	JMenuItem openPattern = new JMenuItem("Open Pattern");
	File.add(openPattern);
	openPattern.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		Pattern.scanBytes.clear();
		Pattern.invalidRow.clear();
					
		JFileChooser chooser = new JFileChooser();
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Select Pattern");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		chooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));

		try {
			if (chooser.showOpenDialog(byteFrame) == JFileChooser.APPROVE_OPTION) {
			String patternLocation = chooser.getSelectedFile().toString();
			Pattern.readPatternFile(patternLocation);}
			}
		catch (Exception ex) {
			JOptionPane.showMessageDialog(null,"Issue found during file selection.");
			}
		
			// Pattern.
			if (Pattern.invalidRow.size() > 0) {
			JOptionPane.showMessageDialog(null, "No valid pattern found in the text file.\n");
			}
			
			ByteScanner.scanResult = "";	
			// Display spaced byte pattern
			String[] bytePatterns = Pattern.getSpacedPatternStrings();
			displayPattern.setListData(bytePatterns);
			
			try {
				String pathName = fileReader.getText();
				finalResult.setText(ByteScanner.scanFileFolder(pathName));
				}
			catch (Exception error) {
				error.printStackTrace();
			}
		}
	});
	
	JMenuItem appExit = new JMenuItem("Exit");
	File.add(appExit);
	appExit.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent arg0) {
	System.exit(0);}
	});
	
	JMenuItem openAbout = new JMenuItem("About");
	help.add(openAbout);
	openAbout.addActionListener(new ActionListener() {
	public void actionPerformed(ActionEvent e) {
	JOptionPane.showMessageDialog(null, "This application is used designed to provide support tools to identify known byte-patterns (signatures) within file contents.\r"
	+ "\nAuthor: Prashanna Nepal" + "\n\r\u00A9 Prashanna 2020","About", JOptionPane.PLAIN_MESSAGE);}
	});

	locationLabel.setText("Location:");
	font = locationLabel.getFont();
	attributes = font.getAttributes();
	attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
	locationLabel.setFont(font.deriveFont(attributes));
	locationLabel.setBounds(50, 30, 85, 15);

	fileReader.setEditable(false);
	fileReader.setBounds(110, 30, 540, 20);
	
	patternLabel.setText("Pattern:");
	font = patternLabel.getFont();
	patternLabel.setFont(font.deriveFont(attributes));
	patternLabel.setBounds(320, 70, 84, 14);
	
	displayPattern.setListData(Pattern.getSpacedPatternStrings());
	displayPattern.setBounds(50, 90, 600, 98);
	
	resultLabel.setText("Output:");
	font = resultLabel.getFont();
	resultLabel.setFont(font.deriveFont(attributes));
	resultLabel.setBounds(10, 200, 84, 14);
	
	finalResult.setEditable(false);
	finalResult.setBounds(10, 200, 812, 454);
	scrlResult.getViewport().add(finalResult, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
	scrlResult.setBounds(10, 225, 675, 285);
	
	byteFrame.getContentPane().add(menuRow);
	byteFrame.getContentPane().add(locationLabel);
	byteFrame.getContentPane().add(fileReader);
	byteFrame.getContentPane().add(patternLabel);
	byteFrame.getContentPane().add(resultLabel);
	byteFrame.getContentPane().add(scrlResult);
	byteFrame.getContentPane().add(displayPattern);
	
	byteFrame.setResizable(false);
	byteFrame.setTitle("Byte Pattern Scanner");
	byteFrame.setBounds(300, 75, 700, 550);
	byteFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	byteFrame.getContentPane().setLayout(null);
	}
}