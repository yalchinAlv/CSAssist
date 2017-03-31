package com.codeletes.csassist;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.codeletes.csassist.classCodes.*;

/**
 * Created by deniz on 22.04.2016.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter
{
    // Initialize the
    CSAssist csAssist = WelcomeScreenActivity.getCsAssist();
    int secNo;
    int labNo;
    int studentIndex;
    private Context _context;
    public ArrayList<CheckBox> checkBoxList;
    private ArrayList<String> _listDataHeader;
    private HashMap<String, ArrayList<Criterion>> _listDataChild;

    public ExpandableListAdapter(Context context, ArrayList<String> listDataHeader,
                                 HashMap<String, ArrayList<Criterion>> listChildData, int secNo, int labNo, int studentIndex) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.secNo = secNo;
        this.labNo = labNo;
        this.studentIndex = studentIndex;
        checkBoxList = new ArrayList<>();
    }

    @Override
    public Object getChild(int groupPosition, int childPositition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPositition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = ((Criterion) getChild(groupPosition, childPosition)).getDescription();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.assignment_list_item, null);
        }

            CheckBox txtListChild = (CheckBox) convertView
                    .findViewById(R.id.checkBox);

            txtListChild.setText(childText);
            txtListChild.setChecked( ((Lab) csAssist.getSection(secNo).getStudentList().get(studentIndex).getAssignment(labNo))
                    .getCriteria().get(childPosition).getStatus());
            txtListChild.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ((Lab) csAssist.getSection(secNo).getStudentList().get(studentIndex).getAssignment(labNo))
                            .getCriteria().get(childPosition).setStatus( isChecked);
                }
            });
            checkBoxList.add(txtListChild);


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.assignment_list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.header);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
