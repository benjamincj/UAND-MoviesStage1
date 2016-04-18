package udacity.popularmovies.asynctask;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.View;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import udacity.popularmovies.BuildConfig;
import udacity.popularmovies.R;
import udacity.popularmovies.logic.MoviePostersActivityFragment;
import udacity.popularmovies.model.Movie;

/**
 * Created by Benjamín on 16-04-2016.
 */
public class FetchMoviesTask extends AsyncTask<MoviePostersActivityFragment, MoviePostersActivityFragment, ArrayList<Movie>> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
    private MoviePostersActivityFragment moviePostersActivityFragment;

    private ArrayList<Movie> getMovieDataFromJson(String movieJsonStr) throws JSONException{

        final String RESULTS = "results";
        final String POSTER_PATH = "poster_path";
        final String ADULT = "adult";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        final String GENRE_IDS = "genre_ids";
        final String ID = "id";
        final String ORIGINAL_TITLE = "original_title";
        final String ORIGINAL_LANGUAGE = "original_language";
        final String TITLE = "title";
        final String BACKDROP_PATH = "backdrop_path";
        final String POPULARITY = "popularity";
        final String VOTE_COUNT = "vote_count";
        final String VIDEO = "video";
        final String VOTE_AVERAGE = "vote_average";

        ArrayList<Movie> movies = new ArrayList<>();

        JSONObject moviesJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = moviesJson.getJSONArray(RESULTS);

        for(int i = 0; i < movieArray.length(); i++){

            JSONObject movieJO = movieArray.getJSONObject(i);

            Movie movie = new Movie();
            movie.setPosterPath(movieJO.getString(POSTER_PATH));
            movie.setAdult(movieJO.getBoolean(ADULT));
            movie.setOverview(movieJO.getString(OVERVIEW));
            movie.setReleaseDate(movieJO.getString(RELEASE_DATE));

            JSONArray genreIdsArray = movieJO.getJSONArray(GENRE_IDS);
            String[] genreIds = new String[genreIdsArray.length()];
            for(int j = 0; j < genreIdsArray.length(); j++){
                genreIds[j] = genreIdsArray.get(j).toString();
            }
            movie.setGenreIds(genreIds);

            movie.setId(movieJO.getString(ID));
            movie.setOriginalTitle(movieJO.getString(ORIGINAL_TITLE));
            movie.setOriginalLanguage(movieJO.getString(ORIGINAL_LANGUAGE));
            movie.setTitle(movieJO.getString(TITLE));
            movie.setBackdropPath(movieJO.getString(BACKDROP_PATH));
            movie.setPopularity(movieJO.getDouble(POPULARITY));
            movie.setVoteCount(movieJO.getInt(VOTE_COUNT));
            movie.setVideo(movieJO.getBoolean(VIDEO));
            movie.setVoteAverage(movieJO.getDouble(VOTE_AVERAGE));

            movies.add(movie);
        }

        return  movies;
    }

    @Override
    protected void onProgressUpdate(MoviePostersActivityFragment... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected ArrayList<Movie> doInBackground(MoviePostersActivityFragment... params) {

        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String moviesJsonStr = null;

        moviePostersActivityFragment = params[0];
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(moviePostersActivityFragment.getActivity());
        //String sortBy = sharedPreferences.getString(
                //moviePostersActivityFragment.getString(R.string.pref_sort_key),
                //moviePostersActivityFragment.getString(R.string.pref_sort_most_popular));

        try{
            String MOVIES_BASE_URL = "";
            final String API_KEY_PARAM = "api_key";

            String prefKey = sharedPreferences.getString(moviePostersActivityFragment.getString(R.string.pref_sort_key), "");

            if(prefKey.equalsIgnoreCase(moviePostersActivityFragment.getString(R.string.pref_sort_most_popular))){
                MOVIES_BASE_URL = "";
            }

            if(prefKey.equalsIgnoreCase(moviePostersActivityFragment.getString(R.string.pref_sort_top_rated))){
                MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie/top_rated?";
            }

            Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.MyMovieDbApiKey)
                    .build();

            URL url = new URL(builtUri.toString());

            Log.d(LOG_TAG, "Built URI " + builtUri.toString());

            // Create the request to OpenWeatherMap, and open the connection
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            moviesJsonStr = buffer.toString();
        }catch (IOException e){
            Log.e(LOG_TAG, "Error: ", e);
        }finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        ArrayList<Movie> movies = new ArrayList<>();

        try {
            if(moviesJsonStr != null) {
                movies = getMovieDataFromJson(moviesJsonStr);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return movies;
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        super.onPostExecute(movies);

        if(movies != null && movies.size() != 0){
            moviePostersActivityFragment.getMoviesAdapter().clear();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                moviePostersActivityFragment.getMoviesAdapter().addAll(movies);
            }else{
                for(Movie movie : movies){
                    moviePostersActivityFragment.getMoviesAdapter().add(movie);
                }
            }
            moviePostersActivityFragment.getProgressBar().setVisibility(View.GONE);
        }
    }
}
