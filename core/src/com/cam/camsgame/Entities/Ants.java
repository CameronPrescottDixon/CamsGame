package com.cam.camsgame.Entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 * Created by Cameron on 2015-11-11.
 */
public class Ants extends Sprite {//https://www.youtube.com/watch?v=NsxNE9uk1ew
    public int nVelY = 0, nVelX = 1;

    private TiledMapTileLayer collisionLayer;

    public Ants(Sprite spAnt, TiledMapTileLayer collisionLayer) {
        super(spAnt);
        this.collisionLayer = collisionLayer;
        rotate(-90);
    }

    @Override
    public void draw(Batch batch) {
        update();
        super.draw(batch);
    }
    public void update() {
        float fTileWidth = collisionLayer.getTileWidth(), fTileHeight = collisionLayer.getTileHeight();
        //Set x
        setX(getX() + nVelX);
        //Set y
        setY(getY() + nVelY);
        // ant is moving right code found at https://www.youtube.com/watch?v=DOpqkaX9844&index=4&list=PLXY8okVWvwZ0qmqSBhOtqYRjzWtUCWylb
            if (collisionLayer.getCell((int) ((getX() + getWidth() / 2) / fTileWidth),
                    (int) ((getY() + getHeight() / 2) / fTileHeight)).getTile().getProperties().containsKey("Up") == true) { //makes it go up
                if (nVelX > 0) {//Checks if it's going right or not
                    rotate(90);
                } else {
                    rotate(-90);
                }
                this.nVelX = 0;
                this.nVelY = 4;
                System.out.println("hi, UP");
            } else if (collisionLayer.getCell((int) ((getX() + getWidth() / 2) / fTileWidth),
                    (int) ((getY() + getHeight() / 2) / fTileHeight)).getTile().getProperties().containsKey("Down") == true) {//Makes it do down
                if (nVelX > 0) {//Checks if it's going right or not
                    rotate(-90);
                } else {
                    rotate(90);
                }
                nVelX = 0;
                nVelY = -4;
                System.out.println("hi, Down");
            } else if (collisionLayer.getCell((int) ((getX() + getWidth() / 2) / fTileWidth),
                    (int) ((getY() + getHeight() / 2) / fTileHeight)).getTile().getProperties().containsKey("Left") == true) {// Makes it go left
                if (nVelY > 0) {//Checks if it's going up or not
                    rotate(90);
                } else {
                    rotate(-90);
                }
                this.nVelX = -4;
                this.nVelY = 0;
                System.out.println("hi, Left");
            } else if (collisionLayer.getCell((int) ((getX() + getWidth() / 2) / fTileWidth),
                    (int) ((getY() + getHeight() / 2) / fTileHeight)).getTile().getProperties().containsKey("Right") == true) {// Makes it go right
                if (nVelY > 0) {//Checks if it's going right or not
                    rotate(-90);
                } else {
                    rotate(90);
                }
                nVelX = 4;
                nVelY = 0;
                System.out.println("hi, Right");
            } else if (collisionLayer.getCell((int) ((getX() + getWidth() / 2) / fTileWidth),
                    (int) ((getY() + getHeight() / 2) / fTileHeight)).getTile().getProperties().containsKey("End") == true) {// Checks if its at the end
                nVelY = 0;
                nVelX = 0;
            }
        }
    @Override
    public void rotate(float degrees) {
        super.rotate(degrees);
    }
}
