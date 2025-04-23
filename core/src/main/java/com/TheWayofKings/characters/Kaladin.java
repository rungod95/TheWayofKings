package com.TheWayofKings.characters;

import com.TheWayofKings.config.GameConfig;
import com.TheWayofKings.managers.PlatformManager;
import com.TheWayofKings.maps.MapCollisionHelper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import lombok.Setter;

import static com.badlogic.gdx.graphics.Colors.reset;

public class Kaladin {


    @Setter
    private float x, /**
     * -- SETTER --
     * Permite ajustar la posición vertical (para plataformas)
     */
        y;
    private float velocidad = 100f;   // píxeles/seg
    private float velocidadSalto = 250f;
    private float gravedad = -800f;
    private float velocidadY = 0f;
    private float spawnX, spawnY;
    private boolean enElAire = false;
    private boolean mirandoDerecha = true;
    private static final int MAX_SALTOS = 2;
    private int saltosRealizados = 0;
    private int vidas = GameConfig.getInitialLives();
    private float damageCooldown = 0f;
    private static final float INVULN = 1f;
    Sound hurtSnd;
    private boolean sobrePlataformaMovil = false;
    private boolean sobrePlataformaMovilAnterior = false;
    private boolean muerto = false;



    private KaladinAnimationHelper animHelper;
    private Animation<TextureRegion> animacionActual;
    private PlatformManager.MovingPlatform plataformaActual = null;
    private float stateTime;


    private MapCollisionHelper collisionHelper;


    private static final float DRAW_SIZE = 64f;


    /**
     * Se llama una vez al iniciar el juego
     */
    public void create(MapCollisionHelper helper) {
        this.collisionHelper = helper;

        animHelper = new KaladinAnimationHelper();
        animacionActual = animHelper.getIdleAnimation();
        stateTime = 0f;

        hurtSnd = Gdx.audio.newSound(Gdx.files.internal("sfx/hurt.mp3"));

        // La posición inicial nos la dará MainGame vía setPosition()
    }


    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        this.spawnX = x;
        this.spawnY = y;
    }

    /**
     * Lógica de movimiento + animación
     */
    public void update(float delta) {

        /* ========== 1. INPUT + FÍSICA BÁSICA ========================== */

        boolean moviendo = false;
        float nextX = x;

        // 1‑A Movimiento horizontal por teclado
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            nextX += velocidad * delta;
            moviendo = true;
            mirandoDerecha = true;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            nextX -= velocidad * delta;
            moviendo = true;
            mirandoDerecha = false;
        }

        // 1‑B Salto (doble)
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) &&
            saltosRealizados < MAX_SALTOS) {

            velocidadY = velocidadSalto;
            enElAire = true;
            saltosRealizados++;
        }

        // 1‑C Movimiento con plataforma móvil
        if (plataformaActual != null) {
            System.out.printf("Plataforma ΔY: %.5f | Kaladin Y: %.5f%n", plataformaActual.deltaY, y);
            x += plataformaActual.deltaX;
            y += plataformaActual.deltaY;
        }

        // 1‑D Sensor de suelo o plataforma móvil
        if (sobrePlataformaMovil) {
            enElAire = false;
            velocidadY = 0;
        }

        if (!enElAire) {
            final float FOOT_EPS = 1f;
            boolean suelo = collisionHelper.overlapsSolidDown(
                x, y - FOOT_EPS, DRAW_SIZE, FOOT_EPS
            );
            if (!suelo) {
                enElAire = true;
                velocidadY = 0f;
            }
        }

        // 1‑E Gravedad
        if (enElAire && !sobrePlataformaMovil) {
            velocidadY += gravedad * delta;
        }

        float posY = y + velocidadY * delta;

        /* ========== 2. COLISIONES CON CAJA COMPLETA =================== */

        final float width = DRAW_SIZE;
        final float height = DRAW_SIZE;
        final float FOOT_EPS = 1f;
        final float HEAD_EPS = 0.05f;
        final int TILE = 16;

        // 2‑A Colisión horizontal
        if (!collisionHelper.overlapsSolidDown(
            nextX, y + FOOT_EPS, width, height - FOOT_EPS)) {
            x = nextX;
        }

        // 2‑B Colisión vertical
        if (velocidadY >= 0) { // SUBIENDO
            if (!collisionHelper.overlapsSolidUp(
                x, posY, DRAW_SIZE, DRAW_SIZE - HEAD_EPS)) {
                y = posY;
            } else {
                velocidadY = 0;
            }



    } else { // CAYENDO
            if (!collisionHelper.overlapsSolidDown(
                x, posY + FOOT_EPS, DRAW_SIZE, DRAW_SIZE - FOOT_EPS)) {
                y = posY;
                enElAire = true;
            } else {
                y = ((int) (posY / TILE) + 1) * TILE;
                velocidadY = 0;
                enElAire = false;
                saltosRealizados = 0;
            }
        }

        /* ========== 3. ANIMACIÓN ========================= */

        Animation<TextureRegion> anterior = animacionActual;

        if (enElAire) animacionActual = animHelper.getJumpAnimation();
        else if (moviendo) animacionActual = animHelper.getRunAnimation();
        else animacionActual = animHelper.getIdleAnimation();

        if (animacionActual != anterior) stateTime = 0f;
        stateTime += delta;

        if (damageCooldown > 0) damageCooldown -= delta;
    }



    /**
     * Dibuja el frame actual orientado hacia la dirección correcta
     */
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


    public int getVidas() {
        return vidas;
    }

    public void curarVida() {
        if (vidas < 3) vidas++;
    }

    public void quitarVida(int cantidad) {
        if (damageCooldown > 0) return;

        vidas -= cantidad;
        hurtSnd.play(0.7f);
        System.out.println("¡Daño! Vidas restantes = " + vidas);
        damageCooldown = INVULN;

        if (vidas <= 0) {
            muerto = true;
        }
    }


    public boolean isDead() {
        return muerto || y < 0;
    }

    /* ---------- NUEVO: respawn ---------- */
    public void reset() {
        x = spawnX;
        y = spawnY;
        velocidadY = 0;
        enElAire = false;
        muerto = false;
        vidas = 3;
    }




    public void dispose() {
        animHelper.dispose();
        hurtSnd.dispose();
    }

    /* -------- getters para la cámara -------- */
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    /**
     * Para que MainGame sepa si está cayendo
     */
    public float getVelocityY() {
        return velocidadY;
    }

    /**
     * Rectángulo de 1px de alto bajo los pies
     */
    public Rectangle getFeetRectangle() {
        float margenLateral = 20f;
        return new Rectangle(x + margenLateral, y, DRAW_SIZE - 2 * margenLateral, 1f);
    }



    /**
     * Llamar cuando aterriza sobre plataforma móvil
     */
    public void land() {
        enElAire = false;
        velocidadY = 0f;
        saltosRealizados = 0;
    }
    public void setSobrePlataformaMovil(boolean sobre) {
        this.sobrePlataformaMovil = sobre;
    }
    public void setPlataformaActual(PlatformManager.MovingPlatform p) {
        this.plataformaActual = p;
    }



}
