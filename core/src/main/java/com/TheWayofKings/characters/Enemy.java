package com.TheWayofKings.characters;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Enemy {
    private float x, y, w, h;
    private float minX, maxX;
    private boolean movingRight = true;
    private float speed = 60f;
    private Texture tex;

    public Enemy(float x, float y, float w, float h, float minX, float maxX, Texture tex) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.minX = minX;
        this.maxX = maxX;
        this.tex = tex;
    }

    public void update(float dt) {
        float dx = speed * dt * (movingRight ? 1 : -1);
        x += dx;

        if (x < minX) {
            x = minX;
            movingRight = true;
        } else if (x + w > maxX) {
            x = maxX - w;
            movingRight = false;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(tex, x, y, w, h);
    }

    public Rectangle getBounds() {
        float hitboxW = 10f;
        float hitboxH = 10f;
        float offsetX = (w - hitboxW) / 2f;
        float offsetY = (h - hitboxH) / 2f;

        return new Rectangle(
            x + offsetX,
            y + offsetY,
            hitboxW,
            hitboxH
        );
    }
}
