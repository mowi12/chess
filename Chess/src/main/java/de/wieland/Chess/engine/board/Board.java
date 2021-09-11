package de.wieland.Chess.engine.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import de.wieland.Chess.engine.Alliance;
import de.wieland.Chess.engine.pieces.Bishop;
import de.wieland.Chess.engine.pieces.King;
import de.wieland.Chess.engine.pieces.Knight;
import de.wieland.Chess.engine.pieces.Pawn;
import de.wieland.Chess.engine.pieces.Piece;
import de.wieland.Chess.engine.pieces.Queen;
import de.wieland.Chess.engine.pieces.Rook;
import de.wieland.Chess.engine.player.BlackPlayer;
import de.wieland.Chess.engine.player.Player;
import de.wieland.Chess.engine.player.WhitePlayer;

/**
 * Public class Board.
 * 
 * @author Moritz Wieland
 * @version 1.0
 * @date 10.09.2021
 */
public class Board {
	private final List<Tile> gameBoard;
	private final Collection<Piece> whitePieces;
	private final Collection<Piece> blackPieces;
	private final WhitePlayer whitePlayer;
	private final BlackPlayer blackPlayer;
	private final Player currentPlayer;
	private final Pawn enPassantPawn;
	
	private Board(final Builder builder) {
		gameBoard = createGameBoard(builder);
		whitePieces = calculateActivePieces(gameBoard, Alliance.WHITE);
		blackPieces = calculateActivePieces(gameBoard, Alliance.BLACK);
		enPassantPawn = builder.enPassantPawn;
		
		final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(whitePieces);
		final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(blackPieces);
		
		whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
		blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
		
		currentPlayer = builder.nextMoveMaker.choosePlayer(whitePlayer, blackPlayer);
	}
	
	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
			final String tileText = this.gameBoard.get(i).toString();
			builder.append(String.format("%3s", tileText));
			
			if((i + 1) % BoardUtils.NUM_TILES_PER_ROW == 0) {
				builder.append("\n");
			}
		}
		
		return builder.toString();
	}
	
	private static List<Tile> createGameBoard(final Builder builder) {
		final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
		
		for (int i = 0; i < BoardUtils.NUM_TILES; i++) {
			tiles[i] = Tile.createTile(i, builder.boardConfig.get(i));
		}
		
		return ImmutableList.copyOf(tiles);
	}
	
	private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Alliance alliance) {
		final List<Piece> activePieces = new ArrayList<>();
		
		for (final Tile tile : gameBoard) {
			if(tile.isTileOccupied()) {
				final Piece piece = tile.getPiece();
				
				if(piece.getPieceAlliance() == alliance) {
					activePieces.add(piece);
				}
			}
		}
		
		return ImmutableList.copyOf(activePieces);
	}
	
	private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
		final List<Move> legalMoves = new ArrayList<>();
		
		for (final Piece piece : pieces) {
			legalMoves.addAll(piece.calculateLegalMoves(this));
		}
		
		return ImmutableList.copyOf(legalMoves);
	}
	
	public static Board createStandardBoard() {
		final Builder builder = new Builder();
		
		//Black Layout
		builder.setPiece(new Rook(Alliance.BLACK, 0));
		builder.setPiece(new Knight(Alliance.BLACK, 1));
		builder.setPiece(new Bishop(Alliance.BLACK, 2));
		builder.setPiece(new Queen(Alliance.BLACK, 3));
		builder.setPiece(new King(Alliance.BLACK, 4, true, true));
		builder.setPiece(new Bishop(Alliance.BLACK, 5));
		builder.setPiece(new Knight(Alliance.BLACK, 6));
		builder.setPiece(new Rook(Alliance.BLACK, 7));
		builder.setPiece(new Pawn(Alliance.BLACK, 8));
		builder.setPiece(new Pawn(Alliance.BLACK, 9));
		builder.setPiece(new Pawn(Alliance.BLACK, 10));
		builder.setPiece(new Pawn(Alliance.BLACK, 11));
		builder.setPiece(new Pawn(Alliance.BLACK, 12));
		builder.setPiece(new Pawn(Alliance.BLACK, 13));
		builder.setPiece(new Pawn(Alliance.BLACK, 14));
		builder.setPiece(new Pawn(Alliance.BLACK, 15));
		
		//White Layout
		builder.setPiece(new Pawn(Alliance.WHITE, 48));
		builder.setPiece(new Pawn(Alliance.WHITE, 49));
		builder.setPiece(new Pawn(Alliance.WHITE, 50));
		builder.setPiece(new Pawn(Alliance.WHITE, 51));
		builder.setPiece(new Pawn(Alliance.WHITE, 52));
		builder.setPiece(new Pawn(Alliance.WHITE, 53));
		builder.setPiece(new Pawn(Alliance.WHITE, 54));
		builder.setPiece(new Pawn(Alliance.WHITE, 55));
		builder.setPiece(new Rook(Alliance.WHITE, 56));
		builder.setPiece(new Knight(Alliance.WHITE, 57));
		builder.setPiece(new Bishop(Alliance.WHITE, 58));
		builder.setPiece(new Queen(Alliance.WHITE, 59));
		builder.setPiece(new King(Alliance.WHITE, 60, true, true));
		builder.setPiece(new Bishop(Alliance.WHITE, 61));
		builder.setPiece(new Knight(Alliance.WHITE, 62));
		builder.setPiece(new Rook(Alliance.WHITE, 63));
		
		//set White to move first
		builder.setMoveMaker(Alliance.WHITE);
		
		return builder.build();
	}
	
	/**
	 * Getter and Setter methods.
	 */
	public Piece getPiece(final int coordinate) { return gameBoard.get(coordinate).getPiece(); }
	public Collection<Piece> getWhitePieces() { return this.whitePieces; }
	public Collection<Piece> getBlackPieces() { return this.blackPieces; }
    public Collection<Piece> getAllPieces() {
    	List<Piece> allPieces = new ArrayList<>();
    	
    	for (final Piece piece : whitePieces) {
    		allPieces.add(piece);
    	}
    	
    	for (final Piece piece : blackPieces) {
    		allPieces.add(piece);
    	}
    	
        return allPieces;
    }
	public Pawn getEnPassantPawn() { return enPassantPawn; }
	public Player getWhitePlayer() { return whitePlayer; }
	public Player getBlackPlayer() { return blackPlayer; }
	public Player getCurrentPlayer() { return currentPlayer; }
	public Iterable<Move> getAllLegalMoves() { return Iterables.unmodifiableIterable(Iterables.concat(whitePlayer.getLegalMoves(), blackPlayer.getLegalMoves())); }
	public Tile getTile(final int tileCoordinate) { return gameBoard.get(tileCoordinate); }
	
	
	/**
	 * Public static class Builder.
	 * 
	 * @author Moritz Wieland
	 * @version 1.0
	 * @date 10.09.2021
	 */
	public static class Builder {
		Map<Integer, Piece> boardConfig;
		Alliance nextMoveMaker;
		Pawn enPassantPawn;
		
		public Builder() {
			this.boardConfig = new HashMap<>();
		}
		
		public Builder setPiece(final Piece piece) {
			this.boardConfig.put(piece.getPiecePosition(), piece);
			return this;
		}
		
		public Builder setMoveMaker(final Alliance nextMoveMaker) {
			this.nextMoveMaker = nextMoveMaker;
			return this;
		}
		
		public void setEnPassantPawn(Pawn enPassantPawn) {
			this.enPassantPawn = enPassantPawn;
		}
		
		public Board build() {
			return new Board(this);
		}
	}
}
