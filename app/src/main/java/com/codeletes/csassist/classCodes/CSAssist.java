package com.codeletes.csassist.classCodes;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.support.annotation.Nullable;
import android.view.Display;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * CSAssist object that has sections
 * @author Codeletes G2A
 * @version 1.0
 */
public class CSAssist implements Serializable{
     
     // properties
     private Section[] sections;
     public static final long serialVersionUID = 1;
     private final int numOfSections = 15;
     
     // constructor
     public CSAssist() {
          sections = new Section[numOfSections];
     }
     
     // methods
     public boolean addSection( String courseID, int secNo, ArrayList<Student> students) {

         // If the section is not null, add the section
          if ( sections[secNo] == null) {
               Section tmp = new Section(courseID, secNo, students);
               sections[secNo] = tmp;
               return true;
          }
          else
               return false;
          
     }

    // Add a new assignment to a section
     public boolean addNewAssignment( int secNo, int labNo, ArrayList<Criterion> criteria) {
          
          if ( sections[secNo] != null ) {
              sections[secNo].addNewAssignment( labNo, criteria);
               return true;
          }
          else
               return false;
     }

    // Save the csAssist object to the phone directory
    public void save( File file1)
    {
        String fileName = "csassist.bin";
        File file = new File(file1, fileName);
        try {
            ObjectOutputStream os = new ObjectOutputStream ( new FileOutputStream(file));
            os.writeObject(this);
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        startCSAssist( file1);
    }

    // Get back the csAssist object in the beginning of application
    public static CSAssist startCSAssist( File file1) {

        // Initialize a new csAssist object if the file does not exist
          CSAssist csassist = new CSAssist();

        // Try to find the file from directory and reload the object
          try {
               String fileName = "csassist.bin";
               File file = new File(file1, fileName);
               ObjectInputStream is = new ObjectInputStream( new FileInputStream(file));
               csassist = (CSAssist) is.readObject();
          } catch (FileNotFoundException e) {
               e.printStackTrace();
          } catch (IOException e) {
               e.printStackTrace();
          } catch (ClassNotFoundException e) {
               e.printStackTrace();
          }
          
          return csassist;
     }
     
     
     // Getter Methods

    // Get the number of sections
     public int getNumOfSections() {
          return sections.length;
     }

    // Get the sections arraylist
     public ArrayList<Section> getSections() {
          
          ArrayList<Section> sectionsInAList = new ArrayList<>();
          for (int i = 0; i < sections.length; i++) {
               
               if ( sections[i] != null) 
                    sectionsInAList.add( sections[i]);
          }
          return sectionsInAList;
     }

    // Get the section object
     public Section getSection( int secNo) {
          return sections[secNo];
     }
     
     public ArrayList<Student> getStudentList( int secNo) {
          return sections[secNo].getStudentList();
     }

    // Get a student by studentID
     public Student getStudent( int secNo, int studentId) {
          return sections[secNo].getStudent(studentId);
     }

    // Get a lab assignment from a section
     public Lab getLab( int secNo, int studentId, int labNo) {
          return (Lab) getStudent( secNo, studentId).getAssignment(labNo);
     }

    // Delete a section from CSAssist
    public void deleteSection( Section section)
    {
        sections[ section.getSecNo()] = null;
    }
}
