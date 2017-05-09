package dev.art.flags;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static dev.art.flags.Utils.Utils.getActiveClass;

import dev.art.flags.Common.Common;
import dev.art.flags.DbHelper.DbHelper;
import dev.art.flags.Model.Ranking;
import dev.art.flags.constants.Constants;

public class Done extends AppCompatActivity {

    Button btnTryAgain, btnMainMenu;
    TextView txtResultScore, txtResultQuestion;
    ProgressBar progressBarResult;
    ImageView medalImageView;
    String previousActivity = "";
    String modeOfRate = "";
    DbHelper db;

    List scores = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done);

        db = new DbHelper(this);


        txtResultScore = (TextView) findViewById(R.id.txtTotalScore);
        txtResultQuestion = (TextView) findViewById(R.id.txtTotalQuestion);
        progressBarResult = (ProgressBar) findViewById(R.id.doneProgressBar);
        btnTryAgain = (Button) findViewById(R.id.btnTryAgain);
        btnMainMenu = (Button) findViewById(R.id.btnMainMenu);
        medalImageView = (ImageView) findViewById(R.id.medal);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            int score;
            if (extra.getInt("SCORE") == 0) {
                score = 1;
            } else {
                score = extra.getInt("SCORE");
            }
            int totalQuestion = extra.getInt("TOTAL");
            int correctAnswer = extra.getInt("CORRECT");
            modeOfRate = extra.getString("RATE_MODE");
            previousActivity = extra.getString("ACTIVE");

            //Update 2.0
            int playCount = 0;
            if(totalQuestion == Constants.EASY_MODE_NUM) // EASY MODE
            {
                playCount = updatePlayCount(0, modeOfRate);
            }
            else if(totalQuestion == Constants.MEDIUM_MODE_NUM) // MEDIUM MODE
            {
                playCount = updatePlayCount(1, modeOfRate);
            }
            else if(totalQuestion == Constants.HARD_MODE_NUM) // HARD MODE
            {
                playCount = updatePlayCount(2, modeOfRate);
            }
            else if(totalQuestion == Constants.HARDEST_MODE_NUM) // HARDEST MODE
            {
                playCount = updatePlayCount(3, modeOfRate);
            }

            double subtract = ((5.0/(float)score)*100)*(playCount-1); //-1 because we playCount++ before we calculate result
            double finalScore = score - subtract;

            txtResultScore.setText(String.format("SCORE : %.1f (-%d)%%", finalScore,5*(playCount-1)));
            txtResultQuestion.setText(String.format("PASSED : %d/%d", correctAnswer, totalQuestion));

            progressBarResult.setMax(totalQuestion);
            progressBarResult.setProgress(correctAnswer);

            //save score
            db.insertScore(finalScore, modeOfRate);
            List<Ranking> lstRanking = db.getRanking(modeOfRate);

            for (Ranking rank : lstRanking) {
                scores.add(rank.getScore());
            }

            int position = scores.indexOf(finalScore);

            if(position == 0 )// top1
                medalImageView.setImageResource(R.drawable.top1);
            else if(position == 1) // top 2
                medalImageView.setImageResource(R.drawable.top2);
            else
                medalImageView.setImageResource(R.drawable.top3);
        }

        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), getActiveClass(previousActivity));
                startActivity(intent);
                finish();
            }
        });

        btnMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private int updatePlayCount(int level, String modeOfRate) {
        int playCount = db.getPlayCount(level, modeOfRate);
        playCount++;

        if (Objects.equals(modeOfRate, Common.RATE_MODE.F.toString())) {
            db.updatePlayCount(level, playCount); // Set PlayCount ++
        } else {
            db.updatePlayCountCapitals(level, playCount); // Set PlayCount ++
        }

        return playCount;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), getModeOptionClassByMode(modeOfRate));
        startActivity(intent);
        finish();
    }

    public static Class getModeOptionClassByMode(String mode) {
        if (Objects.equals(mode, Common.RATE_MODE.F.toString())) {
            return ModeOption.class;
        } else {
            return CapitalsModeOption.class;
        }
    }
}
