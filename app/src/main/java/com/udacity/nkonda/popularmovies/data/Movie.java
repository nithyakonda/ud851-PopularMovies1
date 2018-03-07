package com.udacity.nkonda.popularmovies.data;

/**
 * Created by nkonda on 12/18/17.
 */

public class Movie {
    private int id;
    private String title;
    private String posterPath;
    private boolean favorite;

    public Movie(int id, String title, String posterPath) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", favorite='" + favorite + '\'' +
                '}';
    }
}
