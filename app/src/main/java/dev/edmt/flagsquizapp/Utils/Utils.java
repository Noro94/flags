package dev.edmt.flagsquizapp.Utils;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import dev.edmt.flagsquizapp.Common.Common;
import dev.edmt.flagsquizapp.MainActivity;
import dev.edmt.flagsquizapp.PlayingClassic;
import dev.edmt.flagsquizapp.PlayingRevert;
import dev.edmt.flagsquizapp.R;

import static dev.edmt.flagsquizapp.constants.Constants.*;

public class Utils {

    private Utils() {}

    public static void showCorrectAnswer(View rightClickedAnswer) {
        rightClickedAnswer.setBackgroundResource(R.color.colorRight);
    }

    public static void answeredRight(Context context, View rightClickedAnswer) {
        rightClickedAnswer.setBackgroundResource(R.color.colorRight);
        MediaPlayer mp = MediaPlayer.create(context, R.raw.correct_answer);
        mp.start();
    }

    public static void answeredWrong(Context context, View clickedAnswer, View rightAnswer) {
        clickedAnswer.setBackgroundResource(R.color.colorWrong);
        rightAnswer.setBackgroundResource(R.color.colorRight);
        MediaPlayer mp = MediaPlayer.create(context, R.raw.wrong_answer);
        mp.start();
    }

    public static void setDefaultColor(Map changeList, int defaultColor) {
        ((View) changeList.get("clicked")).setBackgroundResource(defaultColor);
        ((View) changeList.get("right")).setBackgroundResource(defaultColor);
    }

    public static void setDefaultColor(View change, int defaultColor) {
        change.setBackgroundResource(defaultColor);
    }

    public static void playSoundTimeout(Context context) {
        MediaPlayer mp = MediaPlayer.create(context, R.raw.time_out);
        mp.start();
    }

    public static void manipulateButtons(List<Button> buttonsList, boolean state) {
        for (Button button : buttonsList) {
            button.setClickable(state);
        }
    }

    public static int getCountByMode(String mode) {
        int limit = 0;

        if (mode.equals(Common.MODE.EASY.toString()))
            limit = EASY_MODE_NUM;
        else if (mode.equals(Common.MODE.MEDIUM.toString()))
            limit = MEDIUM_MODE_NUM;
        else if (mode.equals(Common.MODE.HARD.toString()))
            limit = HARD_MODE_NUM;
        else if (mode.equals(Common.MODE.HARDEST.toString()))
            limit = HARDEST_MODE_NUM;

        return limit;
    }

    public static void showMessage(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

    public static int getSecondsByMode(String mode) {
        int seconds = 0;

        if (mode.equals(Common.SPEED.SLOW.toString()))
            seconds = SLOW_SPEED;
        else if (mode.equals(Common.SPEED.MEDIUM.toString()))
            seconds = MEDIUM_SPEED;
        else if (mode.equals(Common.SPEED.FAST.toString()))
            seconds = FAST_SPEED;
        else if (mode.equals(Common.SPEED.FASTEST.toString()))
            seconds = FASTEST_SPEED;

        return seconds;
    }

    public static Class getActiveClass(String activeClassName) {
        switch (activeClassName) {
            case ACTIVE_CLASSIC:
                return PlayingClassic.class;
            case ACTIVE_REVERT:
                return PlayingRevert.class;
            default:
                return MainActivity.class;
        }
    }

    public static int SpeedToScore(String mode) {
        int score = 0;

        if (mode.equals(Common.SPEED.SLOW.toString()))
            score = 10;
        else if (mode.equals(Common.SPEED.MEDIUM.toString()))
            score = 15;
        else if (mode.equals(Common.SPEED.FAST.toString()))
            score = 20;
        else if (mode.equals(Common.SPEED.FASTEST.toString()))
            score = 25;

        return score;
    }

    public static Intent getOpenFacebookIntent(Context context) {

        try {
            int versionCode = context.getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://facewebmodal/f?href=" + FACEBOOK_URL));
            } else { //older versions of fb app
                return new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/" + FACEBOOK_PAGE_ID));
            }

        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/geogame"));
        }
    }
}
