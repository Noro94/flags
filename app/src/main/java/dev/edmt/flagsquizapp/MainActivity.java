package dev.edmt.flagsquizapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import dev.edmt.flagsquizapp.DbHelper.DbHelper;

import static dev.edmt.flagsquizapp.constants.Constants.EMPTY_SCORES;
import static dev.edmt.flagsquizapp.constants.Constants.RATE_MSG;
import static dev.edmt.flagsquizapp.Utils.Utils.showMessage;
import static dev.edmt.flagsquizapp.Utils.Utils.getOpenFacebookIntent;

public class MainActivity extends AppCompatActivity {

    Button btnPlay, btnScore, btnFacebook, btnConfigs, btnRateUs;
    DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnScore = (Button) findViewById(R.id.btnScore);
        btnFacebook = (Button) findViewById(R.id.facebook);
        btnConfigs = (Button) findViewById(R.id.configs);
        btnRateUs = (Button) findViewById(R.id.rateUs);

        db = new DbHelper(this);
        try {
            db.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ModeOption.class);
                startActivity(intent);
                finish();
            }
        });

        btnScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getRanking().size() > 0) {
                    Intent intent = new Intent(getApplicationContext(), Score.class);
                    startActivity(intent);
                    finish();
                } else {
                    showMessage(getApplicationContext(), EMPTY_SCORES);
                }
            }
        });

        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Uri uri = Uri.parse(FACEBOOK_URI); // missing 'http://' will cause crashed
                Intent intent = getOpenFacebookIntent(getApplicationContext());
                startActivity(intent);
            }
        });

        btnConfigs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Configurations.class);
                startActivity(intent);
                finish();
            }
        });

        btnRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMessage(getApplicationContext(), RATE_MSG);
            }
        });
    }
}
