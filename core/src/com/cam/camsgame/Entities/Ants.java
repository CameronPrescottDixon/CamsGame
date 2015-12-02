package com.cam.camsgame.Entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.cam.camsgame.CamsGame;
import com.cam.camsgame.Screens.PlayScreen;

/**
 * Created by Cameron on 2015-11-11.
 */
public class Ants extends Sprite {//https://www.youtube.com/watch?v=NsxNE9uk1ew
    public int nVelY = 0, nVelX, nSpeed, nDamage;
    public boolean bFinished = false;
    private TiledMapTileLayer collisionLayer;

    public Ants(Sprite spAnt, TiledMapTileLayer collisionLayer, int nSpeed, int nDamage, int nHP) {
        super(spAnt);
        this.collisionLayer = collisionLayer; //Gets the layer from Playscreen
        this.nSpeed = nSpeed;//Gets the speed from playscreen since ants can be different
        nVelX = this.nSpeed;//Sets the initial velocity to the speed or it would never move..
        this.nDamage = nDamage;
        rotate(-90);//Sets the initial rotation so the ants don't move sideways
    }

    @Override
    public void draw(Batch batch) {
        update();
        super.draw(batch);
    }
    public void update() {
        float fTileWidth = collisionLayer.getTileWidth(), fTileHeight = collisionLayer.getTileHeight();
        //Moves the ant
        setX(getX() + nVelX);
        setY(getY() + nVelY);
        if (getX() > 0) {
            //Looks for collision for specific cells, it only looks for the cell directly in it's path to increase performance instead of looking for every cell on the map
            if (nVelX > 0 || nVelX < 0) { //Checks if it's moving in the x direction or not so it increases efficiency by no looking for y direction related things
                // ant is moving right code found at https://www.youtube.com/watch?v=DOpqkaX9844&index=4&list=PLXY8okVWvwZ0qmqSBhOtqYRjzWtUCWylb
                if (collisionLayer.getCell((int) ((getX() + getWidth() / 2) / fTileWidth),
                        (int) ((getY() + getHeight() / 2) / fTileHeight)).getTile().getProperties().containsKey("Up") == true) { //makes it go up
                    if (nVelX > 0) {//Checks if it's going right or not
                        rotate(90);
                    } else if (nVelX < 0) { // An else statement messes up the code since the velocity would be 0 which is not > 0 but not < 0
                        rotate(-90);
                    }
                    this.nVelX = 0;
                    this.nVelY = nSpeed;
                } else if (collisionLayer.getCell((int) ((getX() + getWidth() / 2) / fTileWidth),
                        (int) ((getY() + getHeight() / 2) / fTileHeight)).getTile().getProperties().containsKey("Down") == true) {//Makes it do down
                    if (nVelX > 0) {//Checks if it's going right or not
                        rotate(-90);
                    } else if (nVelX < 0) {
                        rotate(90);
                    }
                    nVelX = 0;
                    nVelY = -nSpeed;
                }
            } else if (nVelY < 0 || nVelY > 0) {//Checks if it's moving in the y direction or not so it increases efficienct by no looking for x direction related things
                if (collisionLayer.getCell((int) ((getX() + getWidth() / 2) / fTileWidth),
                        (int) ((getY() + getHeight() / 2) / fTileHeight)).getTile().getProperties().containsKey("Left") == true) {// Makes it go left
                    if (nVelY > 0) {//Checks if it's going up or not
                        rotate(90);
                    } else if (nVelY < 0) {
                        rotate(-90);
                    }
                    this.nVelX = -nSpeed;
                    this.nVelY = 0;
                } else if (collisionLayer.getCell((int) ((getX() + getWidth() / 2) / fTileWidth),
                        (int) ((getY() + getHeight() / 2) / fTileHeight)).getTile().getProperties().containsKey("Right") == true) {// Makes it go right
                    if (nVelY > 0) {//Checks if it's going right or not
                        rotate(-90);
                    } else if (nVelY < 0) {
                        rotate(90);
                    }
                    nVelX = nSpeed;
                    nVelY = 0;
                } else if (collisionLayer.getCell((int) ((getX() + getWidth() / 2) / fTileWidth),
                        (int) ((getY() + getHeight() / 2) / fTileHeight)).getTile().getProperties().containsKey("End") == true) {// Checks if its at the end
                    nVelY = 0;
                    nVelX = 0;
                    bFinished = true;
                }
            }
        }
    }
    @Override
    public void rotate(float degrees) { //Simply rotates the ant based on the degree passed when the method is called
        super.rotate(degrees);
    }
}
