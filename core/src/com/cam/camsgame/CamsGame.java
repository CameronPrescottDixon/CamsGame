package com.cam.camsgame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cam.camsgame.Screens.PlayScreen;

public class CamsGame extends Game {
	public static final int V_WIDTH = 1000;
	public static final int V_HEIGHT = 1000;
	public SpriteBatch batch;
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
}

