package com.cam.camsgame.Entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Cameron on 2015-11-27.
 */

public class Bullet extends Sprite{
    public int nAntID, nSpeed = 10;
    public float nVelX, nVelY;

    public Bullet(Sprite spBullet, int nAntID){
        super(spBullet);
        this.nAntID = nAntID;
        update(this.nVelX, this.nVelY);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        setSize(20, 20);
    }
    public void update(float nVelX, float nVelY){
        this.nVelX = nVelX;
        this.nVelY = nVelY;

        setX(getX() + this.nVelX);
        setY(getY() + this.nVelY);


    }
}
