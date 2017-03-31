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
import com.codeletes.csassist.classCodes.Criterion;
import com.codeletes.csassist.classCodes.Parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * The activity to add a new assignment to the section
 * @author Codeletes
 * @version 1.0
 */


public class NewAssignmentActivity extends AppCompatActivity {

    private static final int PICKFILE_RESULT_CODE = 1;

    int secNo;
    EditText labNo;
    File criteria;
    Button add;
    Button newCriteriaList;
    CSAssist csAssist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Set the GUI of activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_assignment);

        // Initialize the variables
        csAssist = WelcomeScreenActivity.getCsAssist();
        labNo = (EditText) findViewById(R.id.labNo);
        secNo = (int) (getIntent().getSerializableExtra("secNo"));
        add = (Button) findViewById(R.id.addButton);

        // Add the assignment to the section
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputStream crit = null;
                int labNum = Integer.parseInt(labNo.getText().toString());

                // Try to add the assignment
                try {
                    // Take the xml file from directory and create the array list of criteria
                    crit = new FileInputStream( criteria);
                    ArrayList<Criterion> c = Parser.getCriteriaList(crit);

                    // Detect the exceptions
                    if ( labNum > csAssist.getSection(secNo).getLabNumber() + 1)
                        throw new InvalidLabNoException();

                    if ( labNum < csAssist.getSection(secNo).getLabNumber() + 1)
                        throw new LabExistsException();

                    if ( c.size() == 0)
                        throw new InvalidCriteriaException();

                    // Add the assignment
                    csAssist.addNewAssignment(secNo, labNum, c);

                    // Report it to user
                    Toast.makeText(NewAssignmentActivity.this, "New assignment added to Section " + secNo,
                            Toast.LENGTH_LONG).show();

                    // Handle the exceptions
                } catch (InvalidLabNoException e) {
                    Toast.makeText(NewAssignmentActivity.this, "Invalid Lab number\nLab " + (csAssist.getSection(secNo).getLabNumber() + 1) + " is missing",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } catch (LabExistsException e) {
                    Toast.makeText(NewAssignmentActivity.this, "Lab " + labNum + " already exists",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } catch (InvalidCriteriaException e) {
                    Toast.makeText(NewAssignmentActivity.this, "Please choose a valid criteria list",
                            Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Save changes in the csAssist object
                csAssist.save(getFilesDir());

                // Start the assignment selection activity
                Intent intent = new Intent( getBaseContext(), AssignmentSelectionActivity.class);
                intent.putExtra("secNo", secNo);
                startActivity(intent);
            }
        });

        // The button that takes the criteria xml file from the directory
        newCriteriaList = (Button)findViewById(R.id.importCriteriaListButton);
        newCriteriaList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("text/xml");
                startActivityForResult(intent, PICKFILE_RESULT_CODE);
            }
        });

    }

    // The method that takes the criteria file from phone directory
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == PICKFILE_RESULT_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Uri xmlUri = data.getData();
                String path = xmlUri.getPath();

                criteria = new File( xmlUri.getPath());
                String buttonName = (criteria.getAbsolutePath());
                buttonName = buttonName.substring(buttonName.lastIndexOf("/"));
                newCriteriaList.setText(buttonName);

            }
        }
    }

    // The exception occurs if the lab number given by user is greater than expected value
    class InvalidLabNoException extends Exception
    {
        //Parameterless Constructor
        public InvalidLabNoException() {}

        //Constructor that accepts a message
        public InvalidLabNoException(String message)
        {
            super(message);
        }
    }

    // The exception occurs if the lab number given by user is smaller than expected value
    class LabExistsException extends Exception
    {
        //Parameterless Constructor
        public LabExistsException() {}

        //Constructor that accepts a message
        public LabExistsException(String message)
        {
            super(message);
        }
    }

    // The exception occurs if the criteria file chosen by user is not valid
    class InvalidCriteriaException extends Exception
    {
        //Parameterless Constructor
        public InvalidCriteriaException() {}

        //Constructor that accepts a message
        public InvalidCriteriaException(String message)
        {
            super(message);
        }
    }
}
