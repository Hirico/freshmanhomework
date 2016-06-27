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
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.text.Keymap;
import javax.swing.text.TextAction;
import rmi.RemoteHelper;


public class MainFrame extends JFrame {
	static final int WIDTH = 500;
	static final int HEIGHT = 400;
	JTextArea codeTextArea;
	JTextArea paramTextArea;
	JTextArea resultTextArea;
	static MainFrame mainFrame;
	static String username = "";
	static String filename = "default";
	static String fileVersion = "";
	public Document codeDocument;
	public MyUndoManager undoManager;
	public static MyDocumentListener documentListener;
	
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
		
		//userMenu components
		menuBar.add(userMenu);
		JMenuItem logoutMenuItem = new JMenuItem("Logout");
		userMenu.add(logoutMenuItem);
		
		logoutMenuItem.addActionListener(new LogoutActionListener());
		
		
		this.setJMenuBar(menuBar);
		
				
		codeTextArea = new JTextArea("(Your code here)");
		codeTextArea.setMargin(new Insets(10, 10, 10, 10));
		codeTextArea.setBounds(0, 0, WIDTH, (int) (0.7*HEIGHT));
		codeTextArea.setBackground(Color.WHITE);
		codeTextArea.setLineWrap(true);
		codeTextArea.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(codeTextArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED );
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
		this.getContentPane().add(scrollPane, BorderLayout.CENTER);
		Document codeDocument = codeTextArea.getDocument();
		undoManager = new MyUndoManager(codeDocument);
		codeDocument.addDocumentListener(documentListener = new MyDocumentListener());
		
		UndoAction undoAction = new UndoAction();
		RedoAction redoAction = new RedoAction();
		//set up undo/redo keystroke
		KeyStroke undoKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK);
		KeyStroke redoKeystroke = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK);
		Keymap keymap = codeTextArea.getKeymap();
		Keymap newmap = JTextComponent.addKeymap("MainFrameMap", keymap);
		newmap.addActionForKeyStroke(undoKeystroke, undoAction);
		newmap.addActionForKeyStroke(redoKeystroke, redoAction);
		codeTextArea.setKeymap(newmap);
		 
		
		panel.setLayout(new FlowLayout());
		paramTextArea = new JTextArea(1,20);
		paramTextArea.setBounds(0, HEIGHT, (int)(0.5*WIDTH), (int)(0.125*HEIGHT));
		paramTextArea.setText("(Parameters here)");
		paramTextArea.setBackground(Color.LIGHT_GRAY);
		panel.add(paramTextArea);
		

		// 显示结果
		resultTextArea = new JTextArea(1,20);
		resultTextArea.setBounds((int) (0.5*WIDTH), HEIGHT, (int)(0.5*WIDTH), (int)(0.125*HEIGHT));
		resultTextArea.setText("Result:");
		resultTextArea.setBackground(Color.LIGHT_GRAY);
		resultTextArea.setEditable(false);
		panel.add(resultTextArea);
		
		this.getContentPane().add(panel, BorderLayout.SOUTH);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(WIDTH, HEIGHT);
		this.setLocation(400, 200);
		this.setVisible(true);
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
			String code = codeTextArea.getText();
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
			codeTextArea.setText("(Your code here)");
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
				result = RemoteHelper.getInstance().getExecuteService().execute(codeTextArea.getText(), paramTextArea.getText());
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
			resultTextArea.setText("Result:"+ result);
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
			
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			undoManager.addInsertEdit(e);
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			undoManager.addRemoveEdit(e);
		}
		
	}
	
	class RedoAction extends TextAction {

		public RedoAction() {
			super("redoAction");
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			undoManager.redo();
		}
		
	}
	
	class UndoAction extends TextAction {

		public UndoAction() {
			super("undoAction");
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			undoManager.undo();
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
		codeTextArea.setText(fileInfo[0]);
		filename = fileInfo[1];
		fileVersion = fileInfo[2];
		mainFrame.setTitle("BF Client -- "+username + " -- "+filename + " " + fileVersion);
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
	
}
