package edu.brown.cs.HelpMe.autocorrect;
//package edu.brown.cs.acj.autocorrect;
//
//import java.util.Comparator;
//import java.util.Map;
//
///**
// * Ranks suggestions based on bigrams, unigrams, and alphabetical order.
// *
// * @author acj
// *
// */
//public class RankOutput implements Comparator<String> {
//
//  private Map<String, Integer> unigramCounts;
//  private Map<Bigram, Integer> bigramCounts;
//  private String prevWord;
//
//  /**
//   * Create ranker for bigrams and unigrams.
//   *
//   * @param unigramCounts
//   *          counts of unigrams.
//   * @param bigramCounts
//   *          counts of bigrams.
//   * @param prevWord
//   *          previous word.
//   */
//  public RankOutput(Map<String, Integer> unigramCounts,
//      Map<Bigram, Integer> bigramCounts, String prevWord) {
//    this.unigramCounts = unigramCounts;
//    this.bigramCounts = bigramCounts;
//    this.prevWord = prevWord;
//  }
//
//  @Override
//  /**
//   * compare two strings based on bigram counts.
//   */
//  public int compare(String o1, String o2) {
//    o1 = o1.split("\\s+")[0];
//    o2 = o2.split("\\s+")[0];
//    if (prevWord.equals("")) {
//      compareUniCount(o1, o2);
//    }
//
//    Bigram bigram1 = new Bigram(prevWord, o1);
//    Bigram bigram2 = new Bigram(prevWord, o2);
//    Integer biCount1 = bigramCounts.get(bigram1);
//    Integer biCount2 = bigramCounts.get(bigram2);
//
//    if (biCount1 == null && biCount2 != null) {
//      return 1;
//    } else if (biCount1 != null && biCount2 == null) {
//      return -1;
//    } else if (biCount1 == null && biCount2 == null) {
//      return compareUniCount(o1, o2);
//    } else if (biCount1 > biCount2) {
//      return -1;
//    } else if (biCount1 < biCount2) {
//      return 1;
//    } else if (biCount1.equals(biCount2)) {
//      return compareUniCount(o1, o2);
//    }
//    return o1.compareTo(o2);
//  }
//
//  /**
//   * Compare two unigram counts.
//   *
//   * @param o1
//   *          first word to compare.
//   * @param o2
//   *          second word to compare.
//   * @return 1, 0, or -1 for comparison.
//   */
//  public int compareUniCount(String o1, String o2) {
//    Integer uniCount1 = unigramCounts.get(o1);
//    Integer uniCount2 = unigramCounts.get(o2);
//    if (uniCount1 == null && uniCount2 != null) {
//      return 1;
//    } else if (uniCount1 != null && uniCount2 == null) {
//      return -1;
//    } else if (uniCount1 == null && uniCount2 == null) {
//      return o1.compareTo(o2);
//    } else if (uniCount1 > uniCount2) {
//      return -1;
//    } else if (uniCount1 < uniCount2) {
//      return 1;
//    }
//    return o1.compareTo(o2);
//  }
//
//  /**
//   * get unigram counts.
//   *
//   * @return unigram counts.
//   */
//  public Map<String, Integer> getUnigramCounts() {
//    return unigramCounts;
//  }
//
//  /**
//   * get bigram counts.
//   *
//   * @return bigram counts.
//   */
//  public Map<Bigram, Integer> getBigramCounts() {
//    return bigramCounts;
//  }
//
//  /**
//   * get previous word.
//   *
//   * @return previous word.
//   */
//  public String getPrevWord() {
//    return prevWord;
//  }
// }
