package org.elusive.ui.tools;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EditableLabel extends JPanel {

	private EditablePanelListener listener;

	private JLabel label;
	private JTextField input;

	public EditableLabel(final EditablePanelListener listener, String text) {
		this.listener = listener;

		label = new JLabel(text);
		input = new JTextField(text);
		label.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				label.setVisible(false);
				input.setVisible(true);
				input.requestFocus();
			}
		});
		input.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
//				System.out.println("EditableLabel.EditableLabel(...).new ActionListener() {...}.actionPerformed()");
//				validateInput();				
			}
		});
		input.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {
//				System.out.println("EditableLabel.EditableLabel(...).new FocusListener() {...}.focusLost()");
				if( !validateInput() ){
					input.setText(label.getText());
					input.setVisible(false);
					label.setVisible(true);
				}
			}
			@Override
			public void focusGained(FocusEvent arg0) {
			}
		});

		this.add(label);
		this.add(input);
		input.setVisible(false);
	}
	
	private boolean validateInput(){
//		System.out.println("EditableLabel.validateInput()   label:"+label.getText()+" , input:"+input.getText());
		if( input.getText().equals(label.getText())){
			input.setVisible(false);
			label.setVisible(true);
			return true;
		}
		boolean accepted = listener.textChanged(input.getText());
		if (accepted) {
			label.setText(input.getText());
			input.setVisible(false);
			label.setVisible(true);
		}
		return accepted;
	}

}
