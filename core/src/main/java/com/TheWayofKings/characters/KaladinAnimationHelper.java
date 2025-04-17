package com.TheWayofKings.characters;

import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.utils.*;
import lombok.Getter;

public class KaladinAnimationHelper {

    private Texture kaladinTexture;
    @Getter
    private Animation<TextureRegion> idleAnimation;
    @Getter
    private Animation<TextureRegion> runAnimation;
    @Getter
    private Animation<TextureRegion> jumpAnimation;
    @Getter
    private Animation<TextureRegion> doubleJumpAnimation;

    public KaladinAnimationHelper() {
        kaladinTexture = new Texture("characters/Crusader.png");
        TextureRegion[][] frames = TextureRegion.split(kaladinTexture, 64, 64);

        // Idle  – 1 frame totalmente estático
        idleAnimation = new Animation<TextureRegion>(
            10f,               // duración enorme (nunca avanza)
            frames[19][0]      // fila 19, columna 0
        );


        idleAnimation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);

        /* -------- RUN (fila 19, col 4‑9) --------------------------- */
        TextureRegion[] run = new TextureRegion[6];
        for (int c = 4; c <= 9; c++) run[c-4] = frames[19][c];
        runAnimation = new Animation<TextureRegion>(0.08f, run);

        /* -------- JUMP (fila 18, col 0) ---------------------------- */
        jumpAnimation = new Animation<TextureRegion>(
            0.2f,
            frames[18][0]
        );

        /* -------- DOBLE‑SALTO (opcional) fila 18 col 3 ------------- */
        doubleJumpAnimation = new Animation<TextureRegion>(
            0.2f,
            frames[18][3]
        );
    }


    public void dispose() {
        kaladinTexture.dispose();
    }
}
