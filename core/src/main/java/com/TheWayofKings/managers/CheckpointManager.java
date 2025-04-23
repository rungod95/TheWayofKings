package com.TheWayofKings.managers;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import lombok.Getter;

public class CheckpointManager {
    private final Array<Rectangle> boxes = new Array<>();
    private final TiledMap map;
    @Getter
    private MapObject finalCheckpoint;

    public CheckpointManager(TiledMap map) {
        this.map = map;

        for (MapObject obj : map.getLayers().get("checkpoints").getObjects()) {
            String tipo = obj.getProperties().get("type", String.class);

            if ("checkpoint".equals(tipo) && obj instanceof RectangleMapObject) {
                boxes.add(((RectangleMapObject)obj).getRectangle());
            }

            if ("checkpoint_final".equals(tipo)) {
                this.finalCheckpoint = obj;
            }
        }
    }
    public boolean reached(float x, float y, float w, float h) {
        Rectangle playerBox = new Rectangle(x, y, w, h);
        for (Rectangle cp : boxes) {
            if (cp.overlaps(playerBox)) return true;
        }
        return false;
    }


}

