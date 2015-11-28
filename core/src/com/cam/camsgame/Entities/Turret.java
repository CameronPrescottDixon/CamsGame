package com.cam.camsgame.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayList;

/**
 * Created by Cameron on 2015-11-27.
 */
public class Turret extends Sprite {
    public int nBullVel, nRange, nDamage, nCost;
   // public ArrayList<Bullet> arspBullets;
    public Turret(Sprite spTurret){
        super(spTurret);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }
    public void update(int nBullV, int nRan, int nDam, int nCost){

    }
    /*public void shoot(ArrayList<Ants> arspAnt, int i){
        arspBullets = new ArrayList<Bullet>();
        arspBullets.add(new Bullet(new Sprite(new Texture("Entities/Bullet.png"))));
        arspBullets.get(0).shoot((int)arspAnt.get(i).getX(),(int)arspAnt.get(i).getY(), (int)getX() , (int)getY() , arspAnt.get(i).nVelX, arspAnt.get(i).nVelY);
    }*/
}
