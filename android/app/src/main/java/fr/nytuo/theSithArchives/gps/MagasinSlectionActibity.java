package fr.nytuo.theSithArchives.gps;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import java.util.List;

import fr.nytuo.theSithArchives.HttpAsyncGet;
import fr.nytuo.theSithArchives.PostExecuteActivity;
import fr.nytuo.theSithArchives.R;


public class MagasinSlectionActibity extends AppCompatActivity implements PostExecuteActivity<PositionMagasin>, MagasinAdapterListener {

    List<PositionMagasin> magasins;
    private String fournisseur;
    public static int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;

    MagasinAdapter adapter;

    String selectedMagasin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magasin_list);
        // on récupère la liste des magasins
        HttpAsyncGet<PositionMagasin> httpAsyncGet = new HttpAsyncGet<PositionMagasin>("https://api.nytuo.fr/api/libraries/positions/5", PositionMagasin.class, this, null);

        Button button5 = findViewById(R.id.button5);
        button5.setOnClickListener(v -> {
            spawnNotification("Vos articles sont réservés dans votre librairie '"+selectedMagasin
                    +"'. Vous recevrez une notification lors de leur disponibilité. Votre commande porte le numéro: "+Math.random());
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmation de commande");
            builder.setMessage("Vos articles sont réservés dans votre librairie '"+selectedMagasin
                    +"'. Vous recevrez une notification lors de leur disponibilité. Votre commande porte le numéro: "+Math.random());
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            AlertDialog alert = builder.create();
            alert.show();



        });
    }

    public void spawnNotification(String text){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "fr.nytuo.theSithArchives";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(R.color.colorPrimary);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Commande")
                .setContentText(text)
                .setContentInfo("Info");

        notificationManager.notify(1, notificationBuilder.build());
    }

    @Override
    public void onPostExecutePokemons(List<PositionMagasin> itemList) {



        magasins = itemList;
        Spinner spinner = findViewById(R.id.spinner);


        for (PositionMagasin magasin : magasins) {
            System.out.println(magasin.getName());
        }
        adapter =new MagasinAdapter(this, magasins);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                PositionMagasin magasin = magasins.get(i);
                selectedMagasin = magasin.getName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                PositionMagasin magasin = magasins.get(0);
            }
        });
        spinner.setAdapter(adapter);

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
        // une fois que l'utilisateur a répondu à la demande de permission, on met à jour la liste si la permission est accordée
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

        for (PositionMagasin magasin : magasins) {
            System.out.println(magasin.getName() + " " + magasin.getDistance());
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
