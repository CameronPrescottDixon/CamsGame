package com.cam.camsgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.cam.camsgame.Entities.Ants;
import com.cam.camsgame.CamsGame;
import com.cam.camsgame.Entities.Bullet;
import com.cam.camsgame.Entities.Turret;
import com.cam.camsgame.Scenes.SelectTurret;
import com.cam.camsgame.Scenes.Hud;
import java.util.ArrayList;

/**
 * Created by Cameron on 2015-11-04.
 */
public class PlayScreen implements Screen {
    private CamsGame game;

    //Camera stuff
    private OrthographicCamera gamecam;
    private StretchViewport gameport; //https://www.youtube.com/watch?v=D7u5B2Oh9r0 <-- maintaining aspect ratios

    //Hud
    public Hud hud;

    //Tiled
    private TmxMapLoader mapLoader; //https://www.youtube.com/watch?v=P8jgD-V5jG8 <-- how the map is loaded
    private TiledMap tlMap;
    private OrthogonalTiledMapRenderer tlRender;

    //Ants + test money for hud
    private ArrayList<Ants> arspAnt;
    int nID = 0;
    boolean bGameOver;
    float timeSinceCollision = 0;

    //Music
    private Music music;

    //Touch position in relation to world
    private Vector3 vtouchPos;

    //Turrets + side panel
    private ArrayList<Turret> arspTurret;
    private ArrayList<SelectTurret> arspTurrs;
    private int nTurSelected = 0;
    private Sprite spSidePanel;
    private Sprite spTurSelect;
    private boolean bTurSelect;

    //Bullets
    private ArrayList<Bullet> arspBullets;

    public PlayScreen(CamsGame game) {
        this.game = game;
        gamecam = new OrthographicCamera();

        //Create the viewport to keep a specific aspect ratio
        gameport = new StretchViewport(CamsGame.V_WIDTH, CamsGame.V_HEIGHT, gamecam);

        //Create the hud
        hud = new Hud(game.batch);

        //Loads tiled map
        mapLoader = new TmxMapLoader();

        //Load tiled map
        tlMap = mapLoader.load("Maps/Map.tmx");
        tlRender = new OrthogonalTiledMapRenderer(tlMap);

        //Ants
        arspAnt = new ArrayList<Ants>();
        bGameOver = false;
        nextRound(); //Initializes the game, without this the rounds wouldn't start

        //Turrets, an array is better for looking and listening for which is clicked
        arspTurrs = new ArrayList<SelectTurret>();
        arspTurrs.add(new SelectTurret(new Sprite(new Texture("Entities/raidant.png"))));
        arspTurrs.get(0).update(3, 300);
        arspTurrs.add(new SelectTurret(new Sprite(new Texture("Entities/raidfly.png"))));
        arspTurrs.get(1).update(1, 400);
        arspTurrs.add(new SelectTurret(new Sprite(new Texture("Entities/RAIDBIG.png"))));
        arspTurrs.get(2).update(-1, 600);
        arspTurrs.add(new SelectTurret(new Sprite(new Texture("Entities/RAIDMAX.png"))));
        arspTurrs.get(3).update(-3, 900);

        //Turrets
        arspTurret = new ArrayList<Turret>();

        //Side panel for the turrets to sit on instead of the map
        spSidePanel = new Sprite(new Texture("SidePanel.jpg"));
        spSidePanel.setPosition(Gdx.graphics.getWidth() - 100, 0);
        spSidePanel.setSize(100, 1000);

        //Shows the selected turret by adding this red box behind it
        spTurSelect = new Sprite(new Texture("red.png"));
        spTurSelect.setSize(100, 120);
        spTurSelect.setPosition(arspTurrs.get(0).getX(), arspTurrs.get(0).getY() - 10);
        bTurSelect = false; //Makes it so the box isnt drawn before a turret is selected


        //Set the gamecams position to half of the width and height of the map (the center of the map)
        gamecam.position.set(gameport.getWorldWidth() / 2, gameport.getWorldHeight() / 2, 0);

        //Get music
        music = CamsGame.manager.get("Music/LetTheBodiesHitTheFloor.mp3", Music.class); //This song is good
        music.setLooping(true);//Loop it
        //Makes the music volume lower so it's not destroying the users ears
        music.setVolume(music.getVolume() * 1 / 10);
        music.play();//play it

        vtouchPos = new Vector3();//fixes the errors with flipping the y co-ords on the x-axis

        //Bullets
        arspBullets = new ArrayList<Bullet>();
    }

    @Override
    public void show() {

    }

    public void update(float dt) {
        gamecam.update();
        //Only renders what the gamecam can see
        tlRender.setView(gamecam);
        hud.updateTime(dt);
        removeAnt();//checks if ants reach the end
        targetAnts(dt);//Sends the ant array to the turrets to shoot them
        if (arspBullets.size() > 0) {
            shoot();
        }
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT))
            onClick();//only passes it when theres a click
    }

    @Override
    public void render(float dt) {
        //Calls update to instantly update to the map
        update(dt); // Sends deltaTime to the update function to be sent to other methods that require it
        // renders the map
        tlRender.render();
        //Gets whats shown by our huds camera
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        //Draws the hud to the screen
        hud.stage.draw();
        //Draw ant
        tlRender.getBatch().begin(); //Draw the sprites, must be in order to draw them one ontop of each other
        for (int i = 0; i < arspAnt.size(); i++) {
            arspAnt.get(i).draw(tlRender.getBatch());
        }
        for (int i = 0; i < arspTurret.size(); i++) arspTurret.get(i).draw(tlRender.getBatch());
        spSidePanel.draw(tlRender.getBatch());
        if (bTurSelect != false) {//makes it so the red box isnt drawn from the start even if none of the turrets are selected
            spTurSelect.draw(tlRender.getBatch());
        }
        for (int i = 0; i < 4; i++) arspTurrs.get(i).draw(tlRender.getBatch());
        for(int i = 0; i<arspBullets.size();i++)arspBullets.get(i).draw(tlRender.getBatch());
        tlRender.getBatch().end();
    }

    public void onClick() { //Helps keep render function clean instead of packing all of this into it
        vtouchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        //Basically this translates the co-ords I got by the input into world space throught the vector3 position set by input...
        gamecam.unproject(vtouchPos);//http://gamedev.stackexchange.com/questions/60787/libgdx-drawing-sprites-when-moving-orthographic-camera fixes the issues with touching + co-ords
        for (int i = 0; i < 4; i++) {
            if (vtouchPos.x >= arspTurrs.get(0).getX()) { // looks for in the click is in the turret select portion of the screen
                if (vtouchPos.y >= arspTurrs.get(i).getY() && vtouchPos.y < arspTurrs.get(i).getY() + arspTurrs.get(i).getHeight()) {// Using arrays schortened this code by 3/4!!!
                    spTurSelect.setPosition(arspTurrs.get(i).getX(), arspTurrs.get(i).getY() - 10); // -10 because the red box is 120 pixels in height but the turrets are only 100
                    nTurSelected = i;
                    bTurSelect = true;
                    break;
                }
            } else if (vtouchPos.x < arspTurrs.get(i).getX()) { //looks for clicks off of the turret select panel
                if (bTurSelect == true) {
                    addTurret();
                    bTurSelect = false;
                }
            }
        }
    }

    private void addTurret() { //Adds turret when clicked
        if (arspTurrs.get(nTurSelected).nCost <= hud.nMoney) {
            if (nTurSelected == 0) {//Adds the turret with the specific image, this also helps reduce total code when it's in a method
                arspTurret.add(new Turret(new Sprite(new Texture("Entities/can_topred.png")), (TiledMapTileLayer) tlMap.getLayers().get(0),2));
            } else if (nTurSelected == 1) {
                arspTurret.add(new Turret(new Sprite(new Texture("Entities/can_topblue.png")), (TiledMapTileLayer) tlMap.getLayers().get(0),1));
            } else if (nTurSelected == 2) {
                arspTurret.add(new Turret(new Sprite(new Texture("Entities/jug_top.png")), (TiledMapTileLayer) tlMap.getLayers().get(0),2));
            } else if (nTurSelected == 3) {
                arspTurret.add(new Turret(new Sprite(new Texture("Entities/can_topblack.png")), (TiledMapTileLayer) tlMap.getLayers().get(0),4));
            }
            arspTurret.get(arspTurret.size() - 1).setSize(50, 50);
            arspTurret.get(arspTurret.size() - 1).setPosition(vtouchPos.x - arspTurret.get(arspTurret.size() - 1).getWidth() / 2,
                    vtouchPos.y - arspTurret.get(arspTurret.size() - 1).getHeight() / 2);
            tlRender.getBatch().begin();
            arspTurret.get(arspTurret.size() - 1).draw(tlRender.getBatch());
            tlRender.getBatch().end();
            if (placeableTurret() == true) {
                hud.subtMoney(arspTurrs.get(nTurSelected).nCost);
            } else {
                arspTurret.remove(arspTurret.size() - 1);//removes the last added turret since it mu
            }
        }
    }

    public void removeAnt() { //Checks to see if the ant hits the end of the road
        for (int i = 0; i < arspAnt.size(); i++) {
            if (arspAnt.get(i).bFinished == true) {//Checks for the end of the path
                hud.loseHP(arspAnt.get(i).nDamage);
                arspAnt.remove(i);
            }
            if (hud.nHP == 0) {//Checks if the hp is now 0
                    arspAnt.clear();
                    bGameOver = true;
                }
            if (arspAnt.size() == 0) { //Checks if the ants array is at 0 to start a new round
                    nextRound();
                }
            }
        }

    public boolean placeableTurret() { //Checks to see if the last placed turret is viable in it's location
        for (int i = 0; i < arspTurret.size() - 1; i++) {
            if (arspTurret.get(arspTurret.size() - 1).getBoundingRectangle().overlaps(arspTurret.get(i).getBoundingRectangle())) { //Checkes to see if the turret overlaps another
                System.out.println("Overlaps");
                return false;
            } else if (arspTurret.get(arspTurret.size() - 1).checkBounds() == false) { //Doesn't allow the turret to be placed if the turret is on the map
                System.out.println("On map");
                return false;
            }
        }
        return true;
    }

    public void nextRound() {
        hud.nextRound(); //Starts the next round
        if (true) {
            int nLevel = hud.nLevel;
            int nAntOne, nAntTwo, nAntThree, nAntFour, nAntFive, nPos;

            if (nLevel <= 15) { // For the first type of ant
                nAntOne = 5 * nLevel; //Number of ants of this type to be spawned
                System.out.println("Number of lvl 1 ants spawned: "+nAntOne);
                nPos = 0;
                for (int i = 0; i < nAntOne; i++) {
                    arspAnt.add(new Ants(new Sprite(new Texture("Entities/ant.png")), (TiledMapTileLayer) tlMap.getLayers().get(0), 3, 1, 1, nPos, nLevel, nID)); //Sptire|TileCollisionLayer|Speed|Damage|HP|Position|AntID
                    nPos++;//Increases the position of the ant next in line
                    nID++;
                }
            }

            if (nLevel >= 5 && nLevel <= 20) { //for the second type of ants
                nAntTwo = 2 * nLevel - 5;
                System.out.println("Number of lvl 2 ants spawned: "+nAntTwo);
                nPos = 0;
                for (int i = 0; i < nAntTwo; i++) {
                    arspAnt.add(new Ants(new Sprite(new Texture("Entities/ant2.png")), (TiledMapTileLayer) tlMap.getLayers().get(0), 6, 3, 2, nPos, nLevel,nID)); //Sptire|TileCollisionLayer|Speed|Damage|HP|Position
                    nPos++;
                    nID++;
                }
            }
            if (nLevel >= 10 && nLevel <= 25) {
                nAntThree = nLevel;
                System.out.println("Number of lvl 3 ants spawned: " + nAntThree);
                nPos = 0;
                for (int i = 0; i < nAntThree; i++) {
                    arspAnt.add(new Ants(new Sprite(new Texture("Entities/ant3.png")), (TiledMapTileLayer) tlMap.getLayers().get(0), 2, 3, 10, nPos, nLevel,nID)); //Sptire|TileCollisionLayer|Speed|Damage|HP|Position
                    nPos++;
                    nID++;
                }
            }
            if (nLevel >= 15 && nLevel < 25) {
                nAntFour = nLevel - 10;
                System.out.println("Number of lvl 4 ants spawned: " + nAntFour);
                nPos = 0;
                for (int i = 0; i < nAntFour; i++) {
                    arspAnt.add(new Ants(new Sprite(new Texture("Entities/ant4.png")), (TiledMapTileLayer) tlMap.getLayers().get(0), 4, 10, 5, nPos, nLevel,nID)); //Sptire|TileCollisionLayer|Speed|Damage|HP|Position
                    nPos++;
                    nID++;
                }
            }
            if (nLevel >= 20) {
                nAntFive = nLevel - 19;
            }
        }
    }

    public void targetAnts(float dt) { //targets the ants with each turret depending on if the ant is in range or not
        for (int i = 0; i < arspTurret.size(); i++) {
            for (int j = 0; j < arspAnt.size(); j++) {
                if ((Math.abs(arspAnt.get(j).getX() - arspTurret.get(i).getX()) + Math.abs(arspAnt.get(j).getY() - arspTurret.get(i).getY())) <= 200) {
                    if (arspAnt.get(j).bDead != true) { // Trying to make it so extra bullets don't get fired at the ants
                        if (dt - arspTurret.get(i).fLastTimeShot > arspTurret.get(i).nFireRate/100 || arspTurret.get(i).fLastTimeShot == 0) {
                            arspAnt.get(j).lowerHP(arspTurret.get(i).nDamage);
                            arspBullets.add(new Bullet(new Sprite(new Texture("Bullet.png")), arspAnt.get(j).nID));
                            arspBullets.get(arspBullets.size() - 1).setX(arspTurret.get(i).getX() + arspTurret.get(i).getWidth() / 2);
                            arspBullets.get(arspBullets.size() - 1).setY(arspTurret.get(i).getY() + arspTurret.get(i).getHeight() / 2);
                            arspTurret.get(i).fLastTimeShot = dt;
                            System.out.println(arspTurret.get(i).fLastTimeShot);
                        }
                    }
                }
            }
        }
    }

    public void shoot() { //Checking the ant ID to the bullet ID that it got from the ant to follow it
            for (int i = 0; i < arspBullets.size(); i++) {
                for (int j = 0; j < arspAnt.size(); j++) {
                    if (arspBullets.get(i).getBoundingRectangle().overlaps(arspAnt.get(j).getBoundingRectangle()) && arspAnt.size() > 0) {
                        arspAnt.remove(j);
                        arspBullets.remove(i);
                        break;
                    }
                    else if(arspBullets.get(i).nAntID == arspAnt.get(j).nID){
                        float diffX = (arspAnt.get(j).getX() + 25) - (arspBullets.get(i).getX() + 10);
                        float diffY = (arspAnt.get(j).getY() + 25) - (arspBullets.get(i).getY() + 10);
                        float angle = (float) Math.atan2(diffY, diffX);
                        arspBullets.get(i).update((float) (arspBullets.get(i).nSpeed * Math.cos(angle)), (float) (arspBullets.get(i).nSpeed * Math.sin(angle)), angle);
                        }
                    }
                }
            }

    @Override
    public void resize(int width, int height) {
        gameport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        tlMap.dispose();
        tlRender.dispose();
        hud.dispose();
    }
}