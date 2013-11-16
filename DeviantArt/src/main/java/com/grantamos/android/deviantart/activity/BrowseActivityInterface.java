package com.grantamos.android.deviantart.activity;

import com.grantamos.android.deviantart.helpers.CategoryItem;

/**
 * Created by Grant on 11/13/13.
 */
public interface BrowseActivityInterface {
    public void onCategorySelected(CategoryItem categoryItem);
    public void dismissDrawer();
}
