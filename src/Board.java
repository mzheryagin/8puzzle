import java.util.Arrays;
import java.util.Stack;

public class Board {
  private final int boardSize;
  private final int[][] tiles;

  /**
   * construct a board from an n-by-n array of blocks
   * where blocks[i][j] = block in row i, column j
   */
  private static int[][] boardCopy(int[][] inBoard) {
    int N = inBoard.length;
    int [][] outBoard = new int[N][N];
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        outBoard[i][j] = inBoard[i][j];
      }
    }
    return outBoard;
  }

  public Board(int[][] blocks) {
    boardSize = blocks.length;
    tiles = boardCopy(blocks);
  }

  public int dimension() {                 // board dimension n
    return boardSize;
  }

  public int hamming() {                   // number of blocks out of place
    int outOfPlaceBlocks = 0;
    for (int i = 0; i < boardSize; i++) {
      for (int j = 0; j < boardSize; j++) {
        if(tiles[i][j] != i * boardSize + j + 1) {
          outOfPlaceBlocks++;
        }
      }
    }
    return --outOfPlaceBlocks; //delete one blank tile
  }

  private int manhattanOfBlock(int i, int j) {
    int dI = tiles[i][j] / boardSize;
    int dJ = tiles[i][j] % boardSize;
    if (dJ == 0) {
      dI -= 1;
      dJ = boardSize - 1;
    } else {
      dJ -= 1;
    }
    return Math.abs(i - dI) + Math.abs(j - dJ);
  }

  public int manhattan() {                 // sum of Manhattan distances between blocks and goal
    int totalDistance = 0;
    for (int i = 0; i < boardSize; i++) {
      for (int j = 0; j < boardSize; j++) {
        if (tiles[i][j] != 0 && tiles[i][j] != i * boardSize + j + 1) {
          totalDistance += manhattanOfBlock(i, j);
        }
      }
    }
    return totalDistance;
  }

  public boolean isGoal() {                // is this board the goal board?
    return manhattan() == 0;
  }

  private static void swapBlocks (int[][] board, int iA, int jA, int iB, int jB) {
    int temp = board[iA][jA];
    board[iA][jA] = board[iB][jB];
    board[iB][jB] = temp;
  }

  public Board twin() {                    // a board that is obtained by exchanging any pair of blocks
    int[][] twinBoard = boardCopy(tiles);
    if (twinBoard[0][0] * twinBoard[0][1] != 0) {     // if blank block is not in tiles (0,0) and (0,1)
      swapBlocks(twinBoard, 0,0,0,1);
    } else {
      swapBlocks(twinBoard, 1,0, 1, 1);
    }
    return new Board(twinBoard);
  }

  public boolean equals(Object y) {        // does this board equal y?
    if(y == this) {
      return true;
    }
    if(!(y instanceof Board)) {
      return false;
    }
    Board that = (Board) y;
    return (this.boardSize == that.boardSize) && (Arrays.deepEquals(this.tiles, that.tiles));
  }

  private void pushBoard(Stack<Board> stack, int x, int y, int m, int n) {
    int[][] neighborTiles = boardCopy(tiles);
    swapBlocks(neighborTiles, x, y, m, n);
    stack.push(new Board(neighborTiles));
  }

  public Iterable<Board> neighbors() {     // all neighboring boards
    Stack<Board> boards = new Stack<Board>();
    boolean blankTileFound = false;
    for (int i = 0; i < boardSize && !blankTileFound; i++) {
      for (int j = 0; j < boardSize; j++) {
        if (tiles[i][j] == 0) {
          if (i > 0) {
            pushBoard(boards, i - 1, j, i, j);
          }
          if (j > 0) {
            pushBoard(boards, i, j - 1, i, j);
          }
          if (i < boardSize - 1) {
            pushBoard(boards, i + 1, j, i, j);
          }
          if (j < boardSize - 1) {
            pushBoard(boards, i, j + 1, i, j);
          }
          blankTileFound = true;
          break;
        }
      }
    }
    return boards;
  }

  public String toString() {               // string representation of this board (in the output format specified below)
    StringBuilder s = new StringBuilder();
    s.append(boardSize + "\n");
    for (int i = 0; i < boardSize; i++) {
      for (int j = 0; j < boardSize; j++) {
        s.append(String.format("%2d ", tiles[i][j]));
      }
      s.append("\n");
    }
    return s.toString();
  }

  public static void main(String[] args) { // unit tests (not graded)

  }
}
