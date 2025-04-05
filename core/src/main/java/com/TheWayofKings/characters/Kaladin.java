package com.TheWayofKings.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Kaladin {
    private Texture spriteSheet;
    private Animation<TextureRegion> walkAnim, runAnim, jumpAnim, attackAnim;
    private Animation<TextureRegion> currentAnim;
    private float stateTime = 0f;
    public float x = 100, y = 240;

    public void create() {
        spriteSheet = new Texture("characters/kaladin_sheet.png");
        TextureRegion[][] tmp = TextureRegion.split(spriteSheet, 256, 256);

        walkAnim = new Animation<>(0.15f, tmp[0]);
        runAnim = new Animation<>(0.10f, tmp[1]);
        jumpAnim = new Animation<>(0.12f, tmp[2]);
        attackAnim = new Animation<>(0.10f, tmp[3]);

        currentAnim = walkAnim;
    }

    public void update(float delta) {
        stateTime += delta;

        // Cambiar animación según input
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            currentAnim = jumpAnim;
        } else if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            currentAnim = attackAnim;
        } else if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            currentAnim = runAnim;
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            currentAnim = walkAnim;
        }

        // Movimiento básico
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) x -= 100 * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) x += 100 * delta;
    }

    public void render(SpriteBatch batch) {
        TextureRegion frame = currentAnim.getKeyFrame(stateTime, true);
        batch.draw(frame, x, y);
    }

    public void dispose() {
        spriteSheet.dispose();
    }
}

