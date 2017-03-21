package dev.edmt.flagsquizapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dev.edmt.flagsquizapp.DbHelper.DbHelper;
import dev.edmt.flagsquizapp.Model.Question;

public class PlayingRevert extends AppCompatActivity implements View.OnClickListener {
    private Handler mHandler = new Handler();


    final static long DELAY_AFTER_ANSWER = 500; // 100 milli second
    final static long INTERVAL = 50; // 100 milli second
    final static long TIMEOUT = 10000; // 10 seconds
    int progressValue = 0;

    CountDownTimer mCountDown; // for progressbar
    List<Question> questionPlay = new ArrayList<>(); //total Question
    DbHelper db;
    int index = 0, score = 0, thisQuestion = 0, totalQuestion, correctAnswer;
    String mode = "";

    //Control
    ProgressBar progressBar;
    TextView countryName;
    Button flagA, flagB, flagC, flagD;
    String flagNameA, flagNameB, flagNameC, flagNameD;
    Button rightAnswerFlagReference;
    TextView txtScore, txtQuestion;
    LinearLayout clickedFlagLayout, rightAnswerFlagLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_revert);

        Bundle extra = getIntent().getExtras();
        if (extra != null)
            mode = extra.getString("MODE");

        db = new DbHelper(this);

        txtScore = (TextView) findViewById(R.id.txtScore);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        countryName = (TextView) findViewById(R.id.country_name);
        flagA = (Button) findViewById(R.id.flagAnswerA);
        flagB = (Button) findViewById(R.id.flagAnswerB);
        flagC = (Button) findViewById(R.id.flagAnswerC);
        flagD = (Button) findViewById(R.id.flagAnswerD);

        flagA.setOnClickListener(this);
        flagB.setOnClickListener(this);
        flagC.setOnClickListener(this);
        flagD.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        questionPlay = db.getQuestionMode(mode);
        totalQuestion = questionPlay.size();

        mCountDown = new CountDownTimer(TIMEOUT, INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(progressValue);
                progressValue++;
            }

            @Override
            public void onFinish() {
                mCountDown.cancel();

                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.end_time);
                mp.start();

                showQuestion(++index);
            }
        };
        showQuestion(index);
    }

    private void showQuestion(int index) {
        if (index < totalQuestion) {
            thisQuestion++;
            txtQuestion.setText(String.format("%d/%d", thisQuestion, totalQuestion));
            progressBar.setProgress(0);
            progressValue = 0;

            flagNameA = questionPlay.get(index).getAnswerA();
            flagNameB = questionPlay.get(index).getAnswerB();
            flagNameC = questionPlay.get(index).getAnswerC();
            flagNameD = questionPlay.get(index).getAnswerD();

            countryName.setText(questionPlay.get(index).getImage().replace("_", " "));

            flagA.setBackgroundResource(this.getResources().getIdentifier(flagNameA.toLowerCase(), "drawable", getPackageName()));
            flagB.setBackgroundResource(this.getResources().getIdentifier(flagNameB.toLowerCase(), "drawable", getPackageName()));
            flagC.setBackgroundResource(this.getResources().getIdentifier(flagNameC.toLowerCase(), "drawable", getPackageName()));
            flagD.setBackgroundResource(this.getResources().getIdentifier(flagNameD.toLowerCase(), "drawable", getPackageName()));

            setRightAnswerButtonReference(questionPlay.get(index));

            mCountDown.start();
        } else {
            Intent intent = new Intent(this, Done.class);
            Bundle dataSend = new Bundle();
            dataSend.putInt("SCORE", score);
            dataSend.putInt("TOTAL", totalQuestion);
            dataSend.putInt("CORRECT", correctAnswer);
            intent.putExtras(dataSend);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View v) {

        mCountDown.cancel();
        if (index < totalQuestion) {
            final Button clickedButton = (Button) v;

            if (getCountryNameByFlag(clickedButton).equals(questionPlay.get(index).getCorrectAnswer())) {  //right answer case

                clickedFlagLayout.setBackgroundResource(R.color.colorRight);

                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.right_answer);
                mp.start();

                score += 10; // increase score
                correctAnswer++; //increase correct answer

                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        clickedFlagLayout.setBackgroundResource(R.color.colorBackgroundDefault);
                        showQuestion(++index);
                    }
                }, DELAY_AFTER_ANSWER);

            } else {  //wrong answer case

                clickedFlagLayout.setBackgroundResource(R.color.colorWrong);
                rightAnswerFlagLayout.setBackgroundResource(R.color.colorRight);

                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.wrong_answer);
                mp.start();

                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        clickedFlagLayout.setBackgroundResource(R.color.colorBackgroundDefault);
                        rightAnswerFlagLayout.setBackgroundResource(R.color.colorBackgroundDefault);

                        showQuestion(++index);
                    }
                }, DELAY_AFTER_ANSWER);
            }

            txtScore.setText(String.format("%d", score));
        }

    }

    private void setRightAnswerButtonReference(Question questionPlay) {
        if (questionPlay.getCorrectAnswer().equals(questionPlay.getAnswerA())) {
            rightAnswerFlagReference = flagA;
            rightAnswerFlagLayout = (LinearLayout) findViewById(R.id.flagLayoutA);
        } else if (questionPlay.getCorrectAnswer().equals(questionPlay.getAnswerB())) {
            rightAnswerFlagReference = flagB;
            rightAnswerFlagLayout = (LinearLayout) findViewById(R.id.flagLayoutB);
        } else if (questionPlay.getCorrectAnswer().equals(questionPlay.getAnswerC())) {
            rightAnswerFlagReference = flagC;
            rightAnswerFlagLayout = (LinearLayout) findViewById(R.id.flagLayoutC);
        } else {
            rightAnswerFlagReference = flagD;
            rightAnswerFlagLayout = (LinearLayout) findViewById(R.id.flagLayoutD);
        }
    }

    private String getCountryNameByFlag(Button clickedFlag) {
        if (clickedFlag == flagA) {
            clickedFlagLayout = (LinearLayout) findViewById(R.id.flagLayoutA);
            return flagNameA;
        } else if (clickedFlag == flagB) {
            clickedFlagLayout = (LinearLayout) findViewById(R.id.flagLayoutB);
            return  flagNameB;
        } else if (clickedFlag == flagC) {
            clickedFlagLayout = (LinearLayout) findViewById(R.id.flagLayoutC);
            return flagNameC;
        } else {
            clickedFlagLayout = (LinearLayout) findViewById(R.id.flagLayoutD);
            return flagNameD;
        }
    }
}
