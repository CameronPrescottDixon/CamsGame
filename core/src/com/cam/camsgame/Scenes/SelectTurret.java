package com.cam.camsgame.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Cameron on 2015-11-22.
 */
public class SelectTurret extends Sprite {
    public SelectTurret(Sprite spTurret) {
        super(spTurret);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }
    public void update(int nPos, int nCost){
        setSize(100,100);
        setPosition(Gdx.graphics.getWidth() - 100, Gdx.graphics.getHeight() /2 + (nPos * 100));
    }
}
