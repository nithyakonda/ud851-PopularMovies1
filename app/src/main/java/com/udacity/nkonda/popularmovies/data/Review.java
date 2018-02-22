package com.udacity.nkonda.popularmovies.data;

/**
 * Created by nkonda on 2/21/18.
 */

public class Review {
    private String author;
    private String content;

    public Review(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
