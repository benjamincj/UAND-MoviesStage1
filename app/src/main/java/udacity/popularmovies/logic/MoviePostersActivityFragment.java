package udacity.popularmovies.logic;

import android.app.Activity;
import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import udacity.popularmovies.R;
import udacity.popularmovies.adapter.MoviesAdapter;
import udacity.popularmovies.asynctask.FetchMoviesTask;
import udacity.popularmovies.extras.NetworkStatus;
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
    private LinearLayout noConnectionLayout;

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

        noConnectionLayout = (LinearLayout) rootView.findViewById(R.id.no_connection);
        noConnectionLayout.setVisibility(View.GONE);
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
        if(NetworkStatus.isOnline(getContext())) {
            fetchMoviesTask = new FetchMoviesTask();
            fetchMoviesTask.execute(this);
        }else{
            progressBar.setVisibility(View.GONE);
            noConnectionLayout.setVisibility(View.VISIBLE);
            Snackbar snackbar = Snackbar.make(rootView, "No internet conecction", Snackbar.LENGTH_LONG);
            snackbar.setAction("Configuration", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            });
            View view = snackbar.getView();
            TextView textView = (TextView) view.findViewById(R.id.snackbar_text);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            snackbar.show();
        }
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
