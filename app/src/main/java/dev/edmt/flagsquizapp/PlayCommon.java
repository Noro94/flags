package dev.edmt.flagsquizapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.edmt.flagsquizapp.DbHelper.DbHelper;
import dev.edmt.flagsquizapp.Model.Question;

import static dev.edmt.flagsquizapp.Utils.Utils.*;

public abstract class PlayCommon extends AppCompatActivity {
    protected Handler mHandler = new Handler();

    final static long DELAY_AFTER_ANSWER = 500; // 500 milli second
    final static long INTERVAL = 50; // 50 milli second
    final static long TIMEOUT = 10000; // 10 seconds
    int progressValue = 0;

    CountDownTimer mCountDown; // for progressbar
    List<Question> questionPlay = new ArrayList<>(); //total Question
    DbHelper db;
    int index = 0, score = 0, thisQuestion = 0, totalQuestion, correctAnswer, buttonDefaultColor;
    String mode = "";

    ProgressBar progressBar;
    TextView txtScore, txtQuestion;
    Button rightAnswerReference;

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
                playSoundTimeout(getApplicationContext());
                showQuestion(++index);
            }
        };
        showQuestion(index);
    }

    protected void rightAnswer(final View answer) {
        answeredRight(getApplicationContext(), answer);

        score += 10;
        correctAnswer++;

        mHandler.postDelayed(new Runnable() {
            public void run() {
                setDefaultColor(answer, buttonDefaultColor);
                showQuestion(++index);
            }
        }, DELAY_AFTER_ANSWER);
    }

    protected void wrongAnswer(final Map answers) {
        final View clickedAnswer = (View) answers.get("clicked");
        final View rightAnswer = (View) answers.get("right");

        answeredWrong(getApplicationContext(), clickedAnswer, rightAnswer);

        mHandler.postDelayed(new Runnable() {
            public void run() {
                setDefaultColor(answers, buttonDefaultColor);
                showQuestion(++index);
            }
        }, DELAY_AFTER_ANSWER);
    }

    protected void finishQuiz() {
        Intent intent = new Intent(this, Done.class);
        Bundle dataSend = new Bundle();
        dataSend.putInt("SCORE", score);
        dataSend.putInt("TOTAL", totalQuestion);
        dataSend.putInt("CORRECT", correctAnswer);
        intent.putExtras(dataSend);
        startActivity(intent);
        finish();
    }

    protected abstract void showQuestion(int index);
}
