/**
 *  @author Chris Turgeon
 *  @version 1.0
 */

package code.tools;
import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {

  /**
   *  This method passes set up information to main window.
   *  It sets the name, the close operation, the opening
   *  position and the initial size.
   *  @param None
   */
  public MainFrame() {
    this.setTitle("Conway's Game of Life");
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setExtendedState(this.getExtendedState() | this.MAXIMIZED_BOTH);
  }
}
