package com.grantamos.android.deviantart.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.grantamos.android.deviantart.AsyncJSONRequest;
import com.grantamos.android.deviantart.fragment.DrillDownListFragment;
import com.grantamos.android.deviantart.fragment.ImageListFragment;
import com.grantamos.android.deviantart.R;
import com.grantamos.android.deviantart.helpers.CategoryItem;
import com.grantamos.android.util.KTreeNode;

public class BrowseActivity extends ActionBarActivity implements BrowseActivityInterface {
    AsyncJSONRequest browseRequest;
    String baseURL = "http://ec2-54-200-105-129.us-west-2.compute.amazonaws.com:1337/v1/media/browse/";
    String category = "";
    String time = "";
    String offset = "";

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    CharSequence mDrawerTitle = "CATEGORIES";
    ImageListFragment mImageListFragment;
    ExpandableListView mDrawerList;
    DrillDownListFragment mDrillDownListFragment;
    CategoryItem selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_browse);

        if (savedInstanceState == null) {
            mImageListFragment = ImageListFragment.newInstance(baseURL);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.image_list_container, mImageListFragment)
                    .commit();

            KTreeNode<CategoryItem> tree = setupNavigationTree();
            selectedCategory = tree.getValue();
            mDrillDownListFragment = DrillDownListFragment.newInstance(tree, R.id.drawer_content);

            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .add(R.id.drawer_content, mDrillDownListFragment)
                    .commit();
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_closed  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(selectedCategory.toString());
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        getSupportActionBar().setTitle(selectedCategory.toString());

        //setupViews();
        //getBrowseData();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        */

        return super.onOptionsItemSelected(item);
    }

    public KTreeNode<CategoryItem> setupNavigationTree() {
        KTreeNode<CategoryItem> root = new KTreeNode<CategoryItem>(new CategoryItem("All Categories", "blah"), null);

        KTreeNode<CategoryItem> child1 = new KTreeNode<CategoryItem>(new CategoryItem("Digital Art", "blah"), root);
        child1.addChild(new KTreeNode<CategoryItem>(new CategoryItem("3-Dimensional Art", "blah"), child1));
        child1.addChild(new KTreeNode<CategoryItem>(new CategoryItem("Animation", "blah"), child1));
        child1.addChild(new KTreeNode<CategoryItem>(new CategoryItem("Drawings & Paintings", "blah"), child1));

        KTreeNode<CategoryItem> child2 = new KTreeNode<CategoryItem>(new CategoryItem("Traditional Art", "blah"), root);
        child2.addChild(new KTreeNode<CategoryItem>(new CategoryItem("Animations", "blah"), child2));
        child2.addChild(new KTreeNode<CategoryItem>(new CategoryItem("Assemblage", "blah"), child2));

        KTreeNode<CategoryItem> child3 = new KTreeNode<CategoryItem>(new CategoryItem("Photography", "blah"), root);
        child3.addChild(new KTreeNode<CategoryItem>(new CategoryItem("Abstract & Surreal", "blah"), child3));
        child3.addChild(new KTreeNode<CategoryItem>(new CategoryItem("Animals, Plants & Nature", "blah"), child3));

        child2.getChild(0).addChild(new KTreeNode<CategoryItem>(new CategoryItem("Colored Animation", "blah"), child2.getChild(0)));
        child2.getChild(0).addChild(new KTreeNode<CategoryItem>(new CategoryItem("Pencil Tests", "blah"), child2.getChild(0)));

        root.addChild(child1);
        root.addChild(child2);
        root.addChild(child3);

        return root;
    }

    public void onCategorySelected(CategoryItem categoryItem){
        selectedCategory = categoryItem;
    }

    public void dismissDrawer(){
        getSupportActionBar().setTitle(selectedCategory.getDisplayText());
        mDrawerLayout.closeDrawers();
    }
}