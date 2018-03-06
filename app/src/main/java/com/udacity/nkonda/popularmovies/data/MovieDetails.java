package com.udacity.nkonda.popularmovies.data;

/**
 * Created by nkonda on 12/20/17.
 */

public class MovieDetails {
    private String originalTitle;
    private String posterPath;
    private String plotSynopsis;
    private double rating;
    private String releaseDate;
    private boolean favorite;

    public MovieDetails(String originalTitle, String posterPath, String plotSynopsis, double rating, String releaseDate) {
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
        this.plotSynopsis = plotSynopsis;
        this.rating = rating;
        this.releaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getPlotSynopsis() {
        return plotSynopsis;
    }

    public double getRating() {
        return rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "MovieDetails{" +
                "originalTitle='" + originalTitle + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", plotSynopsis='" + plotSynopsis + '\'' +
                ", rating=" + rating +
                ", releaseDate='" + releaseDate + '\'' +
                '}';
    }
}
