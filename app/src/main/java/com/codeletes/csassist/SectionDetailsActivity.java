package com.codeletes.csassist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codeletes.csassist.classCodes.CSAssist;
import com.codeletes.csassist.classCodes.Section;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * The activity to show the details of the section
 * @author Codeletes
 * @version 1.0
 */

public class SectionDetailsActivity extends AppCompatActivity {

    Section selectedSection;
    ListView labAverageList;
    ArrayList<String> labs;
    FloatingActionButton delete;
    FloatingActionButton extract;
    CSAssist csAssist;
    TextView course;
    TextView sectionNo;
    TextView studentNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Set the GUI
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section_details);

        // Initialize the variables
        selectedSection = (Section)getIntent().getSerializableExtra("Section");
        setTitle( selectedSection.getCourseID() + "-" + selectedSection.getSecNo() + " Details");

        csAssist = WelcomeScreenActivity.getCsAssist();
        course = (TextView)findViewById(R.id.courseText);
        course.setText( "Course ID: " + selectedSection.getCourseID());
        sectionNo = (TextView)findViewById(R.id.sectionText);
        sectionNo.setText( "Section Number: " + selectedSection.getSecNo());
        studentNumber = (TextView)findViewById(R.id.studentNumberText);
        studentNumber.setText( "Student Number: " + selectedSection.getStudentList().size());
        labAverageList = (ListView)findViewById(R.id.labAverageList);
        labs = new ArrayList<>();

        setLabs();

        // Get lab averages from the array
        ArrayAdapter<String> adapter = new MyListAdapter();
        labAverageList.setAdapter(adapter);

        // The button to delete the section
        delete = (FloatingActionButton)findViewById( R.id.deletefab);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Confirm the delete action
                new AlertDialog.Builder(SectionDetailsActivity.this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Delete Section")
                        .setMessage("Are you sure you want to delete this section?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                csAssist.deleteSection(selectedSection);
                                csAssist.save(getFilesDir());
                                Intent i = new Intent(getBaseContext(), SectionsMenuActivity.class);
                                startActivity(i);

                                Toast.makeText(SectionDetailsActivity.this, "Section deleted from CSAssist",
                                        Toast.LENGTH_LONG).show();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();

            }
        });

        // Explain the function of the button to user
        delete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(SectionDetailsActivity.this, "Delete Section", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

        // The button to extract the xml file of the graded section
        extract = (FloatingActionButton)findViewById(R.id.exportfab);
        extract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Try to share the output
                try {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/xml");
                    String shareBody = readFile(selectedSection.exportXML(getFilesDir()));
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "sec" + selectedSection.getSecNo() + "Grades.xml");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Share via"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Explain the function of the button to user
        extract.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(SectionDetailsActivity.this, "Export XML", Toast.LENGTH_SHORT).show();

                return false;
            }
        });
    }


    // Set the strings for each lab
    public void setLabs() {

        String str = "";

        for (int i = 1; i < selectedSection.getLabNumber() + 1; i++) {
            if (i >= 9) labs.add("Lab" + (i) + " Average: " + selectedSection.getLabAverage(i));
            else labs.add("Lab0" + (i) + " Average: " + selectedSection.getLabAverage( i ));
            }
    }

    // Read xml file and return it as a string
    public String readFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString();
        } finally {
            br.close();
        }
    }

    // Set the view of the lab list
    class MyListAdapter extends ArrayAdapter<String>
    {

        public MyListAdapter()
        {
            super(SectionDetailsActivity.this, R.layout.lab_average_item_view, labs);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View itemView = convertView;
            if( itemView == null)
                itemView = getLayoutInflater().inflate( R.layout.lab_average_item_view, parent, false);

            String s = labs.get( position);

            TextView sectionText = ( TextView)itemView.findViewById( R.id.l);
            sectionText.setText( labs.get(position));


            return itemView;
        }
    }
}
