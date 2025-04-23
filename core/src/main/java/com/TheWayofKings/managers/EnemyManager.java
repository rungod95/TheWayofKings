package com.TheWayofKings.managers;

import com.TheWayofKings.characters.Enemy;
import com.TheWayofKings.characters.Kaladin;
import com.TheWayofKings.config.GameConfig;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class EnemyManager {

    private Array<Enemy> enemies = new Array<>();

    public EnemyManager(TiledMap map, Texture tex) {
        MapLayer layer = map.getLayers().get("enemy_spawns");
        if (layer == null) return;

        for (MapObject obj : layer.getObjects()) {
            if (obj instanceof RectangleMapObject) {
                RectangleMapObject rectObj = (RectangleMapObject) obj;
                {
                Rectangle r = rectObj.getRectangle();

                    float w = 52f;
                    float h = 52f;
                    float x = r.x;
                    float y = r.y;
                    float minX = r.x;
                    float maxX = r.x + r.width;


                    enemies.add(new Enemy(x, y, w, h, minX, maxX, tex));
            }
        }
    }
    }

    public void update(float dt, Kaladin kaladin) {
        for (Enemy e : enemies) {
            e.update(dt);
            Rectangle bounds = e.getBounds();
            Rectangle kaladinBox = new Rectangle(kaladin.getX(), kaladin.getY(), 64, 64);

            if (bounds.overlaps(kaladinBox)) {
                kaladin.quitarVida(GameConfig.getEnemyDamage());
            }


        }
    }

    public void render(SpriteBatch batch) {
        for (Enemy e : enemies) {
            e.render(batch);
        }
    }
}
