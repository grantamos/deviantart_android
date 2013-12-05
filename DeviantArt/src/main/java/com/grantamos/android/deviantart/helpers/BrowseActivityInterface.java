package com.grantamos.android.deviantart.helpers;

import android.view.View;

import com.grantamos.android.deviantart.helpers.CategoryItem;
import com.grantamos.android.deviantart.model.Image;

/**
 * Created by Grant on 11/13/13.
 */
public interface BrowseActivityInterface {
    public void onCategorySelected(CategoryItem categoryItem);
    public void dismissDrawer();
    public void onImageClick(View view, Image imageData);
}
