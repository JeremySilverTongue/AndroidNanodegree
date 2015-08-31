package com.udacity.silver.popularmovies;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.silver.popularmovies.GetNowPlayingTask.NowPlayingReceiver;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;

public class MovieGridActivityFragment extends Fragment implements NowPlayingReceiver {

    public static final String LOG_TAG = MovieGridActivityFragment.class.getName();
    ArrayList<MovieDb> nowPlaying;
    private MovieGridAdapter movieGridAdapter;
    private GridLayoutManager layoutManager;
    private static final String SCROLL_POSITION_KEY = "scroll";
    private int scrollPosition;
    private RecyclerView mRecyclerView;

    public MovieGridActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movie_grid, container, false);

        String apiKey = getActivity().getString(R.string.mdbApiKey);
        GetNowPlayingTask moviesTask = new GetNowPlayingTask(this);
        moviesTask.execute(apiKey);
        try {
            moviesTask.wait();
        } catch (Exception e){
            Log.e(LOG_TAG, e.toString());
        }

        movieGridAdapter = new MovieGridAdapter(getContext());
        mRecyclerView = (RecyclerView) root.findViewById(R.id.rv);
        mRecyclerView.setAdapter(movieGridAdapter);

        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        layoutManager = new GridLayoutManager(getContext(),(int)dpWidth/200);
        mRecyclerView.setLayoutManager(layoutManager);

        if (savedInstanceState != null){
            scrollPosition = savedInstanceState.getInt(SCROLL_POSITION_KEY, 0);
        }
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(movieGridAdapter);
        return root;
    }

    @Override
    public void receiveNowPlaying(List<MovieDb> nowPlaying) {
        this.nowPlaying = new ArrayList<MovieDb>(nowPlaying);
        this.movieGridAdapter.movies = this.nowPlaying;
        this.movieGridAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(scrollPosition);
        for (MovieDb movie : nowPlaying){
            Log.d(LOG_TAG, movie.getTitle());
//            Log.d(LOG_TAG, movie.getPosterPath());
            Log.d(LOG_TAG,movie.getReleaseDate());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCROLL_POSITION_KEY, layoutManager.findFirstCompletelyVisibleItemPosition());
        Log.d(LOG_TAG, "Storing scroll position: " + layoutManager.findFirstCompletelyVisibleItemPosition()  );
    }
}
