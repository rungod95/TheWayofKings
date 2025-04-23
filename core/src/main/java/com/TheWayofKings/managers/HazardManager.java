package com.TheWayofKings.managers;

import com.TheWayofKings.characters.Kaladin;
import com.TheWayofKings.config.GameConfig;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class HazardManager {

    private final Array<Rectangle> hazardBoxes = new Array<>();
    private final float playerW, playerH;


    public HazardManager(TiledMap map, float playerW, float playerH) {
        this.playerW = playerW;
        this.playerH = playerH;

        // lee la capa de objetos

        for (MapObject o : map.getLayers().get("hazard_rects").getObjects()) {
            if ("hazard".equals(o.getProperties().get("type", String.class))
                && o instanceof RectangleMapObject) {

                hazardBoxes.add(((RectangleMapObject) o).getRectangle());
            }
        }
        System.out.println("Hazards cargados: " + hazardBoxes.size);   // debug
    }

    // HazardManager.java
    public void update(Kaladin k) {
        final float FEET_H   = 100f;   // alto del rectángulo de pies
        final float MARGIN_X = 30f;    // margen lateral

        Rectangle feet = new Rectangle(
            k.getX() + MARGIN_X,      // un poco más estrecho
            k.getY(),                 // parte baja del sprite
            64 - 2*MARGIN_X,          // ancho
            FEET_H);                  // solo los pies

        for (Rectangle r : hazardBoxes) {
            if (r.overlaps(feet)) {
                k.quitarVida(GameConfig.getEnemyDamage());
                break;
            }
        }
    }

}


