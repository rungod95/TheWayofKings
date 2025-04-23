package com.TheWayofKings.screens;

import com.TheWayofKings.util.GameScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import lombok.Getter;

public class PauseMenuScreen implements Screen {

    private final Game game;
    @Getter
    private final GameScreen gameScreen;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture fondoMedieval, pergaminoOpciones;

    public PauseMenuScreen(final Game game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
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
        gameScreen.getMainGame().setPausaActiva(true);
        generator.dispose();

        if (gameScreen.getMainGame().getMusicaFondo().isPlaying()) {
            gameScreen.getMainGame().getMusicaFondo().pause();
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
        batch.draw(pergaminoOpciones, 80, 100, 640, 280);

        drawCenteredText("MENÚ DE PAUSA", 340);
        drawCenteredText("P - Reanudar juego", 300);
        drawCenteredText("M - Volver al menú principal", 270);
        drawCenteredText("ESC - Salir del juego", 240);
        drawCenteredText("+ / - - Subir o bajar volumen", 210);

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            if (!gameScreen.getMainGame().getMusicaFondo().isPlaying()) {
                gameScreen.getMainGame().getMusicaFondo().play();
            }
            gameScreen.getMainGame().setPausaActiva(false);
            game.setScreen(gameScreen);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            gameScreen.getMainGame().getMusicaFondo().stop(); // ← detener música del juego
            game.setScreen(new MainMenuScreen(game));

    } else if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.PLUS) || Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)) {
            float current = gameScreen.getMainGame().getMusicaFondo().getVolume();
            float newVol = Math.min(1f, current + 0.3f);
            gameScreen.getMainGame().getMusicaFondo().setVolume(newVol);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.MINUS)) {
            float current = gameScreen.getMainGame().getMusicaFondo().getVolume();
            float newVol = Math.max(0f, current - 0.3f);
            gameScreen.getMainGame().getMusicaFondo().setVolume(newVol);
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

    }
}
