package edu.brown.cs.acj.helpme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * the high-level database of tags.
 * @author andrewjones
 *
 */
public class TagDatabase {

	public static List<Discipline> tagTaxonomy;

	/**
	 * initliaze a database.
	 */
	public TagDatabase() {
		TagDatabase.tagTaxonomy = new ArrayList<>();

		// languages
		Discipline languages = new Discipline(new Tag("Languages"));
		List<Tag> langSubs = new ArrayList<Tag>(Arrays.asList(
				new Tag("American Sign Language"), new Tag("Arabic"),
				new Tag("Cantonese"), new Tag("Dutch"), new Tag("French"),
				new Tag("German"), new Tag("Hebrew"), new Tag("Hindi"),
				new Tag("Italian"), new Tag("Japanese"), new Tag("Korean"),
				new Tag("Latin"), new Tag("Mandarin"), new Tag("Portuguese"),
				new Tag("Russian"), new Tag("Spanish"), new Tag("Swahili")));
		for (Tag l : langSubs) {
			// System.out.println(l.getName());
			languages.addSubdiscipline(l);
		}
		tagTaxonomy.add(languages);

		// math
		Discipline math = new Discipline(new Tag("Math"));
		List<Tag> mathSubs = new ArrayList<Tag>(Arrays.asList(
				new Tag("Algebra"), new Tag("Statistics"),
				new Tag("Probability"), new Tag("Calculus"),
				new Tag("Multivariable Calculus"),
				new Tag("Differential Equations"), new Tag("Discrete Math"),
				new Tag("Geometry"), new Tag("Number Theory"),
				new Tag("Set Theory"), new Tag("Linear Algebra")));
		for (Tag m : mathSubs) {
			math.addSubdiscipline(m);
		}
		tagTaxonomy.add(math);

		// science
		Discipline science = new Discipline(new Tag("Science"));
		List<Tag> scienceSubs = new ArrayList<Tag>(Arrays.asList(
				new Tag("Biology"), new Tag("Microbiology"),
				new Tag("Nutrition"), new Tag("Physiology"),
				new Tag("Plant Biology"), new Tag("Chemistry"),
				new Tag("Inorganic Chemistry"), new Tag("Organic Chemistry"),
				new Tag("Physics"), new Tag("Biochemistry"), new Tag("Geology"),
				new Tag("Materials Science")));
		for (Tag s : scienceSubs) {
			science.addSubdiscipline(s);
		}
		tagTaxonomy.add(science);

		// humanities
		Discipline humanities = new Discipline(new Tag("Humanities"));
		List<Tag> humSubs = new ArrayList<Tag>(Arrays.asList(new Tag("Art"),
				new Tag("Communication"), new Tag("Dance"),
				new Tag("Education"), new Tag("Graphic Design"),
				new Tag("Film and theater"), new Tag("Linguistics"),
				new Tag("Music"), new Tag("Philosophy"), new Tag("Art History"),
				new Tag("English"), new Tag("Ethnic Studies"),
				new Tag("Gender Studies"), new Tag("Music Theory"),
				new Tag("Religious Studies"), new Tag("Writing")));
		for (Tag h : humSubs) {
			humanities.addSubdiscipline(h);
		}
		tagTaxonomy.add(humanities);

		// computer science and technology
		Discipline csAndTech = new Discipline(
				new Tag("Computer Science and Technology"));
		List<Tag> csSubs = new ArrayList<Tag>(Arrays.asList(
				new Tag("Artificial Intelligence"), new Tag("C"), new Tag("C#"),
				new Tag("HTML"), new Tag("Javascript"), new Tag("jQuery"),
				new Tag("Machine Learning"), new Tag("PHP"), new Tag("Python"),
				new Tag("Scala"), new Tag("Visual Basic"), new Tag("Arduino"),
				new Tag("Computer Graphics"), new Tag("C++"), new Tag("CSS"),
				new Tag("Java"), new Tag("Lisp"), new Tag("Perl"),
				new Tag("Ruby"), new Tag("SQL"), new Tag("MATLAB"),
				new Tag("R"), new Tag("STATA"), new Tag("SAS")));
		for (Tag c : csSubs) {
			csAndTech.addSubdiscipline(c);
		}
		tagTaxonomy.add(csAndTech);

		// history
		Discipline history = new Discipline(new Tag("History"));
		List<Tag> historySubs = new ArrayList<Tag>(Arrays.asList(
				new Tag("African History"), new Tag("European History"),
				new Tag("US History"), new Tag("East Asian History"),
				new Tag("Social History"), new Tag("World History")));
		for (Tag h : historySubs) {
			history.addSubdiscipline(h);
		}
		tagTaxonomy.add(history);
	}

	/**
	 * get the taxonomy of tags.
	 * @return list of disciplines which have subdisciplines.
	 */
	public List<Discipline> getTaxonomy() {
		return tagTaxonomy;
	}

}
