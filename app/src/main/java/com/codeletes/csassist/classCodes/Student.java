package com.codeletes.csassist.classCodes;


import java.io.Serializable;
import java.util.*;


/**
 *
 * @author Codeletes G2A
 * @version 1.0
 * The class to create a student object
 */

public class Student implements Serializable
{

     // Properties
     private ArrayList<Assignment> assignments; // every student has assignments
     private String name; // name of the student
     private String surname; // surname of the student
     private int studentID; // every student has an ID number
     private double average; // every student has an average of assignments

     // Constructor
     public Student( String name, String surname, int studentID)
     {
          this.name = name;
          this.surname = surname;
          this.studentID = studentID;
          average = 0;
          assignments = new ArrayList<Assignment>();
     }

     // Methods

     public String getName() // returns the name of the student
     {
         return name;
     }

     public String getSurname() // returns the surname of the student
     {
          return surname;
     }

     public int getID() // returns the ID number of the student
     {
         return studentID;
     }

     public void calculateAverage() // calculates the average of assignments
     {
          for( int i = 0; i < assignments.size(); i++ )
          {
               average = average + assignments.get( i ).getGrade();
          }
          
          average = average / assignments.size();
     }

     public double getAverage() // returns the average of the student
     {
         return average;
     }
     
     public ArrayList<Assignment> getAssignments() // returns the assignments of a student
     {
         // clones the criteria to use a different reference for each student
         return (ArrayList<Assignment>) assignments.clone();
     }
     
     public Assignment getAssignment( int labNo) // returns a particular assignment of a student
     {
         return assignments.get(labNo - 1);
     }

     public double getAssignmentGrade( int labNo) // returns the grade of a particular assignment
     {
          return assignments.get( labNo - 1).getGrade();
     }

     public String getAssignmentFeedback( int labNo) // returns the feedback of a particular assignment
     {
          return assignments.get( labNo - 1).getFeedback();
     }

     public ArrayList<Criterion> getAssignmentCriteria( int labNo) // returns the criteria of a particular assignment
     {
          return ((Lab) assignments.get( labNo - 1)).getCriteria();
     }
     
     public void setGrade( int labNo, int grade) //  sets the grade of a particular assignment
     {
          assignments.get( labNo - 1).setGrade( grade);
     }

     public void addNewAssignment( Assignment a ) // adds a new assignment to assignments
     {
         assignments.add( a );
     }

     public String toString() // returns the String representation of a Student object 
     {
         return name + " " + surname + " " + studentID;
     }
     
     
}