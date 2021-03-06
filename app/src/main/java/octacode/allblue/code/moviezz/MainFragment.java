package octacode.allblue.code.moviezz;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import octacode.allblue.code.moviezz.adapter.Main_Movie_Adapter;
import octacode.allblue.code.moviezz.data.MovieContract;
import octacode.allblue.code.moviezz.fetchers.FetchMovieTask;
import octacode.allblue.code.moviezz.fetchers.FetchSearch;

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int MOVIE_LOADER = 0;
    private GridView main_grid_view;
    private AutoScrollViewPager mPager;
    public static SwipeRefreshLayout swipeRefreshLayout;

    public static final String[] MOVIE_COLUMNS={
            MovieContract.MainMovieTable._ID,
            MovieContract.MainMovieTable.COLUMN_MAIN_VOTE_COUNT_DOUBLE,
            MovieContract.MainMovieTable.COLUMN_MAIN_ADULT_TEXT,
            MovieContract.MainMovieTable.COLUMN_MAIN_BACKDROP_PATH_TEXT,
            MovieContract.MainMovieTable.COLUMN_MAIN_GENRE_IDS_TEXT,
            MovieContract.MainMovieTable.COLUMN_MAIN_MOVIE_ID_DOUBLE,
            MovieContract.MainMovieTable.COLUMN_MAIN_ORG_LANGUAGE_TEXT,
            MovieContract.MainMovieTable.COLUMN_MAIN_OVERVIEW_TEXT,
            MovieContract.MainMovieTable.COLUMN_MAIN_PAGE_INT,
            MovieContract.MainMovieTable.COLUMN_MAIN_POPULARITY_DOUBLE,
            MovieContract.MainMovieTable.COLUMN_MAIN_POSTER_PATH_TEXT,
            MovieContract.MainMovieTable.COLUMN_MAIN_RATINGS_DOUBLE,
            MovieContract.MainMovieTable.COLUMN_MAIN_TITLE_TEXT,
            MovieContract.MainMovieTable.COLUMN_MAIN_VOTE_AVERAGE_DOUBLE
    };

    public static final int COLUMN_ID=0;
    public static final int COLUMN_VOTE_COUNT=1;
    public static final int COLUMN_ADULT=2;
    public static final int COLUMN_BACKDROP_URL=3;
    public static final int COLUMN_REL_DATE=4; //Release date is stored in the genre_ids. Oops! My fault here.
    public static final int COLUMN_MOVIE_ID=5;
    public static final int COLUMN_LANGUAGE=6;
    public static final int COLUMN_OVERVIEW=7;
    public static final int COLUMN_PAGE=8;
    public static final int COLUMN_POPULARITY=9;
    public static final int COLUMN_POSTER_URL=10;
    public static final int COLUMN_RATINGS=11;
    public static final int COLUMN_TITLE=12;
    public static final int COLUMN_VOTE_AVERAGE=13;


    public MainFragment() {
    }

    public void updateMovieRecycler() {
        FetchMovieTask fetchMovieTask=new FetchMovieTask(getContext());
        String settings=PreferenceManager.getDefaultSharedPreferences(getContext()).getString(getString(R.string.pref_sort_key),"popular");
        fetchMovieTask.execute(settings);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.first_fragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                startActivity(new Intent(getContext(),SettingsActivit.class));
                break;
            case R.id.action_favourites:
                startActivity(new Intent(getContext(),FavouritesActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    Main_Movie_Adapter movieAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView= inflater.inflate(R.layout.fragment_main, container, false);
        FloatingActionButton fab = (FloatingActionButton)rootView.findViewById(R.id.fab_search);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchSearch fetchSearch = new FetchSearch(getContext());
                fetchSearch.execute("Dangal");
            }
        });
        //mPager = (AutoScrollViewPager)rootView.findViewById(R.id.pager);
        main_grid_view=(GridView)rootView.findViewById(R.id.main_grid_view);
        swipeRefreshLayout=(SwipeRefreshLayout)rootView.findViewById(R.id.swipe_refresh);
        movieAdapter=new Main_Movie_Adapter(getContext(),null);
        main_grid_view.setAdapter(movieAdapter);
        main_grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                movieAdapter = (Main_Movie_Adapter)parent.getAdapter();
                Cursor cursor = movieAdapter.getCursor();
                Intent intent = new Intent(getContext(),DetailActivity.class);
                intent.putExtra("ID",cursor.getLong(COLUMN_ID));
                intent.putExtra("MOVIE_ID",cursor.getString(COLUMN_MOVIE_ID));
                intent.putExtra("ADULT",cursor.getString(COLUMN_ADULT));
                intent.putExtra("BACKDROP_URL",cursor.getString(COLUMN_BACKDROP_URL));
                intent.putExtra("OVERVIEW",cursor.getString(COLUMN_OVERVIEW));
                intent.putExtra("TITLE",cursor.getString(COLUMN_TITLE));
                intent.putExtra("POSTER_URL",cursor.getString(COLUMN_POSTER_URL));
                intent.putExtra("LANGUAGE",cursor.getString(COLUMN_LANGUAGE));
                intent.putExtra("POPULARITY",cursor.getString(COLUMN_POPULARITY));
                intent.putExtra("VOTE_AVG",cursor.getString(COLUMN_VOTE_AVERAGE));
                intent.putExtra("RATINGS",cursor.getString(COLUMN_RATINGS));
                intent.putExtra("REL_DATE",cursor.getString(COLUMN_REL_DATE));
                intent.putExtra("GENRE_IDS",cursor.getString(COLUMN_VOTE_COUNT));
                startActivity(intent);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateMovieRecycler();
            }
        });

        ImageView buttonSearch = (ImageView)rootView.findViewById(R.id.search_image);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(getClass().getSimpleName(),"Working?");
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieRecycler();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(MOVIE_LOADER,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                getActivity(),
                MovieContract.MainMovieTable.CONTENT_URI,
                MOVIE_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        movieAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        movieAdapter.swapCursor(null);
    }
}
