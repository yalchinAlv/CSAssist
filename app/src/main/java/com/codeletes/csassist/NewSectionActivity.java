package com.codeletes.csassist;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.codeletes.csassist.classCodes.CSAssist;
import com.codeletes.csassist.classCodes.Parser;
import com.codeletes.csassist.classCodes.Student;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * The activity to add a new section to CSAssist
 * @author Codeletes
 * @version 1.0
 */

public class NewSectionActivity extends AppCompatActivity {

    private static final int PICKFILE_RESULT_CODE = 1;

    Button newStudentList;
    Button addButton;
    File section;
    CSAssist csAssist;
    EditText courseCode;
    EditText sectionNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Set the GUI
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_section);

        // Initialize the variables
        csAssist = WelcomeScreenActivity.getCsAssist();
        courseCode = (EditText)findViewById(R.id.nameEdit);
        sectionNumber = (EditText)findViewById(R.id.labNo);
        addButton = (Button)findViewById(R.id.addButton);

        // Add the section to CSAssist
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int secNo = secNo = Integer.parseInt(sectionNumber.getText().toString());
                InputStream sec = null;


                // Try to add the section
                try {

                    // Get the students xml file and create the students array list
                    sec = new FileInputStream( section);
                    ArrayList<Student> students = Parser.getStudentList(sec);

                    // Detect the exceptions
                    if ( csAssist.getSection(secNo) != null)
                        throw new SectionExistsException();

                    if( students.size() == 0)
                        throw new InvalidStudentListException();

                    // Add the section
                    csAssist.addSection(courseCode.getText().toString().toUpperCase(), secNo, students);

                    // Report to user
                    Toast.makeText(NewSectionActivity.this, "New section added to CSAssist",
                            Toast.LENGTH_LONG).show();

                    // Handle exception
                } catch (SectionExistsException e) {
                    Toast.makeText(NewSectionActivity.this, "Section " + secNo + " already exists",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } catch (InvalidStudentListException e) {
                    Toast.makeText(NewSectionActivity.this, "Please choose a valid student list",
                            Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Toast.makeText(NewSectionActivity.this, "Please choose a valid file",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                // Save the csAssist object
                csAssist.save(getFilesDir());

                // Start the section selection activity
                Intent intent = new Intent( getBaseContext(), SectionsMenuActivity.class);
                startActivity(intent);

            }
        });

        // The button that takes the students xml from directory
        newStudentList = (Button)findViewById(R.id.importStudentListButton);
        newStudentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/xml");
                startActivityForResult(intent, PICKFILE_RESULT_CODE);
            }
        });


    }


    // The method that takes the xml from directory
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICKFILE_RESULT_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Uri xmlUri = data.getData();
                String path = xmlUri.getPath();

                section = new File( xmlUri.getPath());
                String buttonName = (section.getAbsolutePath());
                buttonName = buttonName.substring(buttonName.lastIndexOf("/"));
                newStudentList.setText(buttonName);

            }
        }
    }

    // The exception that occurs when the section already exists
    class SectionExistsException extends Exception
    {
        //Parameterless Constructor
        public SectionExistsException() {}

        //Constructor that accepts a message
        public SectionExistsException(String message)
        {
            super(message);
        }
    }

    // The exception that occurs when the students xml is not valid
    class InvalidStudentListException extends Exception
    {
        //Parameterless Constructor
        public InvalidStudentListException() {}

        //Constructor that accepts a message
        public InvalidStudentListException(String message)
        {
            super(message);
        }
    }
}

