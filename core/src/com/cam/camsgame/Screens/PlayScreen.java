package com.cam.camsgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.cam.camsgame.Entities.Ants;
import com.cam.camsgame.CamsGame;
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
    private Hud hud;

    //Tiled
    private TmxMapLoader mapLoader; //https://www.youtube.com/watch?v=P8jgD-V5jG8 <-- how the map is loaded
    private TiledMap tlMap;
    private OrthogonalTiledMapRenderer tlRender;

    //Ants + test money for hud
    private ArrayList<Ants> arspAnt;
    private int nMoney=1;

    //Music
    private Music music;

    //Touch position in relation to world
    private Vector3 vtouchPos;

    //Turrets + side panel
    private ArrayList<Turret> arspTurret;
    private ArrayList <SelectTurret> arspTurrs;
    private int nTurSelected = 0;
    private Sprite spSidePanel;
    private Sprite spTurSelect;
    private boolean bTurSelect;
    private int nLastTurret;

    public PlayScreen(CamsGame game){
        this.game = game;
        gamecam = new OrthographicCamera();

        //Create the viewport to keep a specific aspect ratio
        gameport = new StretchViewport(CamsGame.V_WIDTH, CamsGame.V_HEIGHT, gamecam);

        //Create the hud
        hud = new Hud(game.batch);
        hud.subtMoney(nMoney);

        //Loads tiled map
        mapLoader = new TmxMapLoader();

        //Load tiled map
        tlMap = mapLoader.load("Maps/Map.tmx");
        tlRender = new OrthogonalTiledMapRenderer(tlMap);

        //Ants
        arspAnt = new ArrayList<Ants>();
        arspAnt.add(new Ants(new Sprite(new Texture("Entities/ant.png"))));
        arspAnt.get(0).setPosition(0,20);
        nLastTurret = 0;

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
        spTurSelect.setSize(100,120);
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
    }

    @Override
    public void show() {

    }

    public void update(float dt){
        gamecam.update();
        //Only renders what the gamecam can see
        tlRender.setView(gamecam);
        hud.updateTime(dt);
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) onClick();
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
        for(int i = 0;i<arspAnt.size();i++)arspAnt.get(i).draw(tlRender.getBatch());
        for(int i = 0; i<arspTurret.size();i++)arspTurret.get(i).draw(tlRender.getBatch());
        spSidePanel.draw(tlRender.getBatch());
        if(bTurSelect != false){//makes it so the red box isnt drawn from the start even if none of the turrets are selected
            spTurSelect.draw(tlRender.getBatch());
        }
        for(int i= 0; i<4;i++)arspTurrs.get(i).draw(tlRender.getBatch());
        tlRender.getBatch().end();
    }

    public void onClick(){ //Helps keep render function clean instead of packing all of this into it
        vtouchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        //Basically this translates the co-ords I got by the input into world space throught the vector3 position set by input...
        gamecam.unproject(vtouchPos);//http://gamedev.stackexchange.com/questions/60787/libgdx-drawing-sprites-when-moving-orthographic-camera fixes the issues with touching + co-ords
        for(int i=0;i<4;i++){
            if (vtouchPos.x >= arspTurrs.get(0).getX()) { // looks for in the click is in the turret select portion of the screen
                if (vtouchPos.y >= arspTurrs.get(i).getY() && vtouchPos.y < arspTurrs.get(i).getY() + arspTurrs.get(i).getHeight()) {// Using arrays schortened this code by 3/4!!!
                    spTurSelect.setPosition(arspTurrs.get(i).getX(), arspTurrs.get(i).getY() - 10); // -10 because the red box is 120 pixels in height but the turrets are only 100
                    nTurSelected = i+1;
                    bTurSelect = true;
                    break;
                }
            } else if(vtouchPos.x < arspTurrs.get(i).getX()) { //looks for clicks off of the turret select panel
                if (nTurSelected == 1) {//only does this stuff if the first turrte is selected.. for this example it's just moving the ant.
                    if(arspTurrs.get(0).nCost <= hud.nMoney){
                        hud.subtMoney(arspTurrs.get(1).nCost);
                        arspTurret.add(new Turret(new Sprite(new Texture("Entities/can_topred.png"))));
                        nLastTurret = arspTurret.size()-1;
                        arspTurret.get(nLastTurret).setSize(50, 50);
                        arspTurret.get(nLastTurret).setPosition(vtouchPos.x - arspTurret.get(nLastTurret).getWidth() / 2,
                                vtouchPos.y - arspTurret.get(nLastTurret).getHeight() / 2);
                    }
                } else if (nTurSelected == 2) {
                    if(arspTurrs.get(1).nCost <= hud.nMoney){
                        hud.subtMoney(arspTurrs.get(1).nCost);
                        arspTurret.add(new Turret(new Sprite(new Texture("Entities/can_topblue.png"))));
                        nLastTurret = arspTurret.size()-1;
                        arspTurret.get(nLastTurret).setSize(50, 50);
                        arspTurret.get(nLastTurret).setPosition(vtouchPos.x - arspTurret.get(nLastTurret).getHeight() / 2,
                                vtouchPos.y - arspTurret.get(nLastTurret).getWidth() / 2);
                    }
                } else if (nTurSelected == 3) {
                    if(arspTurrs.get(2).nCost <= hud.nMoney) {
                        hud.subtMoney(arspTurrs.get(2).nCost);
                        arspTurret.add(new Turret(new Sprite(new Texture("Entities/jug_top.png"))));
                        nLastTurret = arspTurret.size()-1;
                        arspTurret.get(nLastTurret).setSize(50, 50);
                        arspTurret.get(nLastTurret).setPosition(vtouchPos.x - arspTurret.get(nLastTurret).getHeight() / 2,
                                vtouchPos.y - arspTurret.get(nLastTurret).getWidth() / 2);
                    }
                } else if (nTurSelected == 4) {
                    if(arspTurrs.get(3).nCost <= hud.nMoney){
                        hud.subtMoney(arspTurrs.get(3).nCost);
                        arspTurret.add(new Turret(new Sprite(new Texture("Entities/can_topblack.png"))));
                        nLastTurret = arspTurret.size()-1;
                        arspTurret.get(nLastTurret).setSize(50, 50);
                        arspTurret.get(nLastTurret).setPosition(vtouchPos.x - arspTurret.get(nLastTurret).getHeight() / 2,
                                vtouchPos.y - arspTurret.get(nLastTurret).getWidth() / 2);
                    }
                }
                bTurSelect = false;
                nTurSelected = 0;
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

    }
}