/**
 *  @author Chris Turgeon
 *  @version 1.0
 */

package code.driver;
import javax.swing.*;
import java.awt.event.*;
import java.awt.FlowLayout;

public class ToolBar extends JPanel {

  private JButton goToTick;
  private JButton config;

  /**
   *  Create two buttons to jump ticks and edit configurations
   *  @param gui - a reference to the GUI object
   */
  public ToolBar(GUI gui) {
    setLayout(new FlowLayout());
    this.goToTick = new JButton("Go To");
    this.config = new JButton("Configuration");

    // Allow a user to jump to any tick that he or she desires < 200
    goToTick.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        GoTo tickSelector = new GoTo();
        String[] options = { "Submit", "Cancel" };
        int optionType = JOptionPane.DEFAULT_OPTION;
        int messageType = JOptionPane.PLAIN_MESSAGE;
        int reply = JOptionPane.showOptionDialog(null, tickSelector, "",
                    optionType, messageType, null, options, options[0]);
        if (reply == -1 || reply == 1) return;
        int tick = tickSelector.getTick("What tick do you want to go to?  ");
        if (tick == -1)
          JOptionPane.showMessageDialog(null, "Invalid input entered: must be int in range [0, 200]");
        else if (tick == -2)
          return;
        else
          gui.processAndLoadTick(tick);
      }
    });

    // Configuration panel set up and event on action
    config.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Config configurationPanel = new Config();
        String[] options = { "Submit", "Cancel" };
        int optionType = JOptionPane.DEFAULT_OPTION;
        int messageType = JOptionPane.PLAIN_MESSAGE;
        int reply = JOptionPane.showOptionDialog(null, configurationPanel, "Set Configurations",
                    optionType, messageType, null, options, options[0]);

        // Return if the 'cancel' or close button is hit
        if (reply == 1 || reply == -1) { return; }
        String directory = configurationPanel.getOutDirectory();
        String filePattern = configurationPanel.getText("Output File Pattern ");
        if (!directory.equals("."))
          gui.outDirectory = directory;

        // Parse the file name input and see if it is valid
        boolean noInput = filePattern.equals("");
        if (!noInput && (filePattern != null) && !(filePattern.matches("^[a-zA-Z]*$"))) {
          JOptionPane.showMessageDialog(null, "Invalid file name entered, use only letters");
        } else if (noInput) {
          gui.outFileName = gui.outFileName;
        } else {
          gui.outFileName = filePattern;
        }
      }
    });
    add(goToTick);
    add(config);
  }
}
