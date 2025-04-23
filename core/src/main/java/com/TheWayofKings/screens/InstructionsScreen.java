package com.TheWayofKings.screens;

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

public class InstructionsScreen implements Screen {

    private final Game game;
    private final Music menuMusic;
    private final float musicVolume;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture fondoMedieval, pergaminoOpciones;

    public InstructionsScreen(Game game, Music menuMusic, float musicVolume) {
        this.game = game;
        this.menuMusic = menuMusic;
        this.musicVolume = musicVolume;
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

        if (!menuMusic.isPlaying()) {
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

        int pergaminoAncho = 780;
        int pergaminoAlto = 300;
        int xPergamino = (800 - pergaminoAncho) / 2; // 800 es el ancho de la pantalla

        batch.begin();
        batch.draw(fondoMedieval, 0, 0, 800, 480);
        batch.draw(pergaminoOpciones, xPergamino, 90, pergaminoAncho, pergaminoAlto);


        drawCenteredText("INSTRUCCIONES", 360);
        drawCenteredText("- Usa IZQ / DER para moverte, SPACE para saltar.", 320);
        drawCenteredText("- Evita trampas y enemigos. Si caes, mueres.", 290);
        drawCenteredText("- Toca pociones para recuperar vida.", 260);
        drawCenteredText("- Llega al final del mapa para avanzar.", 230);
        drawCenteredText("Pulsa ESC para volver al menu", 190);

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game, menuMusic, musicVolume));
            dispose();
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
