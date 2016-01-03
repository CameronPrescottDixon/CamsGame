package com.cam.camsgame.Entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Cameron on 2015-11-27.
 */

public class Bullet extends Sprite{
    public int nAntID, nSpeed = 10, nDmg;
    public float fVelX, fVelY;

    public Bullet(Sprite spBullet, int nAntID, int nDmg){
        super(spBullet);
        this.nAntID = nAntID;
        this.nDmg = nDmg;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        setSize(20, 20);
    }
    public void update(float nVelX, float nVelY){
        this.fVelX = nVelX;
        this.fVelY = nVelY;
        setX(getX() + this.fVelX);
        setY(getY() + this.fVelY);


    }
}
