/**
 *  @author Chris Turgeon
 *  @version 1.0
 */

package code.tools;
import javax.swing.*;
import java.awt.GridLayout;

public class RandomBoardInput extends JPanel {

  private JTextField rowsInput;
  private JTextField colsInput;
  private int numRows = -1;
  private int numCols = -1;

  /**
   *  Constructor for panel used to take in grid dimensions
   *  @param None
   */
  public RandomBoardInput() {
    setLayout(new GridLayout(2, 2));
    this.rowsInput = new JTextField(7);
    this.colsInput = new JTextField(7);
    add(new JLabel("How many rows? "));
    add(this.rowsInput);
    add(new JLabel("How many columns? "));
    add(this.colsInput);
  }

  /**
   *  Create a dialog box to take in user information and process it
   *  @param None
   *  @return return code, 0 if successful, -1 if something went wrong
   */
  public int getUserInput() {
    String[] options = { "Submit", "Cancel" };
    int optionType = JOptionPane.DEFAULT_OPTION;
    int messageType = JOptionPane.PLAIN_MESSAGE;
    int reply = JOptionPane.showOptionDialog(null, this, "Random board dimensions",
                                             optionType, messageType, null, options, options[0]);
    if (reply == 1 || reply == -1) { return -1; }
    String rows = rowsInput.getText();
    String cols = colsInput.getText();
    if (rows == null || cols == null || rows.equals("") || cols.equals("")) {
      JOptionPane.showMessageDialog(null, "We need input to work with");
      return -1;
    }

    // Parse the values that the user gave
    try {
      this.numRows = Integer.valueOf(rows);
      this.numCols = Integer.valueOf(cols);
      if (this.numRows < 3 || this.numCols < 3) {
        JOptionPane.showMessageDialog(null, "Both the row and column count must be larger than 3");
        return -1;
      } else if (this.numRows > 0 && this.numCols > 0) {
        return 0;
      }
      return -1;
    } catch(Exception e) {
      JOptionPane.showMessageDialog(null, "Invalid input entered");
      return -1;
    }
  }

  /**
   *  Return the row count that the user provided
   *  @param None
   *  @return the number of the rows in the new grid
   */
  public int getNumRows() {
    return this.numRows;
  }

  /**
   *  Return the column count that the user provided
   *  @param None
   *  @return the number of columns in the new grid
   */
  public int getNumCols() {
    return this.numCols;
  }
}
