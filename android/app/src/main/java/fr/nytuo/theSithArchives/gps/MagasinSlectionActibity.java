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
    public static int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;

    MagasinAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magasin_list);
        // on récupère la liste des magasins
        HttpAsyncGet<PositionMagasin> httpAsyncGet = new HttpAsyncGet<PositionMagasin>("https://api.nytuo.fr/api/libraries/positions/5", PositionMagasin.class, this, null);


    }

    @Override
    public void onPostExecutePokemons(List<PositionMagasin> itemList) {
        magasins = itemList;
        //on ajoute les magasins à la vue
        adapter = new MagasinAdapter(this, magasins);
        ListView listProduits = findViewById(R.id.listView);
        listProduits.setAdapter(adapter);
        // on demande la permission de localisation si elle n'est pas déjà accordée
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            return;
        }
        // on mais a joure les distances si la permission est déjà accordée
        updateDistanceOnList();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // une fois que l'utilisateur a répondu à la demande de permission, on met à jour la liste si la permission est accordée
        if (MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION == requestCode) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                updateDistanceOnList();

            } else {

                Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void updateDistanceOnList() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // on récupère la position de l'utilisateur et on met à jour la distance de chaque magasin
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
