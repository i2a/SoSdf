package ch.i2a.sosdf.json;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.i2a.sosdf.RandomActivity;
import ch.i2a.sosdf.json.JsonParser;

/**
 * Created by nkcr on 11.06.15.
 */
// you can make this class as another java file so it will be separated from your main activity.
public class AsyncTaskParseJson extends AsyncTask<String, String, String> {

    final String TAG = "AsyncTaskParseJson.java";
    String yourJsonStringUrl = "https://raw.githubusercontent.com/i2a/SoSdf-static/master/sosdf-data.json";
    JSONArray dataJsonArr = null;
    RandomActivity randomActivity;
    Random rnd = new Random();

    public AsyncTaskParseJson(RandomActivity randomActivity) {
        this.randomActivity = randomActivity;
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected String doInBackground(String... arg0) {

        try
        {
            // instantiate our json parser
            JsonParser jParser = new JsonParser();
            // get json string from url
            JSONObject json = jParser.getJSONFromUrl(yourJsonStringUrl);
            // get the array of places
            dataJsonArr = json.getJSONArray("places");
            List<JsonWrap> places = new ArrayList<JsonWrap>();
            // loop through all places
            for (int i = 0; i < dataJsonArr.length(); i++) {
                JSONObject c = dataJsonArr.getJSONObject(i);
                places.add(new JsonWrap(c.getDouble("lat"),c.getDouble("lng"),c.getString("name"),c.getString("food")));
            }
            randomActivity.setRandomPlace(places.get(rnd.nextInt(places.size())));
            randomActivity.startProgressBar();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String strFromDoInBg) {}
}
