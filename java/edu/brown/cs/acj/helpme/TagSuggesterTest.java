package edu.brown.cs.acj.helpme;

import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

/**
 * JUnit Tests for AutoFileReader class
 */
public class TagSuggesterTest {

	@Test
	// create new question
	public void basicSuggestion() {
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

		// second question
		Map<String, Double> q2mathRating = new HashMap<>();
		q2mathRating.put("Algebra", 0.8);
		q2mathRating.put("Calculus", 0.0);
		Map<String, Double> q2csRating = new HashMap<>();
		q2csRating.put("MATLAB", 0.4);
		q2csRating.put("R", 0.8);
		Map<String, Map<String, Double>> q2overallRating = new HashMap<>();
		q2overallRating.put("Math", q2mathRating);
		q2overallRating.put("Computer Science and Technology", q2csRating);
		TagRating trq2 = new TagRating(q2overallRating, td);
		Question q2 = new Question("test", "the the the the the the the", u,
				trq2, td);
		for (Discipline d : td.getTaxonomy()) {
			if (trq2.getRating().containsKey(d.getTopLevel())) {
				for (Tag t : d.getSubdisciplines()) {
					if (trq2.getRating().get(d.getTopLevel()).containsKey(t)) {
						t.updateWordCount("the the the the the");
					}
				}
			}
		}

		TagSuggester ts = new TagSuggester(td);
		ts.getTagSuggestions(q2);
		List<String> sugg = ts.getTopTagSuggestions(q2);
		// for (String s : sugg) {
		// System.out.println("SUGG: " + s);
		// }
		assertTrue(sugg.contains("MATLAB"));
		assertTrue(sugg.contains("Algebra"));
		assertTrue(sugg.contains("Calculus"));
		assertTrue(sugg.contains("R"));
	}

	@Test
	// create new question
	public void suggestionTest2() {
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

		// second question
		Map<String, Double> q2mathRating = new HashMap<>();
		q2mathRating.put("Algebra", 0.8);
		q2mathRating.put("Calculus", 0.0);
		Map<String, Double> q2csRating = new HashMap<>();
		q2csRating.put("MATLAB", 0.4);
		q2csRating.put("R", 0.8);
		Map<String, Map<String, Double>> q2overallRating = new HashMap<>();
		q2overallRating.put("Math", q2mathRating);
		q2overallRating.put("Computer Science and Technology", q2csRating);
		TagRating trq2 = new TagRating(q2overallRating, td);
		Question q2 = new Question("test", "the the the the the the the", u,
				trq2, td);
		for (Discipline d : td.getTaxonomy()) {
			if (trq2.getRating().containsKey(d.getTopLevel())) {
				for (Tag t : d.getSubdisciplines()) {
					if (trq2.getRating().get(d.getTopLevel()).containsKey(t)) {
						t.updateWordCount("the the the the the dog cat animal");
					}
				}
			}
		}

		TagSuggester ts = new TagSuggester(td);
		ts.getTagSuggestions(q2);
		List<String> sugg = ts.getTopTagSuggestions(q2);
		// for (String s : sugg) {
		// System.out.println("SUGG: " + s);
		// }
		assertTrue(sugg.contains("MATLAB"));
		assertTrue(sugg.contains("Algebra"));
		assertTrue(sugg.contains("Calculus"));
		assertTrue(sugg.contains("R"));
	}

	@Test
	// create new question
	public void suggestionTest3() {
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

		// second question
		Map<String, Double> q2mathRating = new HashMap<>();
		q2mathRating.put("Algebra", 0.8);
		q2mathRating.put("Calculus", 0.0);
		Map<String, Double> q2csRating = new HashMap<>();
		q2csRating.put("MATLAB", 0.4);
		q2csRating.put("R", 0.8);
		Map<String, Map<String, Double>> q2overallRating = new HashMap<>();
		q2overallRating.put("Math", q2mathRating);
		q2overallRating.put("Computer Science and Technology", q2csRating);
		TagRating trq2 = new TagRating(q2overallRating, td);
		Question q2 = new Question("test", "the the the the the the the", u,
				trq2, td);
		for (Discipline d : td.getTaxonomy()) {
			if (trq2.getRating().containsKey(d.getTopLevel())) {
				for (Tag t : d.getSubdisciplines()) {
					if (t.getName().equals("Algebra")) {
						t.updateWordCount("the the the the the the the");
					} else if (t.getName().equals("MATLAB")) {
						t.updateWordCount("the the the the the dog cat animal");
					} else if (trq2.getRating().get(d.getTopLevel())
							.containsKey(t)) {
						t.updateWordCount("random word");
					}
				}
			}
		}

		TagSuggester ts = new TagSuggester(td);
		ts.getTagSuggestions(q2);
		List<String> sugg = ts.getTopTagSuggestions(q2);
//		for (String s : sugg) {
//			System.out.println("SUGG: " + s);
//		}
		assertTrue(sugg.get(0).equals("Algebra"));
		assertTrue(sugg.get(1).equals("MATLAB"));
	}
	
	@Test
	// create new question
	public void suggestionTest1() {
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

		// second question
		Map<String, Double> q2mathRating = new HashMap<>();
		q2mathRating.put("Algebra", 0.8);
		q2mathRating.put("Calculus", 0.0);
		Map<String, Double> q2csRating = new HashMap<>();
		q2csRating.put("MATLAB", 0.4);
		q2csRating.put("R", 0.8);
		Map<String, Map<String, Double>> q2overallRating = new HashMap<>();
		q2overallRating.put("Math", q2mathRating);
		q2overallRating.put("Computer Science and Technology", q2csRating);
		TagRating trq2 = new TagRating(q2overallRating, td);
		Question q2 = new Question("test", "the the the the the the the", u,
				trq2, td);
		for (Discipline d : td.getTaxonomy()) {
			if (trq2.getRating().containsKey(d.getTopLevel())) {
				for (Tag t : d.getSubdisciplines()) {
					if (trq2.getRating().get(d.getTopLevel()).containsKey(t)) {
						t.updateWordCount("the the the the the");
					}
				}
			}
		}

		TagSuggester ts = new TagSuggester(td);
		ts.getTagSuggestions(q2);
		List<String> sugg = ts.getTopTagSuggestions(q2);
		// for (String s : sugg) {
		// System.out.println("SUGG: " + s);
		// }
		assertTrue(sugg.contains("MATLAB"));
		assertTrue(sugg.contains("Algebra"));
		assertTrue(sugg.contains("Calculus"));
		assertTrue(sugg.contains("R"));
	}

	@Test
	// create new question
	public void suggestionTest4() {
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

		// second question
		Map<String, Double> q2mathRating = new HashMap<>();
		q2mathRating.put("Algebra", 0.8);
		q2mathRating.put("Calculus", 0.0);
		Map<String, Double> q2csRating = new HashMap<>();
		q2csRating.put("MATLAB", 0.4);
		q2csRating.put("R", 0.8);
		Map<String, Map<String, Double>> q2overallRating = new HashMap<>();
		q2overallRating.put("Math", q2mathRating);
		q2overallRating.put("Computer Science and Technology", q2csRating);
		TagRating trq2 = new TagRating(q2overallRating, td);
		Question q2 = new Question("test", "the the the the the the the", u,
				trq2, td);

		TagSuggester ts = new TagSuggester(td);
		ts.getTagSuggestions(q2);
		List<String> sugg = ts.getTopTagSuggestions(q2);
		// for (String s : sugg) {
		// System.out.println("SUGG: " + s);
		// }
		assertTrue(sugg.isEmpty());
	}
}
