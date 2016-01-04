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
    public int  nRange, nDamage, nFireRate, nTurretType;
    public TiledMapTileLayer collisionLayer;
    public float fLastTimeShot;

    public Turret(Sprite spTurret, TiledMapTileLayer collisionLayer, int nFireRate, int nDmg, int nTurretType, int nRange, int nUpgraded) {
        super(spTurret);
        this.collisionLayer = collisionLayer; //Gets the layer from Playscreen
        this.nFireRate = nFireRate;
        this.nDamage = nDmg;
        this.nTurretType = nTurretType;
        this.nRange = nRange;
        if(nTurretType == 0){
            this.nDamage += 1*nUpgraded;
            this.nRange += 100 * nUpgraded;
            this.nFireRate += -50000000;
        }else if(nTurretType == 1){

        }else if(nTurretType == 2){

        }
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
    }


    public boolean checkBounds() { //Not fully working
        float fTileWidth = collisionLayer.getTileWidth(), fTileHeight = collisionLayer.getTileHeight();
        if (collisionLayer.getCell((int) ((getX() + getWidth() / 2) / fTileWidth),
                (int) ((getY() + getHeight()) / fTileHeight)).getTile().getProperties().containsKey("Blocked") == true) {
            return false;
        } else if (collisionLayer.getCell((int) ((getX() + getWidth() / 2) / fTileWidth),
                (int) (getY() / fTileHeight)).getTile().getProperties().containsKey("Blocked") == true) {
            return false;
        } else if (collisionLayer.getCell((int) ((getX() + getWidth() / 2) / fTileWidth),
                (int) ((getY() + getHeight() / 2) / fTileHeight)).getTile().getProperties().containsKey("Blocked") == true) {
            return false;
        } else if (collisionLayer.getCell((int) (getX() / fTileWidth),
                (int) ((getY() + getHeight() / 2) / fTileHeight)).getTile().getProperties().containsKey("Blocked") == true) {
            return false;
        }
        return true;
    }
}
