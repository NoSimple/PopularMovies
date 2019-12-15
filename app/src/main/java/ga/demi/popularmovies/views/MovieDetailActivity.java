package ga.demi.popularmovies.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import ga.demi.popularmovies.R;
import ga.demi.popularmovies.models.Result;

public class MovieDetailActivity extends AppCompatActivity {

    private Result mMoviePoster;

    private ImageView mMoviePosterIV;
    private TextView mMovieTitleTV;
    private TextView mMovieYearTV;
    private TextView mMovieTimeTV;
    private TextView mMovieAverageTV;
    private TextView mMovieOverviewTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        getSupportActionBar().setTitle("Movie detail");

        mMoviePosterIV = findViewById(R.id.iv_movie_poster);
        mMovieTitleTV = findViewById(R.id.tv_movie_title);
        mMovieYearTV = findViewById(R.id.tv_movie_year);
        mMovieTimeTV = findViewById(R.id.tv_movie_time);
        mMovieAverageTV = findViewById(R.id.tv_movie_average);
        mMovieOverviewTV = findViewById(R.id.tv_movie_overview);

        Bundle arguments = getIntent().getExtras();
        if (arguments != null) {
            mMoviePoster = arguments.getParcelable(Result.class.getSimpleName());

            mMovieTitleTV.setText(mMoviePoster.getTitle());
        }
    }
}
