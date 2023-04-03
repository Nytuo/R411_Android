package fr.nytuo.theSithArchives.gps;


import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import fr.nytuo.theSithArchives.HttpAsyncGet;
import fr.nytuo.theSithArchives.ItemListDialogFragment;
import fr.nytuo.theSithArchives.PostExecuteActivity;
import fr.nytuo.theSithArchives.R;
import fr.nytuo.theSithArchives.cart.CartActivity;
import fr.nytuo.theSithArchives.productsList.ProductsListActivity;


public class MagasinSelectionActivity extends AppCompatActivity implements PostExecuteActivity<PositionMagasin>, MagasinAdapterListener {

    List<PositionMagasin> magasins;
    public static int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;

    MagasinAdapter adapter;

    PositionMagasin selectedMagasin;

    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magasin_list);
        new HttpAsyncGet<>("https://api.nytuo.fr/api/libraries/positions/20", PositionMagasin.class, this, null);
        int commandNumber = (int) (Math.random() * 1000000);
        Button button5 = findViewById(R.id.button5);



        button5.setOnClickListener(v -> {
            spawnNotification("Vos articles sont réservés dans votre librairie '" + selectedMagasin.getName()
                    + "'. Vous recevrez une notification lors de leur disponibilité. Votre commande porte le numéro: " + commandNumber, System.currentTimeMillis(),1);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmation de commande");
            builder.setMessage("Vos articles sont réservés dans votre librairie '" + selectedMagasin.getName()
                    + "'. Vous recevrez une notification lors de leur disponibilité. Votre commande porte le numéro: " + commandNumber);
            builder.setPositiveButton("OK", (dialog, which) -> {
                spawnNotification("Vos articles sont disponibles dans votre librairie '" + selectedMagasin.getName()
                        + "'. Votre commande porte le numéro: " + commandNumber, System.currentTimeMillis() + 10000,2);
                Intent intent = new Intent(this, ProductsListActivity.class);
                startActivity(intent);


            });
            AlertDialog alert = builder.create();
            alert.show();
        });

        Button button6 = findViewById(R.id.button6);
        button6.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });
        Button button7 = findViewById(R.id.button7);
        button7.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProductsListActivity.class);
            startActivity(intent);
        });
        mapView = findViewById(R.id.mapView3);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

    }

    public void spawnNotification(String text, long time,int id) {
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
                .setWhen(time)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Commande")
                .setContentText(text)
                .setContentInfo("Info")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text));

        notificationManager.notify(id, notificationBuilder.build());
    }

    @Override
    public void onPostExecute(List<PositionMagasin> itemList) {


        magasins = itemList;
        selectedMagasin = magasins.get(0);
        mapView.getMapAsync(googleMap -> googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(selectedMagasin.getLatitude(), selectedMagasin.getLongitude()), 15)));
        TextView textView4 = findViewById(R.id.textView4);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            Log.d("GPS", "fails");
            return;
        }
        updateDistanceOnList();

        textView4.setText("Boutique actuelle: " + magasins.get(0).getName() + " (" + magasins.get(0).getDistance()/1000 + "km)");
                Button spinner = findViewById(R.id.button2);
        mapView.getMapAsync(
                googleMap -> {
                    for (PositionMagasin magasin1 : magasins) {
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(magasin1.getLatitude(), magasin1.getLongitude())).title(magasin1.getName()));
                        googleMap.setOnMarkerClickListener(marker -> {
                            for (PositionMagasin magasin : magasins) {
                                if (magasin.getName().equals(marker.getTitle())) {
                                    selectedMagasin = magasin;
                                    Toast.makeText(this, "Vous avez sélectionné la librairie " + magasin.getName(), Toast.LENGTH_SHORT).show();
                                    textView4.setText("Boutique actuelle: " + magasin.getName() + " (" + magasin.getDistance()/1000 + "km)");
                                    return true;
                                }
                            }
                            return false;
                        });
                    }
                }
        );


        spinner.setOnClickListener(v -> showItemListDialogAndWait());
    }

    public void showItemListDialogAndWait() {
        ItemListDialogFragment dialogFragment = ItemListDialogFragment.newInstance(magasins.size(),magasins);
        dialogFragment.show(getSupportFragmentManager(), "dialog");
        final Object lock = new Object();
        dialogFragment.setOnItemSelectedListener(position -> {
            System.out.println("Selected position: " + position);
            synchronized (lock) {
                lock.notify();
            }
        });

        new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            runOnUiThread(() -> {
                System.out.println("Selected position: " + getSelectedPosition());
                itemSelected(getSelectedPosition());
            });
        }).start();
    }
    private int getSelectedPosition() {
        ItemListDialogFragment dialogFragment = (ItemListDialogFragment) getSupportFragmentManager()
                .findFragmentByTag("dialog");
        if (dialogFragment != null && dialogFragment.isAdded()) {
            return dialogFragment.getSelectedItem();
        }
        return -1;
    }
    private void itemSelected(int i){
        PositionMagasin magasin = magasins.get(i);
        TextView textView4 = findViewById(R.id.textView4);
        textView4.setText("Boutique actuelle: " + magasin.getName() + " (" + magasin.getDistance()/1000 + "km)");
        selectedMagasin = magasin;
        if (mapView != null) {
            mapView.getMapAsync(googleMap -> googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(selectedMagasin.getLatitude(), selectedMagasin.getLongitude()), 15)));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
        try {
        adapter.notifyDataSetChanged();
        }catch (NullPointerException e){
            Log.d("GPS", "fails");
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
