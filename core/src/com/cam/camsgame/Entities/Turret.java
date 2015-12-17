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
    public int  nRange, nDamage, nFireRate;
    public TiledMapTileLayer collisionLayer;
    public float fLastTimeShot;

    public Turret(Sprite spTurret, TiledMapTileLayer collisionLayer, int nFireRate, int nDmg) {
        super(spTurret);
        this.collisionLayer = collisionLayer; //Gets the layer from Playscreen
        this.nFireRate = nFireRate;
        this.nDamage = nDmg;
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);
        update();
    }

    public void update() {
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
