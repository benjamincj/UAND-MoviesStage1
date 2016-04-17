package udacity.popularmovies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import udacity.popularmovies.R;
import udacity.popularmovies.model.Movie;

/**
 * Created by Benjamín on 17-04-2016.
 */
public class MoviesAdapter extends ArrayAdapter<Movie> {

    final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/";
    final String IMAGE_SIZE = "w342/";

    public MoviesAdapter(Context context, int resource, int textViewResourceId, List<Movie> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.movie_item, parent, false);
        }

        ImageView posterView = (ImageView) convertView.findViewById(R.id.movie_poster);
        Picasso.with(getContext()).load(POSTER_BASE_URL+IMAGE_SIZE+movie.getPosterPath()).into(posterView);

        return convertView;
    }
}
