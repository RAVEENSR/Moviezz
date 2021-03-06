package octacode.allblue.code.moviezz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

public class CastActivity extends AppCompatActivity {

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getIntent().getStringExtra("NAME"));
        TextView dob_tv = (TextView)findViewById(R.id.dob_text_tv);
        TextView place = (TextView)findViewById(R.id.place_of_birth_text_tv);
        TextView know_more = (TextView)findViewById(R.id.biography_text_tv);
        TextView gender = (TextView)findViewById(R.id.gender_text_tv);
        ImageView imageView = (ImageView) findViewById(R.id.cast_image);
        dob_tv.setText(Utility.datePresenter(getIntent().getStringExtra("DOB")));
        place.setText(getIntent().getStringExtra("PLACE_OF_BIRTH"));
        know_more.setText(getIntent().getStringExtra("BIOGRAPHY"));
        gender.setText(getIntent().getStringExtra("GENDER"));
        Picasso.with(this).load(getIntent().getStringExtra("PROFILE_PATH")).error(R.mipmap.ic_launcher).into(imageView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAdView = (AdView)findViewById(R.id.adViewCast);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
