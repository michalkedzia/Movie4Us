package com.movie4us;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Locale;

public class CategoryAdapter extends ArrayAdapter<CategoryItem> {

    public CategoryAdapter(Context context, ArrayList<CategoryItem> categoryList) {
        super(context, 0, categoryList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.category_spinner_row, parent, false);
        }

        ImageView imageViewIcon = convertView.findViewById(R.id.image_view_icon);
        TextView textViewnName = convertView.findViewById(R.id.text_view_name);
        CategoryItem currentItem = getItem(position);

        if(currentItem!=null)
        {
            imageViewIcon.setImageResource(currentItem.getImageIcon());
            textViewnName.setText(currentItem.getCategoryName());
        }

        return convertView;
    }
}
