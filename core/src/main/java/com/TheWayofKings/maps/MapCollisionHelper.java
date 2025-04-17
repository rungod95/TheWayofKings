package com.TheWayofKings.maps;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.*;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class MapCollisionHelper {

    private TiledMap map;
    private TiledMapTileLayer collisionLayer;

    public MapCollisionHelper(TiledMap map) {
        this.map = map;
        this.collisionLayer = (TiledMapTileLayer) map.getLayers().get("colisiones");
    }

    public boolean overlapsSolid(float x, float y, float w, float h) {
        int tileW = collisionLayer.getTileWidth();
        int tileH = collisionLayer.getTileHeight();


        int startX = Math.max(0, (int) (x        / tileW));
        int endX   = Math.min(collisionLayer.getWidth()  - 1,
            (int) ((x + w - 1) / tileW));

        int startY = Math.max(0, (int) (y        / tileH));
        int endY   = Math.min(collisionLayer.getHeight() - 1,
            (int) ((y + h - 1) / tileH));



        for (int ty = startY; ty <= endY; ty++) {
            for (int tx = startX; tx <= endX; tx++) {
                if (isSolidTile(tx * tileW, ty * tileH)) return true;
            }
        }
        return false;
    }


    public boolean isSolidTile(float worldX, float worldY) {
        int tileW  = collisionLayer.getTileWidth();
        int tileH  = collisionLayer.getTileHeight();
        int tileX  = (int) (worldX / tileW);
        int tileY  = (int) (worldY / tileH);


        // ← nueva barrera en los cuatro bordes
        if (tileX < 0 || tileX >= collisionLayer.getWidth()
            || tileY < 0 || tileY >= collisionLayer.getHeight()) {
            return true;          // se considera sólido
        }

        Cell cell = collisionLayer.getCell(tileX, tileY);
        return cell != null
            && cell.getTile() != null
            && "solid".equals(cell.getTile()
            .getProperties()
            .get("type", String.class));
    }






    public boolean isHazardTile(float worldX, float worldY) {
        Cell cell = getCell(worldX, worldY);
        return cell != null && hasProperty(cell, "hazard");
    }

    private Cell getCell(float worldX, float worldY) {
        int tileX = (int) (worldX / collisionLayer.getTileWidth());
        int tileY = (int) (worldY / collisionLayer.getTileHeight());
        return collisionLayer.getCell(tileX, tileY);
    }

    private boolean hasProperty(Cell cell, String typeValue) {
        MapProperties props = cell.getTile().getProperties();
        return typeValue.equals(props.get("type", String.class));
    }
}
