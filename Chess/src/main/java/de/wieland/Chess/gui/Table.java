package de.wieland.Chess.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import de.wieland.Chess.engine.board.Board;
import de.wieland.Chess.engine.board.BoardUtils;
import de.wieland.Chess.engine.board.Move;
import de.wieland.Chess.engine.board.MoveTransition;
import de.wieland.Chess.engine.board.Move.MoveFactory;
import de.wieland.Chess.engine.board.Tile;
import de.wieland.Chess.engine.pieces.Piece;
import de.wieland.Chess.engine.player.PlayerType;
import de.wieland.Chess.engine.player.ai.MiniMax;
import de.wieland.Chess.engine.player.ai.MoveStrategy;
import de.wieland.Chess.engine.player.ai.StandardBoardEvaluator;
import de.wieland.Chess.pgn.FenUtilities;

import static javax.swing.JFrame.setDefaultLookAndFeelDecorated;


/**
 * Public class Table.
 * 
 * @author Moritz Wieland
 * @version 1.0
 * @date 10.09.2021
 */
@SuppressWarnings("deprecation")
public class Table extends Observable {
	private static final Table SINGLETON = new Table();
	
	private final JFrame gameFrame;
	private final GameHistoryPanel gameHistoryPanel;
	private final TakenPiecesPanel takenPiecesPanel;
	private final DebugPanel debugPanel;
	private final BoardPanel boardPanel;
	private final MoveLog moveLog;
	private final GameSetup gameSetup;
	private Board chessBoard;
	
	private Move computerMove;
	private Tile sourceTile;
	private Tile destinationTile;
	private Piece humanMovedPiece;
	private boolean highlightLegalMoves;
	
	public static String defaultPieceIconPath;
	public static String ending;
	
	private Color lightTileColor = Color.decode("#FFFACD");
	private Color darkTileColor = Color.decode("#593E1A");
	
	private BoardDirection boardDirection;
	
	private static final int OUTER_FRAME_DIMENSION_X = 700;
	private static final int OUTER_FRAME_DIMENSION_Y = 600;
	private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(500, 500);
	private static final Dimension TILE_PANEL_DIMENSION = new Dimension(62, 62);
	
	private Table() {
		defaultPieceIconPath = "src/resources/art/pieces/chess_com/neo/";
		ending = ".gif";
		
		gameFrame = new JFrame("Chess");
		gameFrame.setLayout(new BorderLayout());
		
		chessBoard = Board.createStandardBoard();
		gameHistoryPanel = new GameHistoryPanel();
		takenPiecesPanel = new TakenPiecesPanel();
		debugPanel = new DebugPanel();
		boardPanel = new BoardPanel();
		moveLog = new MoveLog();
		addObserver(new TableGameAIWatcher());
		gameSetup = new GameSetup(gameFrame, true);
		boardDirection = BoardDirection.NORMAL;
		highlightLegalMoves = true;
		
		final JMenuBar tableMenuBar = new JMenuBar();
		populateMenuBar(tableMenuBar);
		
		gameFrame.setJMenuBar(tableMenuBar);
		gameFrame.add(takenPiecesPanel, BorderLayout.WEST);
		gameFrame.add(boardPanel, BorderLayout.CENTER);
		gameFrame.add(gameHistoryPanel, BorderLayout.EAST);
		gameFrame.add(debugPanel, BorderLayout.SOUTH);
		
		setDefaultLookAndFeelDecorated(true);
		gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		gameFrame.setSize(OUTER_FRAME_DIMENSION_X, OUTER_FRAME_DIMENSION_Y);
		gameFrame.setResizable(false);
		gameFrame.setVisible(true);
	}
	
	public static Table get() {
		return SINGLETON;
	}
	
	public void show() {
		Table.get().getMoveLog().clear();
		Table.get().getGameHistoryPanel().redo(Table.get().getGameBoard(), Table.get().getMoveLog());
		Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
		Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
		Table.get().getDebugPanel().redo();
	}
	
	private void populateMenuBar(JMenuBar tableMenuBar) {
		tableMenuBar.add(createFileMenu());
		tableMenuBar.add(createPreferencesMenu());
        tableMenuBar.add(createOptionsMenu());
        tableMenuBar.add(createDesignMenu());
	}
	
	private JMenu createFileMenu() {
		final JMenu fileMenu = new JMenu("File");
		
		final JMenuItem openPGN = new JMenuItem("Load PGN File");
		openPGN.addActionListener(e -> {
			JFileChooser fileChooser = new JFileChooser();
			int option = fileChooser.showOpenDialog(Table.get().getGameFrame());
			if(option == JFileChooser.APPROVE_OPTION) {
				loadPGNFile(fileChooser.getSelectedFile());
			}
		});
		
		final JMenuItem openFEN = new JMenuItem("Load FEN File");
		openFEN.addActionListener(e -> {
			String fenString = JOptionPane.showInputDialog("Input FEN");
			if(fenString != null) {
				undoAllMoves();
				chessBoard = FenUtilities.createGameFromFEN(fenString);
				Table.get().getBoardPanel().drawBoard(chessBoard);
			}
		});
		
		final JMenuItem saveToPGN = new JMenuItem("Save Game");
		saveToPGN.addActionListener(e -> {
			final JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileFilter(new FileFilter() {
				@Override
				public String getDescription() {
					return ".pgn";
				}
				
				@Override
				public boolean accept(final File file) {
					return file.isDirectory() || file.getName().toLowerCase().endsWith("pgn");
				}
			});
			
			final int option = fileChooser.showSaveDialog(Table.get().getGameFrame());
			
			if(option == JFileChooser.APPROVE_OPTION) {
				savePGNFile(fileChooser.getSelectedFile());
			}
		});
		
		final JMenuItem exit = new JMenuItem("Exit");
		exit.addActionListener(e -> {
			Table.get().getGameFrame().dispose();
			System.exit(0);
		});
		
		fileMenu.add(openPGN);
		fileMenu.add(openFEN);
		fileMenu.add(saveToPGN);
		fileMenu.add(exit);
		
		return fileMenu;
	}
	
	private JMenu createPreferencesMenu() {
		final JMenu prefencesMenu = new JMenu("Preferences");
		
		final JMenu colorChooserSubMenu = new JMenu("Choose Colors");
		
		final JMenuItem chooseDarkMenuItem = new JMenuItem("Choose Dark Tile Color");
		chooseDarkMenuItem.addActionListener(e -> {
			final Color colorChoice = JColorChooser.showDialog(Table.get().getGameFrame(),
															   "Choose Dark Tile Color",
															   Table.get().getGameFrame().getBackground());
			if(colorChoice != null) {
				Table.get().getBoardPanel().setTileDarkColor(chessBoard, colorChoice);
			}
		});
		
		final JMenuItem chooseLightMenuItem = new JMenuItem("Choose Light Tile Color");
		chooseLightMenuItem.addActionListener(e -> {
			final Color colorChoice = JColorChooser.showDialog(Table.get().getGameFrame(),
															   "Choose Light Tile Color",
															   Table.get().getGameFrame().getBackground());
			if(colorChoice != null) {
				Table.get().getBoardPanel().setTileLightColor(chessBoard, colorChoice);
			}
		});
		
		colorChooserSubMenu.add(chooseDarkMenuItem);
		colorChooserSubMenu.add(chooseLightMenuItem);
		
		final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
		flipBoardMenuItem.addActionListener(e -> {
			boardDirection = boardDirection.opposite();
			boardPanel.drawBoard(chessBoard);
		});
		
		final JCheckBoxMenuItem legalMoveHighLighterCheckBox = new JCheckBoxMenuItem("Highlight Legal Moves", true);
		legalMoveHighLighterCheckBox.addActionListener(e -> {
			highlightLegalMoves = legalMoveHighLighterCheckBox.isSelected();
		});
		
		prefencesMenu.add(colorChooserSubMenu);
		prefencesMenu.add(flipBoardMenuItem);
		prefencesMenu.add(legalMoveHighLighterCheckBox);
		
		return prefencesMenu;
	}
	
	private JMenu createOptionsMenu() {
		final JMenu optionsMenu = new JMenu("Options");
		
		final JMenuItem resetMenuItem = new JMenuItem("New Game");
		resetMenuItem.addActionListener(e -> {
			undoAllMoves();
		});
		
		final JMenuItem undoMoveMenuItem = new JMenuItem("Undo Last Move");
		undoMoveMenuItem.addActionListener(e -> {
			if(Table.get().getMoveLog().size() > 0) {
				undoLastMove();
			}
		});
		
		final JMenuItem setupGameMenuItem = new JMenuItem("Setup Game");
		setupGameMenuItem.addActionListener(e -> {
			Table.get().getGameSetup().promptUser();
			Table.get().setupUpdate(Table.get().getGameSetup());
		});
		
		final JMenuItem evaluateBoardMenuItem = new JMenuItem("Evaluate Board");
		evaluateBoardMenuItem.addActionListener(e -> {
			System.out.println(StandardBoardEvaluator.get().evaluationDetails(chessBoard, gameSetup.getSearchDepth()));
		});
		
		optionsMenu.add(resetMenuItem);
		optionsMenu.add(undoMoveMenuItem);
		optionsMenu.add(setupGameMenuItem);
		optionsMenu.add(evaluateBoardMenuItem);
		
		return optionsMenu;
	}
	
	private JMenu createDesignMenu() {
		final JMenu designMenu = new JMenu("Design");
		
		final ButtonGroup group = new ButtonGroup();
		
		final JRadioButtonMenuItem neoMenuItem = new JRadioButtonMenuItem("Neo (Chess.com)", true);
		neoMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				defaultPieceIconPath = "src/resources/art/pieces/chess_com/neo/";
				ending = ".gif";
				boardPanel.drawBoard(chessBoard);
			}
		});
		
		final JRadioButtonMenuItem simpleMenuItem = new JRadioButtonMenuItem("Simple (Widow-Chess)");
		simpleMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				defaultPieceIconPath = "src/resources/art/pieces/black_widow/simple/";
				ending = ".gif";
				boardPanel.drawBoard(chessBoard);
			}
		});
		
		final JRadioButtonMenuItem experimentalMenuItem = new JRadioButtonMenuItem("Experimental (3D-Lichess)");
		experimentalMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				defaultPieceIconPath = "src/resources/art/pieces/lichess/3D/Experimental/";
				ending = ".png";
				boardPanel.drawBoard(chessBoard);
			}
		});
		
		designMenu.add(neoMenuItem);
		group.add(neoMenuItem);
		designMenu.add(simpleMenuItem);
		group.add(simpleMenuItem);
		designMenu.add(experimentalMenuItem);
		group.add(experimentalMenuItem);
		
		return designMenu;
	}
	
	private void updateGameBoard(final Board board) {
		chessBoard = board;
	}
	
	private void updateComputerMove(final Move move) {
		computerMove = move;
	}
	
	private void undoAllMoves() {
		for (int i = Table.get().getMoveLog().size() - 1; i >= 0; i--) {
			final Move lastMove = Table.get().getMoveLog().removeMove(Table.get().getMoveLog().size() - 1);
			chessBoard = this.chessBoard.getCurrentPlayer().unMakeMove(lastMove).getToBoard();
		}
		
		computerMove = null;
		
		Table.get().getMoveLog().clear();
		Table.get().getGameHistoryPanel().redo(Table.get().getGameBoard(), Table.get().getMoveLog());
		Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
		Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
		Table.get().getDebugPanel().redo();
	}
	
	private void undoLastMove() {
        final Move lastMove = Table.get().getMoveLog().removeMove(Table.get().getMoveLog().size() - 1);
        
        chessBoard = chessBoard.getCurrentPlayer().unMakeMove(lastMove).getToBoard();
        computerMove = null;
        
        Table.get().getMoveLog().removeMove(lastMove);
        Table.get().getGameHistoryPanel().redo(Table.get().getGameBoard(), Table.get().getMoveLog());
        Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
        Table.get().getDebugPanel().redo();
    }
	
	private static void loadPGNFile(final File pgnFile) {
//      persistPGNFile(pgnFile); TODO PGNUtilities
    }

    private static void savePGNFile(final File pgnFile) {
//      writeGameToPGNFile(pgnFile, Table.get().getMoveLog()); TODO PGNUtilities
    }
	
	private void moveMadeUpdate(final PlayerType playerType) {
		setChanged();
        notifyObservers(playerType);
    }
	
	private void setupUpdate(final GameSetup gameSetup) {
		setChanged();
		notifyObservers(gameSetup);
	}
	
	private JFrame getGameFrame() { return gameFrame; }
	private Board getGameBoard() { return chessBoard; }
	private GameHistoryPanel getGameHistoryPanel() { return gameHistoryPanel; }
	private TakenPiecesPanel getTakenPiecesPanel() { return takenPiecesPanel; }
	private DebugPanel getDebugPanel() { return debugPanel; }
	private BoardPanel getBoardPanel() { return boardPanel; }
	private MoveLog getMoveLog() { return moveLog; }
	private GameSetup getGameSetup() { return gameSetup; }
	
	
	private static class TableGameAIWatcher implements Observer {
		@Override
		public void update(final Observable o,
						   final Object arg) {	
			if(Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().getCurrentPlayer()) &&
			   !Table.get().getGameBoard().getCurrentPlayer().isInCheckMate() &&
			   !Table.get().getGameBoard().getCurrentPlayer().isInStaleMate()) {
				System.out.println(Table.get().getGameBoard().getCurrentPlayer() + " is set to AI, thinking....");
				final AIThinkTank thinkTank = new AIThinkTank();
				thinkTank.execute();
			}
			
			if (Table.get().getGameBoard().getCurrentPlayer().isInCheckMate()) {
                JOptionPane.showMessageDialog(Table.get().getBoardPanel(),
                        "Game Over: Player " + Table.get().getGameBoard().getCurrentPlayer() + " is in checkmate!", "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
            }

            if (Table.get().getGameBoard().getCurrentPlayer().isInStaleMate()) {
                JOptionPane.showMessageDialog(Table.get().getBoardPanel(),
                        "Game Over: Player " + Table.get().getGameBoard().getCurrentPlayer() + " is in stalemate!", "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);
            }
		}
	}
	
	
	private static class AIThinkTank extends SwingWorker<Move, String> {
		
		private AIThinkTank() {}
		
		@Override
		protected Move doInBackground() throws Exception {
			final MoveStrategy miniMax = new MiniMax(Table.get().getGameSetup().getSearchDepth());
			((Observable) miniMax).addObserver(Table.get().getDebugPanel());
			
			final Move bestMove = miniMax.execute(Table.get().getGameBoard());
			
			return bestMove;
		}
		
		@Override
		public void done() {
			try {
				final Move bestMove = get();
				
				Table.get().updateComputerMove(bestMove);
				Table.get().updateGameBoard(Table.get().getGameBoard().getCurrentPlayer().makeMove(bestMove).getToBoard());
				Table.get().getMoveLog().addMove(bestMove);
				Table.get().getGameHistoryPanel().redo(Table.get().getGameBoard(), Table.get().getMoveLog());
				Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
				Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
				Table.get().getDebugPanel().redo();
				Table.get().moveMadeUpdate(PlayerType.COMPUTER);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public enum BoardDirection {
		NORMAL {
			@Override
			List<TilePanel> traverse(final List<TilePanel> boardTiles) {
				return boardTiles;
			}
			
			@Override
			BoardDirection opposite() {
				return FLIPPED;
			}
		},
		FLIPPED {
			@Override
			List<TilePanel> traverse(final List<TilePanel> boardTiles) {
				return Lists.reverse(boardTiles);
			}
			
			@Override
			BoardDirection opposite() {
				return NORMAL;
			}
		};
		
		abstract List<TilePanel> traverse(final List<TilePanel> boardTiles);
		abstract BoardDirection opposite();
	}
	
	
	@SuppressWarnings("serial")
	private class BoardPanel extends JPanel {
		final List<TilePanel> boardTiles;
		
		BoardPanel() {
			super(new GridLayout(8, 8));
			boardTiles = new ArrayList<>();
			
			for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
				final TilePanel tilePanel = new TilePanel(this, i);
				this.boardTiles.add(tilePanel);
				add(tilePanel);
			}
			
			setPreferredSize(BOARD_PANEL_DIMENSION);
			setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			setBackground(Color.decode("#8B4726"));
			validate();
		}
		
		public void drawBoard(final Board board) {
			removeAll();
			
			for (final TilePanel tilePanel : boardDirection.traverse(boardTiles)) {
				tilePanel.drawTile(board);
				add(tilePanel);
			}
			
			validate();
			repaint();
		}
		
		void setTileDarkColor(final Board board,
							  final Color darkColor) {
			for (final TilePanel tilePanel : boardTiles) {
				tilePanel.setDarkTileColor(darkColor);
			}
			
			drawBoard(board);
		}
		
		void setTileLightColor(final Board board,
                			   final Color lightColor) {
			for (final TilePanel boardTile : boardTiles) {
				boardTile.setLightTileColor(lightColor);
			}
			
			drawBoard(board);
		}
	}
	
	
	public static class MoveLog {
		private final List<Move> moves;
		
		MoveLog() {
			this.moves = new ArrayList<>();
		}
		
		public void addMove(final Move move) {
			moves.add(move);
		}
		
		public boolean removeMove(final Move move) {
			return moves.remove(move);
		}
		
		public Move removeMove(final int index) {
			return moves.remove(index);
		}
		
		public void clear() {
			moves.clear();
		}
		
		public int size() {
			return moves.size();
		}
		
		public List<Move> getMoves() { return moves; }
	}
	
	
	@SuppressWarnings("serial")
	private class TilePanel extends JPanel {
		private final int tileId;
		
		TilePanel(final BoardPanel boardPanel,
				  final int tileId) {
			super(new GridBagLayout());
			this.tileId = tileId;
			setPreferredSize(TILE_PANEL_DIMENSION);
			assingTileColor();
			assignTilePieceIcon(chessBoard);
			highlightTileBorder(chessBoard);
			
			addMouseListener(new MouseListener() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					if(Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().getCurrentPlayer()) ||
					   BoardUtils.isEndGame(Table.get().getGameBoard())) {
						return;
					}
					
					if(SwingUtilities.isRightMouseButton(e)) {
						sourceTile = null;
						destinationTile = null;
						humanMovedPiece = null;
					} else if(SwingUtilities.isLeftMouseButton(e)) {
						if(sourceTile == null) {
							sourceTile = chessBoard.getTile(tileId);
							humanMovedPiece = sourceTile.getPiece();
							
							if(humanMovedPiece == null) {
								sourceTile = null;
							}
						} else {
							destinationTile = chessBoard.getTile(tileId);
							final Move move = MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
							final MoveTransition transition = chessBoard.getCurrentPlayer().makeMove(move);
							
							if(transition.getMoveStatus().isDone()) {
								chessBoard = transition.getToBoard();
								moveLog.addMove(move);
							}
							
							sourceTile = null;
							destinationTile = null;
							humanMovedPiece = null;
						}
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								gameHistoryPanel.redo(chessBoard, moveLog);
								takenPiecesPanel.redo(moveLog);
								
								if(gameSetup.isAIPlayer(chessBoard.getCurrentPlayer())) {
									Table.get().moveMadeUpdate(PlayerType.HUMAN);
								}
								
								boardPanel.drawBoard(chessBoard);
								debugPanel.redo();
							}
						});
					}
				}
				@Override
				public void mouseReleased(final MouseEvent e) {					
				}
				
				@Override
				public void mousePressed(final MouseEvent e) {					
				}
				
				@Override
				public void mouseExited(final MouseEvent e) {					
				}
				
				@Override
				public void mouseEntered(final MouseEvent e) {					
				}
			});
			
			validate();
		}
		
		private void drawTile(final Board board) {
			assingTileColor();
			assignTilePieceIcon(board);
			highlightTileBorder(board);
			highlightAIMove();
			highlightLegals(board);
			validate();
			repaint();
		}

		private void assignTilePieceIcon(final Board board) {
			removeAll();
			
			if(board.getTile(tileId).isTileOccupied()) {
				try {
					Image image = ImageIO.read(new File(defaultPieceIconPath + board.getTile(tileId).getPiece().getPieceAlliance().toString().substring(0, 1) + board.getTile(tileId).getPiece().toString() + ending)).getScaledInstance(36, 36, Image.SCALE_SMOOTH);
					add(new JLabel(new ImageIcon(image)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private void assingTileColor() {
			if(BoardUtils.EIGHTH_RANK[tileId] || BoardUtils.SIXTH_RANK[tileId] ||
			   BoardUtils.FOURTH_RANK[tileId] || BoardUtils.SECOND_RANK[tileId]) {
				setBackground(tileId % 2 == 0 ? lightTileColor : darkTileColor);
			} else if (BoardUtils.SEVENTH_RANK[tileId] || BoardUtils.FIFTH_RANK[tileId] ||
					   BoardUtils.THIRD_RANK[tileId] || BoardUtils.FIRST_RANK[tileId]) {
				setBackground(tileId % 2 != 0 ? lightTileColor : darkTileColor);
			}
		}
		
		void setLightTileColor(final Color color) {
            lightTileColor = color;
        }

        void setDarkTileColor(final Color color) {
            darkTileColor = color;
        }
		
		private void highlightTileBorder(final Board board) {
            if((humanMovedPiece != null) &&
              (humanMovedPiece.getPieceAlliance() == board.getCurrentPlayer().getAlliance()) &&
              (humanMovedPiece.getPiecePosition() == tileId)) {
                setBorder(BorderFactory.createLineBorder(Color.cyan));
            } else {
                setBorder(BorderFactory.createLineBorder(Color.GRAY));
            }
        }
		
		private void highlightAIMove() {
            if(computerMove != null) {
                if(tileId == computerMove.getCurrentCoordinate()) {
                    setBackground(Color.pink);
                } else if(this.tileId == computerMove.getDestinationCoordinate()) {
                    setBackground(Color.red);
                }
            }
        }
		
		private void highlightLegals(final Board board) {
			if(highlightLegalMoves) {
				for (final Move move : pieceLegalMoves(board)) {
					if(move.getDestinationCoordinate() == tileId) {
						try {
							add(new JLabel(new ImageIcon(ImageIO.read(new File("src/resources/art/misc/green_dot.png")))));
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		private Collection<Move> pieceLegalMoves(final Board board) {
			if (humanMovedPiece != null && humanMovedPiece.getPieceAlliance() == board.getCurrentPlayer().getAlliance()) {
                // return a list of all the legal moves + the castle moves
                return ImmutableList.copyOf(Iterables.concat(humanMovedPiece.calculateLegalMoves(board),
                		chessBoard.getCurrentPlayer().calculateKingCastles(chessBoard.getCurrentPlayer().getLegalMoves(),
                				chessBoard.getCurrentPlayer().getOpponent().getLegalMoves())));
            }
			
            return Collections.emptyList();
		}
	}
}
