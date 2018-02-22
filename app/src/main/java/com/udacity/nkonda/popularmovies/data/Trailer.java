package com.udacity.nkonda.popularmovies.data;

/**
 * Created by nkonda on 2/21/18.
 */

public class Trailer {
    private String id;
    private String name;

    public Trailer(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
