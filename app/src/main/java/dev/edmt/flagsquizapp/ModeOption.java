package dev.edmt.flagsquizapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ModeOption extends AppCompatActivity {
    Button btnPlayClassicMode;
    Button btnPlayRevertMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_mode);

        btnPlayClassicMode = (Button) findViewById(R.id.btnPlayClassicMode);
        btnPlayRevertMode = (Button) findViewById(R.id.btnPlayRevertMode);

        btnPlayClassicMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayingClassic.class);
                startActivity(intent);
                finish();
            }
        });

        btnPlayRevertMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayingRevert.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
