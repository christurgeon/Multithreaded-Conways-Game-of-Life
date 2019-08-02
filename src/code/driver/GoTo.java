/**
 *  @author Chris Turgeon
 *  @version 1.0
 */

package code.driver;
import java.util.Map;
import java.util.HashMap;
import javax.swing.*;

public class GoTo extends JPanel {

  private HashMap<String, JTextField> labelFieldMap = new HashMap<String, JTextField>();
  private static final String message = "What tick do you want to go to?  ";
  private static final int COLS = 4;

  /**
   *  Constructor for an input to go to a certain tick.
   *  @param None
   */
  public GoTo() {
    add(new JLabel(message));
    JTextField textField = new JTextField(COLS);
    labelFieldMap.put(message, textField);
    add(textField);
    setBorder(BorderFactory.createTitledBorder("Enter a tick from 0 to 200"));
  }

  /**
   *  @param labelText - a String which maps to the user input field
   *  @return the integer value that the user entered, -1 if input was invalid
   *          and -2 if no input was entered.
   */
  public int getTick(String labelText) {
    JTextField textField = labelFieldMap.get(labelText);
    if (textField == null)
      return -2;
    try {
      int returnValue = Integer.valueOf(textField.getText());
      if (returnValue < 0 || returnValue > 200) {
        return -1;
      } else {
        return returnValue;
      }
    } catch(Exception e) {
      return -1;
    }
  }
}
