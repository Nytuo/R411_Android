package fr.nytuo.theSithArchives.clickandcollect;


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
import java.util.Random;

import fr.nytuo.theSithArchives.R;
import fr.nytuo.theSithArchives.cart.CartActivity;
import fr.nytuo.theSithArchives.networking.HttpAsyncGet;
import fr.nytuo.theSithArchives.networking.HttpAsyncPost;
import fr.nytuo.theSithArchives.networking.PostExecuteActivity;
import fr.nytuo.theSithArchives.networking.PostExecutePost;
import fr.nytuo.theSithArchives.productslist.ProductsListActivity;


/**
 * Activité de sélection de la librairie
 */
public class MagasinSelectionActivity extends AppCompatActivity implements PostExecuteActivity<Magasin>, PostExecutePost {

    public static int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    /**
     * Liste des magasins
     */
    private List<Magasin> magasinList;
    /**
     * Magasin sélectionné
     */
    private Magasin selectedMagasin;

    /**
     * MapView (Google Maps)
     */
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magasin_list);

        //On récupère la liste des magasins depuis l'API
        new HttpAsyncGet<>("https://api.nytuo.fr/api/libraries/positions/20", Magasin.class, this, null);

        //On génère un nombre aléatoire pour le numéro de commande
        int commandNumber = new Random().nextInt(10000000);

        //Gestion des elements de l'interface
        Button buttonBuy = findViewById(R.id.buttonBuy);
        Button buttonPanier = findViewById(R.id.buttonPanier_magasin);
        Button buttonHome = findViewById(R.id.buttonHome_magasin);

        Button buttonContact = findViewById(R.id.buttonContact2);

        buttonContact.setOnClickListener(v -> {
            Intent intent = new Intent(this, RetreiveCommandActivity.class);
            startActivity(intent);
        });

        mapView = findViewById(R.id.map);


        buttonBuy.setOnClickListener(v -> {
            spawnNotification("Vos articles sont réservés dans votre librairie '" + selectedMagasin.getName()
                    + "'. Vous recevrez une notification lors de leur disponibilité. Votre commande porte le numéro: " + commandNumber, System.currentTimeMillis(), 1);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmation de commande");
            builder.setMessage("Vos articles sont réservés dans votre librairie '" + selectedMagasin.getName()
                    + "'. Vous recevrez une notification lors de leur disponibilité. Votre commande porte le numéro: " + commandNumber);
            builder.setPositiveButton("OK", (dialog, which) -> {
                spawnNotification("Vos articles sont disponibles dans votre librairie '" + selectedMagasin.getName()
                        + "'. Votre commande porte le numéro: " + commandNumber, System.currentTimeMillis() + 10000, 2);

                Commande commande = new Commande();
                commande.setCommandNumber("test");
                commande.setPrice("54");
                commande.setBooks("test");
                new HttpAsyncPost("https:////api.nytuo.fr/api/command", commande, this);

                Intent intent = new Intent(this, ProductsListActivity.class);
                startActivity(intent);

            });
            AlertDialog alert = builder.create();
            alert.show();
        });

        buttonPanier.setOnClickListener(v -> {
            Intent intent = new Intent(this, CartActivity.class);
            startActivity(intent);
        });
        buttonHome.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProductsListActivity.class);
            startActivity(intent);
        });

        //Gestion de la map
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
    }

    /**
     * Génère une notification
     *
     * @param text Texte de la notification
     * @param time Temps de la notification
     * @param id   ID de la notification
     */
    private void spawnNotification(String text, long time, int id) {
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
    public void onPostExecute(List<Magasin> itemList) {

        magasinList = itemList;
        selectedMagasin = magasinList.get(0); // On prend le premier magasin de la liste par défaut (le plus proche)
        //On bouge la caméra sur la position du magasin sélectionné

        //On demande la permission d'accéder à la position de l'utilisateur
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
            Log.d("GPS", "fails");
            return;
        }
        //On met à jour la distance entre l'utilisateur et les magasins et on modifie le texte de la boutique actuelle
        updateDistanceOnList();


        Button buttonOpenSelectStore = findViewById(R.id.openSelectStore);
        //On définit l'action à effectuer lorsque l'utilisateur clique sur le bouton pour ouvrir la liste des magasins (le fragment)
        buttonOpenSelectStore.setOnClickListener(v -> showItemListDialogAndWait());
    }

    @Override
    public void onError() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("le serveur n'est pas joignable")
                .setTitle("Erreur");
        builder.setPositiveButton("OK", (dialog, id) -> {
            Intent intent = getIntent();
            startActivity(intent);
            finish();
        });
        android.app.AlertDialog dialog = builder.create();
        dialog.show();

    }

    /**
     * Affiche la liste des magasins dans un fragment et attend que l'utilisateur en sélectionne un
     */
    private void showItemListDialogAndWait() {
        //On génère le fragment
        ItemListDialogFragment dialogFragment = ItemListDialogFragment.newInstance(magasinList.size(), magasinList);
        //On affiche le fragment
        dialogFragment.show(getSupportFragmentManager(), "storeList");

        //On attend que l'utilisateur sélectionne un item
        final Object lock = new Object();
        //On définit l'action à effectuer lorsque l'utilisateur sélectionne un item
        dialogFragment.setOnItemSelectedListener(position -> {
            //On débloque le thread en attente
            synchronized (lock) {
                lock.notifyAll();
            }
        });

        new Thread(() -> {
            //On attend que l'utilisateur sélectionne un item
            synchronized (lock) {
                while (getSelectedPosition() == -1) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            runOnUiThread(() -> itemSelected(getSelectedPosition()));
        }).start();
    }

    /**
     * Récupère la position de l'item sélectionné par l'utilisateur depuis le fragment
     *
     * @return la position de l'item sélectionné par l'utilisateur
     */
    private int getSelectedPosition() {
        ItemListDialogFragment dialogFragment = (ItemListDialogFragment) getSupportFragmentManager()
                .findFragmentByTag("storeList");
        if (dialogFragment != null && dialogFragment.isAdded()) {
            return dialogFragment.getSelectedItem();
        }
        return -1;
    }

    /**
     * Met à jour le magasin sélectionné par la selection de l'utiisateur et met à jour la map
     *
     * @param i position de l'item dans la liste
     */
    private void itemSelected(int i) {
        Magasin magasin = magasinList.get(i);
        TextView selectedStoreTextView = findViewById(R.id.selectedStoreTextView);
        selectedStoreTextView.setText(getResources().getString(R.string.ActualStore) + magasin.getName() + " (" + magasin.getDistance() / 1000 + "km)");
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

    private void updateDistanceOnList() {
        getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        double lat;
        double lon;

        //On récupère la position de l'utilisateur
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

        //Impossible de récupérer la position
        if (lat == -1.0 && lon == -1.0) {
            Toast.makeText(getApplicationContext(), "Impossible de récupérer votre position", Toast.LENGTH_SHORT).show();
            return;
        }

        //On met à jour la distance de chaque magasin
        for (Magasin magasin : magasinList) {
            Location locationMagasin = new Location("magasin");
            locationMagasin.setLatitude(magasin.getLatitude());
            locationMagasin.setLongitude(magasin.getLongitude());
            magasin.setDistance(location.distanceTo(locationMagasin));
        }
        onUpdatedPosition();
    }

    /**
     * Définit les actions à effectuer lorsque la position de l'utilisateur et celle des magasins sont mises à jour
     */
    private void onUpdatedPosition() {
        mapView.getMapAsync(googleMap -> googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(selectedMagasin.getLatitude(), selectedMagasin.getLongitude()), 15)));
        TextView selectStoreTextView = findViewById(R.id.selectedStoreTextView);
        selectStoreTextView.setText(getResources().getString(R.string.ActualStore) + magasinList.get(0).getName() + " (" + magasinList.get(0).getDistance() / 1000 + "km)");
        mapView.getMapAsync(
                googleMap -> {
                    for (Magasin magasin1 : magasinList) {
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(magasin1.getLatitude(), magasin1.getLongitude())).title(magasin1.getName()));
                        googleMap.setOnMarkerClickListener(marker -> {
                            for (Magasin magasin : magasinList) {
                                if (magasin.getName().equals(marker.getTitle())) {
                                    selectedMagasin = magasin;
                                    Toast.makeText(this, "Vous avez sélectionné la librairie " + magasin.getName(), Toast.LENGTH_SHORT).show();
                                    selectStoreTextView.setText(getResources().getString(R.string.ActualStore) + magasin.getName() + " (" + magasin.getDistance() / 1000 + "km)");
                                    return true;
                                }
                            }
                            return false;
                        });
                    }
                }
        );
        Button buttonOpenSelectStore = findViewById(R.id.openSelectStore);
        buttonOpenSelectStore.setOnClickListener(v -> showItemListDialogAndWait());
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onSucces() {
        Log.i("MainActivity", "Données récupérées avec succès");
    }

    @Override
    public void onError(int responseCode) {
        Log.e("MainActivity", "Erreur lors de la récupération des données");
    }
}
