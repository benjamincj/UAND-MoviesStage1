package udacity.popularmovies.logic;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import udacity.popularmovies.R;
import udacity.popularmovies.extras.TmpData;
import udacity.popularmovies.model.Movie;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    private View rootView;
    private Activity parentActivity;
    final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    final String IMAGE_SIZE_THUMBNAIL = "w342";
    final String IMAGE_SIZE_BACKDROP = "w780";
    private ProgressBar progressBar;

    public MovieDetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Movie movie = TmpData.getInstance().getSelectedMovie();

        rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_movie_detail);
        progressBar.setVisibility(View.VISIBLE);

        ImageView movieBackdrop = (ImageView) rootView.findViewById(R.id.movie_backdrop);
        Picasso.with(parentActivity).load(POSTER_BASE_URL+IMAGE_SIZE_BACKDROP+movie.getBackdropPath()).into(movieBackdrop);

        TextView movieTitle = (TextView) rootView.findViewById(R.id.movie_title);
        movieTitle.setText(movie.getTitle());

        ImageView movieThumbnail = (ImageView) rootView.findViewById(R.id.movie_thumbnail);
        Picasso.with(parentActivity).load(POSTER_BASE_URL+IMAGE_SIZE_THUMBNAIL+movie.getPosterPath()).into(movieThumbnail);

        TextView voteAverage = (TextView) rootView.findViewById(R.id.vote_average);
        String voteAverageStr = "User rating: "+String.valueOf(movie.getVoteAverage());
        voteAverage.setText(voteAverageStr);

        TextView releaseDate = (TextView) rootView.findViewById(R.id.release_date);
        String releaseDateStr = "Release date: "+movie.getReleaseDate();
        releaseDate.setText(releaseDateStr);

        TextView overview = (TextView) rootView.findViewById(R.id.overview);
        overview.setText(movie.getOverview());

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar.setVisibility(View.GONE);
    }
}
