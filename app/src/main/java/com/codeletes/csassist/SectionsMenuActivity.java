package com.codeletes.csassist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codeletes.csassist.classCodes.*;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/*
 * SectionsMenuActivity
 * @author Codeletes
 * @version 1.0
 */

public class SectionsMenuActivity extends AppCompatActivity {

    //global variables
    ArrayList<Section> sections;
    ListView sectionList;
    CSAssist csAssist;


    //onCreate method
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("SECTIONS");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sections_menu);
        
        //get the csAssist object
        csAssist = WelcomeScreenActivity.getCsAssist();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        //set sectionlist
        sectionList = (ListView)findViewById(R.id.listView);
        addSection();
        sections = csAssist.getSections();

        //remind the user to add a section if there is no section in the application
        if ( sections.size() == 0)
            Toast.makeText(SectionsMenuActivity.this, "\t\t\t\t\t\t\tWelcome to CSAssist\nPlease start by adding new sections",
                    Toast.LENGTH_LONG).show();
        
        //set the array adapter
        ArrayAdapter<Section> adapter = new MyListAdapter();
        sectionList.setAdapter(adapter);
        
        //add action to items of the listview
        clickAction();
        
        //give action to floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 //pass to NewSectionActivity
                Intent intent = new Intent( getBaseContext(), NewSectionActivity.class);
                startActivity(intent);
            }
        });
    }

    //onBackPressed method
    @Override
    public void onBackPressed() {
         //give an alert message to the user if the user presses the back button
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close the application
                         finishAffinity();
                    }
                }).setNegativeButton("No", null).show();
    }
    
    //adSection method 
    public void addSection() {
         
         //get the sections from the directory 
        InputStream is = null;
        try {
            is = getAssets().open("students.xml");
        } catch (Exception e) {

            e.printStackTrace();
        }
    }
   
    //clickAction method
    public void clickAction() {
        ListView list = (ListView) findViewById(R.id.listView);
        
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                 
                //pass data between activities and pass to AssignmentSelectionActivity
                Intent intent = new Intent( getBaseContext(), AssignmentSelectionActivity.class);
                //pass the selected section to the next activity
                intent.putExtra("secNo", sections.get(position).getSecNo());
                
                startActivity(intent);
            }
        });
    }


     //*******************************************************************************//
     //***********************MyListAdapter inner class*******************************//
     //*******************************************************************************//
    class MyListAdapter extends ArrayAdapter<Section> {

        public MyListAdapter() {
            super(SectionsMenuActivity.this, R.layout.section_item_view, sections);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;

            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.section_item_view, parent, false);
            }
            
            //get the section at the current position
            Section section = sections.get(position);
            TextView sectionText = (TextView) itemView.findViewById(R.id.section);
            
            //set the textview text as the current sections information
            sectionText.setText("  " + section.getCourseID() + "-" + section.getSecNo());
            sectionText.setTextColor(Color.WHITE);

            return itemView;
        }

    }
}
