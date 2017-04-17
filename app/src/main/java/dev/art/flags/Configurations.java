package dev.art.flags;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import dev.art.flags.Common.Common;
import dev.art.flags.DbHelper.DbHelper;
import dev.art.flags.constants.Constants;

import static dev.art.flags.Utils.Utils.getCountByMode;
import static dev.art.flags.Utils.Utils.getSecondsByMode;
import static dev.art.flags.Utils.Utils.showMessage;

public class Configurations extends AppCompatActivity {

    DbHelper db;
    String mode = "";
    String speed = "";

    SeekBar seekBarCount, seekBarSpeed;
    TextView txtCountMode, txtSpeedMode;
    Button resetScores;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_config);

        db = new DbHelper(this);
        mode = db.getConfigs().get("CountMode");
        speed = db.getConfigs().get("SpeedMode");

        //reset
        resetScores = (Button) findViewById(R.id.resetScores);

        //Count
        seekBarCount = (SeekBar) findViewById(R.id.seekBarCount);
        txtCountMode = (TextView) findViewById(R.id.txtCountMode);

        applySeekBarCountChange(mode, false);
        seekBarCount.setProgress(Constants.LIST_OF_COUNTS.indexOf(getCountByMode(mode)));

        //Speed
        seekBarSpeed = (SeekBar) findViewById(R.id.seekBarSpeed);
        txtSpeedMode = (TextView) findViewById(R.id.txtSpeedMode);

        applySeekBarSpeedChange(speed, false);
        seekBarSpeed.setProgress(Constants.LIST_OF_SECONDS.indexOf(getSecondsByMode(speed)));


        //Event for count
        seekBarCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0)
                    applySeekBarCountChange(Common.MODE.EASY.toString(), true);
                else if (progress == 1)
                    applySeekBarCountChange(Common.MODE.MEDIUM.toString(), true);
                else if (progress == 2)
                    applySeekBarCountChange(Common.MODE.HARD.toString(), true);
                else if (progress == 3)
                    applySeekBarCountChange(Common.MODE.HARDEST.toString(), true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Event for speed
        seekBarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 0)
                    applySeekBarSpeedChange(Common.SPEED.SLOW.toString(), true);
                else if (progress == 1)
                    applySeekBarSpeedChange(Common.SPEED.MEDIUM.toString(), true);
                else if (progress == 2)
                    applySeekBarSpeedChange(Common.SPEED.FAST.toString(), true);
                else if (progress == 3)
                    applySeekBarSpeedChange(Common.SPEED.FASTEST.toString(), true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        resetScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    db.clearTable(Constants.PLAY_COUNT_TABLE);
                    db.clearTable(Constants.RANKING_TABLE);
                    showMessage(getApplicationContext(), Constants.RESET_DONE_MSG);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void applySeekBarCountChange(String mode, boolean doChangeInDB) {

        if (doChangeInDB) {
            try {
                db.setCountConfig(mode);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        String modeWithValue = String.format("%s (%d QUESTION)", mode, getCountByMode(mode));
        txtCountMode.setText(modeWithValue);
    }

    private void applySeekBarSpeedChange(String speed, boolean doChangeInDB) {

        if (doChangeInDB) {
            try {
                db.setSpeedConfig(speed);
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }

        String modeWithValue = String.format("%s (%d SECOND)", speed, getSecondsByMode(speed));
        txtSpeedMode.setText(modeWithValue);
    }
}
