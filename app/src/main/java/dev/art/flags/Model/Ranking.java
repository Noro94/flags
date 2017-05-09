package dev.art.flags.Model;

public class Ranking {
    private int Id;
    private double Score;
    private String Mode;

    public Ranking(int id, double score, String mode) {
        Id = id;
        Score = score;
        Mode = mode;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public double getScore() {
        return Score;
    }

    public void setScore(double score) {
        Score = score;
    }

    public String getMode() {
        return Mode;
    }

    public void setMode(String mode) {
        Mode = mode;
    }
}
