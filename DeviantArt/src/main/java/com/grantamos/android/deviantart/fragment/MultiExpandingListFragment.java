package com.grantamos.android.deviantart.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.grantamos.android.deviantart.R;
import com.grantamos.android.deviantart.helpers.BrowseActivityInterface;
import com.grantamos.android.deviantart.adapter.MultiExpandingListAdapter;
import com.grantamos.android.deviantart.helpers.CategoryItem;
import com.grantamos.android.util.KTreeNode;

/**
 * Created by Grant on 11/16/13.
 */
public class MultiExpandingListFragment extends ListFragment {

    static String TAG = "MultiExpandingListFragment";
    KTreeNode<CategoryItem> root;
    BrowseActivityInterface mCallback;
    MultiExpandingListAdapter mListAdapter;

    public static MultiExpandingListFragment newInstance(KTreeNode<CategoryItem> root){

        MultiExpandingListFragment multiExpandingListFragment = new MultiExpandingListFragment();
        Bundle args = new Bundle();
        args.putSerializable("root", root);
        multiExpandingListFragment.setArguments(args);

        return multiExpandingListFragment;
    }

    @Override
    public void setArguments(Bundle args){

        this.root = (KTreeNode<CategoryItem>) args.getSerializable("root");
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        if(savedInstanceState != null)
            this.setArguments(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity){

        super.onAttach(activity);

        try{
            mCallback = (BrowseActivityInterface) activity;
        } catch (Exception e){
            Log.d(TAG, e.toString());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState){

        super.onSaveInstanceState(outState);
        outState.putSerializable("root", root);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){

        ListView listView = (ListView) layoutInflater.inflate(R.layout.list_view_drawer, container, false);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        mListAdapter = new MultiExpandingListAdapter(getActivity(), root);
        setListAdapter(mListAdapter);

        return listView;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id){

        KTreeNode<CategoryItem> clickedItem = (KTreeNode<CategoryItem>) getListAdapter().getItem(position);

        mCallback.onCategorySelected(clickedItem.getValue());
        mListAdapter.setSelectedNode(clickedItem);

        if(clickedItem.getIsMarked()){
            mListAdapter.collapseNode(clickedItem);
            mListAdapter.notifyDataSetChanged();
        }
        else if(clickedItem.childCount() > 0){
            mListAdapter.expandNode(clickedItem);
            mListAdapter.notifyDataSetChanged();
        } else {
            getListView().invalidateViews();
            mCallback.dismissDrawer();
        }
    }
}
