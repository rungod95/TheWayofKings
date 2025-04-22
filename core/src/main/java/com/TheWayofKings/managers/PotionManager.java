// En PotionManager.java actualizado con animación flotante
package com.TheWayofKings.managers;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.TheWayofKings.characters.Kaladin;

public class PotionManager {

    private static class Potion {
        Rectangle bounds;
        TiledMapTileMapObject tileObj;
        float floatTimer = 0;

        public Potion(Rectangle bounds, TiledMapTileMapObject tileObj) {
            this.bounds = bounds;
            this.tileObj = tileObj;
        }
    }

    private final Array<Potion> potions = new Array<>();
    private final Sound sonidoRecoger;

    public PotionManager(TiledMap map, Sound sonidoRecoger) {
        this.sonidoRecoger = sonidoRecoger;
        MapLayer capa = map.getLayers().get("healing_potions");
        if (capa == null) return;

        for (MapObject obj : capa.getObjects()) {
            if (obj instanceof TiledMapTileMapObject) {
                TiledMapTileMapObject tileObj = (TiledMapTileMapObject) obj;
                float x = tileObj.getX();
                float y = tileObj.getY();
                float w = tileObj.getTile().getTextureRegion().getRegionWidth();
                float h = tileObj.getTile().getTextureRegion().getRegionHeight();

                Rectangle r = new Rectangle(x, y, w, h);
                potions.add(new Potion(r, tileObj));
            }
        }
    }

    public void update(Kaladin kaladin) {
        Rectangle kaladinBox = new Rectangle(kaladin.getX(), kaladin.getY(), 64, 64);
        for (int i = 0; i < potions.size; i++) {
            Potion p = potions.get(i);
            p.floatTimer += 0.05f; // para animación de flotación
            if (p.bounds.overlaps(kaladinBox)) {
                kaladin.curarVida();
                sonidoRecoger.play(0.6f);
                potions.removeIndex(i);
                break;
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (Potion p : potions) {
            float offsetY = (float) Math.sin(p.floatTimer) * 2f; // movimiento vertical suave
            batch.draw(p.tileObj.getTile().getTextureRegion(),
                p.bounds.x, p.bounds.y + offsetY,
                p.bounds.width, p.bounds.height);
        }
    }
}
