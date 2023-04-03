package fr.nytuo.theSithArchives.gps;


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

import java.util.List;

import fr.nytuo.theSithArchives.HttpAsyncGet;
import fr.nytuo.theSithArchives.PostExecuteActivity;
import fr.nytuo.theSithArchives.R;


public class MagasinSlectionActibity extends AppCompatActivity implements PostExecuteActivity<PositionMagasin>, MagasinAdapterListener {

    List<PositionMagasin> magasins;
    private String fournisseur;
    public static int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;

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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            Log.d("GPS", "fails");
            return;
        }
        updateDistanceOnList();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION == requestCode) {
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
        double lat;
        double lon;

        LocationManager locationManager = (LocationManager)
                getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        try {
            lat = location.getLatitude();
            lon = location.getLongitude();
        } catch (NullPointerException e) {
            lat = -1.0;
            lon = -1.0;
        }

        if (lat == -1.0 && lon == -1.0) {
            Toast.makeText(getApplicationContext(), "Impossible de récupérer votre position", Toast.LENGTH_SHORT).show();
            return;
        }
        for (PositionMagasin magasin : magasins) {
            Location locationMagasin = new Location("magasin");
            locationMagasin.setLatitude(magasin.getLatitude());
            locationMagasin.setLongitude(magasin.getLongitude());
            magasin.setDistance(location.distanceTo(locationMagasin));
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
