package fr.nytuo.android411.gps;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import fr.nytuo.android411.HttpAsyncGet;
import fr.nytuo.android411.PostExecuteActivity;
import fr.nytuo.android411.R;


public class MagasinSlectionActibity extends AppCompatActivity implements PostExecuteActivity<PositionMagasin> {

    List<PositionMagasin> magasins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HttpAsyncGet<PositionMagasin> httpAsyncGet = new HttpAsyncGet<PositionMagasin>("https://api.nytuo.fr/api/libraries/positions", PositionMagasin.class, this, null);



    }

    @Override
    public void onPostExecutePokemons(List<PositionMagasin> itemList) {
        magasins = itemList;

        for (PositionMagasin magasin : magasins) {
            System.out.println(magasin.getName());
        }

        // todo add magazin to the vue


    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
