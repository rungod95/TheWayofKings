package com.TheWayofKings.screens;

import com.TheWayofKings.util.ScoreEntry;
import com.TheWayofKings.managers.ScoreManager;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.utils.Array;

public class ScoreListScreen implements Screen {

    private final Game game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture fondoMedieval, pergaminoOpciones;
    private Array<ScoreEntry> scores;
    private final Music menuMusic;
    private final float musicVolume;
    private final boolean usarMusica;




    public ScoreListScreen(Game game, Music menuMusic, float musicVolume) {
        this.game = game;
        this.menuMusic = menuMusic;
        this.musicVolume = musicVolume;
        this.usarMusica = true;
    }

    public ScoreListScreen(Game game) {
        this.game = game;
        this.menuMusic = null;
        this.musicVolume = 0f;
        this.usarMusica = false;
    }



    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        fondoMedieval = new Texture("ui/wayofkings.jpg");
        pergaminoOpciones = new Texture("ui/pergamino_opciones.png");

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/wisdom.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 18;
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);
        generator.dispose();


        scores = new ScoreManager().getScores();

        if (usarMusica && menuMusic != null && !menuMusic.isPlaying()) {
            menuMusic.setVolume(musicVolume);
            menuMusic.play();
        }

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(fondoMedieval, 0, 0, 800, 480);
        batch.draw(pergaminoOpciones, 80, 80, 640, 320);

        drawCenteredText("PUNTUACIONES TOP 10", 380);

        int y = 340;
        for (int i = 0; i < scores.size; i++) {
            ScoreEntry s = scores.get(i);
            String tiempo = String.format("%02d:%02d", s.timeInSeconds / 60, s.timeInSeconds % 60);
            String texto = String.format("%d. %s - %s - %s", i + 1, s.name, tiempo, s.difficulty);
            drawCenteredText(texto, y);
            y -= 30;
        }

        drawCenteredText("Pulsa ESC para volver", 100);
        batch.end();


        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game, menuMusic, musicVolume));
            dispose();

        }
    }

    private void drawCenteredText(String text, float y) {
        GlyphLayout layout = new GlyphLayout(font, text);
        float x = (800 - layout.width) / 2;
        font.draw(batch, layout, x, y);
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        batch.dispose();
        font.dispose();
        fondoMedieval.dispose();
        pergaminoOpciones.dispose();
    }
}
