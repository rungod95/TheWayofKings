package com.TheWayofKings.managers;

import com.TheWayofKings.util.ScoreEntry;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import lombok.Getter;

public class ScoreManager {
    private static final String FILE = "scores.json";
    private final Json json = new Json();
    @Getter
    private final Array<ScoreEntry> scores = new Array<>();

    public ScoreManager() {
        FileHandle file = Gdx.files.local(FILE);
        if (file.exists()) {
            scores.addAll(json.fromJson(Array.class, ScoreEntry.class, file));
        }
    }

    public void addScore(ScoreEntry score) {
        scores.add(score);
        scores.sort(); // por tiempo ascendente
        if (scores.size > 10) scores.pop(); // máx 10
        save();
    }

    private void save() {
        json.toJson(scores, Gdx.files.local(FILE));
    }
}
