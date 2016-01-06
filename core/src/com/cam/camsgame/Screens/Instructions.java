package com.cam.camsgame.Screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.cam.camsgame.CamsGame;

/**
 * Created by Cameron on 2016-01-02.
 */
public class Instructions extends ApplicationAdapter implements Screen {
    private SpriteBatch batch;
    private Stage stage;
    private Table table;
    private Texture tInst1, tInst2, tInst3, tBack, tButton;
    private BitmapFont fWhite, fBlack;
    private TextButton tbMenu, tbNext, tbBack;
    private TextButton.TextButtonStyle textButtonStyle;
    private TextureAtlas taButton;
    private Skin skNewGame;
    private int nNum=1;
    private CamsGame game;

    public Instructions(CamsGame game) {
        this.game = game;
        stage = new Stage();
        //used Bitmap Font Generator to make different fonts
        //http://www.angelcode.com/products/bmfont/
        fWhite = new BitmapFont(Gdx.files.internal("Fonts/white.fnt"));
        fBlack = new BitmapFont(Gdx.files.internal("Fonts/black.fnt"));

        tInst1 = new Texture(Gdx.files.internal("Misc/instp1.jpg"));
        tInst2 = new Texture(Gdx.files.internal("Misc/instp2.jpg"));
        tInst3 = new Texture(Gdx.files.internal("Misc/instp3.jpg"));

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

        tbNext = new TextButton("Next", textButtonStyle);
        tbNext.pad(10f);
        tbNext.setPosition(500, 0);

        tbBack = new TextButton("Back", textButtonStyle);
        tbBack.pad(10f);
        tbBack.setPosition(0, 0);

        stage.addActor(tbMenu);
        stage.addActor(tbNext);
        stage.addActor(tbBack);
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
    }

    @Override
    public void render(float delta) {
        if(game.getScreen() == this){
            Gdx.input.setInputProcessor(stage);
            tbBack.setDisabled(false);
            if(nNum==1){
                tBack = tInst1;
            }
            if(nNum==2){
                tBack = tInst2;
            }
            if(nNum==3){
                tBack = tInst3;
            }
        }else{
            tbBack.setDisabled(true);
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.getBatch().begin();
        stage.getBatch().draw(tBack, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();
        stage.act();
        stage.draw();
    }

    @Override
    public void hide() {

    }
}
