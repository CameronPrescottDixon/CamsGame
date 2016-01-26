package com.cam.camsgame.Screens;

import com.badlogic.gdx.ApplicationAdapter;
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
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.cam.camsgame.CamsGame;
import com.cam.camsgame.Entities.Ants;
import com.cam.camsgame.Entities.Bullet;
import com.cam.camsgame.Entities.Turret;
import com.cam.camsgame.Scenes.Hud;
import com.cam.camsgame.Scenes.SelectTurret;
import com.cam.camsgame.Scenes.TurretInfo;

import java.util.ArrayList;

/**
 * Created by Cameron on 2015-11-04.
 */
public class PlayScreen extends ApplicationAdapter implements Screen {
    //Hud
    private Hud hud;
    int nID = 0;
    boolean bGameOver;//Checks if the game is finished
    private CamsGame game;
    //Camera stuff
    private OrthographicCamera gamecam;
    private StretchViewport gameport; //https://www.youtube.com/watch?v=D7u5B2Oh9r0 <-- maintaining aspect ratios
    //Tiled
    private TmxMapLoader mapLoader; //https://www.youtube.com/watch?v=P8jgD-V5jG8 <-- how the map is loaded
    private TiledMap tlMap;
    private OrthogonalTiledMapRenderer tlRender;
    //Ants
    private ArrayList<Ants> arspAnt;
    //Music
    private Music music;

    //Touch position in relation to world
    private Vector3 vtouchPos;

    private TurretInfo turrInfo;

    //Turrets + side panel
    private ArrayList<Turret> arspTurret;
    private ArrayList<SelectTurret> arspSelectTurr;
    private int nTurSelected = 0;
    private Sprite spSidePanel;
    private Sprite spTurSelect; //Red box for when a turret type is selected
    private boolean bTypeSelected;//If a side panel turret type is selected
    private boolean bTurretSelected = false;//Specific turret on the map selected
    private int nSelectedTurret;
    private int[] arUpgrades = new int[4];
    private int nMapSelected = 2;

    //Bullets
    private ArrayList<Bullet> arspBullets;
    private Sprite spRangeIndc;
    private boolean bisSelectable = true;//If the side panel turrets are able to be selected

    public PlayScreen(CamsGame game) {
        this.game = game;
        gamecam = new OrthographicCamera();

        turrInfo = new TurretInfo(game.batch);

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
        arspSelectTurr = new ArrayList<SelectTurret>();
        arspSelectTurr.add(new SelectTurret(new Sprite(new Texture("Entities/raidant.png"))));
        arspSelectTurr.get(0).update(3, 300);
        arspSelectTurr.add(new SelectTurret(new Sprite(new Texture("Entities/raidfly.png"))));
        arspSelectTurr.get(1).update(1, 400);
        arspSelectTurr.add(new SelectTurret(new Sprite(new Texture("Entities/RAIDBIG.png"))));
        arspSelectTurr.get(2).update(-1, 600);
        arspSelectTurr.add(new SelectTurret(new Sprite(new Texture("Entities/RAIDMAX.png"))));
        arspSelectTurr.get(3).update(-3, 900);

        //Turrets
        arspTurret = new ArrayList<Turret>();

        //Side panel for the turrets to sit on instead of the map
        spSidePanel = new Sprite(new Texture("Misc/SidePanel.jpg"));
        System.out.println(Gdx.graphics.getWidth() + "Width");

        spSidePanel.setSize(100, 1000);
        spSidePanel.setPosition(900, 0);


        //Shows the selected turret by adding this red box behind it
        spTurSelect = new Sprite(new Texture("Misc/red.png"));
        spTurSelect.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 10 + 20);
        spTurSelect.setPosition(arspSelectTurr.get(0).getX(), arspSelectTurr.get(0).getY() - 10);
        bTypeSelected = false; //Makes it so the box isnt drawn before a turret is selected

        spRangeIndc = new Sprite(new Texture("Misc/circle.png"));

        //Set the gamecams position to half of the width and height of the map (the center of the map)
        gamecam.position.set(gameport.getWorldWidth() / 2, gameport.getWorldHeight() / 2, 0);

        vtouchPos = new Vector3();//fixes the errors with flipping the y co-ords on the x-axis

        //Bullets
        arspBullets = new ArrayList<Bullet>();

        //Set up the array of upgrade ints
        arUpgrades[0] = 0;
        arUpgrades[1] = 0;
        arUpgrades[2] = 0;
        arUpgrades[3] = 0;
    }

    public void startGame(int nNum1, int nNum) { //This stuff is needed to start the screens actions
        changeMap(nNum1);
        changeMusic(nNum);
        hud = new Hud(game.batch);//This resets everything hud related if this is not the first game played
        changeLevel(game.changeLevel());
        bGameOver = false;
        nextRound(); //Initializes the game, without this the rounds wouldn't start
    }

    public void changeMap(int nNum) {
        String sNum = Integer.toString(nNum);
        String sMap = "Maps/Map" + sNum + ".tmx";
        boolean exists = Gdx.files.internal(sMap).exists();
        nMapSelected = nNum;
        if (exists == true) {
            tlMap = mapLoader.load(sMap);
            tlRender.setMap(tlMap);
        } else {
            System.out.println("nope " + sMap + " Not viable");
            nMapSelected = 1;
        }
    }

    public void changeMusic(int nNum) {
        String sNum = Integer.toString(nNum);
        String sMusic = "Music/Music" + sNum + ".mp3";
        music = CamsGame.manager.get(sMusic, Music.class);
        music.setLooping(true);//Loop it
        //Makes the music volume lower so it's not destroying the users ears
        music.setVolume(music.getVolume() * 1 / 10);
        music.play();
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
        tlRender.getBatch().begin(); //Draw the sprites, must be in order to draw them one ontop of each other
        spSidePanel.draw(tlRender.getBatch());// Side panel
        for (int i = 0; i < arspAnt.size(); i++) {// Ants
            arspAnt.get(i).draw(tlRender.getBatch());
        }
        for (int i = 0; i < arspTurret.size(); i++) arspTurret.get(i).draw(tlRender.getBatch());// Turrets
        for (int i = 0; i < arspBullets.size(); i++) arspBullets.get(i).draw(tlRender.getBatch());//Bullets
        if (bTypeSelected != false) {//makes it so the red box isnt drawn from the start even if none of the turrets are selected
            spTurSelect.draw(tlRender.getBatch());
        }
        tlRender.getBatch().end();

        game.batch.begin();
        if (bTurretSelected == true) {//Only done when a specific turret is clicked
            spRangeIndc.draw(game.batch);//range indicator
            if (game.getScreen() == this) {//So that the buttons aren't used when the screen isnt on this one
                Gdx.input.setInputProcessor(turrInfo.stage);
            }
            arspSelectTurr.get(arspTurret.get(nSelectedTurret).nTurretType).draw(game.batch);//Only one turret type is drawn since the user wants to see more info about it
            arspSelectTurr.get(arspTurret.get(nSelectedTurret).nTurretType).setPosition(900, 750);
        } else {
            for (int i = 0; i < 4; i++) {
                arspSelectTurr.get(i).draw(game.batch);//All turret types are drawn
            }
        }
        game.batch.end();
        if (bTurretSelected == true) {//Draw and update the stage when a specific turret is selected
            turrInfo.giveVals(arspTurret.get(nSelectedTurret).nFireRate, arspTurret.get(nSelectedTurret).nDamage,
                    arspTurret.get(nSelectedTurret).nRange, arspSelectTurr.get(arspTurret.get(nSelectedTurret).nTurretType).nCost, arUpgrades[arspTurret.get(nSelectedTurret).nTurretType]);
            turrInfo.stage.draw();
            turrInfo.stage.act();
        }
    }

    @Override
    public void show() {//Listeners for side panel buttons
        turrInfo.tbSell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (bisSelectable == false) {//Only when a specific turret is selected
                    sellTurr();
                    System.out.println("HI");
                }
            }
        });
        turrInfo.tbUpgrade.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (bisSelectable == false) {
                    upgradeTurr();
                }
            }
        });
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
        if (vtouchPos.x >= arspSelectTurr.get(0).getX()) { // looks for in the click is in the turret select portion of the screen
            for (int i = 0; i < 4; i++) {
                if (vtouchPos.y >= arspSelectTurr.get(i).getY() && vtouchPos.y < arspSelectTurr.get(i).getY() + arspSelectTurr.get(i).getHeight() && bisSelectable == true) {// Using arrays schortened this code by 3/4!!!
                    spTurSelect.setPosition(arspSelectTurr.get(i).getX(), arspSelectTurr.get(i).getY()); // -10 because the red box is 120 pixels in height but the turrets are only 100
                    nTurSelected = i;
                    bTypeSelected = true;
                }
            }
        } else if (vtouchPos.x < spSidePanel.getX()) { //looks for clicks off of the turret select panel
            if (bTypeSelected == true) {//If a type of turret is selected and the user clicks on the map
                addTurret();
                bTypeSelected = false;
                bTurretSelected = false;
            } else {
                for (int j = 0; j < arspTurret.size(); j++) {
                    if (vtouchPos.y >= arspTurret.get(j).getY() && vtouchPos.y < arspTurret.get(j).getY() + arspTurret.get(j).getHeight()) {//Checks for if the user clicks on a turret
                        if (vtouchPos.x >= arspTurret.get(j).getX() && vtouchPos.x < arspTurret.get(j).getX() + arspTurret.get(j).getWidth()) {
                            bTurretSelected = true;
                            bTypeSelected = false;
                            bisSelectable = false;
                            nSelectedTurret = j;
                            System.out.println(j + " Selected");
                            spRangeIndc.setSize(arspTurret.get(j).nRange, arspTurret.get(j).nRange);
                            spRangeIndc.setPosition(arspTurret.get(j).getX() - (spRangeIndc.getWidth() / 2 - 75 / 2), arspTurret.get(j).getY() - (spRangeIndc.getWidth() / 2 - 75 / 2));
                            return;
                        }
                    } else {
                        bTurretSelected = false;
                        bTypeSelected = false;
                        bisSelectable = true;
                        for (int q = 0; q < 4; q++) {
                            arspSelectTurr.get(q).resetPos();
                        }
                    }
                }
            }
        }
    }

    public void sellTurr() {//Sells the selected turret
        if (arspTurret.size() > 0) {//hard to sell 0 turrets
            hud.addMoney(arspSelectTurr.get(arspTurret.get(nSelectedTurret).nTurretType).nCost /2);//refunds half the cost
            arspTurret.remove(nSelectedTurret);
            bTurretSelected = false;
            bisSelectable = true;
            for (int q = 0; q < 4; q++) {
                arspSelectTurr.get(q).resetPos();//Resets the position of the selectable turrets on the side panel
            }
        }
    }

    public void upgradeTurr() {//Upgrades the turrets based on the type
        if (arUpgrades[arspTurret.get(nSelectedTurret).nTurretType] <= 3 && hud.nMoney >= 5000 && arspTurret.size() > 0) {
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
                        arspTurret.get(j).nFireRate += -50000000;//Not sure how to format this right about now
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
            arspSelectTurr.get(arspTurret.get(nSelectedTurret).nTurretType).nCost += 500;
            hud.subtMoney(5000);
            arUpgrades[arspTurret.get(nSelectedTurret).nTurretType]++;
            for (int q = 0; q < 4; q++) {
                arspSelectTurr.get(q).resetPos();
            }
            bTurretSelected = false;
            bisSelectable = true;
        }
    }

    private void addTurret() { //Adds turret when clicked
        if (arspSelectTurr.get(nTurSelected).nCost <= hud.nMoney) {
            if (nTurSelected == 0) {//Adds the turret with the specific image, this also helps reduce total code when it's in a method
                arspTurret.add(new Turret(new Sprite(new Texture("Entities/can_topred.png")), (TiledMapTileLayer) tlMap.getLayers().get(0), 0, 1, nTurSelected, 200, arUpgrades[nTurSelected]));
            } else if (nTurSelected == 1) {
                arspTurret.add(new Turret(new Sprite(new Texture("Entities/can_topblue.png")), (TiledMapTileLayer) tlMap.getLayers().get(0), -50000000, 4, nTurSelected, 400, arUpgrades[nTurSelected]));
            } else if (nTurSelected == 2) {
                arspTurret.add(new Turret(new Sprite(new Texture("Entities/jug_top.png")), (TiledMapTileLayer) tlMap.getLayers().get(0), 50000000, 2, nTurSelected, 200, arUpgrades[nTurSelected]));
            } else if (nTurSelected == 3) {
                arspTurret.add(new Turret(new Sprite(new Texture("Entities/can_topblack.png")), (TiledMapTileLayer) tlMap.getLayers().get(0), 700000000, 5, nTurSelected, 600, arUpgrades[nTurSelected]));
            }
            arspTurret.get(arspTurret.size() - 1).setPosition(vtouchPos.x - arspTurret.get(arspTurret.size() - 1).getWidth() / 2,
                    vtouchPos.y - arspTurret.get(arspTurret.size() - 1).getHeight() / 2);
            if (placeableTurret() == true) {
                hud.subtMoney(arspSelectTurr.get(nTurSelected).nCost);
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
                        if (arspAnt.get(j).bisTargeted != true) {
                            arspAnt.get(j).bisTargeted = true;
                            arspTurret.get(i).fLastTimeShot = TimeUtils.nanoTime();
                            arspBullets.add(new Bullet(new Sprite(new Texture("Misc/Bullet.png")), arspAnt.get(j).nID, arspTurret.get(i).nDamage));
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
            game.setGameOver();
            bGameOver = true;
            arspAnt.clear();
            arspBullets.clear();
            arspTurret.clear();
            music.stop();
        }
        if (arspAnt.size() == 0) { //Checks if the ants array is at 0 to start a new round
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
                            } else {
                                arspBullets.remove(j);
                                arspAnt.get(i).bisTargeted = false;
                            }
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
                    arspAnt.add(new Ants(new Sprite(new Texture("Entities/ant2.png")), (TiledMapTileLayer) tlMap.getLayers().get(0), 6, 3, 2, nPos, nLevel, nID, 25, nMapSelected)); //Sptire|TileCollisionLayer|Speed|Damage|HP|Position
                    nPos++;
                }
            }
            if (nLevel >= 15) {
                nAntFour = nLevel - 10;
                System.out.println("Number of lvl 4 ants spawned: " + nAntFour);
                nPos = 0;
                for (int i = 0; i < nAntFour; i++) {
                    arspAnt.add(new Ants(new Sprite(new Texture("Entities/ant4.png")), (TiledMapTileLayer) tlMap.getLayers().get(0), 4, 10, 5, nPos, nLevel, nID, 150, nMapSelected)); //Sptire|TileCollisionLayer|Speed|Damage|HP|Position
                    nPos++;
                }
            }
            if (nLevel <= 15) { // For the first type of ant
                nAntOne = 5 * nLevel; //Number of ants of this type to be spawned
                System.out.println("Number of lvl 1 ants spawned: " + nAntOne);
                nPos = 0;
                for (int i = 0; i < nAntOne; i++) {
                    arspAnt.add(new Ants(new Sprite(new Texture("Entities/ant.png")), (TiledMapTileLayer) tlMap.getLayers().get(0), 3, 1, 1, nPos, nLevel, nID, 10, nMapSelected)); //Sptire|TileCollisionLayer|Speed|Damage|HP|Position|AntID
                    nPos++;//Increases the position of the ant next in line
                }
            }
            if (nLevel >= 10) {
                nAntThree = nLevel;
                System.out.println("Number of lvl 3 ants spawned: " + nAntThree);
                nPos = 0;
                for (int i = 0; i < nAntThree; i++) {
                    arspAnt.add(new Ants(new Sprite(new Texture("Entities/ant3.png")), (TiledMapTileLayer) tlMap.getLayers().get(0), 1, 3, 15, nPos, nLevel, nID, 50, nMapSelected)); //Sptire|TileCollisionLayer|Speed|Damage|HP|Position
                    nPos++;
                }
            }

            if (nLevel >= 20) {
                nAntFive = nLevel - 19;
                System.out.println("Number of lvl 5 ants spawned: " + nAntFive);
                nPos = 0;
                for (int i = 0; i < nAntFive; i++) {
                    arspAnt.add(new Ants(new Sprite(new Texture("Entities/ant5.png")), (TiledMapTileLayer) tlMap.getLayers().get(0), 1, 90, 100, nPos, nLevel, nID, 50, nMapSelected)); //Sptire|TileCollisionLayer|Speed|Damage|HP|Position
                    nPos++;
                }
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

    public void changeLevel(int nNum){
        System.out.println(nNum);
        hud.nLevel=nNum;
        hud.nMoney=nNum*800;
    }

    @Override
    public void dispose() {
        tlMap.dispose();
        tlRender.dispose();
        hud.dispose();
        arspAnt.clear();
        arspBullets.clear();
        arspTurret.clear();
        arspSelectTurr.clear();
    }
}