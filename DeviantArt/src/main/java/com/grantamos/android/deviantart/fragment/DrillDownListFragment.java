package com.grantamos.android.deviantart.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.grantamos.android.deviantart.R;
import com.grantamos.android.deviantart.helpers.BrowseActivityInterface;
import com.grantamos.android.deviantart.adapter.DrillDownArrayAdapter;
import com.grantamos.android.deviantart.helpers.CategoryItem;
import com.grantamos.android.util.KTreeNode;

/**
 * Created by Grant on 11/12/13.
 */
public class DrillDownListFragment extends ListFragment implements View.OnClickListener {
    KTreeNode<CategoryItem> root;
    int mId;
    BrowseActivityInterface mCallback;

    public static DrillDownListFragment newInstance(KTreeNode<CategoryItem> root, int id){
        DrillDownListFragment drillDownListFragmentListFragment = new DrillDownListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("root", root);
        bundle.putInt("mId", id);
        drillDownListFragmentListFragment.setArguments(bundle);

        return drillDownListFragmentListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            setArguments(savedInstanceState);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (BrowseActivityInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement BrowseActivityInterface");
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putSerializable("root", root);
        outState.putInt("mId", mId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.list_view_drill_down, container, false);
        ListView listView = (ListView) layout.findViewById(android.R.id.list);

        listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        setListAdapter(new DrillDownArrayAdapter(getActivity(), R.layout.list_item_drawer, root.children));

        return layout;
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id){
        position -= root.getDepth() + 1;

        KTreeNode<CategoryItem> clickedItem = root.getChild(position);

        mCallback.onCategorySelected(clickedItem.getValue());

        if(clickedItem.childCount() > 0 && clickedItem != root){
            DrillDownListFragment fragment = DrillDownListFragment.newInstance(clickedItem, mId);

            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(mId, fragment)
                    .commit();
        } else {
            ((DrillDownArrayAdapter) getListAdapter()).setSelectedNode(clickedItem);
            getListView().invalidateViews();
            mCallback.dismissDrawer();
        }
    }

    public void setArguments(Bundle arguments){
        root = (KTreeNode<CategoryItem>) arguments.getSerializable("root");
        mId = arguments.getInt("mId");
    }

    @Override
    public void onClick(View view) {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
