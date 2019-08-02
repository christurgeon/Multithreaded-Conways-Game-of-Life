/**
 *  @author Chris Turgeon
 *  @version 1.0
 */

package code.driver;
import java.util.Map;
import java.util.HashMap;
import javax.swing.BoxLayout;
import java.awt.event.*;
import javax.swing.*;
import java.io.File;
import java.awt.Dimension;


public class Config extends JPanel {

  private JButton fileButton;
  private String outputFile = "out";
  private String outputDirec = ".";
  DirectoryChooser direcChooser;
  private HashMap<String, JTextField> labelFieldMap = new HashMap<String, JTextField>();
  private static final String LABEL = "Output File Pattern ";
  private static final int COLS = 8;

  /**
   *  Constructor for a configuration panel to take in game
   *  settings that persist between application runs.
   *  @param None
   */
  public Config() {
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    String msgPrompt = LABEL;
    JTextField textField = new JTextField(COLS);
    labelFieldMap.put(msgPrompt, textField);

    this.fileButton = new JButton("   Select Output Location");
    this.fileButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        Config.this.direcChooser = new DirectoryChooser();
        Config.this.outputDirec = Config.this.direcChooser.getDirectory();
      }
    });

    JLabel label = new JLabel(msgPrompt);
    label.setAlignmentX(CENTER_ALIGNMENT);
    fileButton.setAlignmentX(CENTER_ALIGNMENT);
    add(label);
    add(textField);
    add(Box.createRigidArea(new Dimension(0, 15)));
    add(fileButton);
    add(Box.createRigidArea(new Dimension(0, 15)));
    }

  /**
   *  Accessor method for user input output file name
   *  @param labelText - the label mapped to the users input field
   *  @return the output file name pattern as a string
   *  @exception IllegalArgumentException is thrown if labelText does not exist
   */
  public String getText(String labelText) {
    JTextField textField = labelFieldMap.get(labelText);
    if (textField != null) {
      return textField.getText();
    } else {
      throw new IllegalArgumentException(labelText);
    }
  }

  /**
   *  Wrapper for accessing output directory
   *  @param None
   *  @return the directory for output file placement
   */
   public String getOutDirectory() {
     return this.outputDirec;
   }
}

// ============================================================================

class DirectoryChooser extends JFileChooser {

  private String outDirectory = ".";

  /**
   *  Constructor for DirectoryChooser object. It constructs
   *  a JFileChooser to get a directory selected by the user.
   *  @param None
   */
  public DirectoryChooser() {
    setDialogTitle("Choose a directory for output files");
    setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    setAcceptAllFileFilterUsed(false);
    if (showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
      File directory = getSelectedFile();
      DirectoryChooser.this.outDirectory = directory.getAbsolutePath();
    }
  }

  /**
   *  Accessor method to get the directory for output files
   *  @param None
   *  @return the directory where output files will be placed
   */
  public String getDirectory() {
    return this.outDirectory;
  }
}
