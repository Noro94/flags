package dev.edmt.flagsquizapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.edmt.flagsquizapp.DbHelper.DbHelper;
import dev.edmt.flagsquizapp.Model.Question;

import static dev.edmt.flagsquizapp.Utils.Utils.SpeedToScore;
import static dev.edmt.flagsquizapp.Utils.Utils.manipulateButtons;
import static dev.edmt.flagsquizapp.constants.Constants.ACTIVE_REVERT;

public class PlayingRevert extends PlayCommon implements View.OnClickListener {

    //Control
    TextView countryName;
    Button flagA, flagB, flagC, flagD;
    String flagNameA, flagNameB, flagNameC, flagNameD;
    LinearLayout clickedFlagLayout, rightAnswerFlagLayout;
    List<Button> buttonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_revert);

        db = new DbHelper(this);
        configs = db.getConfigs();
        mode = configs.get("CountMode");
        speed = configs.get("SpeedMode");

        setTimeoutAndInterval(speed);
        rightAnswerScore = SpeedToScore(speed);

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

        buttonList = Arrays.asList(flagA, flagB, flagC, flagD);

        buttonDefaultColor = R.color.colorBackgroundDefault;
    }

    @Override
    protected void showQuestion(int index) {
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
            rightAnswerLayout = rightAnswerFlagLayout;

            mCountDown.start();
        } else {
            activeClassName = ACTIVE_REVERT;
            finishQuiz();
        }
    }

    @Override
    public void onClick(View v) {
        mCountDown.cancel();
        if (index < totalQuestion) {
            final Button clickedButton = (Button) v;
            manipulateButtons(buttonList, false);
            if (getCountryNameByFlag(clickedButton).equals(questionPlay.get(index).getCorrectAnswer())) {
                rightAnswer(clickedFlagLayout);
            } else {
                Map<String, View> answers = new HashMap<>();
                answers.put("clicked", clickedFlagLayout);
                answers.put("right", rightAnswerFlagLayout);
                wrongAnswer(answers);
            }

            txtScore.setText(String.format("%d", score));
        }
    }

    @Override
    protected List<Button> getButtonsList() {
        return buttonList;
    }

    private void setRightAnswerButtonReference(Question questionPlay) {
        if (questionPlay.getCorrectAnswer().equals(questionPlay.getAnswerA())) {
            rightAnswerReference = flagA;
            rightAnswerFlagLayout = (LinearLayout) findViewById(R.id.flagLayoutA);
        } else if (questionPlay.getCorrectAnswer().equals(questionPlay.getAnswerB())) {
            rightAnswerReference = flagB;
            rightAnswerFlagLayout = (LinearLayout) findViewById(R.id.flagLayoutB);
        } else if (questionPlay.getCorrectAnswer().equals(questionPlay.getAnswerC())) {
            rightAnswerReference = flagC;
            rightAnswerFlagLayout = (LinearLayout) findViewById(R.id.flagLayoutC);
        } else {
            rightAnswerReference = flagD;
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
