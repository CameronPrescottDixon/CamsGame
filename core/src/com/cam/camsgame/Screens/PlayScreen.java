package com.cam.camsgame.Screens;

import com.badlogic.gdx.Game;
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
import com.badlogic.gdx.utils.TimeUtils;
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
    private Sprite spSell;
    private Sprite spUpgrade;
    private boolean bTurSelect;
    private boolean bTurretSelected = false;
    private int nSelectedTurret;

    private int[] arUpgrades = new int[4];

    //Bullets
    private ArrayList<Bullet> arspBullets;

    public PlayScreen(CamsGame game) {
        this.game = game;
        gamecam = new OrthographicCamera();

        //Create the viewport to keep a specific aspect ratio
        gameport = new StretchViewport(CamsGame.V_WIDTH, CamsGame.V_HEIGHT, gamecam);

        //Loads tiled map
        mapLoader = new TmxMapLoader();

        //Load tiled map
        tlMap = mapLoader.load("Maps/Map1.tmx");
        tlRender = new OrthogonalTiledMapRenderer(tlMap);

        //Ants
        arspAnt = new ArrayList<Ants>();


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
        spSidePanel = new Sprite(new Texture("Misc/SidePanel.jpg"));
        System.out.println(Gdx.graphics.getWidth() +"Width");

        spSidePanel.setSize(100,1000);
        spSidePanel.setPosition(900, 0);


        //Shows the selected turret by adding this red box behind it
        spTurSelect = new Sprite(new Texture("Misc/red.png"));
        spTurSelect.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/10 + 20);
        spTurSelect.setPosition(arspTurrs.get(0).getX(), arspTurrs.get(0).getY() - 10);
        bTurSelect = false; //Makes it so the box isnt drawn before a turret is selected

        //Make the upgrade and sell buttons
        spSell = new Sprite(new Texture("Misc/sell.png"));
        spSell.setSize(100, 50);


        spUpgrade = new Sprite(new Texture("Misc/upgrade.png"));
        spUpgrade.setSize(100, 50);

        //Set the gamecams position to half of the width and height of the map (the center of the map)
        gamecam.position.set(gameport.getWorldWidth() / 2, gameport.getWorldHeight() / 2, 0);

        //Get music
        music = CamsGame.manager.get("Music/LetTheBodiesHitTheFloor.mp3", Music.class); //This song is good
        music.setLooping(true);//Loop it
        //Makes the music volume lower so it's not destroying the users ears
        music.setVolume(music.getVolume() * 1 / 10);

        vtouchPos = new Vector3();//fixes the errors with flipping the y co-ords on the x-axis

        //Bullets
        arspBullets = new ArrayList<Bullet>();

        //Set up the array of upgrade ints
        arUpgrades[0] = 0;
        arUpgrades[1] = 0;
        arUpgrades[2] = 0;
        arUpgrades[3] = 0;
    }

    @Override
    public void show() {
    }

    public void startGame() { //This stuff is needed to start the screens actions
        hud = new Hud(game.batch);//This resets everything hud related if this is not the first game played
        music.play();//Have this here so it doesn't play the music in the screen in the other one
        bGameOver = false;
        nextRound(); //Initializes the game, without this the rounds wouldn't start
    }

    public void changeMap(int nNum) {
        String sNum = Integer.toString(nNum);
        String sMap = "Maps/Map" + sNum + ".tmx";
        boolean exists = Gdx.files.external(sMap).exists();
        if(exists == true) {
            tlMap = mapLoader.load(sMap);
        }
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
        spSidePanel.draw(tlRender.getBatch());
        if (bTurSelect != false) {//makes it so the red box isnt drawn from the start even if none of the turrets are selected
            spTurSelect.draw(tlRender.getBatch());
        }
        for (int i = 0; i < arspAnt.size(); i++) {
            arspAnt.get(i).draw(tlRender.getBatch());
        }
        for (int i = 0; i < arspTurret.size(); i++) arspTurret.get(i).draw(tlRender.getBatch());
        for (int i = 0; i < 4; i++) arspTurrs.get(i).draw(tlRender.getBatch());
        for (int i = 0; i < arspBullets.size(); i++)
            arspBullets.get(i).draw(tlRender.getBatch());

        if (bTurretSelected == true) {
            spSell.draw(tlRender.getBatch());
            spUpgrade.draw(tlRender.getBatch());
        }
        tlRender.getBatch().end();
    }

    public void update(float dt) {
        if (bGameOver != true) {
            targetAnts();//Sends the ant array to the turrets to shoot them
            gamecam.update();
            //Only renders what the gamecam can see
            tlRender.setView(gamecam);
            hud.updateTime(dt);
            removeAnt();//checks if ants reach the end

            if (arspBullets.size() > 0) {
                bulletTracking();
            }
            if (Gdx.input.isButtonPressed(Input.Buttons.LEFT))
                onClick();//only passes it when theres a click
        }
    }

    public void onClick() { //Helps keep render function clean instead of packing all of this into it
        vtouchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        //Basically this translates the co-ords I got by the input into world space throught the vector3 position set by input...
        gamecam.unproject(vtouchPos);//http://gamedev.stackexchange.com/questions/60787/libgdx-drawing-sprites-when-moving-orthographic-camera fixes the issues with touching + co-ords
        for (int i = 0; i < 4; i++) {
            if (vtouchPos.x >= arspTurrs.get(0).getX()) { // looks for in the click is in the turret select portion of the screen
                if (vtouchPos.y >= arspTurrs.get(i).getY() && vtouchPos.y < arspTurrs.get(i).getY() + arspTurrs.get(i).getHeight()) {// Using arrays schortened this code by 3/4!!!
                    spTurSelect.setPosition(arspTurrs.get(i).getX(), arspTurrs.get(i).getY() - 20); // -10 because the red box is 120 pixels in height but the turrets are only 100
                    nTurSelected = i;
                    bTurSelect = true;
                    break;
                } else if (vtouchPos.y >= spSell.getY() && vtouchPos.y < spSell.getY() + spSell.getHeight() && bTurretSelected == true) {//Checks for the sell button
                    System.out.println(nSelectedTurret);
                    arspTurret.remove(nSelectedTurret);

                    bTurretSelected = false;
                } else if (vtouchPos.y >= spUpgrade.getY() && vtouchPos.y < spUpgrade.getY() + spUpgrade.getHeight() && bTurretSelected == true && hud.nMoney >= 1000 && arUpgrades[arspTurret.get(nSelectedTurret).nTurretType] <= 4) {// Checks for the upgrade button
                    for (int j = 0; j < arspTurret.size(); j++) {
                        if (arspTurret.get(nSelectedTurret).nTurretType == 0) {// Red turret
                            if (arspTurret.get(j).nTurretType == 0) {
                                arspTurret.get(j).nDamage += 1;
                                arspTurret.get(j).nFireRate += -50000000;//Not sure how to format this right about now
                                arspTurret.get(j).nRange += 100;
                            }
                        } else if (arspTurret.get(nSelectedTurret).nTurretType == 1) {// Blue turret
                            if (arspTurret.get(j).nTurretType == 1) {
                                arspTurret.get(j).nDamage += 2;
                                arspTurret.get(j).nFireRate +=-50000000;//Not sure how to format this right about now
                                arspTurret.get(j).nRange += 50;
                            }
                        } else if (arspTurret.get(nSelectedTurret).nTurretType == 2) {// Jug
                            if (arspTurret.get(j).nTurretType == 2) {
                                arspTurret.get(j).nFireRate += -100000000;//Not sure how to format this right about now
                                arspTurret.get(j).nRange += 75;
                            }
                        } else {// Black turret
                            if (arspTurret.get(j).nTurretType == 3) {
                                arspTurret.get(j).nDamage += 1;
                                arspTurret.get(j).nFireRate += -50000000;//Not sure how to format this right about now
                                arspTurret.get(j).nRange += 100;
                            }
                        }
                    }
                    hud.subtMoney(1000);
                    arUpgrades[arspTurret.get(nSelectedTurret).nTurretType]++;
                    System.out.println("Upgraded " +arUpgrades[arspTurret.get(nSelectedTurret).nTurretType]+" Times");
                    bTurretSelected = false;
                }
            } else if (vtouchPos.x < arspTurrs.get(i).getX()) { //looks for clicks off of the turret select panel
                if (bTurSelect == true) {
                    addTurret();
                    bTurSelect = false;
                    bTurretSelected = false;
                } else {
                    for (int j = 0; j < arspTurret.size(); j++) {
                        if (vtouchPos.y >= arspTurret.get(j).getY() && vtouchPos.y < arspTurret.get(j).getY() + arspTurret.get(j).getHeight()) {
                            spSell.setPosition(arspTurrs.get(arspTurret.get(j).nTurretType).getX(), arspTurrs.get(arspTurret.get(j).nTurretType).getY() - 40);
                            spUpgrade.setPosition(arspTurrs.get(arspTurret.get(j).nTurretType).getX(), arspTurrs.get(arspTurret.get(j).nTurretType).getY() - 100);
                            bTurretSelected = true;
                            bTurSelect = false;
                            nSelectedTurret = j;
                        }
                    }
                }
            }
        }
    }

    private void addTurret() { //Adds turret when clicked
        if (arspTurrs.get(nTurSelected).nCost <= hud.nMoney) {
            if (nTurSelected == 0) {//Adds the turret with the specific image, this also helps reduce total code when it's in a method
                arspTurret.add(new Turret(new Sprite(new Texture("Entities/can_topred.png")), (TiledMapTileLayer) tlMap.getLayers().get(0), 0, 1, nTurSelected, 200, arUpgrades[nTurSelected]));
            } else if (nTurSelected == 1) {
                arspTurret.add(new Turret(new Sprite(new Texture("Entities/can_topblue.png")), (TiledMapTileLayer) tlMap.getLayers().get(0), -50000000, 4, nTurSelected, 400, arUpgrades[nTurSelected]));
            } else if (nTurSelected == 2) {
                arspTurret.add(new Turret(new Sprite(new Texture("Entities/jug_top.png")), (TiledMapTileLayer) tlMap.getLayers().get(0), 50000000, 2, nTurSelected, 200, arUpgrades[nTurSelected]));
            } else if (nTurSelected == 3) {
                arspTurret.add(new Turret(new Sprite(new Texture("Entities/can_topblack.png")), (TiledMapTileLayer) tlMap.getLayers().get(0), 100000000, 10, nTurSelected, 1000, arUpgrades[nTurSelected]));
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

    public void targetAnts() { //targets the ants with each turret depending on if the ant is in range or not
        for (int i = 0; i < arspTurret.size(); i++) {
            for (int j = 0; j < arspAnt.size(); j++) {
                if ((Math.abs(arspAnt.get(j).getX() - arspTurret.get(i).getX()) + Math.abs(arspAnt.get(j).getY() - arspTurret.get(i).getY())) <= arspTurret.get(i).nRange) {//Range between them;
                    if ((TimeUtils.nanoTime() - arspTurret.get(i).fLastTimeShot) > (1000000000 + arspTurret.get(i).nFireRate) || arspTurret.get(i).fLastTimeShot == 0) {
                        if (arspAnt.get(j).bDead != true) {
                                arspAnt.get(j).checkHP(arspTurret.get(i).nDamage);
                                arspTurret.get(i).fLastTimeShot = TimeUtils.nanoTime();
                                arspBullets.add(new Bullet(new Sprite(new Texture("Bullet.png")), arspAnt.get(j).nID, arspTurret.get(i).nDamage));
                                arspBullets.get(arspBullets.size() - 1).setX(arspTurret.get(i).getX() + arspTurret.get(i).getWidth() / 2); //Sets the position of the bullet to the center
                                arspBullets.get(arspBullets.size() - 1).setY(arspTurret.get(i).getY() + arspTurret.get(i).getHeight() / 2);
                                return;
                            }
                        }
                    }
                }
            }
        }

    public void bulletTracking() { //Checking the ant ID to the bullet ID that it got from the ant to follow it
        for (int i = 0; i < arspBullets.size(); i++) {
            for (int j = 0; j < arspAnt.size(); j++) {
                if (arspBullets.get(i).nAntID == arspAnt.get(j).nID) { //Moves the bullet to the ant with the same ID as the bullet
                    float diffX = (arspAnt.get(j).getX() + 25) - (arspBullets.get(i).getX() + 10);
                    float diffY = (arspAnt.get(j).getY() + 25) - (arspBullets.get(i).getY() + 10);
                    float angle = (float) Math.atan2(diffY, diffX);
                    float fVelX = (float) (arspBullets.get(i).nSpeed * Math.cos(angle));
                    float fVelY = (float) (arspBullets.get(i).nSpeed * Math.sin(angle));
                    arspBullets.get(i).update(fVelX, fVelY);
                }
            }
        }
    }

    public void removeAnt() { //Checks to see if the ant hits the end of the road
        if (hud.nHP <= 0) {//Checks if the hp is now 0
            ((Game) Gdx.app.getApplicationListener()).setScreen(game.menu);
            bGameOver = true;
            arspAnt.clear();
            arspBullets.clear();
            arspTurret.clear();
            music.stop();
        }
        if (arspAnt.size() == 0) { //Checks if the ants array is at 0 to start a new round
            System.out.println("HI FROM THE NEXT ROUND FUNCTION");
            nextRound();
            if (arspBullets.size() > 0) {
                arspBullets.clear();
            }
        } else {
            for (int i = 0; i < arspAnt.size(); i++) {
                if (arspAnt.get(i).bFinished == true) {//Checks for if the ant reaches the end of the path
                    hud.loseHP(arspAnt.get(i).nDamage);
                    arspAnt.remove(i);
                    break;
                }
                for (int j = 0; j < arspBullets.size(); j++) {
                    if (i <= arspAnt.size() && j <= arspBullets.size()) {
                        if (arspBullets.get(j).getBoundingRectangle().overlaps(arspAnt.get(i).getBoundingRectangle()) && arspAnt.size() > 0 && arspAnt.get(i).nID == arspBullets.get(j).nAntID) { //Looks for if the bullet hits the ant first
                            hud.addMoney(arspAnt.get(i).nWorth);
                            arspAnt.get(i).nHP -= arspBullets.get(j).nDmg;
                            if (arspAnt.get(i).nHP <= 0) {
                                arspAnt.remove(i);
                                arspBullets.remove(j);
                                break;
                            }
                            arspBullets.remove(j);
                        }
                    }
                }
            }
        }
    }

    public void nextRound() {
        if (bGameOver != true) {
            hud.nextRound(); //Starts the next round
            int nLevel = hud.nLevel;
            int nAntOne, nAntTwo, nAntThree, nAntFour, nAntFive, nPos;
            if (nLevel >= 5 && nLevel <= 20) { //for the second type of ants
                nAntTwo = 2 * nLevel - 5;
                System.out.println("Number of lvl 2 ants spawned: " + nAntTwo);
                nPos = 0;
                for (int i = 0; i < nAntTwo; i++) {
                    arspAnt.add(new Ants(new Sprite(new Texture("Entities/ant2.png")), (TiledMapTileLayer) tlMap.getLayers().get(0), 6, 3, 2, nPos, nLevel, nID, 75)); //Sptire|TileCollisionLayer|Speed|Damage|HP|Position
                    nPos++;
                }
            }
            if (nLevel >= 15) {
                nAntFour = nLevel - 10;
                System.out.println("Number of lvl 4 ants spawned: " + nAntFour);
                nPos = 0;
                for (int i = 0; i < nAntFour; i++) {
                    arspAnt.add(new Ants(new Sprite(new Texture("Entities/ant4.png")), (TiledMapTileLayer) tlMap.getLayers().get(0), 4, 10, 5, nPos, nLevel, nID, 200)); //Sptire|TileCollisionLayer|Speed|Damage|HP|Position
                    nPos++;
                }
            }
            if (nLevel <= 15) { // For the first type of ant
                nAntOne = 5 * nLevel; //Number of ants of this type to be spawned
                System.out.println("Number of lvl 1 ants spawned: " + nAntOne);
                nPos = 0;
                for (int i = 0; i < nAntOne; i++) {
                    arspAnt.add(new Ants(new Sprite(new Texture("Entities/ant.png")), (TiledMapTileLayer) tlMap.getLayers().get(0), 3, 1, 1, nPos, nLevel, nID, 50)); //Sptire|TileCollisionLayer|Speed|Damage|HP|Position|AntID
                    nPos++;//Increases the position of the ant next in line
                }
            }

            if (nLevel >= 10 && nLevel <= 25) {
                nAntThree = nLevel;
                System.out.println("Number of lvl 3 ants spawned: " + nAntThree);
                nPos = 0;
                for (int i = 0; i < nAntThree; i++) {
                    arspAnt.add(new Ants(new Sprite(new Texture("Entities/ant3.png")), (TiledMapTileLayer) tlMap.getLayers().get(0), 2, 3, 10, nPos, nLevel, nID, 150)); //Sptire|TileCollisionLayer|Speed|Damage|HP|Position
                    nPos++;
                }
            }
            if (nLevel >= 20) {
                nAntFive = nLevel - 19;
            }
            for (int i = 0; i < arspAnt.size(); i++) {
                arspAnt.get(i).nID = nID;
                nID++;
            }
            nID = 0;
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
        arspAnt.clear();
        arspBullets.clear();
        arspTurret.clear();
        arspTurrs.clear();
    }
}