package fr.nytuo.theSithArchives;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import fr.nytuo.theSithArchives.productsList.ProductsListActivity;

public class SplashActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            playMusic();
            startActivity(new Intent(SplashActivity.this, ProductsListActivity.class).putExtra("mediaPlayer", mediaPlayer.getCurrentPosition()));
            finish();
        }, 1000);

    }
    private void playMusic(){
        if (mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(this, R.raw.maintheme);
            mediaPlayer.start();
        }
    }
}
