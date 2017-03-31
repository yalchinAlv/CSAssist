package com.codeletes.csassist;
/*
 * 
 */
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.codeletes.csassist.classCodes.*;
import java.util.ArrayList;

/*
 * AssignmentSelectionActivity
 * @author Codeletes
 * @version 1.0
 */
public class AssignmentSelectionActivity extends AppCompatActivity {
     //global variables
     Section selectedSection;
     int secNo;
     int labNumber;
     ListView list;
     ArrayList<String> labs;
     FloatingActionButton addNewAssignment;
     FloatingActionButton sectionDetails;
     
     //methods
     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_assignment_selection);
          
          //get the selected section's number from the previous activity
          secNo = (int) (getIntent().getSerializableExtra("secNo"));
          
          //get the selected section
          selectedSection = WelcomeScreenActivity.getCsAssist().getSection(secNo);
          labNumber = selectedSection.getLabNumber();
          list = (ListView)findViewById(R.id.assignmentList);
          labs = new ArrayList<>();
          setTitle( WelcomeScreenActivity.getCsAssist().getSection(secNo).getCourseID() + "-" + secNo);
          setLabs();
          
          //give action to floating action button
          addNewAssignment = (FloatingActionButton) findViewById(R.id.fab);
          addNewAssignment.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    //pass data between activities
                    Intent intent = new Intent( getBaseContext(), NewAssignmentActivity.class);
                    intent.putExtra("secNo", secNo);
                    
                    startActivity(intent);
               }
          });
          
          //give action to floating action button
          sectionDetails = (FloatingActionButton) findViewById(R.id.sectionDetails);
          sectionDetails.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    //pas data between activities
                    Intent intent = new Intent(getBaseContext(), SectionDetailsActivity.class);
                    intent.putExtra( "Section", selectedSection);
                  
                    startActivity(intent);
               }
          });
          
          //set mylistadapter
          ArrayAdapter<String> adapter = new MyListAdapter();
          list.setAdapter(adapter);
          
          clickAction();
     }
     
     //onBackPressed method
     @Override
     public void onBackPressed() {
          //get back to the previous activity
          Intent intent = new Intent( getBaseContext(), SectionsMenuActivity.class);
          startActivity(intent);
          finish();
     }
     
     //setLabs method to initialize the labs arraylist
     public void setLabs()
     {
          String str = "";
          
          for( int i = 1; i < labNumber + 1; i++)
          {
               if( i >= 9)labs.add("Lab" + (i));
               else        labs.add("Lab0" + (i));
          }
     }
     
     //clickAction method to give action to the items of the list view
     public void clickAction()
     {
          
          ListView list = (ListView) findViewById(R.id.assignmentList);
          list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
               @Override
               public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                    int labNo = position + 1;
                    
                    //pass data between activities
                    Intent intent = new Intent( getBaseContext(), StudentListActivity.class);
                    intent.putExtra("secNo", secNo);
                    intent.putExtra("labNo", labNo);
                    
                    //pass to the StudentListActivity 
                    startActivity(intent);
               }
          });
     }
     
     //*******************************************************************************//
     //***********************MyListAdapter inner class*******************************//
     //*******************************************************************************//
     class MyListAdapter extends ArrayAdapter<String>
     {
          
          public MyListAdapter()
          {
               super(AssignmentSelectionActivity.this, R.layout.lab_item_view, labs);
          }
          
          @Override
          public View getView(int position, View convertView, ViewGroup parent)
          {
               
               View itemView = convertView;
               if( itemView == null)
               {
                    itemView = getLayoutInflater().inflate( R.layout.lab_item_view, parent, false);
               }
               
               String s = labs.get( position);
               
               //set the textview text as the current lab number
               TextView sectionText = ( TextView)itemView.findViewById( R.id.lab);
               sectionText.setText( "  " + labs.get(position));
               
               sectionText.setTextColor(Color.WHITE);
               
               return itemView;
          }
          
          
          
     }
}
