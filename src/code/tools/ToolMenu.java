/**
 *  @author Chris Turgeon
 *  @version 1.0
 */

package code.tools;
import javax.swing.*;
import java.awt.event.*;

public class ToolMenu extends JMenu {

  /**
   *  This constructor builds a Tools menu with a drop down menu that
   *  holds a Help item which, when clicked, displays instructions on
   *  how the application works.
   *  @param None
   */
  public ToolMenu() {
    super("Tools");
    setMnemonic(KeyEvent.VK_A);
    getAccessibleContext().setAccessibleDescription("Tools To Enhance User Experience");
    JMenuItem help = new JMenuItem("Help", KeyEvent.VK_T);
    this.add(help);
    help.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JOptionPane.showMessageDialog(null, "<html><ol>" +
            "<li>The game will not begin until you load in a text file by clicking <i>Game</i>, " +
            "then <i>Load Game</i>, and finally navigating to a text file to load into the game.</li>"  +
            "<li><i>Save Range</i> will allow you to specify a range of ticks and will save all ticks as separate files within that range.</li>" +
            "<li><i>Save All</i> will allow you to save all ticks from the first up to the one that you are currently on.</li>" +
            "<li><i>Configuration</i> will allow you to set the directory for output and the output file pattern.</li>" +
            "<li><i>Go To</i> will allow you to jump to a tick, note that transparency resets</li>" +
            "<li>When \"Back\" is selected the transparency of cells is reset.</li>" +
            "<li>You must click submit for any changes to be made on configuration panel.</li>" +
            "</ol></html>");
      }
    });
  }
}
