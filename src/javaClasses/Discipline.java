package edu.brown.cs.acj.helpme;

import java.util.ArrayList;
import java.util.List;

/**
 * a class for a high-level discipline. each discipline contains several
 * subdisciplines.
 * 
 * @author andrewjones
 *
 */
public class Discipline {

	private Tag topLevel;
	private List<Tag> subdisciplines;

	/**
	 * initialize a discipline.
	 * 
	 * @param top
	 *            the top-level tag.
	 */
	public Discipline(Tag top) {
		this.topLevel = top;
		this.subdisciplines = new ArrayList<>();
	}

	/**
	 * get the tag at the top level.
	 * 
	 * @return the tag.
	 */
	public Tag getTopLevel() {
		return topLevel;
	}

	/**
	 * get the subdisciplnes.
	 * 
	 * @return the subdisciplines.
	 */
	public List<Tag> getSubdisciplines() {
		return subdisciplines;
	}

	/**
	 * add a subdiscpline.
	 * 
	 * @param t
	 *            the new subdiscipline.
	 */
	public void addSubdiscipline(Tag t) {
		subdisciplines.add(t);
	}

}
