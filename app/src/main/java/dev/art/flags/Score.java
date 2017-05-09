package dev.art.flags;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import dev.art.flags.Common.Common;
import dev.art.flags.Common.CustomAdapter;
import dev.art.flags.DbHelper.DbHelper;
import dev.art.flags.Model.Ranking;

import static dev.art.flags.Utils.Utils.showMessage;
import static dev.art.flags.constants.Constants.EMPTY_SCORES;

public class Score extends AppCompatActivity {
    ListView lstView;
    Button btnFlagScores, btnCapitalScores;
    LinearLayout flagBtnLayout, capitalBtnLayout;
    String modeOfRate = null;
    DbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        db = new DbHelper(this);
        try {
            db.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        btnFlagScores = (Button) findViewById(R.id.flagScores);
        btnCapitalScores = (Button) findViewById(R.id.capitalScores);
        flagBtnLayout = (LinearLayout) findViewById(R.id.ScoresLayoutFlag);
        capitalBtnLayout = (LinearLayout) findViewById(R.id.ScoresLayoutCapital);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            modeOfRate = extra.getString("PLAY_MODE_FOR_SCORES");
            changeBtnColorsByScoreMode(modeOfRate);
            setupLstView();
        }

        btnFlagScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getRanking(Common.RATE_MODE.F.toString()).size() > 0) {
                    flagBtnLayout.setBackgroundResource(R.color.colorClickedScoreBtn);
                    capitalBtnLayout.setBackgroundResource(R.color.colorUnClickedScoreBtn);
                    modeOfRate = Common.RATE_MODE.F.toString();
                    setupLstView();
                } else {
                    showMessage(getApplicationContext(), EMPTY_SCORES);
                }
            }
        });

        btnCapitalScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.getRanking(Common.RATE_MODE.C.toString()).size() > 0) {
                    capitalBtnLayout.setBackgroundResource(R.color.colorClickedScoreBtn);
                    flagBtnLayout.setBackgroundResource(R.color.colorUnClickedScoreBtn);
                    modeOfRate = Common.RATE_MODE.C.toString();
                    setupLstView();
                } else {
                    showMessage(getApplicationContext(), EMPTY_SCORES);
                }
            }
        });

    }

    private void setupLstView() {
        lstView = (ListView) findViewById(R.id.lstRanking);
        List<Ranking> lstRanking = db.getRanking(modeOfRate);
        if (lstRanking.size() > 0) {
            CustomAdapter adapter = new CustomAdapter(this, lstRanking);
            lstView.setAdapter(adapter);
        }
    }

    private void changeBtnColorsByScoreMode(String mode) {
        if (Objects.equals(mode, Common.RATE_MODE.F.toString())) {
            flagBtnLayout.setBackgroundResource(R.color.colorClickedScoreBtn);
            capitalBtnLayout.setBackgroundResource(R.color.colorUnClickedScoreBtn);
        } else {
            capitalBtnLayout.setBackgroundResource(R.color.colorClickedScoreBtn);
            flagBtnLayout.setBackgroundResource(R.color.colorUnClickedScoreBtn);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
