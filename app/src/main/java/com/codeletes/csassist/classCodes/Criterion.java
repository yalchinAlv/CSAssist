package com.codeletes.csassist.classCodes;


import java.io.Serializable;

/**
 * 
 * @author Codeletes G2A
 * @version 1.0
 * The class to create a criterion object
 */ 

public class Criterion implements Serializable
{
     
     // Properties

    private String description; // every criterion has a description
    private boolean status; // every criterion has a status
    
     // Constructor
     public Criterion( String description)
     {    
         this.description = description;
         status = false;
     }
     
     // Methods
     
     public void setDescription( String description) // sets the description of the criterion
     {
         this.description = description;
     }
     
     public void setStatus( boolean status) // sets the status of the criterion
     {
         this.status = status;
     }
     
     public boolean getStatus() // returns true if the student has met the criterion
     {
         return status;
     }
     
     public String getDescription() // returns the description of the criterion
     {
      return description;
     }

    @Override
    public Criterion clone() // clones this criterion
    { 
        Criterion criterion = new Criterion( this.description);
        criterion.setStatus( this.status);

        return criterion;

    }
     
}