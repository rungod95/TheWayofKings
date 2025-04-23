package com.TheWayofKings.screens;

import com.TheWayofKings.config.GameConfig;
import com.TheWayofKings.managers.ScoreManager;
import com.TheWayofKings.util.ScoreEntry;
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

public class NameInputScreen implements Screen {

    private final Game game;
    private final int timeInSeconds;
    private final String difficulty;
    private final Music menuMusic;
    private final float musicVolume;

    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture fondo, pergamino;
    private String nombre = "";

    public NameInputScreen(Game game, int timeInSeconds, String difficulty, Music menuMusic, float musicVolume) {
        this.game = game;
        this.timeInSeconds = timeInSeconds;
        this.difficulty = difficulty;
        this.menuMusic = menuMusic;
        this.musicVolume = musicVolume;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();

        fondo = new Texture("ui/wayofkings.jpg");
        pergamino = new Texture("ui/pergamino_opciones.png");

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/wisdom.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        parameter.color = Color.BROWN;
        font = generator.generateFont(parameter);
        generator.dispose();
    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(fondo, 0, 0, 800, 480);
        batch.draw(pergamino, 80, 100, 640, 280);

        drawCentered("INTRODUCE TU NOMBRE", 320);
        drawCentered(nombre + "_", 270);
        drawCentered("Pulsa ENTER para confirmar", 200);

        batch.end();
    }

    private void handleInput() {
        for (int i = Input.Keys.A; i <= Input.Keys.Z; i++) {
            if (Gdx.input.isKeyJustPressed(i) && nombre.length() < 12) {
                char letra = (char) ('A' + i - Input.Keys.A);
                nombre += letra;
            }
        }

        for (int i = Input.Keys.NUM_0; i <= Input.Keys.NUM_9; i++) {
            if (Gdx.input.isKeyJustPressed(i) && nombre.length() < 12) {
                char numero = (char) ('0' + i - Input.Keys.NUM_0);
                nombre += numero;
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.BACKSPACE) && nombre.length() > 0) {
            nombre = nombre.substring(0, nombre.length() - 1);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && nombre.length() >= 3) {
            new ScoreManager().addScore(new ScoreEntry(nombre, timeInSeconds, difficulty));
            game.setScreen(new ScoreListScreen(game, menuMusic, musicVolume));
        }
    }

    private void drawCentered(String texto, float y) {
        GlyphLayout layout = new GlyphLayout(font, texto);
        float x = (800 - layout.width) / 2;
        font.draw(batch, layout, x, y);
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        font.dispose();
        batch.dispose();
        fondo.dispose();
        pergamino.dispose();
    }
}
