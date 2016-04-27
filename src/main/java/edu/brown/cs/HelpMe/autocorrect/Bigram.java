package edu.brown.cs.HelpMe.autocorrect;
//package edu.brown.cs.acj.autocorrect;
//
///**
// * Represents a bigram, or two words in succession.
// *
// * @author acj
// *
// */
//public class Bigram {
//  private String word1;
//  private String word2;
//
//  /**
//   * constructor for a Bigram containing two words.
//   *
//   * @param word1
//   *          first word of bigram.
//   * @param word2
//   *          second word of bigram.
//   */
//  public Bigram(String word1, String word2) {
//    this.word1 = word1;
//    this.word2 = word2;
//  }
//
//  @Override
//  public int hashCode() {
//    return (word1.hashCode() + word2.hashCode());
//  }
//
//  @Override
//  public boolean equals(Object o) {
//    if (o instanceof Bigram) {
//      Bigram other = (Bigram) o;
//      return (word1.equals(other.word1) && word2.equals(other.word2));
//    }
//    return false;
//  }
//
//  /**
//   * get first word of bigram.
//   *
//   * @return String of first word
//   */
//  public String getWord1() {
//    return word1;
//  }
//
//  /**
//   * get second word of bigram.
//   *
//   * @return String of second word
//   */
//  public String getWord2() {
//    return word2;
//  }
// }
