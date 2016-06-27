package ui;

import java.util.ArrayList;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentEvent.EventType;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;


public class MyUndoManager{
	public ArrayList<CompoundEdit> edits;
	public CompoundEdit currentEdit;
	public Document document;
	public String documentBeforeRemoval = "";
	
	public MyUndoManager(Document d) {
		edits = new ArrayList<CompoundEdit>();
		document = d;
		try {
			documentBeforeRemoval = d.getText(0, d.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	
	public void undo() {
		
		//call undo() on the last edit, and set currentEdit to the previous one.
		if(edits.size() > 0 && currentEdit!= null) {
			currentEdit.undo(document);
			if(edits.indexOf(currentEdit) > 0) {
				currentEdit = edits.get(edits.indexOf(currentEdit)-1);
			} else {
				currentEdit = null;
			}
		}
		try {
			documentBeforeRemoval = document.getText(0, document.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
	public void redo() {
		
		//call redo() on the next edit, and set currentEdit to the next one.
		if(edits.size() > 0) {
			if(currentEdit == null) {
				edits.get(0).redo(document);
				currentEdit = edits.get(0);
			} else if(edits.indexOf(currentEdit)+1 < edits.size()) {
				currentEdit = edits.get(edits.indexOf(currentEdit)+1);
				currentEdit.redo(document);
			}
		}
		try {
			documentBeforeRemoval = document.getText(0, document.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	
			//create an edit, if the currentEdit is not the last of the edits, trim the edits'size
			//compare the edit's type with the last edit
			//if same, call addEdit() on the last edit. If false, new a compound 
			//and add the edit to edits. Set the new one as current.

	public void addInsertEdit(DocumentEvent ev) {
		int length = ev.getLength();
		int offset = ev.getOffset();
		EventType type = ev.getType();
		String changedText = "";
		try {
			changedText = document.getText(offset, length);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		Edit newEdit = new Edit(type, offset, length, changedText);
		if(currentEdit!= null) {
			if(edits.indexOf(currentEdit) != edits.size()-1) { // trim size
				ArrayList<CompoundEdit> tempList = new ArrayList<CompoundEdit>();
				for(int i = 0; i <= edits.indexOf(currentEdit); i++) {
					tempList.add(edits.get(i));
				}
				edits = tempList;
			}
			if(currentEdit.type == type) {
				currentEdit.addEdit(newEdit);
			} else {
				CompoundEdit newCompoundEdit = new CompoundEdit(newEdit);
				currentEdit = newCompoundEdit;
				edits.add(newCompoundEdit);
			}
		} else {
			edits = new ArrayList<CompoundEdit>();
			CompoundEdit newCompoundEdit = new CompoundEdit(newEdit);
			currentEdit = newCompoundEdit;
			edits.add(newCompoundEdit);
		}
		try {
			documentBeforeRemoval = document.getText(0, document.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
	}
	
	public void addRemoveEdit(DocumentEvent ev) {
		int length = ev.getLength();
		int offset = ev.getOffset();
		EventType type = ev.getType();
		String changedText = "";
		try {
			changedText = documentBeforeRemoval.substring(offset, offset + length);
		} catch (Exception e) {
			try {
				System.out.println(document.getText(0, document.getLength()));
			} catch (BadLocationException e1) {
				e1.printStackTrace();
			}
			System.out.println(documentBeforeRemoval);
			System.out.println(offset);
			System.out.println(length);
		}
		
		Edit newEdit = new Edit(type, offset, length, changedText);
		if(currentEdit!= null) {
			if(edits.indexOf(currentEdit) != edits.size()-1) { // trim size
				ArrayList<CompoundEdit> tempList = new ArrayList<CompoundEdit>();
				for(int i = 0; i <= edits.indexOf(currentEdit); i++) {
					tempList.add(edits.get(i));
				}
				edits = tempList;
			}
			if(currentEdit.type == type) {
				currentEdit.addEdit(newEdit);
			} else {
				CompoundEdit newCompoundEdit = new CompoundEdit(newEdit);
				currentEdit = newCompoundEdit;
				edits.add(newCompoundEdit);
			}
		} else {
			edits = new ArrayList<CompoundEdit>();
			CompoundEdit newCompoundEdit = new CompoundEdit(newEdit);
			currentEdit = newCompoundEdit;
			edits.add(newCompoundEdit);
		}
		
		try {
			documentBeforeRemoval = document.getText(0, document.getLength());
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
	}

}
