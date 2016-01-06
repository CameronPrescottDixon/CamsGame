package com.cam.camsgame.Screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.cam.camsgame.CamsGame;

/**
 * Created by Cameron on 2016-01-05.
 */
public class Maps extends ApplicationAdapter implements Screen {
    private SpriteBatch batch;
    private Stage stage;
    private Table table;
    private Texture  tBack, tButton;
    private BitmapFont fWhite, fBlack;
    private TextButton tbMenu, tbNext, tbBack, tbConfirm;
    private TextButton.TextButtonStyle textButtonStyle;
    private TextureAtlas taButton;
    private Skin skNewGame;
    private int nNum=0;
    private CamsGame game;

    public Maps(CamsGame game) {
    this.game = game;
        stage = new Stage();
        //used Bitmap Font Generator to make different fonts
        //http://www.angelcode.com/products/bmfont/
        fWhite = new BitmapFont(Gdx.files.internal("Fonts/white.fnt"));
        fBlack = new BitmapFont(Gdx.files.internal("Fonts/black.fnt"));

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
        tbMenu.setPosition(0, 500);

        tbConfirm = new TextButton("Confirm Choice", textButtonStyle);
        tbConfirm.pad(10f);
        tbConfirm.setPosition(250,0);

        tbNext = new TextButton("Next", textButtonStyle);
        tbNext.pad(10f);
        tbNext.setPosition(500, 0);

        tbBack = new TextButton("Back", textButtonStyle);
        tbBack.pad(10f);
        tbBack.setPosition(0, 0);

        stage.addActor(tbMenu);
        stage.addActor(tbNext);
        stage.addActor(tbBack);
        stage.addActor(tbConfirm);
    }
    @Override
    public void show() {
        tbBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (tbBack.isDisabled() != true) {
                    if (nNum != 1) {
                        nNum--;
                    } else {
                        nNum = 3;
                    }
                    System.out.println(nNum);
                }
            }
        });
        tbNext.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (tbBack.isDisabled() != true) {
                    if (nNum != 3) {
                        nNum++;
                    } else {
                        nNum = 1;
                    }
                    System.out.println(nNum);
                }
            }
        });
        tbMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (tbBack.isDisabled() != true) {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(game.menu);
                }
            }
        });
        tbConfirm.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (tbBack.isDisabled() != true) {
                    game.playScreen.changeMap(nNum);
                }
            }
        });
    }

    @Override
    public void render(float delta) {
        if(game.getScreen() == this){
            Gdx.input.setInputProcessor(stage);
            tbBack.setDisabled(false);
        }else{
            tbBack.setDisabled(true);
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void hide() {

    }
}
