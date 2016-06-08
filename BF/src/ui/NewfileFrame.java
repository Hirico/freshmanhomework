package ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import rmi.RemoteHelper;
import ui.LogFrame.ButtonActionListener;

public class NewfileFrame extends JFrame {
	JPanel filenamePanel;
	JLabel filenameLabel;
	JTextField filenameField;
	JButton createButton;
	JButton cancelButton;
	static NewfileFrame newfileFrame;
	
	public static NewfileFrame getInstance() {
		newfileFrame = new NewfileFrame();
		return newfileFrame;
	}
	
	private NewfileFrame() {
		
		this.setLayout(new GridLayout(1,1));
		
		filenamePanel = new JPanel();
		
		filenameLabel = new JLabel("Filename:");
		
		filenameField = new JTextField(16);
		
		createButton = new JButton("Create");
		createButton.addActionListener(new ButtonActionListener());
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ButtonActionListener());
		
		filenamePanel.add(filenameLabel);
		filenamePanel.add(filenameField);
		filenamePanel.add(createButton);
		filenamePanel.add(cancelButton);
		
		this.add(filenamePanel);
		
		this.setSize(MainFrame.WIDTH, 60);
		this.setLocation(400, 200);
		this.setTitle("Newfile");
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	class ButtonActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if(command.equals("Create")) {
				String filename = filenameField.getText();
				if(filename.length() >= 1) {
					MainFrame.filename = filename;
					MainFrame.mainFrame.dispose();
					MainFrame.getInstance();
					newfileFrame.dispose();
				}
			} else if(command.equals("Cancel")) {
				newfileFrame.dispose();
			}
			
		}
		
	}
}
