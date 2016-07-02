package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.DocumentFilter.FilterBypass;
import javax.swing.text.JTextComponent;
import javax.swing.text.Keymap;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.TextAction;
import rmi.RemoteHelper;


public class MainFrame extends JFrame {
	static final int WIDTH = 600;
	static final int HEIGHT = 500;
	JTextPane codeTextPane;
	JTextArea paramTextArea;
	JTextArea resultTextArea;
	static MainFrame mainFrame;
	static String username = "";
	static String filename = "default";
	static String fileVersion = "";
	public StyledDocument codeDocument;
	public MyUndoManager undoManager;
	public static MyDocumentListener documentListener;
	public Color themeColor;
	public boolean edited = false;
	
	public JMenu versionMenu;
	
	public static MainFrame getInstance() {
		mainFrame = new MainFrame();
		return mainFrame;
	}
	
	/**init static String */
	public static void reinit() {
		username = "";
		filename = "default";
		fileVersion = "";
	}

	private MainFrame() {
		// 创建窗体
		this.setTitle("BF Client -- "+username);
		if(filename.length() >= 1) {
			this.setTitle("BF Client -- "+username + " -- "+filename + " " + fileVersion);
		}
		CardLayout card = new CardLayout();
		JPanel panel = new JPanel();
		panel.setLayout(card);

		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		JMenu runMenu = new JMenu("Run");
		versionMenu = new JMenu("Version");
		JMenu themeMenu = new JMenu("Theme");
		JMenu userMenu = new JMenu("User");
		
		//fileMenu components
		menuBar.add(fileMenu);
		JMenuItem newMenuItem = new JMenuItem("New");
		fileMenu.add(newMenuItem);
		JMenuItem openMenuItem = new JMenuItem("Open");
		fileMenu.add(openMenuItem);
		JMenuItem saveMenuItem = new JMenuItem("Save");
		fileMenu.add(saveMenuItem);
		JMenuItem undoMenuItem = new JMenuItem("Undo");
		fileMenu.add(undoMenuItem);
		JMenuItem redoMenuItem = new JMenuItem("Redo");
		fileMenu.add(redoMenuItem);
		JMenuItem exitMenuItem = new JMenuItem("Exit");
		fileMenu.add(exitMenuItem);
		
		newMenuItem.addActionListener(new NewActionListener());
		openMenuItem.addActionListener(new OpenActionListener());
		saveMenuItem.addActionListener(new SaveActionListener());
		exitMenuItem.addActionListener(new ExitActionListener());
		redoMenuItem.addActionListener(new RedoActionListener());
		undoMenuItem.addActionListener(new UndoActionListener());
		
		//runMenu components
		menuBar.add(runMenu);
		JMenuItem executeMenuItem = new JMenuItem("Execute");
		runMenu.add(executeMenuItem);
		
		executeMenuItem.addActionListener(new ExecuteActionListener());
		
		//versionMenu components
		menuBar.add(versionMenu);
		
		//themeMenu components
		menuBar.add(themeMenu);
		JMenuItem defaultThemeMenuItem = new JMenuItem("Default");
		JMenuItem darkThemeMenuItem = new JMenuItem("Dark");
		themeMenu.add(defaultThemeMenuItem);
		themeMenu.add(darkThemeMenuItem);
		defaultThemeMenuItem.addActionListener(new ThemeActionListener());
		darkThemeMenuItem.addActionListener(new ThemeActionListener());
		themeColor = Color.WHITE;
		
		//userMenu components
		menuBar.add(userMenu);
		JMenuItem logoutMenuItem = new JMenuItem("Logout");
		userMenu.add(logoutMenuItem);
		
		logoutMenuItem.addActionListener(new LogoutActionListener());
		
		
		this.setJMenuBar(menuBar);
		
				
		codeTextPane = new JTextPane();
		codeTextPane.setText("(Your code here)");
		codeTextPane.setMargin(new Insets(10, 10, 10, 10));
		codeTextPane.setBounds(0, 0, WIDTH, (int) (0.7*HEIGHT));
		codeTextPane.setBackground(Color.WHITE);
		JScrollPane scrollPane = new JScrollPane(codeTextPane);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		codeDocument = codeTextPane.getStyledDocument();
		undoManager = new MyUndoManager(codeDocument);
		codeDocument.addDocumentListener(documentListener = new MyDocumentListener());
		((AbstractDocument) codeDocument).setDocumentFilter(new HighlightDocumentFilter(codeTextPane));
		
		UndoAction undoAction = new UndoAction();
		RedoAction redoAction = new RedoAction();
		//set up undo/redo keystroke
		KeyStroke undoKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK);
		KeyStroke redoKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK);
		Keymap keymap = codeTextPane.getKeymap();
		Keymap newmap = JTextComponent.addKeymap("MainFrameMap", keymap);
		newmap.addActionForKeyStroke(undoKeystroke, undoAction);
		newmap.addActionForKeyStroke(redoKeystroke, redoAction);
		codeTextPane.setKeymap(newmap);
		 
		
		panel.setLayout(new FlowLayout());
		paramTextArea = new JTextArea(5,25);
		paramTextArea.setBounds(0, HEIGHT, (int)(0.5*WIDTH), (int)(0.125*HEIGHT));
		paramTextArea.setText("(Parameters here)");
		paramTextArea.setBackground(Color.LIGHT_GRAY);
		paramTextArea.setWrapStyleWord(true);
		paramTextArea.setLineWrap(true);
		
		JScrollPane paramscrollPane = new JScrollPane(paramTextArea);
		paramscrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
		paramscrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
		panel.add(paramscrollPane);
		

		// 显示结果
		resultTextArea = new JTextArea(5,25);
		resultTextArea.setBounds((int) (0.5*WIDTH), HEIGHT, (int)(0.5*WIDTH), (int)(0.125*HEIGHT));
		resultTextArea.setText("Result:");
		resultTextArea.setBackground(Color.LIGHT_GRAY);
		resultTextArea.setEditable(false);
		resultTextArea.setWrapStyleWord(true);
		resultTextArea.setLineWrap(true);
		JScrollPane resultscrollPane = new JScrollPane(resultTextArea);
		resultscrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
		resultscrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
		panel.add(resultscrollPane);
		
		this.getContentPane().add(panel, BorderLayout.SOUTH);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(WIDTH, HEIGHT);
		this.setLocation(380, 150);
		this.setVisible(true);
		
		SimpleAttributeSet sas = new SimpleAttributeSet();
	    StyleConstants.setForeground(sas, Color.CYAN);
	    codeDocument.setCharacterAttributes(0, codeDocument.getLength(), sas, false);
	    
	}

	
	class ExitActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			mainFrame.dispose();			
		}
	}

	class SaveActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(edited) {
				String code = codeTextPane.getText();
				try {
					executeVersion();
					mainFrame.setTitle("BF Client -- "+username + " -- "+filename + " " + fileVersion);
					RemoteHelper.getInstance().getIOService().writeFile(code, username, filename, fileVersion);
					refreshVersionList();
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}
		}

	}
	
	class NewActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			NewfileFrame.getInstance();
		}

	}
	class OpenActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				String[] fileList = RemoteHelper.getInstance().getIOService().readFileList(username);
				OpenfileFrame.getInstance(fileList);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			
		}

	}

	class LoginActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			codeTextPane.setText("(Your code here)");
			paramTextArea.setText("(Parameters here)");
			resultTextArea.setText("Result:");
		}

	}
	class LogoutActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			mainFrame.dispose();
			MainFrame.reinit();
			LogFrame.getInstance();
		}

	}
	class ExecuteActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String result = "";
			try {
				result = RemoteHelper.getInstance().getExecuteService().execute(executeCodeFilter(), paramTextArea.getText());
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			resultTextArea.setText("Result:"+ result);
		}
	}
	
	class ThemeActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("Default")) {
				codeTextPane.setBackground(Color.WHITE);
				themeColor = Color.WHITE;
			}
			else if(e.getActionCommand().equals("Dark")) {
				codeTextPane.setBackground(Color.BLACK);
				themeColor = Color.BLACK;
			}			
		}
		
	}
	class VersionActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				readFile(RemoteHelper.getInstance().getIOService().readFile(username, filename, e.getActionCommand()));
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	class RedoActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			undoManager.redo();
		}
	}
	
	class UndoActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			undoManager.undo();
		}
	}
	
	class MyDocumentListener implements DocumentListener {

		@Override
		public void changedUpdate(DocumentEvent e) {
			edited = true;
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			undoManager.addInsertEdit(e);
			edited = true;
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			undoManager.addRemoveEdit(e);
			edited = true;
		}
		
	}
	
	class RedoAction extends TextAction {

		public RedoAction() {
			super("redoAction");
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(edited) {
				undoManager.redo();
			}
		}
		
	}
	
	class UndoAction extends TextAction {

		public UndoAction() {
			super("undoAction");
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(edited) {
				undoManager.undo();
			}
		}
		
	}
	
	class HighlightDocumentFilter extends DocumentFilter {
		public JTextPane pane;
		
		public HighlightDocumentFilter(JTextPane pane) {
			this.pane = pane;
		}
		
		@Override
        public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
            super.insertString(fb, offset, text, attr);
            checkColorOfNewText(fb,offset, text.length());
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            super.replace(fb, offset, length, text, attrs);
            checkColorOfNewText(fb,offset, text.length());
        }
	}
	
	/**format: 20160608105308 */
	public String executeVersion() {
		Date now = new Date(); 
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

		fileVersion = dateFormat.format(now);
		return fileVersion;
	}
	
	/**used to "open" a file, fileInfo contains code and filename and fileversion*/
	public void readFile(String[] fileInfo) {
		codeTextPane.setText(fileInfo[0]);
		filename = fileInfo[1];
		fileVersion = fileInfo[2];
		mainFrame.setTitle("BF Client -- "+username + " -- "+filename + " " + fileVersion);
		edited = false;
		undoManager = new MyUndoManager(codeDocument);
	}
	
	public void refreshVersionList() {
		try {
			versionMenu.removeAll();
			String[] versionList = RemoteHelper.getInstance().getIOService().readVersionList(username, filename);
			for(int i = 0; i < versionList.length; i++) {
				JMenuItem newItem = new JMenuItem(versionList[i]);
				newItem.addActionListener(new VersionActionListener());
				versionMenu.add(newItem);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public String executeCodeFilter() {
		char[] fullCode = codeTextPane.getText().toCharArray();
		StringBuilder sb = new StringBuilder();
		boolean commentContact = false;
		for(int i = 0; i < fullCode.length; i++) {
			if(i < fullCode.length-1 && fullCode[i] == '/' && fullCode[i+1] == '/'){
				commentContact = true;
			}
			if(commentContact) {
				if(fullCode[i] == '\n') {
					commentContact = false;
				}
				continue;
			} else {
				if(fullCode[i] != ' ' && fullCode[i] != '\n') {
					sb.append(fullCode[i]);
				}
			}
		}
				
		return sb.toString();
	}
	
	/**set the color of specified text in codeDocument  
	 * @param fb */
	public void setTextColor(FilterBypass fb, int offset, int length, Color c) {
		SimpleAttributeSet sas = new SimpleAttributeSet();
		sas.addAttribute(StyleConstants.FontFamily, "Lucida Console");
	    StyleConstants.setForeground(sas, c);
	    ((StyledDocument) fb.getDocument()).setCharacterAttributes(offset, length, sas, false);
	}
	
	/** document listener will call this to change the last input's color 
	 * @param fb */
	public void checkColorOfNewText(FilterBypass fb, int offset, int length) {
		String newString = "";
		try {
			newString = codeDocument.getText(offset, length);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		outer: for(int i = 0; i < length; i++) {
			for(int j = 0; j < offset; j++) {
				try {
					if(fb.getDocument().getText(offset-j-1, 1).equals("\n")) {
						break;
					}
					if(fb.getDocument().getText(offset-j-1, 1).equals("/") 
							&& offset-j-1 > 0
							&& fb.getDocument().getText(offset-j-2, 1).equals("/")) {
						setTextColor(fb,offset+i,1,Color.GRAY);
						continue outer;
					}
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
			if(newString.charAt(i) == '>' || newString.charAt(i) == '<') {
				setTextColor(fb,offset+i,1,Color.BLUE);
			}
			else if(newString.charAt(i) == '+' || newString.charAt(i) == '-') {
				setTextColor(fb,offset+i,1,Color.ORANGE);
			}
			else if(newString.charAt(i) == '.' || newString.charAt(i) == ',') {
				setTextColor(fb,offset+i,1,Color.RED);
			}
			else if(newString.charAt(i) == '[' || newString.charAt(i) == ']') {
				setTextColor(fb,offset+i,1,Color.GREEN);
			} else
				try {
					if(newString.charAt(i) == '/' && offset + i > 0 && codeDocument.getText(offset+i-1, 1).equals("/")) {
						int appendLength = 0;
						while(!codeDocument.getText(offset+i+appendLength, 1).equals("\n")) {
							appendLength += 1;
						}
						setTextColor(fb,offset+i-1,2+appendLength,Color.GRAY);
					}
					
					else {
						if(themeColor == Color.BLACK) {
							setTextColor(fb,offset+i,1,Color.WHITE);
						} else if(themeColor == Color.WHITE) {
							setTextColor(fb,offset+i,1,Color.BLACK);
						}
					}
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
		}
		
	}
	
}
