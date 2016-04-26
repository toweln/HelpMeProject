package edu.brown.cs.acj.helpme;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserTaxonomy {

  String name;
  UUID ID;
  List<Question> submittedQuestions;
  List<Question> answeredQuestions;
  TagRatingTaxonomy expertise;
  WordCount pastQuestions;

  public UserTaxonomy(String name, TagRatingTaxonomy expertise) {
    this.name = name;
    this.ID = UUID.randomUUID();
    this.submittedQuestions = new ArrayList<>();
    this.answeredQuestions = new ArrayList<>();
    this.expertise = expertise;
  }

  /**
   * get the user's name.
   * @return the user's name.
   */
  public String getName() {
    return name;
  }

  /**
   * get the user's id in the database.
   * @return the user's id.
   */
  public String getID() {
    return ID.toString();
  }

  /**
   * get the list of questions this user has submitted.
   * @return submitted questions.
   */
  public List<Question> getSubmittedQuestions() {
    return submittedQuestions;
  }

  /**
   * get the list of questions this user has answered.
   * @return answered questoins.
   */
  public List<Question> getAnsweredQuestions() {
    return answeredQuestions;
  }

  /**
   * get the TagRating associated with this user's expertise.
   * @return the user's TagRating.
   */
  public TagRatingTaxonomy getExpertise() {
    return expertise;
  }

  public void addSubmittedQuestion(Question q) {
    submittedQuestions.add(q);
  }

  public void addAnsweredQuestion(Question q) {
    answeredQuestions.add(q);
  }

  public WordCount getPastQuestions() {
    return pastQuestions;
  }

  public void setWordCount(WordCount wc) {
    this.pastQuestions = wc;
  }

}
