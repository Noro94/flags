package dev.edmt.flagsquizapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.edmt.flagsquizapp.DbHelper.DbHelper;
import dev.edmt.flagsquizapp.Model.Question;

import static dev.edmt.flagsquizapp.Utils.Utils.SpeedToScore;
import static dev.edmt.flagsquizapp.constants.Constants.ACTIVE_CLASSIC;
import static dev.edmt.flagsquizapp.Utils.Utils.manipulateButtons;

public class PlayingClassic extends PlayCommon implements View.OnClickListener {

    //Control
    ImageView imageView;
    Button btnA, btnB, btnC, btnD;
    List<Button> buttonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing);

        db = new DbHelper(this);
        configs = db.getConfigs();
        mode = configs.get("CountMode");
        speed = configs.get("SpeedMode");

        setTimeoutAndInterval(speed);
        rightAnswerScore = SpeedToScore(speed);

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

        buttonList = Arrays.asList(btnA, btnB, btnC, btnD);

        buttonDefaultColor = R.color.colorButtonDefault;
    }

    @Override
    protected void showQuestion(int index) {
        if (index < totalQuestion) {
            thisQuestion++;
            txtQuestion.setText(String.format("%d/%d", thisQuestion, totalQuestion));
            progressBar.setProgress(0);
            progressValue = 0;

            int ImageId = this.getResources().getIdentifier(questionPlay.get(index).getImage().toLowerCase(), "drawable", getPackageName());
            imageView.setBackgroundResource(ImageId);
            btnA.setText(questionPlay.get(index).getAnswerA().replace("_", " "));
            btnB.setText(questionPlay.get(index).getAnswerB().replace("_", " "));
            btnC.setText(questionPlay.get(index).getAnswerC().replace("_", " "));
            btnD.setText(questionPlay.get(index).getAnswerD().replace("_", " "));

            setRightAnswerButtonReference(questionPlay.get(index));
            rightAnswerLayout = rightAnswerReference;

            mCountDown.start();
        } else {
            activeClassName = ACTIVE_CLASSIC;
            finishQuiz();
        }
    }

    @Override
    public void onClick(View v) {
        mCountDown.cancel();
        if (index < totalQuestion) {
            final Button clickedButton = (Button) v;
            manipulateButtons(buttonList, false);
            if (clickedButton.getText().equals(questionPlay.get(index).getCorrectAnswer().replace("_", " "))) {
                rightAnswer(clickedButton);
                rightAnswerCount++;
            } else {
                Map<String, View> answers = new HashMap<>();
                answers.put("clicked", clickedButton);
                answers.put("right", rightAnswerReference);
                wrongAnswer(answers);
            }

            txtScore.setText(String.format("%d/%d", rightAnswerCount, score));
        }
    }

    @Override
    protected List<Button> getButtonsList() {
        return buttonList;
    }

    private void setRightAnswerButtonReference(Question questionPlay) {
        if (questionPlay.getCorrectAnswer().equals(questionPlay.getAnswerA())) {
            rightAnswerReference = btnA;
        } else if (questionPlay.getCorrectAnswer().equals(questionPlay.getAnswerB())) {
            rightAnswerReference = btnB;
        } else if (questionPlay.getCorrectAnswer().equals(questionPlay.getAnswerC())) {
            rightAnswerReference = btnC;
        } else {
            rightAnswerReference = btnD;
        }
    }
}
