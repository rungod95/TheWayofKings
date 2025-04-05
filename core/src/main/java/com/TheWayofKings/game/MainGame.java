package com.TheWayofKings.game;

import com.TheWayofKings.characters.Kaladin;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;


public class MainGame extends ApplicationAdapter {
    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Texture kaladinTexture;
    private TextureRegion kaladinRegion;
    private float kaladinX = 100, kaladinY = 240;  // Posición inicial
    Kaladin kaladin;

    @Override
    public void create() {

        map = new TmxMapLoader().load("maps/mapa_nivel1.tmx");


        mapRenderer = new OrthogonalTiledMapRenderer(map, 1f);


        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        kaladin = new Kaladin();
        kaladin.create();

    }

    @Override
    public void render() {
        camera.update();
        mapRenderer.setView(camera);
        mapRenderer.render();

        mapRenderer.getBatch().begin();
        kaladin.render((SpriteBatch) mapRenderer.getBatch());
        mapRenderer.getBatch().end();
    }

    @Override
    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        kaladin.dispose();
    }
}
