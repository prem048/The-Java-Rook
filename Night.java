public class Night extends Piece {

    public Night(String position, boolean isWhite) {
        super(position, isWhite);
    }

    @Override
    public String isValidMove(String newPosition, Piece[][] board) {
        // Convert positions like "A2" to board indices
        int currentRow = Character.getNumericValue(position.charAt(1)) - 1;
        int currentCol = Character.toUpperCase(position.charAt(0)) - 'A';
        
        int newRow = Character.getNumericValue(newPosition.charAt(1)) - 1;
        int newCol = Character.toUpperCase(newPosition.charAt(0)) - 'A';

        // Ensure within board limits
        if (newRow < 0 || newRow > 7 || newCol < 0 || newCol > 7) {
            return "Out Of Bounds";  
        }

        // Prevent moving to the same position
        if (newPosition.equals(position)) {
            return "Can't move to the same position";  
        }

        // Check if the move is an L-shape
        int rowDiff = Math.abs(newRow - currentRow);
        int colDiff = Math.abs(newCol - currentCol);
        if ((rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2)) {
            // Allow move if destination is empty or contains an opponent's piece
            if (board[newRow][newCol] == null || board[newRow][newCol].isWhite() != isWhite) {
                return "Valid Move";
            }
        }

        return "Invalid Move";  
    }
}