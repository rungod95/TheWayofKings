package com.TheWayofKings.screens;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.TheWayofKings.game.MainGame;


public class MenuScreen implements Screen {

    private MainGame game;
    private Stage stage;
    private Skin skin;

    public MenuScreen(MainGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
    }
    private void crearUI() {
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label titulo = new Label("Kaladin: El Camino del Viento", skin);
        TextButton jugarBtn = new TextButton("Jugar", skin);
        TextButton instruccionesBtn = new TextButton("Instrucciones", skin);
        TextButton salirBtn = new TextButton("Salir", skin);

        jugarBtn.addListener(event -> {
            if (jugarBtn.isPressed()) {
                // game.setScreen(new PantallaJuego(game));
                System.out.println("Jugar pulsado");
            }
            return false;
        });

        instruccionesBtn.addListener(event -> {
            if (instruccionesBtn.isPressed()) {
                // Mostrar instrucciones o nueva pantalla
                System.out.println("Instrucciones pulsado");
            }
            return false;
        });

        salirBtn.addListener(event -> {
            if (salirBtn.isPressed()) {
                Gdx.app.exit();
            }
            return false;
        });

        table.add(titulo).padBottom(40).row();
        table.add(jugarBtn).padBottom(20).row();
        table.add(instruccionesBtn).padBottom(20).row();
        table.add(salirBtn).padBottom(20).row();
    }

    @Override
    public void show() {}
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override public void resize(int width, int height) { stage.getViewport().update(width, height, true); }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() { stage.dispose(); skin.dispose(); }
}

