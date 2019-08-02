/**
 *  @author Chris Turgeon
 *  @version 1.0
 */

package code.driver;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;

public class GameOfLife {

  private GridThread[] pool;
  private int numThreads;

  private byte[][] grid;
  private byte[][] nextGrid;
  private byte[][] tempGrid;
  protected byte[][] initialBoard;
  protected int numRows;
  protected int numCols;

  /**
   *  Construct a 2D grid with a number of rows and columns
   *  @param numRows - the number of rows in the grid
   *  @param numCols - the number of columns in the grid
   *  @param numThreads - the number of threads for computation
   */
  public GameOfLife(int numRows, int numCols, int numThreads) {
    this.grid = new byte[numRows][numCols];
    this.initialBoard = new byte[numRows][numCols];
    this.nextGrid = new byte[numRows][numCols];
    this.tempGrid = new byte[numRows][numCols];
    this.numRows = numRows;
    this.numCols = numCols;
    this.numThreads = numThreads;
  }


  /**
   *  Sets the number of threads for computing
   *  @param numThreads - the new number of threads
   *  @return None
   */
  public void setThreadNumber(int numThreads) {
    this.numThreads = numThreads;
  }


  /**
   *  This constructor creates the designated number of threads and assigns them
   *  proper indeces for computing.
   *  @param None
   *  @return None
   */
  public void configureThreads() {
    this.pool = new GridThread[this.numThreads];
    int rRem = 0;
    int cRem = 0;

    // Check to see if a chunk of processors should have a larger row block
    int rThreads = this.numThreads / 2;
    int rRangeMod = this.numRows % rThreads;
    int rRange = this.numRows / rThreads;
    if (rRangeMod != 0) {
      rRem = this.numRows % rRange;
    }

    // Check to see if a chunk of processors should have a larger col block
    int cRange = this.numCols / 2;
    if (this.numCols % 2 != 0) {
      cRem = 1;
    }

    // Create the threads array and add the proper coordinates for each
    int r = 0;
    int cnt = 0;
    for (int i = 0; i < rThreads-1; i++, r+=rRange) {
      // Create the left and the right thread block for the current row
      this.pool[cnt++] = new GridThread(this, this.grid,this.nextGrid, r,0, r+rRange-1,cRange-1);
      this.pool[cnt++] = new GridThread(this, this.grid,this.nextGrid, r,cRange, r+rRange-1,cRange*2+cRem-1);
    }
    // Create the bottom left and the bottom right threads
    this.pool[cnt++] = new GridThread(this, this.grid,this.nextGrid, r,0, r+rRange-1,cRange-1);
    this.pool[cnt++] = new GridThread(this, this.grid,this.nextGrid, r,cRange, r+rRange-1,cRange*2+cRem-1);
  }


  /**
   *  This method loads in a new grid and replaces the current
   *  one with the new one. It exits the program if an invalid
   *  grid is supplied. Only call when sure that newGrid has same
   *  dimensions as GameOfLife's current grid.
   *  @param newGrid - the new 2D grid of ints replacing the existing one
   *  @return None
   */
  public void replaceGrid(byte[][] newGrid) {
    try {
      for (int r = 0; r < numRows; r++) {
        for (int c = 0; c < numCols; c++) {
          this.grid[r][c] = newGrid[r][c];
          this.initialBoard[r][c] = newGrid[r][c];
        }
      }
    } catch(Exception e) {
      System.err.format("Likely invalid grid dimensions");
      System.exit(1);
    }
  }


  /**
   *  This method returns the GameOfLife grid as 1s and 0s.
   *  It makes a copy to avoid representation exposure.
   *  @param None
   *  @return a 2D array of ints
   */
  public byte[][] getGrid() {
    byte[][] retGrid = new byte[numRows][numCols];
    for (int r = 0; r < numRows; r++) {
      for (int c = 0; c < numCols; c++) {
        retGrid[r][c] = this.grid[r][c];
      }
    }
    return retGrid;
  }


  /**
   *  This method takes in a Scanner object which then reads in the
   *  states of the Cells in the game from a text file. It adds the state
   *  of the cell to the game grid.
   *  @param sc - a Scanner object reading from a text file
   */
  public void populate(Scanner sc) {
    for (int r = 0; r < numRows; r++) {
      for (int c = 0; c < numCols; c++) {
        char digit = sc.next().charAt(0);
        if (digit == '0')
          grid[r][c] = 0;
        else if (digit == '1')
          grid[r][c] = 1;
      }
    }
  }


  /**
   *  This method takes in the position of a Cell
   *  and computes the number of neighbors only if
   *  that Cell is a corner Cell. It returns -1 if
   *  that Cell is not a corner Cell.
   *  @param r - the row number in the grid
   *  @param c - the column number in the grid
   *  @return the number of alive neighbors of a corner Cell
   */
  private int calculateCorner(int r, int c) {
    int topBound = r;
    int bottomBound = r;
    int leftBound = c;
    int rightBound = c;
    int n = numRows-1;
    int m = numCols-1;
    int numNeighbors = 0;

    if (r == 0 && c == 0) {        // top left corner
      bottomBound++; rightBound++;
      numNeighbors += grid[n][m];
      numNeighbors += grid[0][m] + grid[1][m];
      numNeighbors += grid[n][0] + grid[n][1];
    } else if (r == 0 && c == m) { // top right corner
      bottomBound++; leftBound--;
      numNeighbors += grid[n][0];
      numNeighbors += grid[0][0] + grid[1][0];
      numNeighbors += grid[n][m] + grid[n][m-1];
    } else if (r == n && c == 0) { // bottom left corner
      topBound--; rightBound++;
      numNeighbors += grid[0][m];
      numNeighbors += grid[0][0] + grid[0][1];
      numNeighbors += grid[n][m] + grid[n-1][m];
    } else if (r == n && c == m) { // bottom right corner
      topBound--; leftBound--;
      numNeighbors += grid[0][0];
      numNeighbors += grid[0][m] + grid[0][m-1];
      numNeighbors += grid[n][0] + grid[n-1][0];
    } else {
      return -1; // Not a corner
    }

    // Add up not wrapped Cell states
    for (int i = topBound; i <= bottomBound; i++) {
      for (int j = leftBound; j <= rightBound; j++) {
        if ( !(i == r && j == c) ) {
          numNeighbors += grid[i][j];
        }
      }
    }
    return numNeighbors;
  }


  /**
   *  This method takes in a location in the grid and returns
   *  the state of that Cell in the next tick of the game.
   *  @param r - the row location
   *  @param c - the column location
   *  @return the new state of the Cell after the tick
   */
  public byte checkState(int r, int c) {
    Boolean isAlive = (this.grid[r][c] == 1);
    int numNeighbors = getNumberOfNeighbors(r, c);

    // Apply rules of the game to determine state of the cell
    if (isAlive && (numNeighbors < 2 || numNeighbors > 3)) {
      return 0;
    } else if (isAlive && (numNeighbors == 2 || numNeighbors == 3)) {
      return 1;
    } else if (!isAlive && numNeighbors == 3) {
      return 1;
    } else {
      return this.grid[r][c];
    }
  }


  /**
   *  This method takes in the location of a Cell and it
   *  returns the number of neighbors which that Cell has.
   *  @param r - the row location
   *  @param c - the column location
   *  @return the number of live neighbors of the Cell at (r, c)
   */
  private int getNumberOfNeighbors(int r, int c) {
    Boolean leftEdge = (c == 0);
    Boolean rightEdge = (c == numCols - 1);
    Boolean topEdge = (r == 0);
    Boolean bottomEdge = (r == numRows - 1);
    int topBound = r-1;
    int bottomBound = r+1;
    int leftBound = c-1;
    int rightBound = c+1;
    int numNeighbors = 0;

    // Check for Cell not on the perimeter
    if (!topEdge && !bottomEdge && !leftEdge && !rightEdge) {
      for (int i = topBound; i <= bottomBound; i++) {
        for (int j = leftBound; j <= rightBound; j++) {
          if ( !(i == r && j == c) ) { // Don't check (r,c) in grid
            numNeighbors += this.grid[i][j];
          }
        }
      }
      return numNeighbors;
    }

    // Return the amount of neighbors for a corner cell
    int cornerResult = calculateCorner(r, c);
    if (cornerResult != -1) {
      return cornerResult;
    }

    // Cell is not on the corner nor in the middle.
    // Access the Cell's that are wrapped and get the proper bounds.
    if (topEdge) {
      topBound = r;
      bottomBound = r+1;
      for (int i = leftBound; i <= rightBound; i++) { // loop across bottom
        numNeighbors += this.grid[numRows-1][i];
      }
    } else if (bottomEdge) {
      topBound = r-1;
      bottomBound = r;
      for (int i = leftBound; i <= rightBound; i++) { // loop across top
        numNeighbors += this.grid[0][i];
      }
    } else if (leftEdge) {
      leftBound = c;
      rightBound = c+1;
      for (int i = topBound; i <= bottomBound; i++) { // loop across right
        numNeighbors += this.grid[i][numCols-1];
      }
    } else { // rightEdge
      leftBound = c-1;
      rightBound = c;
      for (int i = topBound; i <= bottomBound; i++) { // loop across left
        numNeighbors += this.grid[i][0];
      }
    }

    // Get the Cell's neighbors which are not wrapped
    for (int i = topBound; i <= bottomBound; i++) {
      for (int j = leftBound; j <= rightBound; j++) {
        if ( !(i == r && j == c) ) { // Don't check (r,c) in grid
          numNeighbors += this.grid[i][j];
        }
      }
    }
    return numNeighbors;
  }


  /**
   *  This method takes in the number of steps to run the GameOfLife
   *  for. It creates an array with the new states of the Cell's
   *  after the current round of the game. It then updates the grid
   *  with the values and writes it to a text file.
   *  @param stepCount - the number of steps to run the game for
   *  @return None
   */
  public void play(int stepCount) {

    // If multithreaded, handle the threads
    if (this.numThreads > 1) {
      for (int i = 0; i < stepCount; i++) {
        configureThreads();

        // Start all of the threads for computation
        for (int t = 0; t < this.numThreads; t++) {
          this.pool[t].start();
        }
      }
      return;
    }

    // If single threaded
    int numCells = numRows * numCols;
    for (int i = 0; i < stepCount; i++) {

      // Check the state of every cell in the grid
      int j = 0;
      byte[] newGrid = new byte[numCells];
      for (int r = 0; r < numRows; r++) {
        for (int c = 0; c < numCols; c++) {
          newGrid[j] = checkState(r, c);
          j++;
        }
      }

      // Update this grid with new states of the Cells
      int rIndex = 0;
      int cIndex = 0;
      for (int c = 0; c < numCells; c++) {
        if (newGrid[c] == 1) {
          grid[rIndex][cIndex] = 1;
        } else {
          grid[rIndex][cIndex] = 0;
        }
        cIndex++;
        if ((c+1) % numCols == 0) {
          rIndex++;
          cIndex = 0;
        }
      }
    }
  }


  /**
   *  Returns whether the cell at a certain location is alive
   *  @param r - the row to check
   *  @param c - the col to check
   *  @return true if the cell is alive, false otherwise
   */
  public boolean isAlive(int r, int c) {
    return this.grid[r][c] == 1;
  }


  /**
   *  Wait for all of the threads to complete before continuing
   *  @param None
   *  @return None
   */
  public void joinThreads() {
    if (this.numThreads == 1) { return; }
    try {
      for (GridThread t : this.pool) {
        t.join();
      }
      this.grid = this.nextGrid;
      this.nextGrid = this.tempGrid;
    } catch(InterruptedException e) {
      e.printStackTrace();
    }
  }


  /**
   *  This method loops through the grid and outputs the result.
   *  to a text file. It is formatted to look like a grid.
   *  @param outGrid - the grid to be printed
   *  @param tick - the current tick of the game
   *  @param outputFile - the output file to write contents to
   *  @throws IOException - throws IOException if outputting fails
   */
  public void print(byte[][] outGrid, int tick, String outputFile) throws IOException {
    try {
      String tickStr = Integer.toString(tick);
      outputFile = outputFile + tick + ".txt";
      BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

      // Print out the entire grid
      for (int r = 0; r < numRows; r++) {
        for (int c = 0; c < numCols; c++) {
          writer.write( String.format("%d", outGrid[r][c]) );
        }
        writer.write("\n");
      }
      writer.close();
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
