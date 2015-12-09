package com.cam.camsgame.Entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Cameron on 2015-11-27.
 */

public class Bullet extends Sprite{

    public int nAntID;

    public Bullet(Sprite spBullet, int nAntID){
        super(spBullet);
        this.nAntID = nAntID;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        setSize(10, 10);
    }
}
