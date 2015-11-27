package com.cam.camsgame.Entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.cam.camsgame.Scenes.Hud;

/**
 * Created by Cameron on 2015-11-27.
 */
public class Turret extends Sprite {
    public int nBullVel, nRange, nDamage, nCost;
    public Turret(Sprite spTurret){
        super(spTurret);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }
    public void update(int nBullV, int nRan, int nDam, int nCost){

    }
}
