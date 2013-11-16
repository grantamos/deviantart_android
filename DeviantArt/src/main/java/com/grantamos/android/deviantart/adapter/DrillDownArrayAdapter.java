package com.grantamos.android.deviantart.adapter;

import android.app.Activity;
import android.content.Context;
import android.opengl.Visibility;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.grantamos.android.deviantart.R;
import com.grantamos.android.deviantart.helpers.CategoryItem;
import com.grantamos.android.util.KTreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Grant on 11/14/13.
 */
public class DrillDownArrayAdapter extends ArrayAdapter<KTreeNode> {
    KTreeNode<CategoryItem> root;
    int accentColor;
    int normalColor;
    KTreeNode<CategoryItem> selectedNode;

    public DrillDownArrayAdapter(Context context, int resource, ArrayList<KTreeNode> objects) {
        super(context, resource, objects);

        accentColor = getContext().getResources().getColor(R.color.accent);
        normalColor = getContext().getResources().getColor(R.color.white);

        if(objects.size() > 0)
            root = objects.get(0).getParent();

        selectedNode = root;
    }

    @Override
    public int getCount() {
        if(super.getCount() == 0)
            return 0;
        return super.getCount() + getItem(0).getDepth();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        position -= root.getDepth() + 1;

        KTreeNode<CategoryItem> item = root.getChild(position);

        if(convertView == null){
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.list_item_drawer, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.text_view);
            viewHolder.padding = convertView.getPaddingLeft();

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(item.getValue().toString());

        if(selectedNode == item)
            viewHolder.textView.setTextColor(accentColor);
        else
            viewHolder.textView.setTextColor(normalColor);

        convertView.setPadding(viewHolder.padding + (item.getDepth() * viewHolder.padding * 2), convertView.getPaddingTop(), convertView.getPaddingRight(), convertView.getPaddingBottom());

        return convertView;
    }

    public void setSelectedNode(KTreeNode<CategoryItem> item) {
        selectedNode = item;
    }

    private static class ViewHolder {
        TextView textView;
        int padding = 0;
    }
}
