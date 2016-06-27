package ui;

import java.util.ArrayList;

import javax.swing.event.DocumentEvent.EventType;
import javax.swing.text.Document;

/**A list of edits with same type, stored in MyUndoManager with other standalone edits and compoundEdits 
 * In MyUndoManager, an instance of this is created when a new edit is with the same type as last one. When last one 
 * is a compoundEdit, the compoundEdit's add() will be called. After doing so, new edit is deleted in 
 * the edits' list of MyUndoManager.
 * */
public class CompoundEdit {
	public EventType type;
	public ArrayList<Edit> edits;
	public int offset;
	
	public CompoundEdit(Edit a, Edit b) {
		edits = new ArrayList<Edit>();
		edits.add(a);
		edits.add(b);
		this.type = a.type;
		offset = b.offset;
	}
	
	public CompoundEdit(Edit a) {
		edits = new ArrayList<Edit>();
		edits.add(a);
		this.type = a.type;
		offset = a.offset;
	}
	
	public void addEdit(Edit e) {
		edits.add(e);
		offset = e.offset;
	}
	
	public void redo(Document d) {
		for(int i = 0; i < edits.size(); i++) {
			edits.get(i).redo(d);
		}
	}
	
	public void undo(Document d) {
		for(int i = edits.size()-1; i >= 0; i--) {
			edits.get(i).undo(d);
		}
	}
	
	
}
