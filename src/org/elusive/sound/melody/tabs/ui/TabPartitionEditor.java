package org.elusive.sound.melody.tabs.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.Utilities;

import org.elusive.main.tools.IOtools;
import org.elusive.sound.melody.tabs.Couplet;
import org.elusive.sound.melody.tabs.GuitarTab;
import org.elusive.sound.melody.tabs.TabChord;
import org.elusive.sound.melody.tabs.TabChordChooserListener;
import org.elusive.sound.melody.tabs.TabNote;
import org.elusive.sound.melody.tabs.TabPartition;
import org.elusive.sound.melody.tabs.Tranche;
import org.elusive.sound.rythm.Hit;
import org.elusive.sound.rythm.Rythm;
import org.elusive.sound.rythm.ui.RythmEditor;
import org.elusive.ui.action.ActionCategory;
import org.elusive.ui.action.MyAction;
import org.elusive.ui.action.shortcut.MyKeyStrokes;
import org.elusive.ui.config.Colors;
import org.elusive.ui.fenetre.ElusivePanel;
import org.elusive.ui.menus.MenuTools;
import org.elusive.ui.resources.Icons;
import org.elusive.ui.texteditors.JTextPaneNoWrap;
import org.elusive.ui.tools.PopupListener;
import org.elusive.ui.tools.TextComponentTools;
import org.elusive.ui.tools.ToggleInsertDocument;
import org.elusive.ui.tools.file.OpenSaveManager;

public class TabPartitionEditor extends ElusivePanel {
	
	
	public static void main(String[] args) {
		TabPartition tabPartition = new TabPartition();
		
		Rythm rythm = new Rythm();
		rythm.addHit(new Hit(0, 1000));
		rythm.addHit(new Hit(44100, 1000));
		rythm.addHit(new Hit(88200, 2000));
		
		GuitarTab tab = new GuitarTab("------\n------\n------\n------\n------\n-0-1-2");
		
		tabPartition.setRythm(rythm);
		tabPartition.setTab(tab);
		
		TabPartitionEditor editor = new TabPartitionEditor(tabPartition);
		JFrame frame = new JFrame();
		frame.getContentPane().add(editor);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}

	private TabPartition tabPartition;
	
	private OpenSaveManager openSaveManager;
	
	// actions
	private MyAction newAction;
	private MyAction columnCopyAction;
	private MyAction columnCutAction;
	private MyAction columnPasteAction;
	private MyAction insertChordAction;
	
	
	//UI
	private JTextPane textcomp;
	private JFrame chordChooser;
	private RythmEditor rythmEditor;
	
	public static final File tabsDirectory = new File("tabs");

	private String[] columnClipboard;

	// static UI
	private static final String DEFAULT_STYLE = "DefaultStyle";
	private static final String STYLE_TAB_NOTE = "StyleTabNote";
	private static final String STYLE_TAB_NOTE_HIT = "StyleTabNoteHit";

	public TabPartitionEditor(final TabPartition tabPartition) {
		super("Tab Partition Editor");
		this.tabPartition = tabPartition;
		
		// OpenSaveManager
		openSaveManager = new OpenSaveManager(this, tabsDirectory, "tab", ".txt") {
			@Override
			public String save() {
				return textcomp.getText();
			}

			@Override
			public boolean open(String object) {
				textcomp.setText(object);
				textcomp.setCaretPosition(0);
				TextComponentTools.addUndoRedo(textcomp);
				recalculeTab();
				return true;
			}
		};
		
		// Chord Chooser
		chordChooser = new JFrame();
		chordChooser.setLocationRelativeTo(textcomp);
		TabChordChooser chooser = new TabChordChooser(new TabChordChooserListener() {
			@Override
			public void tabChordChosen(TabChord tabChord) {
				int pos = textcomp.getCaretPosition();
				int topStringPos = topGuitarStringPosition(pos);
				pos = topStringPos;

				StringBuilder[] sbs = new StringBuilder[6];
				for (int i = 0; i < 6; i++) {
					sbs[i] = new StringBuilder();
				}
				int maxLength = 0;
				for (TabNote note : tabChord) {
					String str = Integer.toString(note.getFrette());
					sbs[note.getCorde()].append(str);
					if (str.length() > maxLength)
						maxLength = str.length();
				}
				maxLength++;
				for (StringBuilder sb : sbs) {
					while (sb.length() < maxLength) {
						sb.append('-');
					}
				}

				int col = TextComponentTools.getColumn(textcomp, pos);
				try {

					for (int corde = 5; corde >= 0; corde--) {
						// System.out.println("insert("+pos+", "+sbs[corde]+")");
						textcomp.getDocument().insertString(pos, sbs[corde].toString(), null);
						pos = Utilities.getRowEnd(textcomp, pos) + col;
					}
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
				recalculeTab();
				chordChooser.setVisible(false);
			}
		});
		chordChooser.add(chooser);
		chordChooser.pack();

		rythmEditor = new RythmEditor(tabPartition.getRythm()) {
			@Override
			protected void rythmChanged(Rythm rythm) {
				tabPartition.setRythm(rythm);
				recalculeStyleDocument();
			}
		};
		

		// Actions
		newAction = new MyAction("New", MyKeyStrokes.NEW_KEY_STROKE, "New tab", Icons.NEW_ICON, ActionCategory.FILE) {
			@Override
			public void actionPerformed(ActionEvent e) {
				newAction();
			}
		};
		columnCopyAction = new MyAction("Column copy", MyKeyStrokes.COLUMN_COPY_STROKE, "Column copy", Icons.COLUMN_COPY_ICON, ActionCategory.EDIT) {
			@Override
			public void actionPerformed(ActionEvent e) {
				columnCopy(false);
			}
		};
		columnCutAction = new MyAction("Column cut", MyKeyStrokes.COLUMN_CUT_STROKE, "Column cut", Icons.COLUMN_CUT_ICON, ActionCategory.EDIT) {
			@Override
			public void actionPerformed(ActionEvent e) {
				columnCopy(true);
			}
		};
		columnPasteAction = new MyAction("Column paste", MyKeyStrokes.COLUMN_PASTE_STROKE, "Column paste", Icons.COLUMN_PASTE_ICON, ActionCategory.EDIT) {
			@Override
			public void actionPerformed(ActionEvent e) {
				paste();
			}
		};
		insertChordAction = new MyAction("Insert Chord", MyKeyStrokes.INSERT_CHORD_STROKE, "Insert chord", Icons.FREQUENCE_ICON, ActionCategory.EDIT) {
			@Override
			public void actionPerformed(ActionEvent e) {
				insertChordAction();
			}
		};
		actions.add(openSaveManager.getOpenAction());
		actions.add(openSaveManager.getSaveAction());
		actions.add(openSaveManager.getSaveAsAction());
		actions.add(newAction);
		actions.add(columnCopyAction);
		actions.add(columnCutAction);
		actions.add(columnPasteAction);
		actions.add(insertChordAction);
		
		initPanel();
	}
	
	public void newAction(){
		String file = this.getClass().getResource("/resources/tabs/new.txt").getFile();
		String text = IOtools.readFile(new File(file));
		textcomp.setText(text);
		recalculeTab();
	}
	
	public void insertChordAction(){
		chordChooser.setVisible(true);
	}

	private void initPanel() {
		ToggleInsertDocument doc = new ToggleInsertDocument();
		textcomp = new JTextPaneNoWrap();
		textcomp.setBackground(Colors.BACKGROUND_TAB_EDITOR);
		textcomp.setDocument(doc);
		textcomp.setText(tabPartition.getTab().toString());
		textcomp.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				int k = e.getKeyCode();
				if( k != KeyEvent.VK_LEFT && k != KeyEvent.VK_RIGHT && k != KeyEvent.VK_UP && k != KeyEvent.VK_DOWN ){ 
					recalculeTab();
				}
			}
		});
		textcomp.addKeyListener(doc); // why ?
		TextComponentTools.addUndoRedo(textcomp);
		textcomp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int caret = textcomp.getCaretPosition();
				System.out.println("caret="+caret);
			}
		});
		textcomp.setCaretColor(Colors.TAB_EDITOR_CARET);

		// TEXT STYLES
		Style style = textcomp.addStyle(DEFAULT_STYLE, null);
		StyleConstants.setForeground(style, Colors.TAB_EDITOR_TEXT);
		//
		style = textcomp.addStyle(STYLE_TAB_NOTE, null);
		StyleConstants.setForeground(style, Colors.TAB_EDITOR_NOTE);
		StyleConstants.setBold(style, true);
		//
		style = textcomp.addStyle(STYLE_TAB_NOTE_HIT, null);
		StyleConstants.setForeground(style, Colors.TAB_EDITOR_NOTE_HIT);
		StyleConstants.setBold(style, true);

		recalculeTab();

//		JPanel panel = new JPanel(new BorderLayout());
//		panel.add(textcomp);
		JScrollPane scrollPane = new JScrollPane(textcomp);

		final JPopupMenu popup = MenuTools.menuBarToPopupMenu(createMenuBarFromActions());
		textcomp.addMouseListener(new PopupListener() {
			@Override
			public JPopupMenu showMenu(MouseEvent e) {
				return popup;
			}
		});
		
		
		
		// *********** LAYOUT ************** //
		this.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy ++;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(createButtonBarFromActions(), gbc);
		
		gbc.gridy = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		this.add(scrollPane, gbc);
		
		gbc.gridx++;
		gbc.weightx = 0;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.VERTICAL;
		this.add(rythmEditor, gbc); 

	}

	private void recalculeTab() {
		GuitarTab tab = new GuitarTab(textcomp.getText());
		tabPartition.setTab(tab);
		recalculeStyleDocument();
	}

	/**
	 * To be called after rythm modified
	 */
	private void recalculeStyleDocument() {
		StyledDocument doc = textcomp.getStyledDocument();

		doc.setCharacterAttributes(0, doc.getLength(), textcomp.getStyle(DEFAULT_STYLE), true);

		// Text colors

		int numTranche = 0;
		int rythmSize = tabPartition.getRythm().size();
		for (Couplet couplet : tabPartition.getTab().getCouplets()) {
			for (Tranche tranche : couplet) {
				for (TabNote note : tranche) {

					int offset = note.getGlobalOffset();
					int length = note.toTabString().length();
					Style style = textcomp.getStyle(STYLE_TAB_NOTE);
					// if( columnMode ){
					// style = textcomp.getStyle(STYLE_TAB_NOTE_COLUMN_MODE);
					// }
					if( numTranche < rythmSize ){
						style = textcomp.getStyle(STYLE_TAB_NOTE_HIT);
					}
					doc.setCharacterAttributes(offset, length, style, true);
					
				}
				numTranche++;
			}
		}
	}
	
	/*
	 * Returns the position in the text component corresponding to the top
	 * guitar string (high E) just over the position passed as parameter returns
	 * -1 if not found (though it should fin it)
	 */
	public int topGuitarStringPosition(int position) {
		List<Couplet> couplets = this.tabPartition.getTab().getCouplets();
		int pos = 0;
		for (Couplet couplet : this.tabPartition.getTab().getCouplets()) {
			int offsetCouplet = couplet.getOffset();
			if (offsetCouplet > position) {
				break;
			}
			pos = offsetCouplet;
		}
		pos += TextComponentTools.getColumn(textcomp, position) - 1;
		return pos;

	}

	public JTextPane getTextcomp() {
		return textcomp;
	}

	private void columnCopy(boolean cut) {
		int selectionStart = textcomp.getSelectionStart();
		int selectionEnd = textcomp.getSelectionEnd();
		int start = TextComponentTools.getColumn(textcomp, selectionStart);
		int end = TextComponentTools.getColumn(textcomp, selectionEnd);
		if (end < start) {
			return;
		}
		int length = end - start;
		int numLines = 1 + TextComponentTools.getRow(textcomp, selectionEnd) - TextComponentTools.getRow(textcomp, selectionStart);

		columnClipboard = new String[numLines];
		try {
			for (int i = 0; i < numLines; i++) {
				columnClipboard[i] = textcomp.getText(selectionStart, length);
				if( cut ){
					textcomp.getDocument().remove(selectionStart, length);
				}
				selectionStart = Utilities.getRowEnd(textcomp, selectionStart);
				selectionStart += start;
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	private void paste(){
		if( columnClipboard != null ){
			int selectionStart = textcomp.getCaretPosition();
			int start = TextComponentTools.getColumn(textcomp, selectionStart);
			try {
				for(String s : columnClipboard){
					textcomp.getDocument().insertString(selectionStart, s, null);
					selectionStart = Utilities.getRowEnd(textcomp, selectionStart);
					selectionStart += start;
				}
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		recalculeTab();
	}
	
}
