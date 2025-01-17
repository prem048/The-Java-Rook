import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.swing.*;

public class ChessUI extends JFrame {
    private ChessBoard board;
    private boolean isWhiteTurn;
    private String selectedPosition;

    public ChessUI() {
        board = new ChessBoard();
        isWhiteTurn = true;
        selectedPosition = null;

        setTitle("The Java Rook");
        ImageIcon icon = new ImageIcon(getClass().getResource("/images/logo.jpg"));
        setIconImage(icon.getImage()); 
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ChessPanel panel = new ChessPanel();
        add(panel);
        setVisible(true);
    }
    
    private BufferedImage loadPieceImage(String color, String pieceName) {
        String path = "images/" + color + pieceName + ".png";
        try {
            InputStream is = getClass().getResourceAsStream("/" + path);
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private class ChessPanel extends JPanel {
        private String highlightedSquare = null;
        private int tileSize;
        private Image infoImage;

        public ChessPanel() {
            infoImage = new ImageIcon(getClass().getResource("/images/info.jpg")).getImage();
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    handleMousePress(e);
                }
            });
        }

        private void handleMousePress(MouseEvent e) {
            int col = (isWhiteTurn) ? e.getX() / (tileSize) : 7 - (e.getX() / (tileSize));
            int row = (isWhiteTurn) ? e.getY() / (tileSize) : 7 - (e.getY() / (tileSize));
            if ((row >= 0 && row <=7) && (col >= 0 && col <=7)) {
                String position = "" + (char) ('A' + col) + (8 - row);
    
                if ((selectedPosition == null) || hasSelectedSameColor(selectedPosition, position)) {
                    Piece piece = board.getPieceAt(position);
                    if (piece != null && piece.isWhite() == isWhiteTurn) {
                        selectedPosition = position;
                        highlightedSquare = position;
                        repaint();
                    }
                } else {
                    String moveStatus = board.movePiece(selectedPosition, position);
                    if (moveStatus.equals("Valid Move")) {
                        isWhiteTurn = !isWhiteTurn;
                    } else if (moveStatus.equals("Invalid Move")) {
                        JOptionPane.showMessageDialog(this, "Play a Valid move :(", "Invalid Move!", JOptionPane.INFORMATION_MESSAGE);
                    } else if (moveStatus.equals("Check")) {
                        JOptionPane.showMessageDialog(this, "King Safety is Important!", "Check!!!", JOptionPane.INFORMATION_MESSAGE);
                    } else if (moveStatus.endsWith("Promotion")) {
                        Promotion promotionWindow = new Promotion((JFrame) SwingUtilities.getWindowAncestor(this));
                        String promotedPiece = promotionWindow.getPromotedPiece();
                        board.promote(moveStatus, promotedPiece, position);
                        isWhiteTurn = !isWhiteTurn;
                    }
                    selectedPosition = null;
                    highlightedSquare = null;
                    repaint();

                    if (moveStatus.equals("White Wins")) {
                        JOptionPane.showMessageDialog(this, moveStatus, "Checkmate!", JOptionPane.INFORMATION_MESSAGE);
                    } else if (moveStatus.equals("Black Wins")) {
                        JOptionPane.showMessageDialog(this, moveStatus, "Checkmate!", JOptionPane.INFORMATION_MESSAGE);
                    } else if (moveStatus.equals("Stalemate")) {
                        JOptionPane.showMessageDialog(this, moveStatus, "Draw!", JOptionPane.INFORMATION_MESSAGE);
                    } 
                }
            }
        }

        private boolean hasSelectedSameColor(String selectedPosition, String position) {
            return (board.getPieceAt(position) != null) && ((board.getPieceAt(selectedPosition).isWhite()) == (board.getPieceAt(position).isWhite()));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawBoard(g);
        }

        private void drawBoard(Graphics g) {
            tileSize = getHeight() / 8;
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    String currentPosition = (isWhiteTurn)? "" + (char) ('A' + col) + (8 - row) : "" + (char) ('H' - col) + (row + 1);
                    
                    if ((row + col) % 2 == 0) {
                        g.setColor(new Color(235, 237, 209));
                    } else {
                        g.setColor(new Color(114, 149, 83));
                    }
                    g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);

                    // Highlight selected square
                    if (currentPosition.equals(highlightedSquare)) {
                        g.setColor(new Color(255, 255, 0, 100)); // Semi-transparent yellow
                        g.fillRect(col * tileSize, row * tileSize, tileSize, tileSize);
                    }

                    Piece piece = board.getPieceAt(currentPosition);
                    if (piece != null) {
                        String color = piece.isWhite() ? "w" : "b";
                        BufferedImage pieceImage = loadPieceImage(color, piece.getClass().getSimpleName().toLowerCase().substring(0, 1));
                        if (pieceImage != null) {
                            g.drawImage(pieceImage, col * tileSize, row * tileSize, tileSize, tileSize, this);
                        }
                    }
                }
            }
            g.drawImage(infoImage, tileSize * 8, 0, (getWidth() - (tileSize * 8)), getHeight(), this);
        }
    }
}

class Promotion extends JDialog implements ActionListener{
    JRadioButton queen;
    JRadioButton rook;
    JRadioButton bishop;
    JRadioButton knight;
    JButton okButton;
    String promotedPiece;

    Promotion(Frame parent) {
        super(parent, "Promote Piece", true);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(parent);
        this.setLayout(new FlowLayout());

        queen = new JRadioButton("Queen");
        rook = new JRadioButton("Rook");
        bishop = new JRadioButton("Bishop");
        knight = new JRadioButton("Knight");

        ButtonGroup promotablePieces = new ButtonGroup();
        promotablePieces.add(queen);
        promotablePieces.add(rook);
        promotablePieces.add(bishop);
        promotablePieces.add(knight);
        
        queen.addActionListener(this);
        rook.addActionListener(this);
        bishop.addActionListener(this);
        knight.addActionListener(this);

        okButton = new JButton("Ok");
        okButton.addActionListener(this);
  
        this.add(queen);
        this.add(rook);
        this.add(bishop);
        this.add(knight);
        this.add(okButton);
        this.pack();
        this.setLocationRelativeTo(parent);
        this.setVisible(true);
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) {
            if(queen.isSelected()) {
                promotedPiece = "Queen";
            } else if(rook.isSelected()) {
                promotedPiece = "Rook";
            } else if(bishop.isSelected()) {
                promotedPiece = "Bishop";
            } else if (knight.isSelected()) {
                promotedPiece = "Knight";
            } else {
                JOptionPane.showMessageDialog(this, "Please select at least one option.", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }
            this.dispose();
        }
    }

    public String getPromotedPiece() {
        return promotedPiece;
    }
}