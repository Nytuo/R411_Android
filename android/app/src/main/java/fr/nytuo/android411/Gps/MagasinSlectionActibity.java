package fr.nytuo.android411.Gps;


import android.os.Bundle;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import fr.nytuo.android411.HttpAsyncGet;
import fr.nytuo.android411.PostExecuteActivity;
import fr.nytuo.android411.R;


public class MagasinSlectionActibity extends AppCompatActivity implements PostExecuteActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpAsyncGet<PositionMagasin> httpAsyncGet = new HttpAsyncGet<PositionMagasin>("https://api.jsonserve.com/ZjoC6h", PositionMagasin.class, this, null);



    }

    @Override
    public void onPostExecutePokemons(List itemList) {
        PositionMagasin[] magasins = (PositionMagasin[]) itemList.toArray();


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
