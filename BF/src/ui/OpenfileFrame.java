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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import rmi.RemoteHelper;
import ui.LogFrame.ButtonActionListener;

public class OpenfileFrame extends JFrame {
	JPanel buttonPanel;
	JList list;
	JButton openButton;
	JButton cancelButton;
	String[] fileList;
	static OpenfileFrame openfileFrame;
	
	public static OpenfileFrame getInstance(String[] fileList) {
		openfileFrame = new OpenfileFrame(fileList);
		return openfileFrame;
	}
	
	private OpenfileFrame(String[] fileList) {
		
		this.fileList = fileList;
		
		openButton = new JButton("Open");
		openButton.addActionListener(new ButtonActionListener());
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ButtonActionListener());
		buttonPanel = new JPanel();
		buttonPanel.add(openButton);
		buttonPanel.add(cancelButton);
		this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		list = new JList<String>(fileList);
		JScrollPane pane = new JScrollPane(list);
		this.getContentPane().add(pane, BorderLayout.CENTER);
		
		this.setSize(400, 200);
		this.setLocation(400, 200);
		this.setTitle("Open");
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	class ButtonActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if(command.equals("Open")) {
				try {
					if(list.getSelectedValue() != null) {
						MainFrame.mainFrame.readFile(RemoteHelper.getInstance().getIOService().readFile(MainFrame.username, (String)list.getSelectedValue()));
						MainFrame.mainFrame.refreshVersionList();
						MainFrame.mainFrame.edited = false;
						openfileFrame.dispose();
					} else {
	
					}
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			} else if(command.equals("Cancel")) {
				openfileFrame.dispose();
			}
			
		}
		
	}
}
