package com.TheWayofKings.game;

import com.TheWayofKings.managers.*;
import com.TheWayofKings.characters.Kaladin;
import com.TheWayofKings.maps.MapCollisionHelper;
import com.TheWayofKings.screens.GameOverScreen;
import com.TheWayofKings.screens.PauseMenuScreen;
import com.TheWayofKings.screens.VictoryScreen;
import com.TheWayofKings.util.GameScreen;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import lombok.Getter;
import lombok.Setter;

/**
 * Controlador principal del juego.
 * – Carga el mapa, las colisiones y sitúa a Kaladin en el objeto "spawn" definido en Tiled.
 * – Mantiene la cámara dentro de los límites del mapa.
 */
public class MainGame extends ApplicationAdapter {

    private static final int TILE_SIZE = 16;   // 16 px por tile
    private static final int HEART_W   = 16;   // ancho corazón
    private static final int HEART_H   = 16;

    private OrthographicCamera          camera;
    private TiledMap                    map;
    private OrthogonalTiledMapRenderer  renderer;
    private Kaladin                     kaladin;
    private MapCollisionHelper          collisionHelper;
    private HazardManager               hazards;
    private Texture                     heartTex;
    private Texture                     platformTex;
    private PlatformManager platformManager;
    private CheckpointManager checkpoints;
    private String[] niveles = {
        "maps/mapa_nivel1.tmx",
        "maps/mapa_nivel2.tmx"
    };
    private int nivelActual = 0;
    private PotionManager potionManager;
    @Getter
    private Music musicaFondo;
    private Texture enemyTex;
    private EnemyManager enemyManager;
    private float volumen = 0.1f;
    @Setter
    private Game game;
    @Setter
    private GameScreen gameScreen;
    private BitmapFont hudFont;
    @Getter
    private static float tiempoTotal = 0f;
    @Setter
    private boolean pausaActiva = false; // para gestionar pausa desde fuera



    @Override public void create () {


        musicaFondo = Gdx.audio.newMusic(Gdx.files.internal("sfx/music_fondo.mp3"));
        musicaFondo.setLooping(true);
        musicaFondo.setVolume(volumen); // ajusta volumen si quieres
        musicaFondo.play();
        tiempoTotal = 0f;


        camera = new OrthographicCamera();
        camera.setToOrtho(false,
                Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());


        cargarNivel(0);

        collisionHelper = new MapCollisionHelper(map);

        // Texturas
        heartTex    = new Texture("ui/corazontex.png");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/wisdom.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16;
        parameter.color = Color.WHITE;
        hudFont = generator.generateFont(parameter);
        generator.dispose();


        hazards  = new HazardManager(map, 64, 64);   // usa los rects
        platformManager = new PlatformManager(map);


        colocarKaladinEnSpawn();


    }

    private void cargarNivel(int idx) {
        if (idx < 0 || idx >= niveles.length) return;
        // libera mapa anterior
        if (map != null) map.dispose();

        // carga el TMX nuevo
        map       = new TmxMapLoader().load(niveles[idx]);
        renderer  = new OrthogonalTiledMapRenderer(map, 1f);
        //sonido
        Sound sonidoPocion = Gdx.audio.newSound(Gdx.files.internal("sfx/heal.mp3"));

        // rehace helper, checkpoints y hazards sobre el nuevo mapa
        potionManager = new PotionManager(map, sonidoPocion);
        enemyTex = new Texture("characters/enemy.png");
        enemyManager = new EnemyManager(map, enemyTex);
        collisionHelper = new MapCollisionHelper(map);
        checkpoints     = new CheckpointManager(map);
        hazards         = new HazardManager(map, 64, 64);
        platformManager = new PlatformManager(map);

        //  vuelve a darle a Kaladin el helper y posición inicial
        if (kaladin == null) {
            kaladin = new Kaladin();
            kaladin.create(collisionHelper);
        } else {
            kaladin.create(collisionHelper);   // reasigna colisión y resetea animaciones
        }
        colocarKaladinEnSpawn();
    }


    @Override
    public void render () {

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float dt = Gdx.graphics.getDeltaTime();

        if (!pausaActiva) {
            tiempoTotal += dt;
        }


        platformManager.update(dt);

        kaladin.setSobrePlataformaMovil(false);
        for (PlatformManager.MovingPlatform plat : platformManager.getPlatforms()) {
            if (plat.bounds.overlaps(kaladin.getFeetRectangle())) {
                kaladin.setSobrePlataformaMovil(true);
                break;
            }
        }
        handlePlatformCollision();
        kaladin.update(dt);


        hazards.update(kaladin);

        MapObject finalCheckpoint = checkpoints.getFinalCheckpoint();
        if (finalCheckpoint instanceof RectangleMapObject) {
            Rectangle finalRect = ((RectangleMapObject) finalCheckpoint).getRectangle();
            if (kaladin.getFeetRectangle().overlaps(finalRect)) {
                musicaFondo.stop();
                game.setScreen(new VictoryScreen(game));
                return;
            }
        }

// Checkpoint normal
        if (checkpoints.reached(kaladin.getX(), kaladin.getY(), 64, 64)) {
            nivelActual = (nivelActual + 1) % niveles.length;
            cargarNivel(nivelActual);
            return;

        }



        if (kaladin.isDead()) {
            musicaFondo.stop(); // ← detiene música del juego
            game.setScreen(new GameOverScreen(game)); // ← Muestra pantalla de muerte
            return;

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new PauseMenuScreen(game, gameScreen)); // ← usamos la instancia existente
            return;
        }

        actualizarCamara();
        renderer.setView(camera);

        // 2) Dibuja mapa y jugador
        renderer.render();
        SpriteBatch batch = (SpriteBatch)renderer.getBatch();
        potionManager.update(kaladin);

        enemyManager.update(dt, kaladin);

        batch.begin();
        platformManager.render(batch);
        potionManager.render(batch);
        enemyManager.render(batch);

        kaladin.render(batch);

        batch.end();

        // 3) HUD de corazones (solo los que queden)
        batch.begin();
        float startX = camera.position.x - camera.viewportWidth/2 + 8;
        float startY = camera.position.y + camera.viewportHeight/2 - 24;
        for (int i = 0; i < kaladin.getVidas(); i++) {
            batch.draw(heartTex,
                    startX + i*(HEART_W+4),
                    startY,
                    HEART_W, HEART_H);
        }
        String nivelStr = "Nivel: " + (nivelActual + 1);
        int minutos = (int)(tiempoTotal / 60);
        int segundos = (int)(tiempoTotal % 60);
        String tiempoStr = String.format("Tiempo: %02d:%02d", minutos, segundos);

        GlyphLayout layoutNivel = new GlyphLayout(hudFont, nivelStr);
        float xNivel = camera.position.x - layoutNivel.width / 2;
        float yNivel = camera.position.y + camera.viewportHeight / 2 - 8;

        hudFont.draw(batch, layoutNivel, xNivel, yNivel);


        hudFont.draw(batch, tiempoStr, camera.position.x + camera.viewportWidth / 2 - 130,
            camera.position.y + camera.viewportHeight / 2 - 8);

        batch.end();
    }

    private void handlePlatformCollision() {
        Rectangle feet = kaladin.getFeetRectangle();
        boolean colisionDetectada = false;

        for (PlatformManager.MovingPlatform plat : platformManager.getPlatforms()) {
            boolean cayendo = kaladin.getVelocityY() <= 0;
            float margenLateral = 20f;
            float margenVertical = 2f;

            float feetX = kaladin.getX() + margenLateral;
            float feetWidth = 64 - 2 * margenLateral;

            boolean sobreX = feetX + feetWidth > plat.bounds.x && feetX < plat.bounds.x + plat.bounds.width;
            boolean sobreY = Math.abs(kaladin.getY() - (plat.bounds.y + plat.bounds.height)) <= margenVertical;

            if (cayendo && sobreX && sobreY) {
                kaladin.land();
                kaladin.setSobrePlataformaMovil(true);
                kaladin.setPlataformaActual(plat);
                colisionDetectada = true;
                break;
            }
        }

        if (!colisionDetectada) {
            kaladin.setSobrePlataformaMovil(false);
            kaladin.setPlataformaActual(null);
        }
    }


    @Override public void dispose () {
        map.dispose();
        renderer.dispose();
        kaladin.dispose();
        heartTex.dispose();
        platformTex.dispose();
        musicaFondo.dispose();
    }




    private void colocarKaladinEnSpawn() {


        MapLayer spawnLayer = map.getLayers().get("spawn");
        if (spawnLayer == null) return;

        // 2. Objeto llamado "spawn"
        MapObject spawnObj = spawnLayer.getObjects().get("spawn");
        if (spawnObj == null) return;


        // Altura de tile: viene en las propiedades globales del mapa
        int tileHeight = map.getProperties().get("tileheight", Integer.class);
        int mapHeightPx = map.getProperties().get("height", Integer.class) * tileHeight;



        float spawnX, spawnY;

        if (spawnObj instanceof RectangleMapObject) {
            Rectangle r = ((RectangleMapObject) spawnObj).getRectangle();
            spawnX = r.x;
            spawnY = mapHeightPx - r.y - r.height;
        } else {
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

    public void setVolume(float v) { this.volumen = v; }

}
