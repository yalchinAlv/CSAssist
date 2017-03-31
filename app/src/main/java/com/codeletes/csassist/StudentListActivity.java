package com.codeletes.csassist;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.codeletes.csassist.classCodes.*;

import java.util.ArrayList;

/**
 * Class StudentListActivity
 * @author Codeletes
 * @version 1.0
 */

public class StudentListActivity extends AppCompatActivity {
    
    int secNo;
    ArrayList<Student> students;
    ListView studentList;
    int labNo;
    Section section;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);
        
        secNo = (int) (getIntent().getSerializableExtra("secNo"));
        students = WelcomeScreenActivity.getCsAssist().getStudentList(secNo);
        studentList = (ListView) findViewById(R.id.studentList);
        ArrayAdapter<Student> adapter = new MyListAdapter();
        labNo = (int)getIntent().getSerializableExtra("labNo");
        studentList.setAdapter(adapter);
        setTitle("STUDENTS                         " + WelcomeScreenActivity.getCsAssist().getSection(secNo).getCourseID() + "-" + secNo);
        clickAction();
    }
    
    // when the user taps on a student name
    public void clickAction() {
        ListView list = (ListView) findViewById(R.id.studentList);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                Student clickedStudent = students.get(position);
                
                // direct to Student Profile activity
                Intent intent = new Intent(getBaseContext(), StudentProfileActivity.class);
                intent.putExtra("studentIndex", position);
                intent.putExtra("currentLab", labNo);
                intent.putExtra("secNo", secNo);
                startActivity(intent);
            }
        });
    }

    // when the back button is pressed, the user is redirected to the assignment selection activity
    @Override
    public void onBackPressed() {
        Intent intent = new Intent( getBaseContext(), AssignmentSelectionActivity.class);
        intent.putExtra("secNo", secNo);
        startActivity(intent);
        finish();
    }
    
    // inner class for a list listener
    class MyListAdapter extends ArrayAdapter<Student> {
        
        public MyListAdapter() {
            super(StudentListActivity.this, R.layout.student_item_view, students);
        }
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.student_item_view, parent, false);
            }
            
            Student s = students.get(position);
            System.out.println(((Lab) (s.getAssignment(labNo))).getStatus());
            
            TextView sectionText = (TextView) itemView.findViewById(R.id.student);
            sectionText.setText( "  " + s.getName() + " " + s.getSurname());
            
            // if the status of a student is changed, then the color is changed accordingly
            if( ((Lab) (s.getAssignment( labNo))).getStatus() == 1)
            {
                itemView.setBackgroundColor( Color.YELLOW);
            }
            else if( ((Lab) (s.getAssignment( labNo))).getStatus() == 2)
            {
                itemView.setBackgroundColor(Color.GREEN);
            }
            else
                itemView.setBackgroundColor(Color.parseColor("#00000000"));
            
            
            return itemView;
        }
        
    }
}