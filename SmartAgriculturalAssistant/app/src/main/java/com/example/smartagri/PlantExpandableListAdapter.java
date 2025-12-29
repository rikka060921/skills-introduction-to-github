package com.example.smartagri;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Adapter for Plant ExpandableListView
 * 植物可扩展列表适配器
 */
public class PlantExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> groupList;
    private HashMap<String, List<String>> childMap;
    
    public PlantExpandableListAdapter(Context context, List<String> groupList, 
                                     HashMap<String, List<String>> childMap) {
        this.context = context;
        this.groupList = groupList;
        this.childMap = childMap;
    }
    
    @Override
    public int getGroupCount() {
        return groupList.size();
    }
    
    @Override
    public int getChildrenCount(int groupPosition) {
        String key = groupList.get(groupPosition);
        List<String> children = childMap.get(key);
        return children != null ? children.size() : 0;
    }
    
    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }
    
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        String key = groupList.get(groupPosition);
        List<String> children = childMap.get(key);
        return children != null ? children.get(childPosition) : null;
    }
    
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    
    @Override
    public boolean hasStableIds() {
        return false;
    }
    
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, 
                            View convertView, ViewGroup parent) {
        String groupTitle = (String) getGroup(groupPosition);
        
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_expandable_list_item_1, null);
        }
        
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(groupTitle);
        textView.setTextSize(18);
        textView.setPadding(50, 20, 0, 20);
        
        return convertView;
    }
    
    @Override
    public View getChildView(int groupPosition, int childPosition, 
                            boolean isLastChild, View convertView, ViewGroup parent) {
        String childText = (String) getChild(groupPosition, childPosition);
        
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
        }
        
        TextView textView = convertView.findViewById(android.R.id.text1);
        textView.setText(childText);
        textView.setTextSize(14);
        textView.setPadding(100, 15, 0, 15);
        
        return convertView;
    }
    
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
