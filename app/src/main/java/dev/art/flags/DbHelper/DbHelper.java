package dev.art.flags.DbHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import dev.art.flags.Common.Common;
import dev.art.flags.Model.CapitalsQuestion;
import dev.art.flags.Model.Question;
import dev.art.flags.Model.Ranking;

import static dev.art.flags.Utils.Utils.getCountByMode;

public class DbHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "LocalDataBase_v2.db";
    private static String DB_PATH = "";
    private SQLiteDatabase mDataBase;
    private Context mContext = null;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, 1);

        DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        File file = new File(DB_PATH+"LocalDataBase_v2.db");
        if(file.exists())
            openDataBase(); // Add this line to fix db.insert can't insert values
        this.mContext = context;
    }

    public void openDataBase() {
        String myPath = DB_PATH + DB_NAME;
        mDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void copyDataBase() throws IOException {
        try {
            InputStream myInput = mContext.getAssets().open(DB_NAME);
            String outputFileName = DB_PATH + DB_NAME;
            OutputStream myOutput = new FileOutputStream(outputFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0)
                myOutput.write(buffer, 0, length);

            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase tempDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            tempDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        if (tempDB != null)
            tempDB.close();
        return tempDB != null ? true : false;
    }

    public void createDataBase() throws IOException {
        boolean isDBExists = checkDataBase();
        if (isDBExists) {

        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //CRUD For Table
    public List<Question> getAllQuestion() {
        List<Question> listQuestion = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        try {
            c = db.rawQuery("SELECT * FROM Question ORDER BY Random()", null);
            if (c == null) return null;
            c.moveToFirst();
            do {
                int Id = c.getInt(c.getColumnIndex("ID"));
                String Image = c.getString(c.getColumnIndex("Image"));
                String AnswerA = c.getString(c.getColumnIndex("AnswerA"));
                String AnswerB = c.getString(c.getColumnIndex("AnswerB"));
                String AnswerC = c.getString(c.getColumnIndex("AnswerC"));
                String AnswerD = c.getString(c.getColumnIndex("AnswerD"));
                String CorrectAnswer = c.getString(c.getColumnIndex("CorrectAnswer"));

                Question question = new Question(Id, Image, AnswerA, AnswerB, AnswerC, AnswerD, CorrectAnswer);
                listQuestion.add(question);
            }
            while (c.moveToNext());
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
        return listQuestion;
    }

    public List<CapitalsQuestion> getAllCountriesQuestion() {
        List<CapitalsQuestion> listCapitalsQuestion = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        try {
            c = db.rawQuery("SELECT * FROM CtyQuestion ORDER BY Random()", null);
            if (c == null) return null;
            c.moveToFirst();
            do {
                int id = c.getInt(c.getColumnIndex("ID"));
                String ctyName = c.getString(c.getColumnIndex("CtyName"));
                String answerA = c.getString(c.getColumnIndex("AnswerA"));
                String answerB = c.getString(c.getColumnIndex("AnswerB"));
                String answerC = c.getString(c.getColumnIndex("AnswerC"));
                String answerD = c.getString(c.getColumnIndex("AnswerD"));
                String correctAnswer = c.getString(c.getColumnIndex("CorrectAnswer"));
                String continent = c.getString(c.getColumnIndex("Continent"));

                CapitalsQuestion capitalsQuestion = new CapitalsQuestion(id, ctyName, answerA, answerB, answerC, answerD, correctAnswer, continent);
                listCapitalsQuestion.add(capitalsQuestion);
            }
            while (c.moveToNext());
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
        return listCapitalsQuestion;
    }

    public Map<String, String> getConfigs() {
        SQLiteDatabase db = this.getWritableDatabase();
        Map<String, String> configs = new HashMap<>();
        Cursor c;
        try {
            c = db.rawQuery("SELECT * FROM Settings", null);
            if (c == null) return null;
            c.moveToFirst();
            configs.put("CountMode", c.getString(c.getColumnIndex("CountMode")));
            configs.put("SpeedMode", c.getString(c.getColumnIndex("SpeedMode")));
            configs.put("PlayMode", c.getString(c.getColumnIndex("PlayMode")));
            configs.put("SoundMode", c.getString(c.getColumnIndex("SoundMode")));

            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return configs;
    }

    public void setCountConfig(String count) {
        String query = "UPDATE Settings SET CountMode = \""+count+"\";";
        mDataBase.execSQL(query);
    }

    public void setSpeedConfig(String speed) {
        String query = "UPDATE Settings SET SpeedMode = \""+speed+"\";";
        mDataBase.execSQL(query);
    }

    public void setPlayConfig(String play) {
        String query = "UPDATE Settings SET PlayMode = \""+play+"\";";
        mDataBase.execSQL(query);
    }

    public void setSoundConfig(String sound) {
        String query = "UPDATE Settings SET SoundMode = \""+sound+"\";";
        mDataBase.execSQL(query);
    }

    //We need improve this function to optimize process from PlayingClassic
    public List<Question> getQuestionMode(String mode) {
        List<Question> listQuestion = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        int limit = 0;
        limit = getCountByMode(mode);
        try {
            c = db.rawQuery(String.format("SELECT * FROM Question ORDER BY Random() LIMIT %d", limit), null);
            if (c == null) return null;
            c.moveToFirst();
            do {
                int Id = c.getInt(c.getColumnIndex("ID"));
                String Image = c.getString(c.getColumnIndex("Image"));
                String AnswerA = c.getString(c.getColumnIndex("AnswerA"));
                String AnswerB = c.getString(c.getColumnIndex("AnswerB"));
                String AnswerC = c.getString(c.getColumnIndex("AnswerC"));
                String AnswerD = c.getString(c.getColumnIndex("AnswerD"));
                String CorrectAnswer = c.getString(c.getColumnIndex("CorrectAnswer"));

                Question question = new Question(Id, Image, AnswerA, AnswerB, AnswerC, AnswerD, CorrectAnswer);
                listQuestion.add(question);
            }
            while (c.moveToNext());
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
        return listQuestion;
    }


    public List<CapitalsQuestion> getCapitalsQuestionMode(String mode, String whereStatement) {
        List<CapitalsQuestion> listCapitalsQuestion = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c;
        int limit = 0;
        limit = getCountByMode(mode);
        try {
            c = db.rawQuery(String.format("SELECT * FROM CtyQuestion %s ORDER BY Random() LIMIT %d", whereStatement, limit), null);
            if (c == null) return null;
            c.moveToFirst();
            do {
                int id = c.getInt(c.getColumnIndex("ID"));
                String ctyName = c.getString(c.getColumnIndex("CtyName"));
                String answerA = c.getString(c.getColumnIndex("AnswerA"));
                String answerB = c.getString(c.getColumnIndex("AnswerB"));
                String answerC = c.getString(c.getColumnIndex("AnswerC"));
                String answerD = c.getString(c.getColumnIndex("AnswerD"));
                String correctAnswer = c.getString(c.getColumnIndex("CorrectAnswer"));
                String continent = c.getString(c.getColumnIndex("Continent"));

                CapitalsQuestion capitalsQuestion = new CapitalsQuestion(id, ctyName, answerA, answerB, answerC, answerD, correctAnswer, continent);
                listCapitalsQuestion.add(capitalsQuestion);
            }
            while (c.moveToNext());
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
        return listCapitalsQuestion;
    }


    //Insert Score to Ranking table
    public void insertScore(double score, String mode) {
        String query = "INSERT INTO Ranking(Score, Mode) VALUES("+score+", \"" + mode + "\")";
        mDataBase.execSQL(query);
    }

    //Get Score and sort ranking
    public List<Ranking> getRanking(String mode) {
        List<Ranking> listRanking = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c;
        try {
            c = db.rawQuery("SELECT * FROM Ranking WHERE Mode = \"" + mode + "\"  Order By Score DESC;", null);
            if (c == null) return null;
            c.moveToNext();
            do {
                int Id = c.getInt(c.getColumnIndex("Id"));
                double Score = c.getDouble(c.getColumnIndex("Score"));

                Ranking ranking = new Ranking(Id, Score, mode);
                listRanking.add(ranking);
            } while (c.moveToNext());
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return listRanking;

    }


    //Update version 2.0
    public int getPlayCount(int level, String mode)
    {
        int result = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c;
        try{
            c = db.rawQuery("SELECT PlayCount FROM UserPlayCount WHERE Level="+level+";",null);
            if(c == null) return 0;
            c.moveToNext();
            do{
                if (Objects.equals(mode, Common.RATE_MODE.F.toString())) {
                    result  = c.getInt(c.getColumnIndex("PlayCount"));
                } else {
                    result  = c.getInt(c.getColumnIndex("PlayCountCapitals"));
                }

            }while(c.moveToNext());
            c.close();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return result;
    }

    public void updatePlayCount(int level,int playCount)
    {
        String query = String.format("UPDATE UserPlayCount Set PlayCount = %d WHERE Level = %d",playCount,level);
        mDataBase.execSQL(query);
    }

    public void updatePlayCountCapitals(int level,int playCountCapitals)
    {
        String query = String.format("UPDATE UserPlayCount Set PlayCountCapitals = %d WHERE Level = %d",playCountCapitals,level);
        mDataBase.execSQL(query);
    }

    public void clearTable(String tableName) {
        String query = String.format("DELETE FROM %s", tableName);
        mDataBase.execSQL(query);
    }
}
