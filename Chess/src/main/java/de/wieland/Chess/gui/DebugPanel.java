package de.wieland.Chess.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * public class DebugPanel
 * 
 * @author Moritz Wieland
 * @version 1.0
 * @date 10.09.2021
 */
@SuppressWarnings({ "serial", "deprecation" })
public class DebugPanel extends JPanel implements Observer {
	private static final Dimension CHAT_PANEL_DIMENSION = new Dimension(700, 150);
	private final JTextArea textArea;
	
	public DebugPanel() {
		super(new BorderLayout());
		
		textArea = new JTextArea("");
		add(textArea);
		setPreferredSize(CHAT_PANEL_DIMENSION);
		validate();
		setVisible(true);
	}

	@Override
	public void update(final Observable o,
					   final Object arg) {
		textArea.setText(o.toString().trim());
		redo();
	}
	
	public void redo() {
		validate();
	}
}
