package com.cam.camsgame.Entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import java.util.ArrayList;

/**
 * Created by Cameron on 2015-11-27.
 */
public class Turret extends Sprite {
    public int nBullVel, nRange, nDamage, nCost;
    // public ArrayList<Bullet> arspBullets;
    public TiledMapTileLayer collisionLayer;
    public boolean bPlaceable = true;

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
        float fTileWidth = collisionLayer.getTileWidth(), fTileHeight = collisionLayer.getTileHeight();
//Checks for the upwards direction works
        if (collisionLayer.getCell((int) ((getX() + getWidth() / 2) / fTileWidth),
                (int) ((getY() + getHeight() / 2) / fTileHeight)).getTile().getProperties().containsKey("Blocked") == true && bPlaceable == true) {
            bPlaceable = false;
            System.out.println("BLOCKED " + bPlaceable +" 1");

            //Downwards doesnt work
        } else if (collisionLayer.getCell((int) ((getX() + getWidth() / 2) / fTileWidth),
            (int) ((getY() - getHeight()/2) / fTileHeight)).getTile().getProperties().containsKey("Blocked") == true && bPlaceable == true) {
            bPlaceable = false;
            System.out.println("BLOCKED" + bPlaceable+" 2");

            //Right works
        } else if (collisionLayer.getCell((int) ((getX() + getWidth()) / fTileWidth),
        (int) ((getY() + getHeight() / 2) / fTileHeight)).getTile().getProperties().containsKey("Blocked") == true && bPlaceable == true) {
            bPlaceable = false;
            System.out.println("BLOCKED" + bPlaceable +" 3");

            //Left doesnt't work
        } else if (collisionLayer.getCell((int) ((getX() - getWidth()) / fTileWidth),
        (int) ((getY() + getHeight() / 2) / fTileHeight)).getTile().getProperties().containsKey("Blocked") == true && bPlaceable == true) {
            bPlaceable = false;
            System.out.println("BLOCKED" + bPlaceable+ " 4");
        }
        }



    /*public void shoot(ArrayList<Ants> arspAnt, int i){
        arspBullets = new ArrayList<Bullet>();
        arspBullets.add(new Bullet(new Sprite(new Texture("Entities/Bullet.png"))));
        arspBullets.get(0).shoot((int)arspAnt.get(i).getX(),(int)arspAnt.get(i).getY(), (int)getX() , (int)getY() , arspAnt.get(i).nVelX, arspAnt.get(i).nVelY);
    }*/
    }
