package dev.edmt.flagsquizapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ModeOption extends AppCompatActivity {
    Button btnPlayClassicMode;
    Button btnPlayRevertMode;
    String mode = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_mode);

        btnPlayClassicMode = (Button) findViewById(R.id.btnPlayClassicMode);
        btnPlayRevertMode = (Button) findViewById(R.id.btnPlayRevertMode);

        //Get Data from MainActivity
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            mode = extra.getString("MODE");
        }


        btnPlayClassicMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayingClassic.class);
                intent.putExtra("MODE", mode); // Send Mode to PlayingClassic page
                startActivity(intent);
                finish();
            }
        });

        btnPlayRevertMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayingRevert.class);
                intent.putExtra("MODE", mode); // Send Mode to PlayingClassic page
                startActivity(intent);
                finish();
            }
        });
    }
}
