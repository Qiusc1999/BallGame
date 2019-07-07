package BallGame;

public class Score {
    private int Score=0;
    public Score(int score){
        Score=score;
        if(Score<=0){
            Score=0;
        }
    }

    public void setScore(int score) {
        Score = score;
    }

    public void addScore(int score){
        Score+=score;
        if(Score<=0){
            Score=0;
        }
    }

    public int getScore() {
        return Score;
    }
}
