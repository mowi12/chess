package de.wieland.Chess.gui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import com.google.common.primitives.Ints;

import de.wieland.Chess.engine.board.Move;
import de.wieland.Chess.engine.pieces.Piece;
import de.wieland.Chess.gui.swing.Table.MoveLog;

/**
 * public class TakenPiecesPanel
 * 
 * @author Moritz Wieland
 * @version 1.0
 * @date 10.09.2021
 */
@SuppressWarnings("serial")
public class TakenPiecesPanel extends JPanel {
	private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(100, 250);
	private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
	private static final Color PANEL_COLOR = Color.decode("0xFDF5E6");
	
	private final JPanel northPanel;
	private final JPanel southPanel;
	
	public TakenPiecesPanel() {
		super(new BorderLayout());
		setBackground(PANEL_COLOR);
		setBorder(PANEL_BORDER);
		setPreferredSize(TAKEN_PIECES_DIMENSION);
		
		northPanel = new JPanel(new GridLayout(8, 2));
		northPanel.setBackground(PANEL_COLOR);
		southPanel = new JPanel(new GridLayout(8, 2));
		southPanel.setBackground(PANEL_COLOR);
		
		add(northPanel, BorderLayout.NORTH);
		add(southPanel, BorderLayout.SOUTH);
		
		validate();
	}
	
	public void redo(final MoveLog moveLog) {
		northPanel.removeAll();
		southPanel.removeAll();
		
		final List<Piece> whiteTakenPieces = new ArrayList<>();
		final List<Piece> blackTakenPieces = new ArrayList<>();
		
		for (final Move move : moveLog.getMoves()) {
			if(move.isAttack()) {
				final Piece takenPiece = move.getAttackedPiece();
				
				if(takenPiece.getPieceAlliance().isWhite()) {
					whiteTakenPieces.add(takenPiece);
				} else if(takenPiece.getPieceAlliance().isBlack()) {
					blackTakenPieces.add(takenPiece);
				} else {
					throw new RuntimeException("should not reach here!");
				}
			}
		}
		
		Collections.sort(whiteTakenPieces, new Comparator<Piece>() {
			@Override
			public int compare(final Piece o1, final Piece o2) {
				return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
			}
		});
		
		Collections.sort(blackTakenPieces, new Comparator<Piece>() {
			@Override
			public int compare(final Piece o1, final Piece o2) {
				return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
			}
		});
		
		for (final Piece takenPiece : whiteTakenPieces) {
			try {
				final Image image = ImageIO.read(new File(Table.defaultPieceIconPath + takenPiece.getPieceAlliance().toString().substring(0, 1) + takenPiece.toString() + Table.ending)).getScaledInstance(25, 25, Image.SCALE_SMOOTH);
				final ImageIcon imageIcon = new ImageIcon(image);
				final JLabel imageLabel = new JLabel(imageIcon);
				northPanel.add(imageLabel);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		for (final Piece takenPiece : blackTakenPieces) {
			try {
				final Image image = ImageIO.read(new File(Table.defaultPieceIconPath + takenPiece.getPieceAlliance().toString().substring(0, 1) + takenPiece.toString() + Table.ending)).getScaledInstance(25, 25, Image.SCALE_SMOOTH);
				final ImageIcon imageIcon = new ImageIcon(image);
				final JLabel imageLabel = new JLabel(imageIcon);
				southPanel.add(imageLabel);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		revalidate();
		repaint();
	}
}
