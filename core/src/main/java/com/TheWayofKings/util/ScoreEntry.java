package com.TheWayofKings.util;

public class ScoreEntry implements Comparable<ScoreEntry> {
    public String name;
    public int timeInSeconds;
    public String difficulty;

    public ScoreEntry() {} // requerido por Json

    public ScoreEntry(String name, int timeInSeconds, String difficulty) {
        this.name = name;
        this.timeInSeconds = timeInSeconds;
        this.difficulty = difficulty;
    }

    @Override
    public int compareTo(ScoreEntry other) {
        return Integer.compare(this.timeInSeconds, other.timeInSeconds);
    }
}
