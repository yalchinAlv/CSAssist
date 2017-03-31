package com.codeletes.csassist.classCodes;


import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.*;


/**
 * The section object that holds the students
 * @author Codeletes G2A
 * @version 1.0
 */

public class Section implements Serializable
{

     // Properties
     private ArrayList<Student> students;
     private ArrayList<Double> labAverage;
     private int secNo;
     private String courseID;

     // Constructor
     public Section( String courseID, int secNo, ArrayList<Student> studentList)
     {
          this.secNo = secNo;
          this.courseID = courseID;
          this.students = studentList;
          labAverage = new ArrayList<Double>();

     }

     // Methods

    // Calculate the average of an assignment
     public void calculateLabAverage( int labNo)
     {
          double sum;
          sum = 0;
          for ( int i = 0; i < students.size(); i++)
               sum = sum + students.get(i).getAssignmentGrade( labNo);

          labAverage.set( labNo - 1, round(sum / students.size(), 2));
     }

    // Get the average of an assignment
     public double getLabAverage( int labNo)
     {
          return labAverage.get( labNo - 1);
     }

    // Get course ID
     public String getCourseID() {
          return courseID;
     }

    // Get section number
     public int getSecNo() {
          return secNo;
     }

    // Add a new assignment to the section
     public void addNewAssignment( int labNo, ArrayList<Criterion> criteria)
     {
         // Add the assignment for each student
          for ( int i = 0; i < students.size(); i++)
          {
              ArrayList<Criterion> studentCriteria = new ArrayList<>();
              for ( int j = 0; j < criteria.size(); j++)
              {
                  studentCriteria.add( criteria.get(j).clone());
              }
              students.get(i).addNewAssignment( new Lab( labNo, studentCriteria));
          }

          labAverage.add(0.00);
     }

    // Get the number of labs of the section
     public int getLabNumber()
     {
          return labAverage.size();
     }

    // Get the array list of students
    public ArrayList<Student> getStudentList() {
        return students;
    }

    // Add a new student to the section
    public void addStudent( Student student) {
     students.add( student);
    }

    // Get a student by student ID
    public Student getStudent( int studentId) {
        for (Student student : students) {
            if ( student.getID() == studentId)
                return student;
         }
        return null;
    }

    // To string method for section objects
    public String toString()
    {
         String str = "[";

         for( int i = 0; i < students.size(); i++)
         {
              str += students.get(i).getName() + " ";
         }
         return str + "]";
    }

    // Getting the student information from the student array list of the section and creating xml file
    public File exportXML( File file1)
    {
        String fileName = ("sec" + secNo + ".xml");
        FileOutputStream fos = null;
        File file = new File(file1, fileName);

        try {
            fos = new FileOutputStream(file);
            PrintStream out = new PrintStream(fos);
            System.setOut(out);

            System.out.println( "<sec" + secNo + ">");
            // Creates a part for each student
            for( int i = 0; i < students.size(); i++)
            {
                System.out.println( "\t<student>");
                System.out.println("\t\t<name>" + students.get(i).getName() + "</name>");

                System.out.println("\t\t<surname>" + students.get(i).getSurname() + "</surname>");

                System.out.println("\t\t<studentID>" + students.get(i).getID() + "</studentID>");

                System.out.println( "\t\t<assignments>");
                // Creates a part for each lab assignment
                for ( int j = 1; j <= labAverage.size(); j++)
                {
                    if ( j < 10){
                        System.out.println("\t\t\t<lab0" + j + ">");
                        System.out.println("\t\t\t\t<grade>" + students.get(i).getAssignmentGrade(j) + "</grade>");
                        System.out.println("\t\t\t\t<feedback>" + students.get(i).getAssignmentFeedback(j) + "</feedback>" );
                        System.out.println("\t\t\t</lab0" + j + ">");
                    }
                    else {
                        System.out.println("\t\t\t<lab" + j + ">");
                        System.out.println("\t\t\t\t<grade>" + students.get(i).getAssignmentGrade(j) + "</grade>");
                        System.out.println("\t\t\t\t<feedback>" + students.get(i).getAssignmentFeedback(j) + "</feedback>" );
                        System.out.println("\t\t\t</lab" + j + ">");
                    }
                }

                System.out.println( "\t\t</assignments>");
                System.out.println("\t</student>");

                System.out.println();
            }

            System.out.println( "</sec" + secNo + ">");

        }    catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    // Round the number to two decimal
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}