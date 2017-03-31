package com.codeletes.csassist.classCodes;


import java.io.Serializable;
import java.util.*;
/**
 *
 * @author Codeletes G2A
 * @version 1.0
 * The class create a lab object
 */

public class Lab extends Assignment implements Serializable // lab is an assignment
{

     // Properties

     private int labNo; // every lab assignment has a number
     private int status; // every lab assignment has a status( finished, in revision or no attempt )
     private ArrayList<Criterion> criteria; // every lab assignment has criteria

     // Constructor
     public Lab( int labNo, ArrayList<Criterion> criteria )
     {
          this.criteria = criteria;
          this.labNo = labNo;
     }

     // Methods
     public int getLabNo() // returns the number of the lab assignment
     {
          return labNo;
     }

     public int getStatus() // returns the status of the lab assignment
     {
          return status;
     }

     // 0 = null ( no attempt ), 1 = yellow ( in revision ), 2 = green ( finished )
     public void setStatus( int status )
     {
          this.status = status;
     }

     public void addCriteria( Criterion e) // adds a criterion to the criteria
     {
      criteria.add( e);
     }

     public ArrayList<Criterion> getCriteria() // returns the criteria
     {
          return criteria;
     }


     // TO DO: getInstructions()

}