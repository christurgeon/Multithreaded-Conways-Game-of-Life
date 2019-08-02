/**
 *  @author Chris Turgeon
 *  @version 1.0
 */

package code.driver;
import java.awt.*;
import javax.swing.*;

public class Board extends JPanel {

  private GameOfLife game;
  private GUI gui;
  protected int rows;
  protected int cols;

  /**
   *  Contructor for the Board class
   *  @param gui - a reference to the Game of Life gui
   */
  public Board(GUI gui) {
    this.gui = gui;
  }

  /**
   *  Attach a GameOfLife reference to the board for repainting
   *  @param game - the GameOfLife reference to add to the board
   *  @return None
   */
  public void addGame(GameOfLife game) {
    this.game = game;
    this.rows = game.numRows;
    this.cols = game.numCols;
  }

  /**
   *  Paint the JPanel to show the location of cells
   *  @param g - the graphics object used for painting
   *  @return None
   */
  private void paintBoard(Graphics g) {
    Dimension d = this.getSize();
    int height = (int) d.getHeight();
    int width = (int) d.getWidth();
    if (rows < 3 || cols < 3) {
      return;
    } else if (rows*cols <= height*width) { // Less cells than pixels
      paintSmallerBoard(g, height, width);
    } else { // More cells than pixels
      paintLargerBoard(g, height, width);
    }
  }

  /**
   *  This method calculates the sizes of the cells on the board
   *  when the amount of cells is less than the amount of pixels
   *  @param g - the Graphics object used for painting
   *  @param h - the height of the board
   *  @param w - the width of the board
   *  @return None
   */
  private void paintSmallerBoard(Graphics g, int h, int w) {

    System.out.println("painting smaller board");
    super.paintComponent(g);
    int deltaR = h / rows + 1;
    int deltaC = w / cols + 1;
    int rLoc = 0;
    for (int r = 0; r < rows; r++) {
      int cLoc = 0;
      for (int c = 0; c < cols; c++) {
        if (this.game.isAlive(r, c)) { // Fill the alive cell
          g.setColor(gui.currentColor);
          g.fillRect(cLoc, rLoc, deltaC, deltaR);
        } else { // Fill the dead cell
          g.setColor(Color.WHITE);
          g.fillRect(cLoc, rLoc, deltaC, deltaR);
        }
        cLoc += deltaC;
      }
      rLoc += deltaR;
    }
  }

  /**
   *  This method calculates and packs cells into each pixel for displaying
   *  @param g - the Graphics object for painting
   *  @param h - the height of the board
   *  @param w - the width of the board for painting
   *  @return None
   */
  private void paintLargerBoard(Graphics g, int h, int w) {
    if (gui.frame.getExtendedState() != Frame.MAXIMIZED_BOTH) {
      System.err.println("Waiting for screen to be maximized to print...");
      return;
    }
    super.paintComponent(g);
    int deltaR = rows / h;
    int deltaC = cols / w;
    int remR = rows % h;
    int remC = cols % w;
    boolean extraR = true;

    int x = 0;
    for (int i = 0; i < h; i++) {

      int y = 0;
      for (int j = 0; j < w; j++) {

        if (remR > 0 && remC > 0) {
          extraR = true;
          if (computeRect(x, y, deltaR+1, deltaC+1)) { // Compute the larger subgrid
            g.setColor(gui.currentColor);
            g.fillRect(j, i, 1, 1);
          } else {
            g.setColor(Color.WHITE);
            g.fillRect(j, i, 1, 1);
          }
          y += deltaC + 1;
          remR--;
          remC--;
        }
        else if (remR > 0) {
          extraR = true;
          if (computeRect(x, y, deltaR+1, deltaC)) { // Compute the larger subgrid
            g.setColor(gui.currentColor);
            g.fillRect(j, i, 1, 1);
          } else {
            g.setColor(Color.WHITE);
            g.fillRect(j, i, 1, 1);
          }
          y += deltaC;
          remR--;
        }
        else if (remC > 0) {
          extraR = false;
          if (computeRect(x, y, deltaR, deltaC+1)) { // Compute the larger subgrid
            g.setColor(gui.currentColor);
            g.fillRect(j, i, 1, 1);
          } else {
            g.setColor(Color.WHITE);
            g.fillRect(j, i, 1, 1);
          }
          y += deltaC + 1;
          remC--;
        }
        else {
          extraR = false;
          if (computeRect(x, y, deltaR, deltaC)) { // Compute the larger subgrid
            g.setColor(gui.currentColor);
            g.fillRect(j, i, 1, 1);
          } else {
            g.setColor(Color.WHITE);
            g.fillRect(j, i, 1, 1);
          }
          y += deltaC;
        }
      }
      // Increment the x range value
      if (extraR) {
        x += deltaR + 1;
      } else {
        x += deltaR;
      }
    }
  }

  /**
   *  Loop through the board and see if half or more of the cells in
   *  the mini grid are alive.
   *  @param x - the top row
   *  @param y - the left most column
   *  @param xAdd - the number of rows in the cell
   *  @param yAdd - the number of cols in the cell
   *  @return true if half or more in the bounds are true, false otherwise
   */
  public boolean computeRect(int x,int y, int xAdd,int yAdd) {
    int total = xAdd * yAdd;
    int aliveCount = 0;
    for (int i = x; i < x+xAdd; i++) {
      for (int j = y; j < y+yAdd; j++) {
        if (this.game.isAlive(i, j)) {
          aliveCount++;
        }
      }
    }
    if (aliveCount >= total / 2) { // If half or more are live, return true
      return true;
    }
    return false;
  }

  /**
   *  Override for paintComponent()
   *  @param g - Graphics object for painting
   *  @return None
   */
  @Override
  protected void paintComponent(Graphics g) {
    paintBoard(g);
  }
}
