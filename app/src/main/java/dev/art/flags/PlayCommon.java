package dev.art.flags;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import dev.art.flags.Common.Common;
import dev.art.flags.DbHelper.DbHelper;
import dev.art.flags.Model.CapitalsQuestion;
import dev.art.flags.Model.Question;

import static dev.art.flags.Utils.Utils.answeredRight;
import static dev.art.flags.Utils.Utils.answeredWrong;
import static dev.art.flags.Utils.Utils.getSecondsByMode;
import static dev.art.flags.Utils.Utils.manipulateButtons;
import static dev.art.flags.Utils.Utils.playSoundTimeout;
import static dev.art.flags.Utils.Utils.setDefaultColor;
import static dev.art.flags.Utils.Utils.showCorrectAnswer;

public abstract class PlayCommon extends AppCompatActivity {
    String playType = null;

    protected Handler mHandler = new Handler();

    final long DELAY_AFTER_ANSWER = 1000; // 1 second
    final long DELAY_NO_ANSWER = 2000; // 2 second
    long interval = 50; // 50 milli second
    long timeout = 10000; // 10 seconds
    int progressValue = 0;
    int rightAnswerScore;
    int rightAnswerCount = 0;

    boolean finishQuizCycle = false;

    CountDownTimer mCountDown; // for progressbar
    List<Question> questionPlay = new ArrayList<>(); //total Question
    List<CapitalsQuestion> capitalsQuestionPlay = new ArrayList<>();
    Map<String,String> configs = new HashMap<>();
    DbHelper db;
    int index = 0, score = 0, thisQuestion = 0, totalQuestion, correctAnswer, buttonDefaultColor;
    String mode = Common.MODE.EASY.toString();
    String speed = Common.SPEED.SLOW.toString();
    String activeClassName = "";
    String whereStatement = "";

    ProgressBar progressBar;
    TextView txtScore, txtQuestion;
    Button rightAnswerReference;
    View rightAnswerLayout;

    @Override
    protected void onResume() {
        super.onResume();

        if (Objects.equals(playType, Common.PLAY.FLAGS.toString())) {
            questionPlay = db.getQuestionMode(mode);
            totalQuestion = questionPlay.size();
        } else if (Objects.equals(playType, Common.PLAY.CAPITALS.toString())) {
            capitalsQuestionPlay = db.getCapitalsQuestionMode(mode, whereStatement);
            totalQuestion = capitalsQuestionPlay.size();
        } else {
            finish();
        }

        mCountDown = new CountDownTimer(timeout, interval) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.setProgress(progressValue);
                progressValue++;
            }

            @Override
            public void onFinish() {
                mCountDown.cancel();
                if (!finishQuizCycle) {
                    playSoundTimeout(getApplicationContext());
                    showCorrectAnswer(rightAnswerLayout);
                    mHandler.postDelayed(new Runnable() {
                        public void run() {
                            manipulateButtons(getButtonsList(), true);
                            setDefaultColor(rightAnswerLayout, buttonDefaultColor);
                            showQuestion(++index);
                        }
                    }, DELAY_NO_ANSWER);
                } else {
                    finish();
                }
            }
        };
        showQuestion(index);
    }

    @Override
    protected void onStop() {
        super.onStop();
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.RunningTaskInfo foregroundTaskInfo = am.getRunningTasks(1).get(0);
        if(!foregroundTaskInfo.topActivity.getPackageName().equals(this.getPackageName()))
        {
            finishThisQuiz();
        }
    }

    @Override
    public void onBackPressed() {
        if (Objects.equals(playType, Common.PLAY.FLAGS.toString())) {
            Intent intent = new Intent(getApplicationContext(), ModeOption.class);
            startActivity(intent);
            finishThisQuiz();
        } else if (Objects.equals(playType, Common.PLAY.CAPITALS.toString())) {
            Intent intent = new Intent(getApplicationContext(), CapitalsModeOption.class);
            startActivity(intent);
            finishThisQuiz();
        } else {
            finish();
        }
    }

    protected void rightAnswer(final View answer) {
        answeredRight(getApplicationContext(), answer);

        score += rightAnswerScore;
        correctAnswer++;

        mHandler.postDelayed(new Runnable() {
            public void run() {
                manipulateButtons(getButtonsList(), true);
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
                manipulateButtons(getButtonsList(), true);
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
        dataSend.putString("ACTIVE", activeClassName);
        intent.putExtras(dataSend);
        startActivity(intent);
        finish();
    }

    protected void setTimeoutAndInterval(String speed) {
        int seconds = getSecondsByMode(speed);
        timeout = seconds * 1000;  //convert to milli seconds
        interval = timeout/200;
    }

    protected void finishThisQuiz() {
        finishQuizCycle = true;
        mCountDown.onFinish();
    }

    protected abstract void showQuestion(int index);

    protected abstract List<Button> getButtonsList();
}
