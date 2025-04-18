package com.TheWayofKings.characters;

import com.TheWayofKings.maps.MapCollisionHelper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Kaladin {


    private float x, y;
    private float velocidad       = 100f;   // píxeles/seg
    private float velocidadSalto  = 250f;
    private float gravedad        = -800f;
    private float velocidadY      = 0f;
    private float spawnX, spawnY;
    private boolean enElAire       = false;
    private boolean mirandoDerecha = true;
    private static final int MAX_SALTOS = 2;
    private int saltosRealizados = 0;



    private KaladinAnimationHelper animHelper;
    private Animation<TextureRegion> animacionActual;
    private float stateTime;


    private MapCollisionHelper collisionHelper;


    private static final float DRAW_SIZE = 64f;



    /** Se llama una vez al iniciar el juego */
    public void create(MapCollisionHelper helper) {
        this.collisionHelper = helper;

        animHelper      = new KaladinAnimationHelper();
        animacionActual = animHelper.getIdleAnimation();
        stateTime       = 0f;

        // La posición inicial nos la dará MainGame vía setPosition()
    }


    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        this.spawnX = x;
        this.spawnY = y;
    }

    /** Lógica de movimiento + animación */
    public void update(float delta) {

        /* ========== 1. INPUT + FÍSICA BÁSICA ========================== */

        boolean moviendo = false;
        float nextX = x;
        float nextY = y;

        // 1‑A  Movimiento horizontal por teclado
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            nextX += velocidad * delta;
            moviendo = true;
            mirandoDerecha = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            nextX -= velocidad * delta;
            moviendo = true;
            mirandoDerecha = false;
        }

        // 1‑B  Salto (doble)
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) &&
            saltosRealizados < MAX_SALTOS) {

            velocidadY = velocidadSalto;
            enElAire = true;
            saltosRealizados++;
        }

        /* --- SENSOR DE SUELO ----------------------------------------- */
        if (!enElAire) {
            final float width  = DRAW_SIZE;
            final float FOOT_EPS = 1f;
            // ¿sigue habiendo bloque 1 px bajo los pies?
            boolean suelo = collisionHelper.overlapsSolid(
                x,              y - FOOT_EPS,
                width, FOOT_EPS);
            if (!suelo) {
                enElAire = true;         // ya no hay suelo → comienza la caída
                velocidadY = 0;          // parte de reposo
            }
        }



        // 1‑C  Gravedad continua
        if (enElAire) {                          // ← añade esta condición
            velocidadY += gravedad * delta;
        }
        nextY += velocidadY * delta;


        /* ========== 2. COLISIONES CON CAJA COMPLETA =================== */

        final float width  = DRAW_SIZE;
        final float height = DRAW_SIZE;
        final float FOOT_EPS = 1f;   // hueco bajo los pies para no pegarse
        final float HEAD_EPS = 2f;   // hueco sobre la cabeza
        final int   TILE     = 16;   // tamaño de tile de tu mapa

        // 2‑A  Horizontal (prueba caja desplazada en X)
        if (!collisionHelper.overlapsSolid(nextX, y + FOOT_EPS,
            width, height - FOOT_EPS)) {
            x = nextX;
        }

        // 2‑B  Vertical (primero subida, después bajada)
        if (velocidadY >= 0) {                       // subiendo
            if (!collisionHelper.overlapsSolid(x, nextY,
                width, height - HEAD_EPS)) {
                y = nextY;
            } else {
                velocidadY = 0;                     // golpea techo
            }
        } else { // cayendo
            if (!collisionHelper.overlapsSolid(x, nextY + FOOT_EPS,
                width, height - FOOT_EPS)) {
                y = nextY;
                enElAire = true;
            } else {                         // ← aquí aterriza
                y = ((int)(nextY / TILE) + 1) * TILE;
                velocidadY = 0;
                enElAire = false;
                saltosRealizados = 0;        // <<< reinicia contador
            }
        }


        /* ========== 3. SELECCIÓN DE ANIMACIÓN ========================= */
        Animation<TextureRegion> anterior = animacionActual;   // ① guarda la actual

        if (enElAire)          animacionActual = animHelper.getJumpAnimation();
        else if (moviendo)     animacionActual = animHelper.getRunAnimation();
        else                   animacionActual = animHelper.getIdleAnimation();

        /* ② si ha cambiado, reinicia el reloj */
        if (animacionActual != anterior) stateTime = 0f;

        stateTime += delta;                                    // ③ sigue contando

    }


    /** Dibuja el frame actual orientado hacia la dirección correcta */
    public void render(SpriteBatch batch) {

        TextureRegion frame;
        if (animacionActual == animHelper.getIdleAnimation()) {
            frame = animacionActual.getKeyFrame(0f, false);   // ← SIEMPRE primer frame
        } else {
            frame = animacionActual.getKeyFrame(stateTime, true);
        }



    // voltear según la dirección
        if (!mirandoDerecha && !frame.isFlipX()) {
            frame.flip(true, false);
        } else if (mirandoDerecha && frame.isFlipX()) {
            frame.flip(true, false);
        }

        batch.draw(frame, x, y, DRAW_SIZE, DRAW_SIZE);
    }
    public boolean isDead() { return y < 0; }


    /* ---------- NUEVO: respawn ---------- */
    public void reset() {
        x = spawnX;
        y = spawnY;
        velocidadY = 0;
        enElAire = false;
    }



    public void dispose() {
        animHelper.dispose();
    }

    /* -------- getters para la cámara -------- */
    public float getX() { return x; }
    public float getY() { return y; }
}
