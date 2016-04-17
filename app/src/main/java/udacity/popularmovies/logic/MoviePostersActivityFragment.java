package udacity.popularmovies.logic;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import udacity.popularmovies.R;
import udacity.popularmovies.adapter.MoviesAdapter;
import udacity.popularmovies.asynctask.FetchMoviesTask;
import udacity.popularmovies.extras.TmpData;
import udacity.popularmovies.model.Movie;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviePostersActivityFragment extends Fragment {

    private FetchMoviesTask fetchMoviesTask;
    private View rootView;
    private Activity parentActivity;
    private MoviesAdapter moviesAdapter;
    private ProgressBar progressBar;

    public MoviePostersActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        moviesAdapter =
                new MoviesAdapter(
                parentActivity,
                R.layout.movie_item,
                R.id.movie_poster,
                new ArrayList<Movie>()
        );

        rootView = inflater.inflate(R.layout.fragment_movie_posters, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_movie_posters);
        progressBar.setVisibility(View.VISIBLE);

        GridView gridView = (GridView) rootView.findViewById(R.id.movies_gridView);
        gridView.setAdapter(moviesAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = moviesAdapter.getItem(position);
                Intent intent = new Intent(parentActivity, MovieDetailActivity.class);
                TmpData.getInstance().setSelectedMovie(movie);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void updateMovies(){
        fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.execute(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    public FetchMoviesTask getFetchMoviesTask() {
        return fetchMoviesTask;
    }

    public View getRootView() {
        return rootView;
    }

    public Activity getParentActivity() {
        return parentActivity;
    }

    public MoviesAdapter getMoviesAdapter() {
        return moviesAdapter;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }
}
