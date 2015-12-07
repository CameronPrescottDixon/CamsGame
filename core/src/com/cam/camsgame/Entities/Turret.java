package com.cam.camsgame.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.ArrayList;

/**
 * Created by Cameron on 2015-11-27.
 */
public class Turret extends Sprite {
    public int nBullVel, nRange, nDamage, nCost;
    public ArrayList<Bullet> arspBullets;
    public TiledMapTileLayer collisionLayer;

    public Turret(Sprite spTurret, TiledMapTileLayer collisionLayer) {
        super(spTurret);
        this.collisionLayer = collisionLayer; //Gets the layer from Playscreen
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        update();
    }

    public void update() {

    }

    public void targetAnts(ArrayList<Ants> arspAnts) {
        float fX, fY;
        for (int i = 0; i < arspAnts.size(); i++) {
            fX = Math.abs(this.getX() - arspAnts.get(i).getX());
            fY = Math.abs(this.getY() - arspAnts.get(i).getY());
            if ((Math.pow(fX, 2) + Math.pow(fY, 2)) <= nRange) {
               // shoot();
            }
        }
    }


    public void shoot(ArrayList<Ants> arspAnt, int i) {
        arspBullets = new ArrayList<Bullet>();
        arspBullets.add(new Bullet(new Sprite(new Texture("Entities/Bullet.png"))));
        arspBullets.get(0).shoot((int) arspAnt.get(i).getX(), (int) arspAnt.get(i).getY(), (int) getX(), (int) getY(), arspAnt.get(i).nVelX, arspAnt.get(i).nVelY);
    }

    public boolean checkBounds() {
        float fTileWidth = collisionLayer.getTileWidth(), fTileHeight = collisionLayer.getTileHeight();
        //Checks for the upwards direction works
        if (collisionLayer.getCell((int) ((getX() + getWidth() / 2) / fTileWidth),
                (int) ((getY() + getHeight() / 2) / fTileHeight)).getTile().getProperties().containsKey("Blocked") == true) {
            return false;
            //Downwards somewhat work
        } else if (collisionLayer.getCell((int) ((getX() + getWidth() / 2) / fTileWidth),
                (int) ((getY() - (getHeight() / 2)) / fTileHeight)).getTile().getProperties().containsKey("Blocked") == true) {
            return false;
            //Right works
        } else if (collisionLayer.getCell((int) ((getX() + getWidth() / 2) / fTileWidth),
                (int) ((getY() + getHeight() / 2) / fTileHeight)).getTile().getProperties().containsKey("Blocked") == true) {
            return false;
            //Left somewhat works
        } else if (collisionLayer.getCell((int) (getX() / fTileWidth),
                (int) ((getY() + getHeight() / 2) / fTileHeight)).getTile().getProperties().containsKey("Blocked") == true) {
            return false;
        }
        return true;
    }
}
