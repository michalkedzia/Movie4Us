package swipemenu;

import android.graphics.Bitmap;

public class ItemModel {
    private String image, title, description, film_rating;

    public ItemModel(){
    }

    public ItemModel(String image, String title, String description, String film_rating){
        this.image = image;
        this.title = title;
        this.description = description;
        this.film_rating = film_rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilm_rating() {
        return film_rating;
    }

    public void setFilm_rating(String film_rating) {
        this.film_rating = film_rating;
    }
}
