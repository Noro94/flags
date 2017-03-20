package dev.edmt.flagsquizapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import dev.edmt.flagsquizapp.DbHelper.DbHelper;
import dev.edmt.flagsquizapp.Model.Question;

public class PlayingClassic extends AppCompatActivity implements View.OnClickListener {

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
    ImageView imageView;
    Button btnA, btnB, btnC, btnD;
    Button rightAnswerButtonReference;
    TextView txtScore, txtQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        Bundle extra = getIntent().getExtras();
        if (extra != null)
            mode = extra.getString("MODE");

        db = new DbHelper(this);

        txtScore = (TextView) findViewById(R.id.txtScore);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imageView = (ImageView) findViewById(R.id.question_flag);
        btnA = (Button) findViewById(R.id.btnAnswerA);
        btnB = (Button) findViewById(R.id.btnAnswerB);
        btnC = (Button) findViewById(R.id.btnAnswerC);
        btnD = (Button) findViewById(R.id.btnAnswerD);

        btnA.setOnClickListener(this);
        btnB.setOnClickListener(this);
        btnC.setOnClickListener(this);
        btnD.setOnClickListener(this);

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

            int ImageId = this.getResources().getIdentifier(questionPlay.get(index).getImage().toLowerCase(), "drawable", getPackageName());
            imageView.setBackgroundResource(ImageId);
            btnA.setText(questionPlay.get(index).getAnswerA());
            btnB.setText(questionPlay.get(index).getAnswerB());
            btnC.setText(questionPlay.get(index).getAnswerC());
            btnD.setText(questionPlay.get(index).getAnswerD());

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
            if (clickedButton.getText().equals(questionPlay.get(index).getCorrectAnswer())) {  //right answer case
                clickedButton.setBackgroundResource(R.color.colorRight);
                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.right_answer);
                mp.start();

                score += 10; // increase score
                correctAnswer++; //increase correct answer

                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        clickedButton.setBackgroundResource(R.color.colorButtonDefault);
                        showQuestion(++index);
                    }
                }, DELAY_AFTER_ANSWER);

            } else {  //wrong answer case
                clickedButton.setBackgroundResource(R.color.colorWrong);
                rightAnswerButtonReference.setBackgroundResource(R.color.colorRight);

                MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.wrong_answer);
                mp.start();

                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        clickedButton.setBackgroundResource(R.color.colorButtonDefault);
                        rightAnswerButtonReference.setBackgroundResource(R.color.colorButtonDefault);
                        showQuestion(++index);
                    }
                }, DELAY_AFTER_ANSWER);
            }

            txtScore.setText(String.format("%d", score));
        }

    }

    private void setRightAnswerButtonReference(Question questionPlay) {
        if (questionPlay.getCorrectAnswer().equals(questionPlay.getAnswerA())) {
            rightAnswerButtonReference = btnA;
        } else if (questionPlay.getCorrectAnswer().equals(questionPlay.getAnswerB())) {
            rightAnswerButtonReference = btnB;
        } else if (questionPlay.getCorrectAnswer().equals(questionPlay.getAnswerC())) {
            rightAnswerButtonReference = btnC;
        } else {
            rightAnswerButtonReference = btnD;
        }
    }
}
