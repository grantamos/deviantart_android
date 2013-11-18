package com.grantamos.android.deviantart.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.grantamos.android.deviantart.R;
import com.grantamos.android.deviantart.helpers.CategoryItem;
import com.grantamos.android.util.KTreeNode;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by Grant on 11/16/13.
 */
public class MultiExpandingListAdapter extends BaseAdapter {

    KTreeNode<CategoryItem> mRoot, mSelectedNode;
    LinkedList<KTreeNode<CategoryItem>> mItems;
    ListIterator<KTreeNode<CategoryItem>> mIterator;
    Activity mContext;
    int mAccentColor, mNormalColor;

    public MultiExpandingListAdapter(Activity context, KTreeNode<CategoryItem> root){
        mRoot = root;
        mContext = context;

        mItems = new LinkedList<KTreeNode<CategoryItem>>();

        expandNode(root);

        mSelectedNode = mItems.get(0);
        mAccentColor = mContext.getResources().getColor(R.color.accent);
        mNormalColor = mContext.getResources().getColor(R.color.white);
    }

    public void expandNode(KTreeNode<CategoryItem> node){

        int index = mItems.indexOf(node) + 1;
        node.setIsMarked(true);

        for(KTreeNode<CategoryItem> item : node.children){
            mItems.add(index, item);
            index++;
        }

        mIterator = mItems.listIterator();
    }

    public void collapseNode(KTreeNode<CategoryItem> node){

        int index = mItems.indexOf(node) + 1;
        ListIterator listIterator = mItems.listIterator(index);
        node.setIsMarked(false);

        for(int i = 0; i < node.childCount() && listIterator.hasNext(); i++){
            KTreeNode<CategoryItem> n = (KTreeNode<CategoryItem>) listIterator.next();
            if(n.getIsMarked()){
                collapseNode(n);
                listIterator = mItems.listIterator(index + i);
                listIterator.next();
            }
            listIterator.remove();
        }

        mIterator = mItems.listIterator();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        KTreeNode<CategoryItem> ret = null;

        if(i < 0 || i > getCount())
            return ret;

        while(i >= mIterator.nextIndex() && mIterator.hasNext())
            ret = mIterator.next();

        while(i <= mIterator.previousIndex() && mIterator.hasPrevious())
            ret = mIterator.previous();

        return ret;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder;

        KTreeNode<CategoryItem> item = (KTreeNode<CategoryItem>) getItem(position);

        if(convertView == null){
            convertView = mContext.getLayoutInflater().inflate(R.layout.list_item_drawer, viewGroup, false);

            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.text_view);
            viewHolder.padding = convertView.getPaddingLeft();

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(item.getValue().toString());

        if(mSelectedNode == item)
            viewHolder.textView.setTextColor(mAccentColor);
        else
            viewHolder.textView.setTextColor(mNormalColor);

        convertView.setPadding(viewHolder.padding + (item.getDepth() * viewHolder.padding * 2), convertView.getPaddingTop(), convertView.getPaddingRight(), convertView.getPaddingBottom());

        return convertView;
    }

    public void setSelectedNode(KTreeNode<CategoryItem> selectedNode) {
        this.mSelectedNode = selectedNode;
    }

    static class ViewHolder{
        TextView textView;
        int padding;
    }
}
