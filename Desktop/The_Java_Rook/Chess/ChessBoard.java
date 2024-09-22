package Chess;

public class ChessBoard {
    private Piece[][] board;

    public ChessBoard(){
        board = new Piece[8][8];
        initialiseGame();
    }

    public void initialiseGame(){
        // Initialize pieces for both players (White on rows 1-2, Black on rows 7-8)
        
        // White pieces
        board[0][0] = new Rook("A1", true);
        board[0][1] = new Knight("B1", true);
        board[0][2] = new Bishop("C1", true);
        board[0][3] = new Queen("D1", true);
        board[0][4] = new King("E1", true);
        board[0][5] = new Bishop("F1", true);
        board[0][6] = new Knight("G1", true);
        board[0][7] = new Rook("H1", true);

        // White pawns
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn((char) ('A' + i) + "2", true);
        }

        // Black pieces
        board[7][0] = new Rook("A8", false);
        board[7][1] = new Knight("B8", false);
        board[7][2] = new Bishop("C8", false);
        board[7][3] = new Queen("D8", false);
        board[7][4] = new King("E8", false);
        board[7][5] = new Bishop("F8", false);
        board[7][6] = new Knight("G8", false);
        board[7][7] = new Rook("H8", false);

        // Black pawns
        for (int i = 0; i < 8; i++) {
            board[6][i] = new Pawn((char) ('A' + i) + "7", false);
        }
    }

    public void printBoard(boolean isWhiteTurn) {
        // Print column labels
        System.out.print("    A   B   C   D   E   F   G   H\n");
        System.out.print("  +---+---+---+---+---+---+---+---+\n");
    
        if (isWhiteTurn) {
            // Print from White's perspective
            for (int i = 7; i >= 0; i--) {
                System.out.print((i + 1) + " |");
                for (int j = 0; j < 8; j++) {
                    printPiece(i, j);
                }
                System.out.print("\n  +---+---+---+---+---+---+---+---+\n");
            }
        } else {
            // Print from Black's perspective
            for (int i = 0; i < 8; i++) {
                System.out.print((8 - i) + " |");
                for (int j = 7; j >= 0; j--) {
                    printPiece(i, j);
                }
                System.out.print("\n  +---+---+---+---+---+---+---+---+\n");
            }
        }
    }
    
    private void printPiece(int i, int j) {
        if (board[i][j] != null) {
            Piece piece = board[i][j];
            char pieceChar = piece.isWhite() ?
                    Character.toUpperCase(piece.getClass().getSimpleName().charAt(0)) :
                    Character.toLowerCase(piece.getClass().getSimpleName().charAt(0));
            System.out.print(" " + pieceChar + " |");
        } else {
            System.out.print("   |");
        }
    }

    public boolean movePiece(String currentPosition, String newPosition){
        // Convert positions like "A2" to board indices
        int currentRow = 8 - Character.getNumericValue(currentPosition.charAt(1));
        int currentCol = currentPosition.charAt(0) - 'A';
        int newRow = 8 - Character.getNumericValue(newPosition.charAt(1));
        int newCol = newPosition.charAt(0) - 'A';

        Piece piece = board[currentRow][currentCol];
        if (piece != null && piece.isValidMove(newPosition, board)) {
            // Move piece to new position
            board[newRow][newCol] = piece;
            piece.setPosition(newPosition);
            board[currentRow][currentCol] = null;
            return true;
        } else {
            System.out.println("Invalid move!");
            return false;
        }
    }

    public Piece getPieceAt(String position) {
        int row = 8 - Character.getNumericValue(position.charAt(1));
        int col = position.charAt(0) - 'A';
        return board[row][col];
    }
}
