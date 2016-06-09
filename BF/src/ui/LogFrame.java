package ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import rmi.RemoteHelper;

public class LogFrame extends JFrame {
	JPanel namePanel;
	JPanel pwPanel;
	JPanel buttonPanel;
	JLabel nameLabel;
	JLabel pwLabel;
	JTextField nameField;
	JPasswordField pwField;
	JButton enterButton;
	JButton quitButton;
	static LogFrame logFrame;
	
	public static LogFrame getInstance() {
		logFrame = new LogFrame();
		return logFrame;
	}
	
	private LogFrame() {
		
		this.setLayout(new GridLayout(3,1));
		
		namePanel = new JPanel();
		pwPanel = new JPanel();
		buttonPanel = new JPanel();
		
		nameLabel = new JLabel("Username:");
		pwLabel = new JLabel("Password:");
		
		nameField = new JTextField(16);
		pwField = new JPasswordField(16);
		
		enterButton = new JButton("Enter");
		enterButton.addActionListener(new ButtonActionListener());
		quitButton = new JButton("Quit");
		quitButton.addActionListener(new ButtonActionListener());
		
		namePanel.add(nameLabel);
		namePanel.add(nameField);
		pwPanel.add(pwLabel);
		pwPanel.add(pwField);
		buttonPanel.add(enterButton);
		buttonPanel.add(quitButton);
		
		this.add(namePanel);
		this.add(pwPanel);
		this.add(buttonPanel);
		
		this.setSize(MainFrame.WIDTH, 200);
		this.setLocation(400, 200);
		this.setTitle("Login");
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	class ButtonActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if(command.equals("Enter")) {
				try {
					if(RemoteHelper.getInstance().getUserService().login(nameField.getText(), String.valueOf(pwField.getPassword()))) {
						logFrame.dispose();
						MainFrame.username = nameField.getText();
						MainFrame.getInstance();
					} else {
						JDialog warning = new JDialog(logFrame,true);
						JLabel warningText = new JLabel("Username or password is wrong.");
						warning.setSize(300, 60);
						warning.setLocation(500, 250);
						warning.getContentPane().add(warningText, BorderLayout.CENTER);
						warning.setTitle("Warning");
						warning.setVisible(true);
						warning.setDefaultCloseOperation(HIDE_ON_CLOSE);
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			} else if(command.equals("Quit")) {
				logFrame.dispose();
			}
			
		}
		
	}
}
