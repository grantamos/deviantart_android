package com.grantamos.android.deviantart.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.grantamos.android.deviantart.AsyncJSONRequest;
import com.grantamos.android.deviantart.ImageData;
import com.grantamos.android.deviantart.fragment.ImageListFragment;
import com.grantamos.android.deviantart.R;
import com.grantamos.android.deviantart.fragment.MultiExpandingListFragment;
import com.grantamos.android.deviantart.helpers.CategoryItem;
import com.grantamos.android.util.ImageCache;
import com.grantamos.android.util.KTreeNode;

public class BrowseActivity extends ActionBarActivity implements BrowseActivityInterface {
    AsyncJSONRequest browseRequest;
    String baseURL = "http://ec2-54-200-105-129.us-west-2.compute.amazonaws.com:1337/v1/media/browse/";
    String category = "";
    String time = "8HRS";
    int offset = 0, length = 0;

    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    CharSequence mDrawerTitle = "CATEGORIES";
    ImageListFragment mImageListFragment;
    MultiExpandingListFragment mMultiExpandingListFragment;
    CategoryItem selectedCategory;
    boolean shouldRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageCache.init();

        setContentView(R.layout.activity_browse);

        if (savedInstanceState == null) {
            mImageListFragment = ImageListFragment.newInstance(baseURL, category, time, offset, length);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.image_list_container, mImageListFragment)
                    .commit();

            KTreeNode<CategoryItem> tree = setupNavigationTree();
            selectedCategory = tree.getValue();
            mMultiExpandingListFragment = MultiExpandingListFragment.newInstance(tree);

            getSupportFragmentManager().beginTransaction()
                    //.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .add(R.id.drawer_content, mMultiExpandingListFragment)
                    .commit();
        } else {
            selectedCategory = (CategoryItem) savedInstanceState.getSerializable("selectedCategory");
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

                if(shouldRefresh)
                    mImageListFragment.refreshData();

                shouldRefresh = false;
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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){

        super.onSaveInstanceState(outState);
        outState.putSerializable("selectedCategory", selectedCategory);
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
        getMenuInflater().inflate(R.menu.menu_browse, menu);

        MenuItem item = menu.findItem(R.id.action_time);
        Spinner s = (Spinner) MenuItemCompat.getActionView(item);

        SpinnerAdapter mSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.time,
                R.layout.support_simple_spinner_dropdown_item);

        s.setAdapter(mSpinnerAdapter);

        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String newTime = adapterView.getItemAtPosition(i).toString();
                if(newTime.compareTo(time) != 0){
                    time = newTime;
                    mImageListFragment.setTime(time);
                    mImageListFragment.refreshData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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

        KTreeNode<CategoryItem> root1 = new KTreeNode<CategoryItem>(new CategoryItem("All Categories", ""), root);

        new KTreeNode<CategoryItem>(new CategoryItem("Digital Art", "digitalart"), root);
        new KTreeNode<CategoryItem>(new CategoryItem("Traditional Art", "traditional"), root);
        new KTreeNode<CategoryItem>(new CategoryItem("Photography", "photography"), root);
        new KTreeNode<CategoryItem>(new CategoryItem("Artisan Crafts", "artisan"), root);
        new KTreeNode<CategoryItem>(new CategoryItem("Literature", "literature"), root);
        new KTreeNode<CategoryItem>(new CategoryItem("Film & Animation", "file"), root);
        new KTreeNode<CategoryItem>(new CategoryItem("Motion Books", "motionbooks"), root);
        new KTreeNode<CategoryItem>(new CategoryItem("Flash", "flase"), root);
        new KTreeNode<CategoryItem>(new CategoryItem("Designs & Interfaces", "designs"), root);
        new KTreeNode<CategoryItem>(new CategoryItem("Customization", "customization"), root);
        new KTreeNode<CategoryItem>(new CategoryItem("Cartoons & Comics", "cartoons"), root);
        new KTreeNode<CategoryItem>(new CategoryItem("Manga & Anime", "manga"), root);
        new KTreeNode<CategoryItem>(new CategoryItem("Anthro", "anthro"), root);
        new KTreeNode<CategoryItem>(new CategoryItem("Fan Art", "fanart"), root);
        new KTreeNode<CategoryItem>(new CategoryItem("Resources & Stock Images", "resources"), root);
        new KTreeNode<CategoryItem>(new CategoryItem("Community Projects", "projects"), root);
        new KTreeNode<CategoryItem>(new CategoryItem("Contests", "contests"), root);
        new KTreeNode<CategoryItem>(new CategoryItem("deviantART Related", "darelated"), root);
        new KTreeNode<CategoryItem>(new CategoryItem("Scraps", "scraps"), root);

        return root;
    }

    public void onImageClick(View view, ImageData imageData) {
        Intent imageDetailIntent = new Intent(this, ImageDetailActivity.class);
        imageDetailIntent.putExtra("imageData", imageData);

        int[] screenLocation = new int[2];
        view.getLocationOnScreen(screenLocation);

        imageDetailIntent.putExtra("left", screenLocation[0]);
        imageDetailIntent.putExtra("top", screenLocation[1]);
        imageDetailIntent.putExtra("width", view.getWidth());
        imageDetailIntent.putExtra("height", view.getHeight());
        //imageDetailIntent.putExtra("screenshot", loadBitmapFromView(mImageListFragment.getView()));

        startActivity(imageDetailIntent);

        overridePendingTransition(0, 0);
    }

    public static Bitmap loadBitmapFromView(View v) {
        Bitmap b = Bitmap.createBitmap(v.getWidth()/4, v.getHeight()/4, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        //v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }

    public void onCategorySelected(CategoryItem categoryItem){
        if(selectedCategory == categoryItem)
            return;

        selectedCategory = categoryItem;
        category = selectedCategory.getUrlText();

        mImageListFragment.setCategory(category);
        shouldRefresh = true;
    }

    public void dismissDrawer(){
        getSupportActionBar().setTitle(selectedCategory.getDisplayText());
        mDrawerLayout.closeDrawers();
    }
}