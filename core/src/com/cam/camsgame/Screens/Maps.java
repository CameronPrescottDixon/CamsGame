package com.cam.camsgame.Screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.cam.camsgame.CamsGame;

/**
 * Created by Cameron on 2016-01-05.
 */
public class Maps extends ApplicationAdapter implements Screen {
private CamsGame game;
    public Maps(CamsGame game) {
    this.game = game;

    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void hide() {

    }
}
