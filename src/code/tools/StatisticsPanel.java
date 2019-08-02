/**
 *  @author Chris Turgeon
 *  @version 1.0
 */

package code.tools;
import java.awt.*;
import javax.swing.*;

public class StatisticsPanel extends JPanel {

  private int tick;
  private int numAlive, numDead;
  private int diffNumAlive, diffNumDead;
  private JPanel currPanel, diffPanel;
  private JLabel labelAlive, labelDead;
  private JLabel diffAlive, diffDead;
  private JLabel tickLabel;

  /**
   *  The constructor for StatisticsPanel takes in the number of
   *  alive and dead cells to start the game. It constructs two
   *  JPanel objects to hold the num alive/dead stats for the
   *  current tick and the difference between the previous. It also
   *  creates a JLabel to hold the tick count.
   *  @param numAlive - the number of alive cells
   *  @param numDead - the number of dead cells
   */
  public StatisticsPanel(int numAlive, int numDead) {
    this.tick = 0;
    this.numAlive = numAlive;
    this.numDead = numDead;
    this.diffNumAlive = 0;
    this.diffNumDead = 0;
    this.tickLabel = new JLabel();
    this.currPanel = new JPanel();
    this.diffPanel = new JPanel();
    currPanel.setLayout(new BoxLayout(currPanel, BoxLayout.Y_AXIS));
    diffPanel.setLayout(new BoxLayout(diffPanel, BoxLayout.Y_AXIS));
    this.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 7));

    this.labelAlive = new JLabel("ALIVE:  0");
    this.labelDead = new JLabel("DEAD:  0");
    this.currPanel.add(labelAlive);
    this.currPanel.add(labelDead);

    this.diffAlive = new JLabel("ALIVE COMPARED TO LAST TICK:  0");
    this.diffDead = new JLabel("DEAD COMPARED TO LAST TICK:  0");
    this.diffPanel.add(diffAlive);
    this.diffPanel.add(diffDead);
    display();
  }

  /**
   *  Accessor method for the current tick of the game.
   *  @return an int that represents current tick of the game
   */
  public int getTick() {
    return this.tick;
  }

  /**
   *  This method takes in the number of alive and dead cells
   *  in the next round and updates the game statistics.
   *  @param newNumAlive - the number of alive cells in new round
   *  @param newNumDead - the number of dead cells in the new round
   *  @param newTick - the next tick in the game / next tick selected by user
   *  @return None
   */
  public void update(int newNumAlive, int newNumDead, int newTick) {
    diffNumAlive = newNumAlive - numAlive;
    diffNumDead = newNumDead - numDead;
    numAlive = newNumAlive;
    numDead = newNumDead;
    tick = newTick;
    display();
  }

  /**
   *  This method increments or decrements the current tick.
   *  @param increaseTick - a boolean value, if true increment, else decrement
   *  @return None
   */
  public void updateTick(boolean increaseTick) {
    if (increaseTick)
      tick++;
    else
      tick--;
  }

  /**
   *  This method displays the current tick, the amount of cells that
   *  are alive and dead in the current tick, as well as the difference
   *  in the amount of cells that are dead and alive compared to the
   *  previous tick.
   *  @param None
   *  @return None
   */
  private void display() {

    // Display the current tick of the game
    this.tickLabel.setText( String.format("TICK:  %d", tick) );

    // Display the stats from the current tick
    labelAlive.setText( String.format("ALIVE:  %d", numAlive) );
    labelDead.setText( String.format("DEAD:  %d", numDead) );

    // Display the comparitive stats between current and previous ticks
    // Build the text which states the differenc in the amount alive/dead
    StringBuilder aliveBuilder = new StringBuilder("ALIVE COMPARED TO LAST TICK:  ");
    if (diffNumAlive > 0) aliveBuilder.append("+");
    if (diffNumAlive < 0) aliveBuilder.append("-");
    aliveBuilder.append(Integer.toString(Math.abs(diffNumAlive)));
    StringBuilder deadBuilder = new StringBuilder("DEAD COMPARED TO LAST TICK:  ");
    if (diffNumDead > 0) deadBuilder.append("+");
    if (diffNumDead < 0) deadBuilder.append("-");
    deadBuilder.append(Integer.toString(Math.abs(diffNumDead)));

    // Add text to the JLabels
    diffAlive.setText(aliveBuilder.toString());
    diffDead.setText(deadBuilder.toString());

    // Add to StatisticsPanel object
    this.add(tickLabel);
    this.add(currPanel);
    this.add(diffPanel);
  }
}
