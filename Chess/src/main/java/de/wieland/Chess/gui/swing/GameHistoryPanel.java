package de.wieland.Chess.gui.swing;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import de.wieland.Chess.engine.board.Board;
import de.wieland.Chess.engine.board.Move;
import de.wieland.Chess.gui.swing.Table.MoveLog;

/**
 * public class GameHistoryPanel
 * 
 * @author Moritz Wieland
 * @version 1.0
 * @date 10.09.2021
 */
@SuppressWarnings("serial")
public class GameHistoryPanel extends JPanel {
	private static final Dimension HISTORY_PANEL_DIMENSION = new Dimension(100, 550);
	
	private final DataModel model;
	private final JScrollPane scrollPane;
	
	GameHistoryPanel() {
		setLayout(new BorderLayout());
		model = new DataModel();
		
		final JTable table = new JTable(model);
		table.setRowHeight(15);
		
		scrollPane = new JScrollPane(table);
		scrollPane.setColumnHeaderView(table.getTableHeader());
		scrollPane.setPreferredSize(HISTORY_PANEL_DIMENSION);
		
		add(scrollPane, BorderLayout.CENTER);
		setVisible(true);
	}
	
	void redo(final Board board,
			  final MoveLog moveHistory) {
		int currentRow = 0;
		model.clear();
		
		for (final Move move : moveHistory.getMoves()) {
			final String moveText = move.toString();
			
			if(move.getMovedPiece().getPieceAlliance().isWhite()) {
				if(move.isCheckMove()) {//TODO
					model.setValueAt(moveText + "+", currentRow, 0);
				} else {
					model.setValueAt(moveText, currentRow, 0);
				}
			} else if(move.getMovedPiece().getPieceAlliance().isBlack()) {
				if(move.isCheckMove()) {//TODO
					model.setValueAt(moveText + "+", currentRow, 1);
					currentRow++;
				} else {
					model.setValueAt(moveText, currentRow, 1);
					currentRow++;
				}
			}
		}
		
		if(moveHistory.getMoves().size() > 0) {
			final Move lastMove = moveHistory.getMoves().get(moveHistory.size() - 1);
			final String moveText = lastMove.toString();
			
			if(lastMove.getMovedPiece().getPieceAlliance().isWhite()) {
				model.setValueAt(moveText + calculateCheckAndCheckMateHash(board), currentRow, 0);
			} else if(lastMove.getMovedPiece().getPieceAlliance().isBlack()) {
				model.setValueAt(moveText + calculateCheckAndCheckMateHash(board), currentRow - 1, 1);
			}
		}
		
		final JScrollBar vertical = scrollPane.getVerticalScrollBar();
		vertical.setValue(vertical.getMaximum());
	}
	
	private String calculateCheckAndCheckMateHash(final Board board) {
		if(board.getCurrentPlayer().isInCheckMate()) {
			return "#";
		} else if(board.getCurrentPlayer().isInCheck()) {
			return "+"; //TODO isInCheck has to be stored in Move also
		}
		return "";
	}
	
	
	private static class Row {
		private String whiteMove;
		private String blackMove;
		
		Row() {}
		
		public void setWhiteMove(final String move) { whiteMove = move; }
		public String getWhiteMove() { return whiteMove; }
		public void setBlackMove(final String move) { blackMove = move; }
		public String getBlackMove() { return blackMove; }
	}


	private static class DataModel extends DefaultTableModel {
		private final List<Row> values;
		private static final String[] NAMES = {"White", "Black"};
		
		DataModel() {
			values = new ArrayList<>();
		}
		
		@Override
		public int getRowCount() {
			if(values == null) {
				return 0;
			}
			
			return values.size();
		}
		
		@Override
		public int getColumnCount() {
			return NAMES.length;
		}
		
		@Override
		public Object getValueAt(final int row,
								 final int column) {
			final Row currentRow = values.get(row);
			
			if(column == 0) {
				return currentRow.getWhiteMove();
			} else if(column == 1) {
				return currentRow.getBlackMove();
			}
			
			return null;
		}
		
		@Override
		public void setValueAt(final Object aValue,
							   final int row,
							   final int column) {
			final Row currentRow;
			
			if(values.size() <= row) {
				currentRow = new Row();
				values.add(currentRow);
			} else {
				currentRow = values.get(row);
			}
			
			if(column == 0) {
				currentRow.setWhiteMove((String) aValue);
				fireTableRowsInserted(row, row);
			} else if(column == 1) {
				currentRow.setBlackMove((String) aValue);
				fireTableCellUpdated(row, column);
			}
		}
		
		@Override
		public Class<?> getColumnClass(final int column) {
			return Move.class;
		}
		
		@Override
		public String getColumnName(final int column) {
			return NAMES[column];
		}
		
		public void clear() {
			values.clear();
			setRowCount(0);
		}
	}
}
