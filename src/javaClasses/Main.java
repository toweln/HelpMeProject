package edu.brown.cs.acj.helpme;

import java.io.IOException;

/**
 * High-level control of autocorrect program. Manages system exiting.
 * @author acj
 */
public final class Main {

  /**
   * main method for autocorrect.
   * @param args
   *          args from command line.
   * @throws IOException
   *           if file doens't exist.
   */
  public static void main(String[] args) throws IOException {

    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() throws IOException {
    // AllTags helpMeTags = new AllTags();
  }

}
