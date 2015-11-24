package com.cam.camsgame.Entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Cameron on 2015-11-11.
 */
public class Ants extends Sprite {//https://www.youtube.com/watch?v=NsxNE9uk1ew
    public int nVelY=0, nVelX=0;

    public Ants(Sprite spAnt){
        super(spAnt);
    }

    @Override
    public void draw(Batch batch) {
        update();
        super.draw(batch);
    }
    public void update(){
        setX(getX()+ nVelX);
        setY(getY()+ nVelY);
    }
}
