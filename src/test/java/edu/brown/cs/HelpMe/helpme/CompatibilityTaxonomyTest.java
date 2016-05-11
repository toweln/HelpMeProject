package edu.brown.cs.HelpMe.helpme;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import edu.brown.cs.HelpMe.main.Question;
import edu.brown.cs.HelpMe.main.TagDatabase;
import edu.brown.cs.HelpMe.main.TagRating;
import edu.brown.cs.HelpMe.main.TutorCompatibility;
import edu.brown.cs.HelpMe.main.User;
import edu.brown.cs.HelpMe.main.WordCount;

/**
 * JUnit Tests for AutoFileReader class
 */
public class CompatibilityTaxonomyTest {

	////////////////////////////////
	// TUTOR QUESTION COMPATIBILITY
	////////////////////////////////
	@Test
	public void basicTQCompatTest() {
		TagDatabase td = new TagDatabase();

		// create new user
		Map<String, Double> mathExpertise = new HashMap<>();
		mathExpertise.put("Algebra", 1.0);
		mathExpertise.put("Calculus", 0.4);
		Map<String, Double> csExpertise = new HashMap<>();
		csExpertise.put("MATLAB", 0.8);
		csExpertise.put("R", 0.2);
		Map<String, Map<String, Double>> overallExpertise = new HashMap<>();
		overallExpertise.put("Math", mathExpertise);
		overallExpertise.put("Computer Science and Technology", csExpertise);

		TagRating tru = new TagRating(overallExpertise, td);
		User u = new User("John", tru);

		// create new question
		Map<String, Double> qmathRating = new HashMap<>();
		qmathRating.put("Algebra", 0.8);
		qmathRating.put("Calculus", 0.0);
		Map<String, Double> qcsRating = new HashMap<>();
		qcsRating.put("MATLAB", 0.4);
		qcsRating.put("R", 0.8);
		Map<String, Map<String, Double>> qoverallRating = new HashMap<>();
		qoverallRating.put("Math", qmathRating);
		qoverallRating.put("Computer Science and Technology", qcsRating);
		TagRating trq = new TagRating(qoverallRating, td);
		Question q = new Question("test", "testtest", u, trq, td);

		// test compatibility
		TutorCompatibility testCompat = new TutorCompatibility(td);
		double compat = testCompat.tutorQuestionCompatibility(u, q);
		assertTrue(compat == 0.786);
	}

	@Test
	public void TQCompatTest2() {
		TagDatabase td = new TagDatabase();

		// create new user
		Map<String, Double> langExpertise = new HashMap<>();
		langExpertise.put("Arabic", 1.0);
		langExpertise.put("Japanese", 1.0);
		langExpertise.put("Cantonese", 1.0);
		Map<String, Double> scienceExpertise = new HashMap<>();
		scienceExpertise.put("Biology", 1.0);
		scienceExpertise.put("Nutrition", 1.0);
		Map<String, Map<String, Double>> overallExpertise = new HashMap<>();
		overallExpertise.put("Languages", langExpertise);
		overallExpertise.put("Science", scienceExpertise);
		TagRating tru = new TagRating(overallExpertise, td);
		User u = new User("John", tru);

		// create new question
		Map<String, Double> qlangExpertise = new HashMap<>();
		qlangExpertise.put("Arabic", 1.0);
		qlangExpertise.put("Japanese", 1.0);
		qlangExpertise.put("Cantonese", 1.0);
		Map<String, Double> qscienceExpertise = new HashMap<>();
		qscienceExpertise.put("Biology", 1.0);
		qscienceExpertise.put("Nutrition", 1.0);
		Map<String, Map<String, Double>> qoverallRating = new HashMap<>();
		qoverallRating.put("Languages", qlangExpertise);
		qoverallRating.put("Science", qscienceExpertise);
		TagRating trq = new TagRating(qoverallRating, td);
		Question q = new Question("test", "testtest", u, trq, td);

		// test compatibility
		TutorCompatibility testCompat = new TutorCompatibility(td);
		double compat = testCompat.tutorQuestionCompatibility(u, q);
		assertTrue(compat == 1.0);
	}

	@Test
	public void TQCompatTest3() {
		TagDatabase td = new TagDatabase();

		// create new user
		Map<String, Double> langExpertise = new HashMap<>();
		langExpertise.put("Arabic", 1.0);
		langExpertise.put("German", 1.0);
		langExpertise.put("Italian", 1.0);
		Map<String, Double> scienceExpertise = new HashMap<>();
		scienceExpertise.put("Biology", 1.0);
		scienceExpertise.put("Nutrition", 1.0);
		Map<String, Map<String, Double>> overallExpertise = new HashMap<>();
		overallExpertise.put("Languages", langExpertise);
		overallExpertise.put("Science", scienceExpertise);
		TagRating tru = new TagRating(overallExpertise, td);
		User u = new User("John", tru);

		// create new question
		Map<String, Double> qlangExpertise = new HashMap<>();
		qlangExpertise.put("Arabic", 1.0);
		qlangExpertise.put("Japanese", 1.0);
		qlangExpertise.put("Cantonese", 1.0);
		Map<String, Double> qscienceExpertise = new HashMap<>();
		qscienceExpertise.put("Biology", 1.0);
		qscienceExpertise.put("Nutrition", 1.0);
		Map<String, Map<String, Double>> qoverallRating = new HashMap<>();
		qoverallRating.put("Languages", qlangExpertise);
		qoverallRating.put("Science", qscienceExpertise);
		TagRating trq = new TagRating(qoverallRating, td);
		Question q = new Question("test", "testtest", u, trq, td);

		// test compatibility
		TutorCompatibility testCompat = new TutorCompatibility(td);
		double compat = testCompat.tutorQuestionCompatibility(u, q);
		assertTrue(compat == 0.6);
	}

	@Test
	public void TQCompatTest4() {
		TagDatabase td = new TagDatabase();

		// create new user
		Map<String, Double> langExpertise = new HashMap<>();
		langExpertise.put("Hindi", 1.0);
		langExpertise.put("German", 1.0);
		langExpertise.put("Italian", 1.0);
		Map<String, Double> scienceExpertise = new HashMap<>();
		scienceExpertise.put("Biology", 1.0);
		scienceExpertise.put("Nutrition", 1.0);
		Map<String, Map<String, Double>> overallExpertise = new HashMap<>();
		overallExpertise.put("Languages", langExpertise);
		overallExpertise.put("Science", scienceExpertise);
		TagRating tru = new TagRating(overallExpertise, td);
		User u = new User("John", tru);

		// create new question
		Map<String, Double> qlangExpertise = new HashMap<>();
		qlangExpertise.put("Arabic", 1.0);
		qlangExpertise.put("Japanese", 1.0);
		qlangExpertise.put("Cantonese", 1.0);
		Map<String, Double> qscienceExpertise = new HashMap<>();
		qscienceExpertise.put("Plant Biology", 1.0);
		qscienceExpertise.put("Physics", 1.0);
		Map<String, Map<String, Double>> qoverallRating = new HashMap<>();
		qoverallRating.put("Languages", qlangExpertise);
		qoverallRating.put("Science", qscienceExpertise);
		TagRating trq = new TagRating(qoverallRating, td);
		Question q = new Question("test", "testtest", u, trq, td);

		// test compatibility
		TutorCompatibility testCompat = new TutorCompatibility(td);
		double compat = testCompat.tutorQuestionCompatibility(u, q);
		assertTrue(compat == 0.0);
	}

	@Test
	public void TQCompatTest5() {
		TagDatabase td = new TagDatabase();

		// create new user
		Map<String, Double> langExpertise = new HashMap<>();
		langExpertise.put("Arabic", 0.6);
		langExpertise.put("German", 1.0);
		langExpertise.put("Italian", 1.0);
		Map<String, Double> scienceExpertise = new HashMap<>();
		scienceExpertise.put("Biology", 0.2);
		scienceExpertise.put("Nutrition", 0.4);
		Map<String, Map<String, Double>> overallExpertise = new HashMap<>();
		overallExpertise.put("Languages", langExpertise);
		overallExpertise.put("Science", scienceExpertise);
		TagRating tru = new TagRating(overallExpertise, td);
		User u = new User("John", tru);

		// create new question
		Map<String, Double> qlangExpertise = new HashMap<>();
		qlangExpertise.put("Arabic", 0.2);
		qlangExpertise.put("Japanese", 1.0);
		qlangExpertise.put("Cantonese", 1.0);
		Map<String, Double> qscienceExpertise = new HashMap<>();
		qscienceExpertise.put("Biology", 0.2);
		qscienceExpertise.put("Nutrition", 0.4);
		Map<String, Map<String, Double>> qoverallRating = new HashMap<>();
		qoverallRating.put("Languages", qlangExpertise);
		qoverallRating.put("Science", qscienceExpertise);
		TagRating trq = new TagRating(qoverallRating, td);
		Question q = new Question("test", "testtest", u, trq, td);

		// test compatibility
		TutorCompatibility testCompat = new TutorCompatibility(td);
		double compat = testCompat.tutorQuestionCompatibility(u, q);
		assertTrue(compat == 0.134);
	}

	//////////////////////////////////////
	// CURRENT-PAST QUESTION COMPATIBILITY
	//////////////////////////////////////
	@Test
	public void CPQCompatTest1() {
		TagDatabase td = new TagDatabase();

		// create new user
		Map<String, Double> mathExpertise = new HashMap<>();
		mathExpertise.put("Algebra", 1.0);
		mathExpertise.put("Calculus", 0.4);
		Map<String, Double> csExpertise = new HashMap<>();
		csExpertise.put("MATLAB", 0.8);
		csExpertise.put("R", 0.2);
		Map<String, Map<String, Double>> overallExpertise = new HashMap<>();
		overallExpertise.put("Math", mathExpertise);
		overallExpertise.put("Computer Science and Technology", csExpertise);

		TagRating tru = new TagRating(overallExpertise, td);
		User u = new User("John", tru);
		WordCount wc1 = new WordCount("the");
		u.setWordCount(wc1);

		// create new question
		Map<String, Double> qmathRating = new HashMap<>();
		qmathRating.put("Algebra", 0.8);
		qmathRating.put("Calculus", 0.0);
		Map<String, Double> qcsRating = new HashMap<>();
		qcsRating.put("MATLAB", 0.4);
		qcsRating.put("R", 0.8);
		Map<String, Map<String, Double>> qoverallRating = new HashMap<>();
		qoverallRating.put("Math", qmathRating);
		qoverallRating.put("Computer Science and Technology", qcsRating);
		TagRating trq1 = new TagRating(qoverallRating, td);
		Question q1 = new Question("test", "the dog", u, trq1, td);
		TutorCompatibility testCompat = new TutorCompatibility(td);
		double compat1 = testCompat.currentPastQuestionCompatibility(u, q1);
		assertTrue(compat1 == .707);
	}

	@Test
	public void CPQCompatTest2() {
		TagDatabase td = new TagDatabase();

		// create new user
		Map<String, Double> mathExpertise = new HashMap<>();
		mathExpertise.put("Algebra", 1.0);
		mathExpertise.put("Calculus", 0.4);
		Map<String, Double> csExpertise = new HashMap<>();
		csExpertise.put("MATLAB", 0.8);
		csExpertise.put("R", 0.2);
		Map<String, Map<String, Double>> overallExpertise = new HashMap<>();
		overallExpertise.put("Math", mathExpertise);
		overallExpertise.put("Computer Science and Technology", csExpertise);

		TagRating tru = new TagRating(overallExpertise, td);
		User u = new User("John", tru);
		WordCount wc1 = new WordCount("blasphemy");
		u.setWordCount(wc1);

		// create new question
		Map<String, Double> qmathRating = new HashMap<>();
		qmathRating.put("Algebra", 0.8);
		qmathRating.put("Calculus", 0.0);
		Map<String, Double> qcsRating = new HashMap<>();
		qcsRating.put("MATLAB", 0.4);
		qcsRating.put("R", 0.8);
		Map<String, Map<String, Double>> qoverallRating = new HashMap<>();
		qoverallRating.put("Math", qmathRating);
		qoverallRating.put("Computer Science and Technology", qcsRating);
		TagRating trq1 = new TagRating(qoverallRating, td);
		Question q1 = new Question("test", "the dog", u, trq1, td);
		TutorCompatibility testCompat = new TutorCompatibility(td);
		double compat1 = testCompat.currentPastQuestionCompatibility(u, q1);
		assertTrue(compat1 == 0.0);
	}

	@Test
	public void CPQCompatTest3() {
		TagDatabase td = new TagDatabase();

		// create new user
		Map<String, Double> mathExpertise = new HashMap<>();
		mathExpertise.put("Algebra", 1.0);
		mathExpertise.put("Calculus", 0.4);
		Map<String, Double> csExpertise = new HashMap<>();
		csExpertise.put("MATLAB", 0.8);
		csExpertise.put("R", 0.2);
		Map<String, Map<String, Double>> overallExpertise = new HashMap<>();
		overallExpertise.put("Math", mathExpertise);
		overallExpertise.put("Computer Science and Technology", csExpertise);

		TagRating tru = new TagRating(overallExpertise, td);
		User u = new User("John", tru);
		WordCount wc1 = new WordCount("my cat");
		u.setWordCount(wc1);

		// create new question
		Map<String, Double> qmathRating = new HashMap<>();
		qmathRating.put("Algebra", 0.8);
		qmathRating.put("Calculus", 0.0);
		Map<String, Double> qcsRating = new HashMap<>();
		qcsRating.put("MATLAB", 0.4);
		qcsRating.put("R", 0.8);
		Map<String, Map<String, Double>> qoverallRating = new HashMap<>();
		qoverallRating.put("Math", qmathRating);
		qoverallRating.put("Computer Science and Technology", qcsRating);
		TagRating trq1 = new TagRating(qoverallRating, td);
		Question q1 = new Question("test", "your cat", u, trq1, td);
		TutorCompatibility testCompat = new TutorCompatibility(td);
		double compat1 = testCompat.currentPastQuestionCompatibility(u, q1);
		assertTrue(compat1 == 0.5);
	}

	@Test
	public void CPQCompatTest4() {
		TagDatabase td = new TagDatabase();

		// create new user
		Map<String, Double> mathExpertise = new HashMap<>();
		mathExpertise.put("Algebra", 1.0);
		mathExpertise.put("Calculus", 0.4);
		Map<String, Double> csExpertise = new HashMap<>();
		csExpertise.put("MATLAB", 0.8);
		csExpertise.put("R", 0.2);
		Map<String, Map<String, Double>> overallExpertise = new HashMap<>();
		overallExpertise.put("Math", mathExpertise);
		overallExpertise.put("Computer Science and Technology", csExpertise);

		TagRating tru = new TagRating(overallExpertise, td);
		User u = new User("John", tru);
		WordCount wc1 = new WordCount("my cat is the bomb");
		u.setWordCount(wc1);

		// create new question
		Map<String, Double> qmathRating = new HashMap<>();
		qmathRating.put("Algebra", 0.8);
		qmathRating.put("Calculus", 0.0);
		Map<String, Double> qcsRating = new HashMap<>();
		qcsRating.put("MATLAB", 0.4);
		qcsRating.put("R", 0.8);
		Map<String, Map<String, Double>> qoverallRating = new HashMap<>();
		qoverallRating.put("Math", qmathRating);
		qoverallRating.put("Computer Science and Technology", qcsRating);
		TagRating trq1 = new TagRating(qoverallRating, td);
		Question q1 = new Question("test", "your cat", u, trq1, td);
		TutorCompatibility testCompat = new TutorCompatibility(td);
		double compat1 = testCompat.currentPastQuestionCompatibility(u, q1);
		assertTrue(compat1 == 0.316);
	}

	/////////////////////////
	// OVERALL COMPATIBILITY
	/////////////////////////
	@Test
	public void overallCompat() {
		TagDatabase td = new TagDatabase();

		// create new user
		Map<String, Double> mathExpertise = new HashMap<>();
		mathExpertise.put("Algebra", 1.0);
		mathExpertise.put("Calculus", 0.4);
		Map<String, Double> csExpertise = new HashMap<>();
		csExpertise.put("MATLAB", 0.8);
		csExpertise.put("R", 0.2);
		Map<String, Map<String, Double>> overallExpertise = new HashMap<>();
		overallExpertise.put("Math", mathExpertise);
		overallExpertise.put("Computer Science and Technology", csExpertise);

		TagRating tru = new TagRating(overallExpertise, td);
		User u = new User("John", tru);
		WordCount wc1 = new WordCount("the");
		u.setWordCount(wc1);

		// create new question
		Map<String, Double> qmathRating = new HashMap<>();
		qmathRating.put("Algebra", 0.8);
		qmathRating.put("Calculus", 0.0);
		Map<String, Double> qcsRating = new HashMap<>();
		qcsRating.put("MATLAB", 0.4);
		qcsRating.put("R", 0.8);
		Map<String, Map<String, Double>> qoverallRating = new HashMap<>();
		qoverallRating.put("Math", qmathRating);
		qoverallRating.put("Computer Science and Technology", qcsRating);
		TagRating trq1 = new TagRating(qoverallRating, td);
		Question q1 = new Question("test", "the dog", u, trq1, td);
		TutorCompatibility testCompat = new TutorCompatibility(td);
		double overallCompat = testCompat.getOverallCompatibility(u, q1);
		assertTrue(overallCompat == 1.493);
	}
}
