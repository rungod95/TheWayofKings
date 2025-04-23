package com.TheWayofKings.screens;

import com.TheWayofKings.config.GameConfig;
import com.TheWayofKings.game.Main;
import com.TheWayofKings.game.MainGame;
import com.TheWayofKings.util.GameScreen;
import com.badlogic.gdx.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class MainMenuScreen implements Screen {

    private final com.badlogic.gdx.Game game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private float musicVolume = 0.1f;
    private Texture fondoMedieval;
    private Texture pergaminoTitulo;
    private Texture pergaminoOpciones;
    private Music menuMusic;
    private Preferences prefs;




    public MainMenuScreen(final com.badlogic.gdx.Game game, Music menuMusic, float musicVolume) {
        this.game = game;
        this.menuMusic = menuMusic;
        this.musicVolume = musicVolume;
    }
    public MainMenuScreen(final Game game) {
        this.game = game;
        this.menuMusic = null; // ¡importante!
    }


    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        batch = new SpriteBatch();
        prefs = Gdx.app.getPreferences("KaladinPrefs");
        if (menuMusic == null) {
            musicVolume = prefs.getFloat("menuVolume", 0.1f);
            menuMusic = Gdx.audio.newMusic(Gdx.files.internal("sfx/medieval.mp3"));
            menuMusic.setLooping(true);
            menuMusic.setVolume(musicVolume);
            menuMusic.play();
        } else {
            menuMusic.setVolume(musicVolume); // asegura que tenga el volumen correcto
            if (!menuMusic.isPlaying()) menuMusic.play();
        }
        fondoMedieval = new Texture("ui/wayofkings.jpg");
        pergaminoTitulo = new Texture("ui/pergamino_titulo.png");
        pergaminoOpciones = new Texture("ui/pergamino_opciones.png");

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/wisdom.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 24;
        parameter.color = Color.BLACK;
        font = generator.generateFont(parameter);
        generator.dispose();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int screenW = 800;
        int screenH = 480;

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Fondo centrado
        batch.draw(fondoMedieval, 0, 0, screenW, screenH);

        // Medidas del título
        int anchoTitulo = 800;
        int altoTitulo = 200;
        int xTitulo = (screenW - anchoTitulo) / 2;
        int yTitulo = 330; // prueba con 330 o 350

        // Medidas del contenedor de opciones
        int anchoOpciones = 850;
        int altoOpciones = 350;
        int xOpciones = (screenW - anchoOpciones) / 1;
        int yOpciones = yTitulo - altoOpciones + 30;

        // Dibujar pergaminos
        batch.draw(pergaminoTitulo, xTitulo, yTitulo, anchoTitulo, altoTitulo);
        drawCenteredText("KALADIN - La Forja de los Vientos", yTitulo + altoTitulo / 2 + 10);

        batch.draw(pergaminoOpciones, xOpciones, yOpciones, anchoOpciones, altoOpciones);

        // Dibujar opciones dentro del pergamino
        int espacio = 40;
        int yBase = yOpciones + altoOpciones - espacio-40;
        drawCenteredText("Enter para comenzar la travesia", yBase);
        drawCenteredText("Dificultad: " + GameConfig.difficulty, yBase - espacio);
        drawCenteredText("Volumen: " + (int)(musicVolume * 100) + "%", yBase - 2 * espacio);
        drawCenteredText("Pulsa I para consultar el codice", yBase - 3 * espacio);
        drawCenteredText("Pulsa S para ver la lista de puntuaciones", yBase - 4 * espacio);

        batch.end();

        // Controles
        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {
            cambiarDificultad(-1);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
            cambiarDificultad(1);
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            musicVolume = Math.min(1.0f, musicVolume + 0.1f);
            menuMusic.setVolume(musicVolume);
            prefs.putFloat("menuVolume", musicVolume).flush();
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            musicVolume = Math.max(0.0f, musicVolume - 0.1f);
            menuMusic.setVolume(musicVolume);
            prefs.putFloat("menuVolume", musicVolume).flush();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.S)) {
            game.setScreen(new ScoreListScreen(game, menuMusic, musicVolume));
        }




        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            menuMusic.stop(); // o menuMusic.pause();
            MainGame juego = new MainGame();
            juego.setVolume(musicVolume);
            game.setScreen(new GameScreen(game));

            dispose();
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.I)) {
            game.setScreen(new InstructionsScreen(game, menuMusic, musicVolume));
            // ¡No llames a dispose()!
        }

    }

    private void cambiarDificultad(int direccion) {
        GameConfig.Difficulty[] difs = GameConfig.Difficulty.values();
        int idx = GameConfig.difficulty.ordinal();
        idx = (idx + direccion + difs.length) % difs.length;
        GameConfig.difficulty = difs[idx];
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
        batch.dispose();
        fondoMedieval.dispose();
        pergaminoTitulo.dispose();
        pergaminoOpciones.dispose();
        font.dispose();
    }
}
