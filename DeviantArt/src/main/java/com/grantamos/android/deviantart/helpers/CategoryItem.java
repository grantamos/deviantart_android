package com.grantamos.android.deviantart.helpers;

import java.io.Serializable;

/**
 * Created by Grant on 11/12/13.
 */
public class CategoryItem implements Serializable {
    String displayText;
    String urlText;
    boolean isOpen = false;

    public CategoryItem (String displayText, String urlText) {
        this.displayText = displayText.toUpperCase();
        this.urlText = urlText;
    }

    public String getDisplayText() { return displayText; }

    public String toString() { return displayText; }
}
