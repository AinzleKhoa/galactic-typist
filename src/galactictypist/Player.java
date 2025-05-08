package galactictypist;

import java.io.Serializable;

/**
 *
 * @author Ainzle
 */
class Player implements Serializable {

    private static final long serialVersionUID = 1L; // Add this line for version control

    private String name;
    private int level;
    private int score = 0;
    private int lives = 5;
    private int streak = 0;
    private double accuracy = 100.0;
    private int mistakes = 0;
    private int totalWordsTyped = 0;
    private int completedGameMode = -1; //-1 meant havent beat any gamemode

    //Constructor for story mode
    public Player(String name) {
        this.name = name;
        this.level = 1;
    }

    //Constructor for endless mode
    public Player(String name, int level) {
        this.name = name;
        this.level = level;
    }

    //Constructor for debug
    public Player(String name, int score, int level, int lives, int streak, int mistakes, double accuracy) {
        this.name = name;
        this.score = score;
        this.level = level;
        this.lives = lives;
        this.streak = streak;
        this.mistakes = mistakes;
        this.accuracy = accuracy;
    }

    public void setCompletedGameMode(int completedGameMode) {
        this.completedGameMode = completedGameMode;
    }

    public int getCompletedGameMode() {
        return completedGameMode;
    }

    public String getCompletedGameModeInString() {
        switch (completedGameMode) {
            case 0:
                return "Endless";
            case 1:
                return "Easy";
            case 2:
                return "Medium";
            case 3:
                return "Hard";
            default:
                return "None";
        }
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void decrementLives(int lives) {
        this.lives -= lives;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void incrementScore(int score) {
        this.score += score;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void incrementLevel(int level) {
        this.level += level;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public void incrementStreak(int streak) {
        this.streak += streak;
    }

    public int getMistakes() {
        return mistakes;
    }

    public void setMistakes(int mistakes) {
        this.mistakes = mistakes;
    }

    public void incrementMistakes(int mistakes) {
        this.mistakes += mistakes;
        updateAccuracy();
    }

    public int getTotalWordTyped() {
        return totalWordsTyped;
    }

    public void setTotalWordTyped(int totalWordTyped) {
        this.totalWordsTyped = totalWordTyped;
    }

    public void incrementTotalWordTyped(int TotalWordTyped) {
        this.totalWordsTyped += TotalWordTyped;
        updateAccuracy();
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public String getFormattedAccuracy() {
        return String.format("%.2f", this.accuracy);
    }

    public void updateAccuracy() {
        if (totalWordsTyped == 0) {
            this.accuracy = 100.0;
        } else {
            this.accuracy = ((double) (totalWordsTyped - mistakes) / totalWordsTyped) * 100;
        }
    }

    @Override
    public String toString() {
        return "Player{name='" + name + "', score=" + score
                + ", level=" + level + ", lives=" + lives
                + ", streak=" + streak + ", accuracy=" + getFormattedAccuracy() + "%}";
    }
}
