package ch.hefr.tic.hefr_mylocation;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Random;

import ch.hefr.tic.hefr_mylocation.json.AsyncTaskParseJson;
import ch.hefr.tic.hefr_mylocation.json.JsonWrap;

public class RandomActivity extends FragmentActivity implements Serializable {
    public final static String NEW_LOCATION = "ch.hefr.tic.hefr_mylocation.MESSAGE";

    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    private JsonWrap randomPlace;

    static Random rnd = new Random();
    static LatLng restos[] = new LatLng[] {
        new LatLng(46.7943609, 7.1573518), // L'Imprevu
            new LatLng(46.794657, 7.1572713), // Le Cyclo
            new LatLng(46.7965967, 7.1552784), // L'Etoile d'Or
            new LatLng(46.7962794, 7.155182), // Denner
            new LatLng(46.7979302, 7.1530523), // Migros
            new LatLng(46.7954017, 7.1574297), // Can Dersim
            new LatLng(46.7951217, 7.1568235), // L'Arrache
            new LatLng(46.7932309, 7.1591132) // Resto de l'ecole d'ing
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random);
        mProgress = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new AsyncTaskParseJson(this).execute();
    }

    public void setRandomPlace(JsonWrap jsonWrap) {
        this.randomPlace = jsonWrap;
    }

    public void startProgressBar() {
        ProgressBarUpdater pro = new ProgressBarUpdater(this);
        pro.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_random, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static LatLng giveMeSomeRestoPlease() {
        return restos[rnd.nextInt(restos.length)];
    }

    public class ProgressBarUpdater extends Thread {
        RandomActivity a;
        public ProgressBarUpdater(RandomActivity a) {
            this.a = a;
        }

        public void run() {
            while(mProgressStatus < 100) {
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mProgress.setProgress(mProgressStatus);
                mProgressStatus++;
            }
            a.switchView();
        }
    }

    public void switchView() {
        Intent intent = new Intent(this, LocationActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(NEW_LOCATION, randomPlace);
        intent.putExtras(mBundle);

        startActivity(intent);
    }

}
