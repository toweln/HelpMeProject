//package edu.brown.cs.acj.helpme;
//
//import static org.junit.Assert.assertTrue;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.junit.Test;
//
///**
// * JUnit Tests for AutoFileReader class
// */
//public class CompatibilityTest {
//
//  @Test
//  public void TQCompatTest() {
//    TagDatabase td = new TagDatabase();
//
//    // create new user
//    Map<String, Double> expertise = new HashMap<>();
//    expertise.put("Math", 1.0);
//    // expertise.put("English", 1.0);
//    // expertise.put("Biology", 1.0);
//    expertise.put("English", 0.4);
//    expertise.put("Biology", 0.4);
//    TagRating tru = new TagRating(expertise, td);
//    User u = new User("John", tru);
//
//    // create new question
//    Map<String, Double> questionRating1 = new HashMap<>();
//    questionRating1.put("Math", 0.8);
//    questionRating1.put("English", 0.0);
//    questionRating1.put("Biology", 0.0);
//    TagRating trq1 = new TagRating(questionRating1, td);
//    Question q1 = new Question("test", "testtest", u, trq1);
//
//    // create new question
//    Map<String, Double> questionRating2 = new HashMap<>();
//    questionRating2.put("Math", 0.8);
//    questionRating2.put("English", 0.4);
//    questionRating2.put("Biology", 0.0);
//    TagRating trq2 = new TagRating(questionRating2, td);
//    Question q2 = new Question("test", "testtest", u, trq2);
//
//    // test compatibility
//    TutorCompatibility testCompat = new TutorCompatibility(td);
//    double compat1 = testCompat.tutorQuestionCompatibility(u, q1);
//    double compat2 = testCompat.tutorQuestionCompatibility(u, q2);
//    System.out.println("COMPAT1: " + compat1);
//    System.out.println("COMPAT2: " + compat2);
//    assertTrue(compat1 == 0.87);
//    assertTrue(compat2 == 0.934);
//  }
//
//  @Test
//  public void CPQCompatTest() {
//    TagDatabase td = new TagDatabase();
//
//    // create new user
//    Map<String, Double> expertise = new HashMap<>();
//    expertise.put("Math", 1.0);
//    expertise.put("English", 0.4);
//    expertise.put("Biology", 0.4);
//    TagRating tru = new TagRating(expertise, td);
//    WordCount wc1 = new WordCount("the");
////    WordCount wc1 = new WordCount(
////        "the the the the cow cow cow jumped jumped over");
//    User u = new User("John", tru);
//    u.setWordCount(wc1);
//
//    // create new question
//    Map<String, Double> questionRating1 = new HashMap<>();
//    questionRating1.put("Math", 0.8);
//    questionRating1.put("English", 0.0);
//    questionRating1.put("Biology", 0.0);
//    TagRating trq1 = new TagRating(questionRating1, td);
////    WordCount wc2 = new WordCount(
////        "the the cow jumped jumped goat goat goat goat over over over");
//    Question q1 = new Question("test", "the dog", u, trq1);
//    TutorCompatibility testCompat = new TutorCompatibility(td);
//    double compat1 = testCompat.currentPastQuestionCompatibility(u, q1);
//    assertTrue(compat1 == .707);
////    System.out.println("COMPAT3: " + compat1);
//  }
//}
