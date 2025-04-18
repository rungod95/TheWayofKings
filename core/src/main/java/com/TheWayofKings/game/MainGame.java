package com.TheWayofKings.game;

import com.TheWayofKings.characters.Kaladin;
import com.TheWayofKings.maps.MapCollisionHelper;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

/**
 * Controlador principal del juego.
 * – Carga el mapa, las colisiones y sitúa a Kaladin en el objeto "spawn" definido en Tiled.
 * – Mantiene la cámara dentro de los límites del mapa.
 */
public class MainGame extends ApplicationAdapter {
    private static final int TILE_SIZE = 16; // tamaño de un tile en píxeles

    private OrthographicCamera camera;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private Kaladin kaladin;
    private MapCollisionHelper collisionHelper;

    // ------------------------------------------------------------------------
    // Ciclo de vida
    // ------------------------------------------------------------------------
    @Override
    public void create() {
        // 1. Cargar mapa y renderer
        map = new TmxMapLoader().load("maps/mapa_base_ejemplo_tiles.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1f); // trabajamos en píxeles

        // 2. Cámara con el mismo tamaño del viewport en píxeles
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // 3. Colisiones + personaje
        collisionHelper = new MapCollisionHelper(map);
        kaladin = new Kaladin();
        kaladin.create(collisionHelper);

        colocarKaladinEnSpawn();
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        kaladin.update(delta);

        if (kaladin.isDead()) {
            kaladin.reset();           // reaparece en el spawn

        }

        actualizarCamara();
        renderer.setView(camera);

        renderizarCapasMapa();

        // Dibujar a Kaladin sobre el mapa
        renderer.getBatch().begin();
        kaladin.render((SpriteBatch) renderer.getBatch());
        renderer.getBatch().end();
    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        kaladin.dispose();
    }


    private void colocarKaladinEnSpawn() {


        MapLayer spawnLayer = map.getLayers().get("spawn");
        if (spawnLayer == null) return;

        // 2. Objeto llamado "spawn"
        MapObject spawnObj = spawnLayer.getObjects().get("spawn");
        if (spawnObj == null) return;

        // 3. Altura real del mapa en píxeles
        TiledMapTileLayer anyTileLayer = (TiledMapTileLayer) map.getLayers().get(0);
        int tileHeight   = anyTileLayer.getTileHeight();
        int mapHeightPx  = map.getProperties().get("height", Integer.class) * tileHeight;


        float spawnX, spawnY;

        if (spawnObj instanceof RectangleMapObject) {
            Rectangle r = ((RectangleMapObject) spawnObj).getRectangle();
            spawnX = r.x;
            spawnY = mapHeightPx - r.y - r.height;   // esquina inferior del rectángulo
        } else {                                     // si fuera un punto
            float sx = spawnObj.getProperties().get("x", Float.class);
            float sy = spawnObj.getProperties().get("y", Float.class);
            spawnX = sx;
            spawnY = mapHeightPx - sy;
        }

        kaladin.setPosition(spawnX, spawnY);
    }



    private void actualizarCamara() {

        float viewportH = camera.viewportHeight;
        float viewportW = camera.viewportWidth;

        float mapW = map.getProperties().get("width",  Integer.class) * TILE_SIZE;
        float mapH = map.getProperties().get("height", Integer.class) * TILE_SIZE;

        float halfH = viewportH / 2f;
        float halfW = viewportW / 2f;

        // Si el mapa es más bajo que la ventana, centra la cámara en vertical
        float minY = (mapH < viewportH) ? mapH / 2f : halfH;
        float maxY = (mapH < viewportH) ? mapH / 2f : mapH - halfH;

        float minX = (mapW < viewportW) ? mapW / 2f : halfW;
        float maxX = (mapW < viewportW) ? mapW / 2f : mapW - halfW;

        float camX = MathUtils.clamp(kaladin.getX(), minX, maxX);
        float camY = MathUtils.clamp(kaladin.getY(), minY, maxY);

        camera.position.set(camX, camY, 0);
        camera.update();
    }
    /**
     * Renderiza todas las capas salvo «colisiones» y «peligros».
     */
    private void renderizarCapasMapa() {
        for (int i = 0; i < map.getLayers().getCount(); i++) {
            String name = map.getLayers().get(i).getName();
            if (!"colisiones".equals(name) && !"peligros".equals(name)) {
                renderer.render(new int[]{i});
            }
        }
    }
}
