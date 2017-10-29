import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;


public class Solver {
  private boolean isSolvable;
  private int totalMoves;
  private Node lastNode;

  private class Node implements Comparable<Node> {
    private Board board;
    private int priority;
    private Node prevNode;
    private int moves;

    public Node (Board board, int moves, Node prevNode) {
      this.board = board;
      this.moves = moves;
      this.prevNode = prevNode;
      this.priority = board.manhattan() + moves;
    }

    public int compareTo (Node that) {
      if (this.priority > that.priority) {
        return 1;
      }
      if (this.priority < that.priority) {
        return -1;
      }
      return 0;
    }
  }

  private void addNodes (Node n, MinPQ<Node> pq) {
    for (Board next: n.board.neighbors()) {
      if( (n.prevNode == null) || !next.equals(n.prevNode.board) ) {
        pq.insert(new Node(next, n.moves + 1, n));
      }
    }
  }

  public Solver(Board initial) {           // find a solution to the initial board (using the A* algorithm)
    if (initial == null) {
      throw new java.lang.IllegalArgumentException();
    }
    MinPQ<Node> nodeMinPQ = new MinPQ<>();
    Node startNode = new Node(initial, 0, null);
    nodeMinPQ.insert(startNode);
    MinPQ<Node> twinNodeMinPQ = new MinPQ<>();
    Node startTwinNode = new Node(initial.twin(), 0 , null);
    twinNodeMinPQ.insert(startTwinNode);

    while (true) {
      Node node = nodeMinPQ.delMin();
      Node twinNode = twinNodeMinPQ.delMin();
      if (node.board.isGoal()){
        lastNode = node;
        totalMoves = node.moves;
        isSolvable = true;
        break;
      }
      if (twinNode.board.isGoal()) {
        totalMoves = -1;
        isSolvable = false;
        break;
      }
      addNodes(node,nodeMinPQ);
      addNodes(twinNode,twinNodeMinPQ);
    }
  }

  public boolean isSolvable() {            // is the initial board solvable?
    return isSolvable;
  }

  public int moves() {                     // min number of moves to solve initial board; -1 if unsolvable
    return totalMoves;
  }

  public Iterable<Board> solution() {      // sequence of boards in a shortest solution; null if unsolvable
    Stack<Board> solution = new Stack<>();
    if (!isSolvable) {
      solution = null;
    } else {
      Node last = lastNode;
      while (last != null) {
        solution.push(last.board);
        last = last.prevNode;
      }
    }
    return solution;
  }

  public static void main(String[] args) { // solve a slider puzzle (given below)
    // create initial board from file
    In in = new In(args[0]);
    int n = in.readInt();
    int[][] blocks = new int[n][n];
    for (int i = 0; i < n; i++)
      for (int j = 0; j < n; j++)
        blocks[i][j] = in.readInt();
    Board initial = new Board(blocks);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
      StdOut.println("No solution possible");
    else {
      StdOut.println("Minimum number of moves = " + solver.moves());
      for (Board board : solver.solution())
        StdOut.println(board);
    }
  }
}