package netViewer;
/*
 * NetViewer
 * Class: ArbitraryPanel
 *
 * Text area that knows how to print() and println().
 */

import javax.swing.JTextArea;

public class MyTextArea extends JTextArea {

	private static final long serialVersionUID = 1L;

	MyTextArea() {
		super();
	}

	public void println(String text) {
		append(text+"\n");
		setCaretPosition(getText().length()); // scroll to bottom
	}

	public void print(String text) {
		append(text);
	}

	public void println() {
		append("\n");
		setCaretPosition(getText().length()); // scroll to bottom
	}







}