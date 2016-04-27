package edu.brown.cs.acj.helpme;

import java.util.HashMap;
import java.util.Map;

/**
 * numerical ranking on several categories -- used to rate both questions and
 * users.
 * 
 * @author andrewjones
 *
 */
public class TagRating {

	TagDatabase td;
	private Map<Tag, Map<Tag, Double>> rating;

	/**
	 * create new TagRating.
	 * 
	 * @param vec
	 *            an input tag wtih ratings
	 * @param td
	 *            the TagDatabase.
	 */
	public TagRating(Map<String, Map<String, Double>> vec,
			TagDatabase td) {
		this.rating = new HashMap<>();
		this.td = td;
		for (Discipline d : td.getTaxonomy()) {
			Tag currTop = d.getTopLevel();
			if (vec.containsKey(currTop.getName())) {
				Map<Tag, Double> currSub = new HashMap<>();
				// System.out.println("GOT TOP: " + currTop.getName());
				for (Tag sub : d.getSubdisciplines()) {
					if (vec.get(currTop.getName()).containsKey(sub.getName())) {
						currSub.put(sub,
								vec.get(currTop.getName()).get(sub.getName()));
						// System.out.println("SUB: " + sub.getName() + ", " +
						// vec
						// .get(currTop.getName()).get(sub.getName()));
					}
				}
				this.rating.put(currTop, currSub);
			}
		}
//		for (Tag t : this.rating.keySet()) {
//			System.out.println("TOP: " + t.getName());
//			for (Tag t1 : this.rating.get(t).keySet()) {
//				System.out.println("  SUB: " + t1.getName());
//			}
//		}
	}

	/**
	 * get the TagRating.
	 * 
	 * @return the TagRating.
	 */
	public Map<Tag, Map<Tag, Double>> getRating() {
		return rating;
	}

	public TagDatabase getTagDatabaseTaxonomy() {
		return td;
	}
}
