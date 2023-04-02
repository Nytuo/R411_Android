package fr.nytuo.android411.gps;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import fr.nytuo.android411.HttpAsyncGet;
import fr.nytuo.android411.MainActivity;
import fr.nytuo.android411.PostExecuteActivity;
import fr.nytuo.android411.ProductAdapterListener;
import fr.nytuo.android411.R;


public class MagasinSlectionActibity extends AppCompatActivity implements PostExecuteActivity<PositionMagasin>, ProductAdapterListener {

    List<PositionMagasin> magasins;
    private String fournisseur;
    public static int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    MagasinAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magasin_list);

        HttpAsyncGet<PositionMagasin> httpAsyncGet = new HttpAsyncGet<PositionMagasin>("https://api.nytuo.fr/api/libraries/positions/5", PositionMagasin.class, this, null);


    }

    @Override
    public void onPostExecutePokemons(List<PositionMagasin> itemList) {
        magasins = itemList;


        for (PositionMagasin magasin : magasins) {
            System.out.println(magasin.getName());
        }
        adapter = new MagasinAdapter(this, magasins);
        ListView listProduits = findViewById(R.id.listView);
        listProduits.setAdapter(adapter);

        LocationManager androidLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            Log.d("GPS", "fails");
            return;
        }
        updateDistanceOnList();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION == requestCode) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                updateDistanceOnList();

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }

    }

    public void updateDistanceOnList() {
        LocationManager androidLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location loc = androidLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(loc != null) {
            Log.d("GPS", "Latitude : " + loc.getLatitude() + " Longitude : " + loc.getLongitude());
            for (PositionMagasin magasin : magasins) {
                Location location = new Location("magasin");
                location.setLatitude(magasin.getLatitude());
                location.setLongitude(magasin.getLongitude());
                magasin.setDistance(loc.distanceTo(location));
            }
        }
        adapter.notifyDataSetChanged();
        for (PositionMagasin magasin : magasins) {
            System.out.println(magasin.getName() + " " + magasin.getDistance());
        }
    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void onElementClick(int position) {

    }
}
