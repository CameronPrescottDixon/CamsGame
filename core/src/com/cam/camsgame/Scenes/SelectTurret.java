package com.cam.camsgame.Scenes;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Cameron on 2015-11-22.
 */
public class SelectTurret extends Sprite {
    public int nCost;
    public int nPos;

    public SelectTurret(Sprite spTurret) {
        super(spTurret);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }

    public void update(int nPos, int nCst) {
        nCost = nCst;
        this.nPos = nPos;
        setSize(100, 100);
        setPosition(900, 500 + (nPos * 100));
    }
    public void resetPos(){
        setPosition(900, 500 + (nPos * 100));
    }
}
