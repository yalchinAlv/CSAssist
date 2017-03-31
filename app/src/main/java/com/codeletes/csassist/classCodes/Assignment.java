package com.codeletes.csassist.classCodes;


import java.io.Serializable;

/**
 *  
 * @author Codeletes G2A
 * @version 1.0
 * The class to create an assignment object
 */ 

public abstract class Assignment implements Serializable
{
     
     // Properties
     private double grade; // each assignment has a grade
     private String feedback; // each assignment has a feedback
     
     // Constructor
     public Assignment()
     {
          feedback = "";
     }
     
     // Methods
     public double getGrade() // returns the grade of the assignment
     {
          return grade;
     }
     
     public String getFeedback() // returns the feedback of the assignment
     {
          return feedback;
     }
     
     public void setGrade( double grade) // sets the grade of the assignment
     {
          this.grade = grade;
     }
     
     public void setFeedback(String s) // sets the feedback of the assignment
     {
          this.feedback = s;
     }
     
}