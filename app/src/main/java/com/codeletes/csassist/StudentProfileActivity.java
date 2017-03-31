package com.codeletes.csassist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.codeletes.csassist.classCodes.*;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Class StudentProfileActivity
 * @author Codeletes
 * @version 1.0
 */

public class StudentProfileActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    ArrayList<String> listDataHeader;
    HashMap<String, ArrayList<Criterion>> listDataChild;
    EditText grade;
    EditText feedback;
    ImageButton edit;
    Button saveButton;
    Button revisionButton;
    ArrayList<Criterion> tasks;
    Student student;
    int studentIndex;
    int labNo;
    int secNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        labNo = (int) getIntent().getSerializableExtra("currentLab");
        secNo = (int) getIntent().getSerializableExtra("secNo");
        studentIndex = (int) getIntent().getSerializableExtra("studentIndex");
        student = WelcomeScreenActivity.getCsAssist().getSection(secNo).getStudentList().get(studentIndex);

        expListView = (ExpandableListView) findViewById(R.id.exp_list);
        grade = (EditText) findViewById(R.id.grade_text);
        feedback = (EditText) findViewById(R.id.feedback);
        feedback.setText( student.getAssignmentFeedback(labNo));
        edit = (ImageButton) findViewById(R.id.edit_button);
        saveButton = (Button) findViewById(R.id.saveButton);

        revisionButton = (Button) findViewById((R.id.revisionButton));
        if ((((Lab) student.getAssignment(labNo)).getStatus()) % 3 == 1) {
            revisionButton.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
            revisionButton.setText("incomplete");
            revisionButton.invalidate();
        }
        else if ((((Lab) student.getAssignment(labNo)).getStatus()) % 3 == 2) {
            revisionButton.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
            revisionButton.setText("completed");
            revisionButton.invalidate();
        }
        else {
            revisionButton.getBackground().setColorFilter(Color.parseColor("#D6D7D7"), PorterDuff.Mode.MULTIPLY);
            revisionButton.setText("no attempt");
            revisionButton.invalidate();
        }

        setTitle(student.getName() + " " + student.getSurname());

        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, secNo, labNo, studentIndex);

        grade.setText( (int) student.getAssignmentGrade( labNo) + "");

        grade.setEnabled(false);
        
        // if the edit button is pressed then the grade textfield is enabled and grade is to be entered
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grade.setEnabled(!grade.isEnabled());
                grade.setText("");
                grade.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(grade, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        // if the save button is pressed then the available data is saved on the file
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if the grade entered is 0/20/80/100, then it is saved
                if (Integer.parseInt(grade.getText().toString()) == 0 ||
                        Integer.parseInt(grade.getText().toString()) == 20 ||
                        Integer.parseInt(grade.getText().toString()) == 80 ||
                        Integer.parseInt(grade.getText().toString()) == 100
                        )
                    student.setGrade(labNo, Integer.parseInt(grade.getText().toString()));
                else
                    Toast.makeText(StudentProfileActivity.this, "Lab grade must be 0, 20, 80 or 100",
                            Toast.LENGTH_LONG).show();

                WelcomeScreenActivity.getCsAssist().getSection(secNo).calculateLabAverage(labNo);
                grade.setEnabled(false);
                
                try {
                    for (int i = 0; i < tasks.size() - 1; i++) {
                        tasks.get(i).setStatus(listAdapter.checkBoxList.get(i).isChecked());
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

                student.getAssignment(labNo).setFeedback(feedback.getText().toString());

                WelcomeScreenActivity.getCsAssist().save(getFilesDir());

                Toast.makeText(StudentProfileActivity.this, "Changes are saved",
                        Toast.LENGTH_LONG).show();
            }
        });
        
        // if the revision button is pressed then the status is changed
        revisionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((Lab) student.getAssignment(labNo)).setStatus(
                        (((Lab) student.getAssignment(labNo)).getStatus() + 1) % 3);

                if ((((Lab) student.getAssignment(labNo)).getStatus()) % 3 == 1) {
                    revisionButton.getBackground().setColorFilter(Color.YELLOW, PorterDuff.Mode.MULTIPLY);
                    revisionButton.setText("Incomplete");
                    v.invalidate();
                }
                else if ((((Lab) student.getAssignment(labNo)).getStatus()) % 3 == 2) {
                    revisionButton.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                    revisionButton.setText("Completed");
                    v.invalidate();
                }
                else {
                    revisionButton.getBackground().setColorFilter(Color.parseColor("#D6D7D7"), PorterDuff.Mode.MULTIPLY);
                    revisionButton.setText("No attempt");
                    v.invalidate();
                }
                
                // saving the data on the file
                WelcomeScreenActivity.getCsAssist().save(getFilesDir());

            }
        });

        expListView.setAdapter(listAdapter);
    }
    
    // when the back button is pressed, the user is redirected to StudentListActivity
    @Override
    public void onBackPressed() {
        Intent intent = new Intent( getBaseContext(), StudentListActivity.class);
        intent.putExtra("secNo", secNo);
        intent.putExtra("labNo", labNo);
        startActivity(intent);
        finish();
    }

    // initializing the task list 
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, ArrayList<Criterion>>();

        // Adding child data
        listDataHeader.add("TASK");

        // Adding child data
        tasks = student.getAssignmentCriteria(labNo);
        listDataChild.put(listDataHeader.get(0), tasks);
    }
}