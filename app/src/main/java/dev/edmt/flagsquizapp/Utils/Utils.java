package dev.edmt.flagsquizapp.Utils;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.View;

import java.util.Map;
import dev.edmt.flagsquizapp.R;

public class Utils {

    private Utils() {}

    public static void answeredRight(Context context, View rightClickedAnswer) {
        rightClickedAnswer.setBackgroundResource(R.color.colorRight);
        MediaPlayer mp = MediaPlayer.create(context, R.raw.right_answer);
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
        MediaPlayer mp = MediaPlayer.create(context, R.raw.end_time);
        mp.start();
    }
}
