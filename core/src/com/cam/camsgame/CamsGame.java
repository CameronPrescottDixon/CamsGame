package com.cam.camsgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cam.camsgame.Screens.Instructions;
import com.cam.camsgame.Screens.Maps;
import com.cam.camsgame.Screens.Menu;
import com.cam.camsgame.Screens.PlayScreen;


public class CamsGame extends Game {
    public static final int V_WIDTH = 1000;
    public static final int V_HEIGHT = 1000;
    public SpriteBatch batch;
    public static AssetManager manager;
    public Menu menu;
    public PlayScreen playScreen;
    public Instructions instructions;
    public Maps maps;


    @Override
    public void create() {
        batch = new SpriteBatch();
        manager = new AssetManager();
        manager.load("Music/Halo- Menu Music.mp3", Music.class);
        manager.load("Music/Music1.mp3", Music.class);
        manager.load("Music/Music2.mp3", Music.class);
        manager.load("Music/Music3.mp3", Music.class);
        manager.load("Music/Music4.mp3", Music.class);
        manager.finishLoading(); // finish loading all the assets for now
        playScreen = new PlayScreen(this);
        menu = new Menu(this);
        instructions = new Instructions(this);
        maps = new Maps(this);
        setScreen(menu);
    }

    @Override
    public void render() {
        super.render();
    }
}