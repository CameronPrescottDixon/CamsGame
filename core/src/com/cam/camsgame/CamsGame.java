package com.cam.camsgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cam.camsgame.Screens.GameOver;
import com.cam.camsgame.Screens.Instructions;
import com.cam.camsgame.Screens.Maps;
import com.cam.camsgame.Screens.Menu;
import com.cam.camsgame.Screens.PlayScreen;


public class CamsGame extends Game {
    public static final int V_WIDTH = 1000;
    public static final int V_HEIGHT = 1000;
    public SpriteBatch batch;
    public static AssetManager manager;
    private Menu scrMenu;
    public PlayScreen scrPlayScreen;
    private Instructions scrInstructions;
    public Maps scrMaps;
	private GameOver scrGameOver;


    @Override
    public void create() {
        batch = new SpriteBatch();
        manager = new AssetManager();
        manager.load("Music/Halo- Menu Music.mp3", Music.class);
		manager.load("Music/Gameover.mp3", Music.class);
        manager.load("Music/Music1.mp3", Music.class);
        manager.load("Music/Music2.mp3", Music.class);
        manager.load("Music/Music3.mp3", Music.class);
        manager.load("Music/Music4.mp3", Music.class);
        manager.finishLoading(); // finish loading all the assets for now
        scrPlayScreen = new PlayScreen(this);
        scrMenu = new Menu(this);
        scrInstructions = new Instructions(this);
        scrMaps = new Maps(this);
		scrGameOver = new GameOver(this);
        setScreen(scrMenu);
    }
    public void setInstructions(){
    setScreen(scrInstructions);
    }
    public void setMenu(){
    setScreen(scrMenu);
    }
    public void setPlayscreen(){
setScreen(scrPlayScreen);
    }
    public void setMaps(){
setScreen(scrMaps);
    }
    public void setGameOver(){
setScreen(scrGameOver);
    }
    @Override
    public void render() {
        super.render();
    }
}