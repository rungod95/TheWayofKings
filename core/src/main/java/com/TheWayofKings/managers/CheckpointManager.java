package com.TheWayofKings.managers;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class CheckpointManager {
    private final Array<Rectangle> boxes = new Array<>();

    public CheckpointManager(TiledMap map) {

        for (MapObject obj : map.getLayers().get("checkpoints").getObjects()) {
            if ("checkpoint".equals(obj.getName())
                && obj instanceof RectangleMapObject) {
                boxes.add(((RectangleMapObject)obj).getRectangle());
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

