package com.TheWayofKings.maps;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class MapCollisionHelper {

    private final TiledMapTileLayer layer;

    public MapCollisionHelper(TiledMap map) {
        // capa «colisiones» con tiles type=solid o type=platform
        this.layer = (TiledMapTileLayer) map.getLayers().get("colisiones");
    }
    // Solo bloquea plataformas (para detectar aterrizaje desde arriba)
    public boolean overlapsOnlyPlatformDown(float x, float y, float w, float h) {
        return overlapsTypes(x, y, w, h, "platform");
    }


    public boolean overlapsSolidUp(float x, float y, float w, float h) {
        return overlapsTypes(x, y, w, h, "solid", "platform");
    }

    /** Bloquea muros y plataformas (type=solid o type=platform). Para el feet‑check. */
    public boolean overlapsSolidDown(float x, float y, float w, float h) {
        return overlapsTypes(x, y, w, h, "solid", "platform");
    }

    /** Recorre la caja [x,y,w,h] y devuelve true si algún tile tiene uno de los allowedTypes. */
    private boolean overlapsTypes(float x, float y, float w, float h, String... types) {
        int tw = layer.getTileWidth(), th = layer.getTileHeight();
        int startX = Math.max(0, (int)(x    / tw));
        int endX   = Math.min(layer.getWidth()-1,  (int)((x+w-1)/tw));
        int startY = Math.max(0, (int)(y    / th));
        int endY   = Math.min(layer.getHeight()-1, (int)((y+h-1)/th));

        for (int ty = startY; ty <= endY; ty++) {
            for (int tx = startX; tx <= endX; tx++) {
                Cell c = layer.getCell(tx, ty);
                if (c!=null && c.getTile()!=null) {
                    String t = c.getTile().getProperties().get("type", String.class);
                    for (String want: types) {
                        if (want.equals(t)) return true;
                    }
                }
            }
        }
        return false;
    }
}








