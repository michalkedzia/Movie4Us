package com.movie4us;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import data.MovieData;

import java.util.ArrayList;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private ArrayList<MovieData> items;

    public CardAdapter(ArrayList<MovieData> items) {
        this.items = items;
    }

    public ArrayList<MovieData> getItems() {
        return items;
    }

    public void setItems(ArrayList<MovieData> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_card, parent, false);
        return new ViewHolder(view);
    }

    //TODO: Testy teściki
    String returnPathToImage(int position)
    {
        return items.get(position).getPoster_path();
    }

    String returnTitle(int position)
    {
        return items.get(position).title;
    }

    String returnDescription(int position)
    {
        return items.get(position).overview;
    }

    String returnFilmRating(int position)
    {
        String Rating=Float.toString(items.get(position).vote_average);
        return Rating;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, description, film_rating;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.item_image);
            title = itemView.findViewById(R.id.item_title);
            description = itemView.findViewById(R.id.item_description);
            film_rating = itemView.findViewById(R.id.item_film_rating);
        }

        void setData(MovieData movieCardModel) {
            Picasso.get().load(movieCardModel.getPoster_path()).into(image);
            title.setText(movieCardModel.title);
            description.setText(movieCardModel.overview);
            film_rating.setText(Float.toString(movieCardModel.vote_average));
        }


    }

}
