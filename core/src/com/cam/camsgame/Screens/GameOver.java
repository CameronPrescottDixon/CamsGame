package com.cam.camsgame.Screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.cam.camsgame.CamsGame;

/**
 * Created by Stefan on 12/01/2016.
 */public class GameOver extends ApplicationAdapter implements Screen {
    private Stage stage;
    private Texture tGOver, tButton;
    private BitmapFont fWhite, fBlack;
    private TextButton tbMenu, tbExit;
    private TextButton.TextButtonStyle textButtonStyle;
    private TextureAtlas taButton;
    private Skin skNewGame;
    private Music music;
    private CamsGame game;

    public GameOver(CamsGame game) {
        this.game = game;
        stage = new Stage();

        music = CamsGame.manager.get("Music/Gameover.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(music.getVolume() * 1 / 6);
        //used Bitmap Font Generator to make different fonts
        //http://www.angelcode.com/products/bmfont/
        fWhite = new BitmapFont(Gdx.files.internal("Fonts/white.fnt"));
        fBlack = new BitmapFont(Gdx.files.internal("Fonts/black.fnt"));

        tGOver = new Texture(Gdx.files.internal("Misc/GameOver.jpg"));

        //menu button and pack comes from TheDeepDarkTaurock code
        //creates buttons
        taButton = new TextureAtlas("Misc/MenuButton.pack");
        skNewGame = new Skin(taButton);
        tButton = new Texture(Gdx.files.internal("Misc/expences-button-png-hi.png"));
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skNewGame.newDrawable("MenuButtonUp");
        textButtonStyle.down = skNewGame.newDrawable("MenuButtonDown");
        textButtonStyle.checked = skNewGame.newDrawable("MenuButtonUp");
        textButtonStyle.over = skNewGame.newDrawable("MenuButtonUp");
        textButtonStyle.font = fWhite;

        tbMenu = new TextButton("Menu", textButtonStyle);
        tbMenu.pad(10f);
        tbMenu.setPosition(0, 0);

        tbExit = new TextButton("Exit", textButtonStyle);
        tbExit.pad(10f);
        tbExit.setPosition(500, 0);


        stage.addActor(tbMenu);
        stage.addActor(tbExit);
    }

    @Override
    public void show() {
        tbExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (tbExit.isDisabled() != true) {
                    Gdx.app.exit();
                }
            }
        });
        tbMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (tbExit.isDisabled() != true) {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(game.menu);
                    music.stop();
                }
            }
        });
    }

    @Override
    public void render(float delta) {
        if (game.getScreen() == this) {
            Gdx.input.setInputProcessor(stage);
            tbExit.setDisabled(false);
            music.play();
        } else {
            tbExit.setDisabled(true);
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getBatch().begin();
        stage.getBatch().draw(tGOver, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();
        stage.act();
        stage.draw();
    }

    @Override
    public void hide() {

    }
}

