package org.elusive.sound.rythm;

public class Hit{
	//offset to previous hit
	private int offset;
	private int length = 22050;
	public Hit(int offset, int length) {
		this.offset = offset;
		this.length = length;
	}
	public Hit(int offset) {
		this.offset = offset;
	}
	public int getOffset() {
		return offset;
	}
	public void setOffset(int offset) {
		this.offset = offset;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	
	
	
	//UI
	private int line;
	private int caretPosition;
	private int caretPositionEnd;
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public int getCaretPosition() {
		return caretPosition;
	}
	public void setCaretPosition(int caretPosition) {
		this.caretPosition = caretPosition;
	}
	public int getCaretPositionEnd() {
		return caretPositionEnd;
	}
	public void setCaretPositionEnd(int caretPositionEnd) {
		this.caretPositionEnd = caretPositionEnd;
	}
}