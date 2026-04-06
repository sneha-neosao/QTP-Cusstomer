package activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.grocery.QTPmart.R;

import java.util.HashMap;

public class GetLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    //SessionManagement sessionManagement;
    HashMap user;
    IconGenerator generator;
    LayoutInflater inflater;
    String latt;
    View markerView ;
    TextView textView;
    private LocationManager locationManager;
    Location currentlocation;

    MarkerOptions markerOptions;
    Marker marker;
    Bitmap icon;

    int from=0;
    double lat,lng;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_location);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        from=getIntent().getIntExtra("from",0);
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        String locationProvider = locationManager.NETWORK_PROVIDER;
        currentlocation = locationManager.getLastKnownLocation(locationProvider);
        latt=String.valueOf(getIntent().getDoubleExtra("lat",0));

           /* lat=getIntent().getDoubleExtra("lat",0);
            lng=getIntent().getDoubleExtra("lng",0);*/

        lat=currentlocation.getLatitude();
        lng=currentlocation.getLongitude();

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng sydney = new LatLng(lat, lng);
        LatLng sydney1 = new LatLng
                (Double.parseDouble("41.40338"),
                        Double.parseDouble("2.17403"));
        mMap.addMarker(new MarkerOptions().position(sydney));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom
                (sydney,
                        16.0f));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(R.anim.slide_down,R.anim.slide_up);
        overridePendingTransition(R.anim.slide_down, R.anim.slide_up);
    }
}