package ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import rmi.RemoteHelper;


public class MainFrame extends JFrame {
	static final int WIDTH = 500;
	static final int HEIGHT = 400;
	JTextArea codeTextArea;
	JTextArea paramTextArea;
	JTextArea resultTextArea;
	static MainFrame mainFrame;
	static String username = "";
	static String filename = "";
	static String fileVersion = "";
	
	public static MainFrame getInstance() {
		mainFrame = new MainFrame();
		return mainFrame;
	}
	
	/**init static String */
	public static void reinit() {
		username = "";
		filename = "";
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
		JMenu versionMenu = new JMenu("Version");
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
		undoMenuItem.addActionListener(new UndoActionListener());
		redoMenuItem.addActionListener(new RedoActionListener());
		exitMenuItem.addActionListener(new ExitActionListener());
		
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
				RemoteHelper.getInstance().getIOService().writeFile(code, "admin", "code");
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
			codeTextArea.setText("(Your code here)");
			paramTextArea.setText("(Parameters here)");
			resultTextArea.setText("Result:");
		}

	}
	class UndoActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			undo();
		}

	}
	class RedoActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			redo();
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
	
	public void undo() {
		
	}
	
	public void redo() {
		
	}
}
