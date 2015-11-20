package com.cam.camsgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cam.camsgame.Screens.PlayScreen;

public class CamsGame extends Game {
	public static final int V_WIDTH = 1000;
	public static final int V_HEIGHT = 1000;
	public SpriteBatch batch;


	public static AssetManager manager;
	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("Music/LetTheBodiesHitTheFloor.mp3", Music.class);
		manager.finishLoading(); // finish loading all the assets for now
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}

