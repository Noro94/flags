package dev.art.flags;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

import dev.art.flags.DbHelper.DbHelper;

import static dev.art.flags.constants.Constants.EMPTY_SCORES;
import static dev.art.flags.constants.Constants.RATE_MSG;
import static dev.art.flags.Utils.Utils.showMessage;
import static dev.art.flags.Utils.Utils.getOpenFacebookIntent;

public class MainActivity extends AppCompatActivity {

    Button btnPlayFlags, btnPlayCapitals, btnScore, btnFacebook, btnConfigs;
    DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlayFlags = (Button) findViewById(R.id.btnPlayFlags);
        btnPlayCapitals = (Button) findViewById(R.id.btnPlayCapitals);
        btnScore = (Button) findViewById(R.id.btnScore);
        btnFacebook = (Button) findViewById(R.id.facebook);
        btnConfigs = (Button) findViewById(R.id.configs);

        db = new DbHelper(this);
        try {
            db.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnPlayFlags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ModeOption.class);
                startActivity(intent);
                finish();
            }
        });

        btnPlayCapitals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CapitalsModeOption.class);
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
    }
}
