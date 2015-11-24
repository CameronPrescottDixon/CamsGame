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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.cam.camsgame.Entities.Ants;
import com.cam.camsgame.CamsGame;
import com.cam.camsgame.Scenes.SelectTurret;
import com.cam.camsgame.Scenes.Hud;

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
    private Ants ant;
    private int nMoney=1;

    //Music
    private Music music;

    //Touch position in relation to world
    private Vector3 vtouchPos;

    //Entities
    private SelectTurret turOne;
    private SelectTurret turTwo;
    private SelectTurret turThree;
    private SelectTurret turFour;
    private int nTurSelected = 0;
    private Sprite spSidePanel;
    private Sprite spTurSelect;

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
        ant = new Ants(new Sprite(new Texture("Entities/ant.png")));
        ant.setPosition(ant.getWidth(), ant.getHeight() - 30);

        //Entities
        turOne = new SelectTurret(new Sprite(new Texture("Entities/raidant.png")));
        turOne.update(3, 300);

        turTwo = new SelectTurret(new Sprite(new Texture("Entities/raidfly.png")));
        turTwo.update(1, 400);

        turThree = new SelectTurret(new Sprite(new Texture("Entities/RAIDBIG.png")));
        turThree.update(-1, 600);

        turFour = new SelectTurret(new Sprite(new Texture("Entities/RAIDMAX.png")));
        turFour.update(-3, 900);

        spSidePanel = new Sprite(new Texture("SidePanel.jpg"));
        spSidePanel.setPosition(Gdx.graphics.getWidth() - 100, 0);
        spSidePanel.setSize(100,1000);

        spTurSelect = new Sprite(new Texture("red.png"));
        spTurSelect.setSize(100,120);
        spTurSelect.setPosition(turOne.getX(), turOne.getY()-10);



        //Set the gamecams position to half of the width and height of the map (the center of the map)
        gamecam.position.set(gameport.getWorldWidth()/2, gameport.getWorldHeight()/2, 0);

        //Get music
        music = CamsGame.manager.get("Music/LetTheBodiesHitTheFloor.mp3", Music.class); //This song is good
        music.setLooping(true);//Loop it
        //Makes the music volume lower so it's not destroying the users ears
        music.setVolume(music.getVolume()*1/10);
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
    }
    @Override
    public void render(float dt) {
        //Calls update to instantly update to the map
        update(dt); // Sends deltaTime to the update function to be sent to other methods that require it
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // renders the map
        tlRender.render();
        //Gets whats shown by our huds camera
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        //Draws the hud to the screen
        hud.stage.draw();
        //Draw ant
        tlRender.getBatch().begin(); //Draw the sprites, must be in order to draw them one ontop of each other
        ant.draw(tlRender.getBatch());
        spSidePanel.draw(tlRender.getBatch());
        spTurSelect.draw(tlRender.getBatch());
        turOne.draw(tlRender.getBatch());
        turTwo.draw(tlRender.getBatch());
        turThree.draw(tlRender.getBatch());
        turFour.draw(tlRender.getBatch());
        tlRender.getBatch().end();
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            vtouchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            //Basically this translates the co-ords I got by the input into world space throught the vector3 position set by input...
            gamecam.unproject(vtouchPos);//http://gamedev.stackexchange.com/questions/60787/libgdx-drawing-sprites-when-moving-orthographic-camera fixes the issues with touching + co-ords
            if (vtouchPos.x >= turOne.getX()) { // looks for in the click is in the turret select portion
                if (vtouchPos.y >= turOne.getY() && vtouchPos.y < turOne.getY() + turOne.getHeight()) {
                    System.out.println("1, hi");
                    spTurSelect.setPosition(turOne.getX(), turOne.getY()-10); // -10 because the red box is 120 pixels in height but the turrets are only 100
                    nTurSelected = 1;
                }
                if (vtouchPos.y >= turTwo.getY() && vtouchPos.y < turTwo.getY() + turTwo.getHeight()) {
                    System.out.println("2, hi");
                    spTurSelect.setPosition(turTwo.getX(), turTwo.getY()-10);
                    nTurSelected = 2;
                }
                if (vtouchPos.y >= turThree.getY() && vtouchPos.y < turThree.getY() + turThree.getHeight()) {
                    System.out.println("3, hi");
                    spTurSelect.setPosition(turThree.getX(), turThree.getY()-10);
                    nTurSelected = 3;
                }
                if (vtouchPos.y >= turFour.getY() && vtouchPos.y < turFour.getY() + turFour.getHeight()) {
                    System.out.println("4, hi");
                    spTurSelect.setPosition(turFour.getX(), turFour.getY() - 10);
                    nTurSelected = 4;
                }
            }
        }
        if (vtouchPos.x < turOne.getX()) { //looks for clicks off of the turret select panel
            if (nTurSelected == 1) {//only does this stuff if the first turrte is selected.. for this example it's just moving the ant.
                ant.setPosition(vtouchPos.x - ant.getHeight() / 2, vtouchPos.y - ant.getWidth()/2);
            } else if (nTurSelected == 2) {

            } else if (nTurSelected == 3) {

            } else if (nTurSelected == 4) {

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

