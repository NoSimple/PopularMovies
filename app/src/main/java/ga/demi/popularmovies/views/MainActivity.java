package ga.demi.popularmovies.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ga.demi.popularmovies.R;
import ga.demi.popularmovies.adapters.MoviePosterAdapter;
import ga.demi.popularmovies.api.RequestToApiMovieDB;
import ga.demi.popularmovies.models.PopularMovieModel;
import ga.demi.popularmovies.models.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class MainActivity extends AppCompatActivity implements MoviePosterAdapter.ListItemClickListener {

    private RecyclerView mMoviePostersRV;
    private ProgressBar mMoviePostersPB;

    private RequestToApiMovieDB mRequestToApiMovieDB;
    private List<Result> mMoviePosterList;

    private MoviePosterAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviePostersRV = findViewById(R.id.rv_movie_posters);
        mMoviePostersPB = findViewById(R.id.pb_movie_posters);

        mRequestToApiMovieDB = RequestToApiMovieDB.getInstanceRequestToApi();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mMoviePostersRV.setLayoutManager(layoutManager);
        mMoviePostersRV.setHasFixedSize(true);

        getRequestApi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.choice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (mMoviePosterList != null) {
            switch (item.getItemId()) {
                case (R.id.most_popular_search): {
                    Comparator<Result> mostPopularComparator =
                            (result1, result2) -> result2.getPopularity().compareTo(result1.getPopularity());
                    Collections.sort(mMoviePosterList, mostPopularComparator);
                    onPostExecute(mMoviePosterList);
                    break;
                }
                case (R.id.highest_rated_search): {
                    Comparator<Result> highestRatedComparator =
                            (result1, result2) -> result2.getVoteAverage().compareTo(result1.getVoteAverage());
                    Collections.sort(mMoviePosterList, highestRatedComparator);
                    onPostExecute(mMoviePosterList);
                    break;
                }
            }
        } else Toast.makeText(getBaseContext(), "Not content", Toast.LENGTH_LONG).show();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(Result.class.getSimpleName(), mMoviePosterList.get(clickedItemIndex));
        startActivity(intent);
    }

    private void showProgressBar(boolean show) {
        if (show) mMoviePostersPB.setVisibility(View.VISIBLE);
        else mMoviePostersPB.setVisibility(View.GONE);
    }

    private void setRecyclerView(List<Result> moviePosterList) {
        mAdapter = new MoviePosterAdapter(moviePosterList, this);
        mMoviePostersRV.setAdapter(mAdapter);
    }

    private void onPostExecute(List<Result> moviePosterList) {
        mAdapter.setWeatherData(moviePosterList);
    }

    private void getRequestApi() {
        showProgressBar(true);
        mRequestToApiMovieDB.getMoviePostersRequest()
                .enqueue(new Callback<PopularMovieModel>() {
                    @Override
                    public void onResponse(Call<PopularMovieModel> call, Response<PopularMovieModel> response) {
                        if (response.body() != null) {
                            mMoviePosterList = response.body().getResults();
                            setRecyclerView(mMoviePosterList);
                        } else {
                            Toast.makeText(getBaseContext(), "Not content", Toast.LENGTH_LONG).show();
                        }
                        showProgressBar(false);
                    }

                    @Override
                    public void onFailure(Call<PopularMovieModel> call, Throwable t) {
                        Toast.makeText(getBaseContext(), t.getMessage(), Toast.LENGTH_LONG).show();
                        showProgressBar(false);
                    }
                });
    }
}