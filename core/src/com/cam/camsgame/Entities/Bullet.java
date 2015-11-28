package com.cam.camsgame.Entities;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Cameron on 2015-11-27.
 */
public class Bullet extends Sprite{
    float dx, dy;

    public Bullet(Sprite spBullet){
        super(spBullet);
    }

    public void shoot(int nAntX, int nAntY, int nTurX, int nTurY, int nAntVelX, int nAntVelY){
        dx =  nTurX - nAntX;
        dy = nTurY - nAntY;
        float norm = (float) Math.sqrt(dx * dx + dy * dy);  //http://gamedev.stackexchange.com/questions/46565/how-to-get-enemy-to-follow-character-using-angles-atan2
        dx *= (nAntVelX/ norm);
        dy *= (nAntVelY / norm);
    }
}
