package com.TheWayofKings.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class PlatformManager {

    public static class MovingPlatform {
        public Rectangle bounds;
        public TextureRegion region;
        public float startX, startY, range, speed;
        public boolean vertical, forward = true;
        public float deltaX = 0;
        public float deltaY = 0;

        public MovingPlatform(Rectangle bounds, TextureRegion region,
                              boolean vertical, float range, float speed) {
            this.bounds = new Rectangle(bounds);
            this.region = region;
            this.vertical = vertical;
            this.range = range;
            this.speed = speed;
            this.startX = bounds.x;
            this.startY = bounds.y;
        }

        float prevY, prevX;

        public void update(float dt) {
            float delta = speed * dt * (forward ? 1 : -1);
            deltaX = 0;
            deltaY = 0;

            if (vertical) {
                prevY = bounds.y;
                bounds.y += delta;
                if (bounds.y > startY + range) {
                    bounds.y = startY + range;
                    forward = false;
                } else if (bounds.y < startY) {
                    bounds.y = startY;
                    forward = true;
                }
                deltaY = bounds.y - prevY; // ← cambio real del frame
            } else {
                prevX = bounds.x;
                bounds.x += delta;
                if (bounds.x > startX + range) {
                    bounds.x = startX + range;
                    forward = false;
                } else if (bounds.x < startX) {
                    bounds.x = startX;
                    forward = true;
                }
                deltaX = bounds.x - prevX;
            }
        }
    }

        private final Array<MovingPlatform> platforms = new Array<>();

    public PlatformManager(TiledMap map) {
        MapLayer layer = map.getLayers().get("moving_platforms");
        if (layer == null) return;

        MapProperties lp = layer.getProperties();
        boolean vertical = "vertical".equals(lp.get("axis", String.class));
        float range = lp.get("range", Float.class);
        float speed = lp.get("speed", Float.class);

        MapProperties mp = map.getProperties();
        int tileH = mp.get("tileheight", Integer.class);
        int mapHTiles = mp.get("height", Integer.class);
        float mapHpx = mapHTiles * tileH;

        // Agrupar objetos por fila o columna
        Array<TiledMapTileMapObject> objetos = new Array<>();
        for (MapObject obj : layer.getObjects()) {
            if (obj instanceof TiledMapTileMapObject)
                objetos.add((TiledMapTileMapObject) obj);
        }

        objetos.sort((a, b) -> {
            if (vertical) return Float.compare(a.getX(), b.getX());
            else return Float.compare(a.getY(), b.getY());
        });

        while (objetos.size > 0) {
            TiledMapTileMapObject base = objetos.removeIndex(0);
            float bx = base.getX(), by = base.getY();
            float bw = base.getTile().getTextureRegion().getRegionWidth();
            float bh = base.getTile().getTextureRegion().getRegionHeight();
            TextureRegion region = base.getTile().getTextureRegion();

            float minX = bx, maxX = bx + bw;
            float minY = by, maxY = by + bh;

            // Agrupar contiguos
            for (int i = objetos.size - 1; i >= 0; i--) {
                TiledMapTileMapObject o = objetos.get(i);
                float ox = o.getX(), oy = o.getY();

                boolean mismoNivel = vertical ? Math.abs(ox - bx) < 0.1f : Math.abs(oy - by) < 0.1f;
                boolean contiguo = vertical ? oy + bh >= minY && oy <= maxY : ox + bw >= minX && ox <= maxX;

                if (mismoNivel && contiguo) {
                    objetos.removeIndex(i);
                    minX = Math.min(minX, ox);
                    maxX = Math.max(maxX, ox + bw);
                    minY = Math.min(minY, oy);
                    maxY = Math.max(maxY, oy + bh);
                }
            }

            Rectangle r = new Rectangle(
                minX,
                minY,
                maxX - minX,
                maxY - minY
            );

            platforms.add(new MovingPlatform(r, region, vertical, range, speed));
        }

        Gdx.app.log("PlatformManager", "Plataformas agrupadas: " + platforms.size);
    }

    public void update(float dt) {
        for (MovingPlatform p : platforms) p.update(dt);
    }

    public void render(SpriteBatch batch) {
        for (MovingPlatform p : platforms) {
            batch.draw(p.region, p.bounds.x, p.bounds.y, p.bounds.width, p.bounds.height);
        }
    }

    public Rectangle[] getBounds() {
        Rectangle[] arr = new Rectangle[platforms.size];
        for (int i = 0; i < platforms.size; i++) {
            arr[i] = platforms.get(i).bounds;
        }
        return arr;
    }

    public Array<MovingPlatform> getPlatforms() {
        return platforms;
    }
}
