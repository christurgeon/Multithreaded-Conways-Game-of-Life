/**
 *  @author Chris Turgeon
 *  @version 1.0
 */

package code.driver;

public class GridThread extends Thread {

  private GameOfLife game;
  private byte[][] readGrid;
  private byte[][] writeGrid;
  private int x0, y0; // The top left coordinate of the mini-grid
  private int x1, y1; // The bottom right coordinate of the mini-grid

  /**
   *  Constructor for a Thread used to compute the GameOfLife for a
   *  portion of the board.
   *  @param game - the Game of Life board taken by reference
   *  @param readGrid - the grid to read from
   *  @param writeGrid - the grid to put next cell states in 
   *  @param x0 - the starting x coord
   *  @param y0 - the starting y coord
   *  @param x1 - the ending x coord
   *  @param y1 - the ending y coord
   */
  public GridThread(GameOfLife game, byte[][] readGrid,byte[][] writeGrid, int x0,int y0, int x1,int y1) {
    this.game = game;
    this.readGrid  = readGrid;
    this.writeGrid = writeGrid;
    this.x0 = x0;
    this.y0 = y0;
    this.x1 = x1;
    this.y1 = y1;
    //System.out.println("Thread: (" +x0+","+y0+ ") to (" +x1+","+y1+ ")");
  }

  /**
   *  Computes the next tick in the chunk of board designated for the thread instance
   *  @param None
   *  @return None
   */
  private void compute() {
    for (int r = x0; r <= x1; r++) {
      for (int c = y0; c <= y1; c++) {
        this.writeGrid[r][c] = this.game.checkState(r, c);
      }
    }
    this.readGrid = this.writeGrid; // Reference is now
  }

  /**
   *  Override for runnable run() method
   *  @param None
   *  @return None
   */
  @Override
  public void run() {
    compute();
  }
}
