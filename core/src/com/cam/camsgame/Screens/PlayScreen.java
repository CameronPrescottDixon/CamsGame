package com.cam.camsgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.cam.camsgame.Entities.Ants;
import com.cam.camsgame.CamsGame;
import com.cam.camsgame.Scenes.Hud;

/**
 * Created by Cameron on 2015-11-04.
 */
public class PlayScreen implements Screen {
    private CamsGame game;
    private OrthographicCamera gamecam;
    private FitViewport gameport; //https://www.youtube.com/watch?v=D7u5B2Oh9r0 <-- maintaining aspect ratios

    //Hud
    private Hud hud;

    //Tiled
    private TmxMapLoader mapLoader; //https://www.youtube.com/watch?v=P8jgD-V5jG8 <-- how the map is loaded
    private TiledMap tlMap;
    private OrthogonalTiledMapRenderer tlRender;

    private Ants ant;

    private int nMoney=1;

    public PlayScreen(CamsGame game){
        this.game = game;
        gamecam = new OrthographicCamera();

        //Create the viewport to keep a specific aspect ratio
        gameport = new FitViewport(CamsGame.V_WIDTH, CamsGame.V_HEIGHT, gamecam);

        //Create the hud
        hud = new Hud(game.batch);
        hud.subtMoney(nMoney);

        mapLoader = new TmxMapLoader();

        //Load tiled map
        tlMap = mapLoader.load("Maps/Map.tmx");
        tlRender = new OrthogonalTiledMapRenderer(tlMap);

        //Ants
        ant = new Ants(new Sprite(new Texture("ant.png")));
        ant.setPosition(ant.getWidth(),ant.getHeight()-30);

        //Set the gamecams position to half of the width and height of the map (the center of the map)
        gamecam.position.set(gameport.getWorldWidth()/2, gameport.getWorldHeight()/2, 0);

    }

    @Override
    public void show() {

    }

    public void update(float dt){
        gamecam.update();
        //Only renders what the gamecam can see
        tlRender.setView(gamecam);
        hud.update(dt);
        if(dt < 10){
            ant.nVelY=0;
        }
    }
    @Override
    public void render(float dt) {
        //Calls update to instantly update to the map
        update(dt);
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // renders the map
        tlRender.render();
        //Gets whats shown by our huds camera
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        //Draws the hud to the screen
        hud.stage.draw();
        //Draw ant
        tlRender.getBatch().begin();
        ant.draw(tlRender.getBatch());
        tlRender.getBatch().end();

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

