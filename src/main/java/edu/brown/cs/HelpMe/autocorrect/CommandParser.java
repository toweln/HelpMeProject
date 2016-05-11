package edu.brown.cs.HelpMe.autocorrect;

/**
 * Parses all necessary commands for making suggestions.
 *
 * @author acj
 *
 */
public class CommandParser {

  private String db;
  private boolean led;
  private int ledmaxdist;
  private boolean prefix;
  private boolean whitespace;
  private boolean smart;

  /**
   * Constructor for building command parser from scratch.
   *
   * @param led
   *          boolean for edit dist.
   * @param ledmaxdist
   *          max edit dist.
   * @param prefix
   *          boolean for prefix suggestions.
   * @param whitespace
   *          boolean for whitespace suggestions.
   * @param smart
   *          boolean for smart suggetsoins.
   */
  public CommandParser(boolean led, int ledmaxdist, boolean prefix,
      boolean whitespace, boolean smart) {
    this.led = led;
    this.ledmaxdist = ledmaxdist;
    this.prefix = prefix;
    this.whitespace = whitespace;
    this.smart = smart;
  }

  /**
   * are we calculating led.
   *
   * @return boolean for led
   */
  public boolean getled() {
    return led;
  }

  /**
   * get the max distance we use for led.
   *
   * @return integer representing the max edit distance.
   */
  public int getledmaxdist() {
    return ledmaxdist;
  }

  /**
   * are we calculating prefixes.
   *
   * @return boolean for prefixes
   */
  public boolean getPrefix() {
    return prefix;
  }

  /**
   * are we calculating whitespace.
   *
   * @return boolean for whitespace
   */
  public boolean getWhitespace() {
    return whitespace;
  }

  /**
   * Are we using smart ranking.
   *
   * @return boolean for smart ranking
   */
  public boolean getSmart() {
    return smart;
  }

  /**
   * Get the corpora we are using.
   *
   * @return list of input files.
   */
  public String getDB() {
    return db;
  }

  /**
   * set boolean for prefix suggestions.
   *
   * @param pprefix
   *          boolean for prefix.
   */
  public void setPrefix(boolean pprefix) {
    this.prefix = pprefix;
  }

  /**
   * set the value of led boolean.
   *
   * @param lled
   *          boolean for led suggestions.
   */
  public void setled(boolean lled) {
    this.led = lled;
  }

  /**
   * set the value of ledmaxdist.
   *
   * @param lledmaxdist
   *          int for max value.
   */
  public void setledmaxdist(int lledmaxdist) {
    this.ledmaxdist = lledmaxdist;
  }

  /**
   * set the value of whitespace boolean.
   *
   * @param wwhitespace
   *          boolean for whitespace suggestions.
   */
  public void setWhitespace(boolean wwhitespace) {
    this.whitespace = wwhitespace;
  }

  /**
   * set the value of smart boolean.
   *
   * @param ssmart
   *          boolean for smart suggestions.
   */
  public void setSmart(boolean ssmart) {
    this.smart = ssmart;
  }
}
