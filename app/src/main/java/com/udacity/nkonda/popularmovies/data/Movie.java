package com.udacity.nkonda.popularmovies.data;

/**
 * Created by nkonda on 12/18/17.
 */

public class Movie {
    private int id;
    private String title;
    private String posterPath;

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

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", posterPath='" + posterPath + '\'' +
                '}';
    }
}
