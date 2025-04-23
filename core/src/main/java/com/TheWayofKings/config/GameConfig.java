package com.TheWayofKings.config;

public class GameConfig {

    public enum Difficulty {
        EASY, NORMAL, HARD
    }

    public static Difficulty difficulty = Difficulty.NORMAL;

    public static int getInitialLives() {
        switch (difficulty) {
            case EASY:
                return 8;
            case NORMAL:
                return 6;
            case HARD:
                return 3;
            default:
                return 10;
        }
    }

    public static float getEnemySpeed() {
        switch (difficulty) {
            case EASY:
                return 40f;
            case NORMAL:
                return 60f;
            case HARD:
                return 80f;
            default:
                return 60f;
        }
    }

    public static int getEnemyDamage() {
        switch (difficulty) {
            case EASY:
                return 1;
            case NORMAL:
                return 1;
            case HARD:
                return 2;
            default:
                return 1;
        }
    }
}
