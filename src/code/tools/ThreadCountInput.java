/**
 *  @author Chris Turgeon
 *  @version 1.0
 */

package code.tools;
import javax.swing.*;
import java.awt.FlowLayout;

 public class ThreadCountInput extends JPanel {

  private JTextField textField;
  private int numThreads = -1;

  /**
   *  Constructor for panel used to get the number of processors
   *  @param currNumThreads - the current number of threads
   */
  public ThreadCountInput(int currNumThreads) {
    setLayout(new FlowLayout());
    this.textField = new JTextField(4);
    this.textField.setText(Integer.toString(currNumThreads));
    add(new JLabel("Enter thread count: "));
    add(this.textField);
  }

  /**
   *  Create a dialog box to take in the desired number of threads
   *  @param r - the number of rows
   *  @param c - the number of columns
   *  @return -1 if invalid user input is entered and 0 if the value is a good one
   */
  public int getUserInput(int r, int c) {
    String[] options = { "Submit", "Cancel" };
    int optionType = JOptionPane.DEFAULT_OPTION;
    int messageType = JOptionPane.PLAIN_MESSAGE;
    int reply = JOptionPane.showOptionDialog(null, this, "Enter number of threads",
                                             optionType, messageType, null, options, options[0]);
    if (reply == 1 || reply == -1) { return -1; }
    String text = textField.getText();
    if (text == null || text.equals("")) {
      JOptionPane.showMessageDialog(null, "We need input to work with");
      return -1;
    }

    // Parse the value that the user gave
    try {
      int cnt = Integer.valueOf(text);
      if (cnt < 1) {
        JOptionPane.showMessageDialog(null, "We cannot have that number of threads");
        return -1;
      } else if (r < 3 || c < 3) {
        JOptionPane.showMessageDialog(null, "Please load in a grid first");
        return -1;
      } else if (cnt > 100) {
        JOptionPane.showMessageDialog(null, "That is an unreasonable number of threads");
        return -1;
      } else if (cnt % 2 == 1) {
        JOptionPane.showMessageDialog(null, "Enter an even number of threads please");
        return -1;
      } else if (r*c < cnt && r < 100 && c < 100) {
        JOptionPane.showMessageDialog(null, "There is no point of having more threads than there are cells");
        return -1;
      }
      this.numThreads = cnt;
      return 0;
    } catch(Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, "Invalid value entered");
      return -1;
    }
  }

  /**
   *  Get the number of threads
   *  @param None
   *  @return the number of threads the user entered, or -1 if no value
   */
  public int numThreads() {
    return this.numThreads;
  }
 }
