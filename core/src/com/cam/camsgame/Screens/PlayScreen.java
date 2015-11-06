package com.cam.camsgame.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.cam.camsgame.CamsGame;
import com.cam.camsgame.Scenes.Hud;

/**
 * Created by Cameron on 2015-11-04.
 */
public class PlayScreen implements Screen {
    private CamsGame game;
    private OrthographicCamera gamecam;
    private FitViewport gameport; //https://www.youtube.com/watch?v=D7u5B2Oh9r0
    private Hud hud;

    private TmxMapLoader mapLoader;
    private TiledMap tlMap;
    private OrthogonalTiledMapRenderer tlRender;

    public PlayScreen(CamsGame game){
        this.game = game;
        gamecam = new OrthographicCamera();

        //Create the viewport to keep a specific aspect ratio
        gameport = new FitViewport(CamsGame.V_WIDTH, CamsGame.V_HEIGHT, gamecam);

        //Create the hud
        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        //Load tiled map
        tlMap = mapLoader.load("Maps/Map.tmx");
        tlRender = new OrthogonalTiledMapRenderer(tlMap);
        //Set the gamecams position to half of the width and height of the map (the center of the map)
        gamecam.position.set(gameport.getWorldWidth()/2, gameport.getWorldHeight()/2, 0);
    }

    @Override
    public void show() {

    }

    public void update(){
        gamecam.update();
        //Only renders what the gamecam can see
        tlRender.setView(gamecam);
    }

    @Override
    public void render(float delta) {
        //Calls update to instantly update to the map
        update();
        Gdx.gl.glClearColor(1,0,1,0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        // renders the map
        tlRender.render();
        //Gets whats shown by our huds camera
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        //Draws the hud to the screen
        hud.stage.draw();
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

