package dev.art.flags;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.art.flags.Common.Common;
import dev.art.flags.DbHelper.DbHelper;
import dev.art.flags.Model.CapitalsQuestion;
import dev.art.flags.constants.Constants;

import static dev.art.flags.Utils.Utils.SpeedToScore;
import static dev.art.flags.Utils.Utils.manipulateButtons;
import static dev.art.flags.Utils.Utils.makeCapitalsWhereStatement;

public class PlayingCapitals extends PlayCommon implements View.OnClickListener {
    //Control
    TextView countryName;
    Button btnA, btnB, btnC, btnD;
    List<Button> buttonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_capitals);

        playType = Common.PLAY.CAPITALS.toString();

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            whereStatement = makeCapitalsWhereStatement(extra.getString("CONTINENT"));
        }

        db = new DbHelper(this);
        configs = db.getConfigs();
        mode = configs.get("CountMode");
        speed = configs.get("SpeedMode");

        setTimeoutAndInterval(speed);
        rightAnswerScore = SpeedToScore(speed);

        txtScore = (TextView) findViewById(R.id.txtScore);
        txtQuestion = (TextView) findViewById(R.id.txtQuestion);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        countryName = (TextView) findViewById(R.id.cty_name);
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


            countryName.setText(capitalsQuestionPlay.get(index).getCtyName().trim());
            btnA.setText(capitalsQuestionPlay.get(index).getAnswerA());
            btnB.setText(capitalsQuestionPlay.get(index).getAnswerB());
            btnC.setText(capitalsQuestionPlay.get(index).getAnswerC());
            btnD.setText(capitalsQuestionPlay.get(index).getAnswerD());

            setRightAnswerButtonReference(capitalsQuestionPlay.get(index));
            rightAnswerLayout = rightAnswerReference;

            mCountDown.start();
        } else {
            activeClassName = Constants.ACTIVE_CAPITALS;
            finishQuiz();
        }
    }

    @Override
    public void onClick(View v) {
        mCountDown.cancel();
        if (index < totalQuestion) {
            final Button clickedButton = (Button) v;
            manipulateButtons(buttonList, false);
            if (clickedButton.getText().equals(capitalsQuestionPlay.get(index).getCorrectAnswer())) {
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

    private void setRightAnswerButtonReference(CapitalsQuestion capitalsQuestionPlay) {
        if (capitalsQuestionPlay.getCorrectAnswer().equals(capitalsQuestionPlay.getAnswerA())) {
            rightAnswerReference = btnA;
        } else if (capitalsQuestionPlay.getCorrectAnswer().equals(capitalsQuestionPlay.getAnswerB())) {
            rightAnswerReference = btnB;
        } else if (capitalsQuestionPlay.getCorrectAnswer().equals(capitalsQuestionPlay.getAnswerC())) {
            rightAnswerReference = btnC;
        } else {
            rightAnswerReference = btnD;
        }
    }
}
