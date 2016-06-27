package ui;

import javax.swing.event.DocumentEvent.EventType;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class Edit {
	EventType type;
	int offset;
	int length;
	String changedText;
	boolean redone = false;
	
	public Edit (EventType type, int offset, int length, String changedText) {
		this.type = type;
		this.offset = offset;
		this.changedText = changedText;
		this.length = length;
	}
	
	public void redo(Document d) {
		d.removeDocumentListener(MainFrame.documentListener); // remove the listener first to avoid loop firing
		if(type == EventType.INSERT) {
			try {
				d.insertString(offset, changedText, null);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		else if(type == EventType.REMOVE) {
			try {
				d.remove(offset, length);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		d.addDocumentListener(MainFrame.documentListener);
		redone = true;
	}
	
	public void undo(Document d) {
		d.removeDocumentListener(MainFrame.documentListener); // remove the listener first to avoid loop firing
		if(type == EventType.INSERT) {
			try {
				d.remove(offset, length);
			} catch (BadLocationException e) {
				try {
					System.out.println(d.getText(0, d.getLength()));
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
								
				System.out.println(offset);
				System.out.println(length);
				e.printStackTrace();
			}
		}
		else if(type == EventType.REMOVE) {
			try {
				d.insertString(offset, changedText, null);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		d.addDocumentListener(MainFrame.documentListener);
		redone = false;
	}
}
