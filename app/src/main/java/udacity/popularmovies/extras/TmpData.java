package udacity.popularmovies.extras;

import udacity.popularmovies.model.Movie;

/**
 * Created by Benjamín on 17-04-2016.
 */
public class TmpData {

    private static TmpData tmpData = null;

    public TmpData(){}

    public static TmpData getInstance(){
        if(tmpData == null){
            tmpData = new TmpData();
        }
        return tmpData;
    }

    private Movie selectedMovie;

    public Movie getSelectedMovie() {
        return selectedMovie;
    }

    public void setSelectedMovie(Movie selectedMovie) {
        this.selectedMovie = selectedMovie;
    }
}
