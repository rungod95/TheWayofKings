package com.TheWayofKings.screens;

import com.TheWayofKings.util.GameScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class GameOverScreen implements Screen {

    private final Game game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture fondoMedieval, pergaminoOpciones;
    private Sound deathSound;
    private boolean sonidoReproducido = false;

    public GameOverScreen(Game game) {
        this.game = game;
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
        parameter.size = 20;
        parameter.color = Color.DARK_GRAY;
        font = generator.generateFont(parameter);
        generator.dispose();

        // Sonido de muerte
        deathSound = Gdx.audio.newSound(Gdx.files.internal("sfx/death_sound.mp3"));
        deathSound.play(0.8f); // volumen entre 0 y 1
        sonidoReproducido = true;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(fondoMedieval, 0, 0, 800, 480);
        batch.draw(pergaminoOpciones, 100, 120, 600, 240);

        drawCenteredText("HAS MUERTO", 320);
        drawCenteredText("Pulsa R para reiniciar", 270);
        drawCenteredText("Pulsa M para volver al menú", 240);

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            game.setScreen(new GameScreen(game));
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    private void drawCenteredText(String texto, float y) {
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
        fondoMedieval.dispose();
        pergaminoOpciones.dispose();
        if (sonidoReproducido) deathSound.dispose();
    }
}
