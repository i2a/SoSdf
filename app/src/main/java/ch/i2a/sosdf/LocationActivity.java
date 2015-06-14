package ch.i2a.sosdf;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import com.directions.route.Route;
import com.directions.route.Routing;

import ch.i2a.sosdf.json.JsonWrap;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, com.directions.route.RoutingListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LatLng targetLatLng;
    private Intent randomIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        // connects to the google api
        buildGoogleApiClient();
        // make an asyncrounous call to get the map. Will then call onMapReady()
        mapFragment.getMapAsync(this);
        // retrieves the intent from the caller
        randomIntent = getIntent();
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Map is ready to be used.
        mMap = googleMap;
        // Set the long click listener as a way to exit the map.
        mMap.setOnMapLongClickListener(this);
    }

    // Build the google API
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Get the current location
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LatLng currentLatLng;
        if (mLastLocation != null) {
            currentLatLng = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
        } else {
            Log.d("nkcr", "Null location");
            // The default location, at HEIA-FR
            currentLatLng = new LatLng(46.792529, 7.159996);
        }
        // Get the place
        JsonWrap randomJsonData = (JsonWrap)(randomIntent.getParcelableExtra(RandomActivity.NEW_LOCATION));
        targetLatLng = new LatLng(randomJsonData.lat,randomJsonData.lng);
        // Set the text views
        ((TextView)findViewById(R.id.textViewName)).setText(randomJsonData.name);
        ((TextView)findViewById(R.id.textViewFood)).setText(randomJsonData.food);
        // Add markers
        mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Votre position"));
        mMap.addMarker(new MarkerOptions().position(targetLatLng).title(randomJsonData.name)).showInfoWindow();
        // Zoom on map
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(currentLatLng);
        builder.include(targetLatLng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),250));
        // Create path
        Routing routing = new Routing(Routing.TravelMode.WALKING);
        routing.registerListener(this);
        routing.execute(currentLatLng, targetLatLng);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("nkcr", "Connection error");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        System.err.print("Connection error (nkcr)");
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Will then capp onConnected() if ok
        mGoogleApiClient.connect();
    }

    @Override
    public void onRoutingFailure() {
        // The Routing request failed
    }

    @Override
    public void onRoutingStart() {
        // The Routing Request starts
    }

    @Override
    public void onRoutingSuccess(PolylineOptions mPolyOptions, Route route) {
        // Draw the route line
        PolylineOptions polyOptions = new PolylineOptions();
        polyOptions.color(Color.BLUE);
        polyOptions.width(10);
        polyOptions.addAll(mPolyOptions.getPoints());
        mMap.addPolyline(polyOptions);
    }

    /** Called when the user clicks the Send button */
    public void again(View view) {
        // Back to randomActivity
        Intent intent = new Intent(this, RandomActivity.class);
        startActivity(intent);
    }

}
