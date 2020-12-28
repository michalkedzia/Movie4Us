package com.movie4us;

public class CategoryItem {
    private String mCategoryName;
    private int mImageIcon;

    public CategoryItem(String categoryName, int imageIcon)
    {
        mCategoryName=categoryName;
        mImageIcon=imageIcon;
    }

    public String getCategoryName()
    {
        return mCategoryName;
    }

    public int getImageIcon()
    {
        return mImageIcon;
    }
}
