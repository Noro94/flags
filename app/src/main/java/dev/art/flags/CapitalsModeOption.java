package dev.art.flags;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import dev.art.flags.Common.Common;

public class CapitalsModeOption extends AppCompatActivity {
    Button btnEU, btnAS, btnNA, btnSA, btnAF, btnOC, btnAN, btnAll;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capitals_play_mode);

        btnEU = (Button) findViewById(R.id.EU_europe);
        btnAS = (Button) findViewById(R.id.AS_asia);
        btnNA = (Button) findViewById(R.id.NA_north_america);
        btnSA = (Button) findViewById(R.id.SA_south_america);
        btnAF = (Button) findViewById(R.id.AF_africa);
        btnOC = (Button) findViewById(R.id.OC_oceania);
        btnAN = (Button) findViewById(R.id.AN_antarctica);
        btnAll = (Button) findViewById(R.id.OO_all);


        btnEU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayingCapitals.class);
                intent.putExtra("CONTINENT", Common.CONTINENTS.EU.toString());
                startActivity(intent);
                finish();
            }
        });

        btnAS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayingCapitals.class);
                intent.putExtra("CONTINENT", Common.CONTINENTS.AS.toString());
                startActivity(intent);
                finish();
            }
        });

        btnNA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayingCapitals.class);
                intent.putExtra("CONTINENT", Common.CONTINENTS.NA.toString());
                startActivity(intent);
                finish();
            }
        });

        btnSA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayingCapitals.class);
                intent.putExtra("CONTINENT", Common.CONTINENTS.SA.toString());
                startActivity(intent);
                finish();
            }
        });

        btnAF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayingCapitals.class);
                intent.putExtra("CONTINENT", Common.CONTINENTS.AF.toString());
                startActivity(intent);
                finish();
            }
        });

        btnOC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayingCapitals.class);
                intent.putExtra("CONTINENT", Common.CONTINENTS.OC.toString());
                startActivity(intent);
                finish();
            }
        });

        btnAN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayingCapitals.class);
                intent.putExtra("CONTINENT", Common.CONTINENTS.AN.toString());
                startActivity(intent);
                finish();
            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayingCapitals.class);
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

