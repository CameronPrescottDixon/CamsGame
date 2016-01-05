package com.cam.camsgame.Screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cam.camsgame.CamsGame;

import java.awt.SystemColor;

/**
 * Created by Cameron on 2015-12-15.
 */
public class Menu extends ApplicationAdapter implements Screen {

    private Texture tBack, tButton;
    private Sprite spBack;
    private TextureAtlas taButton;
    private BitmapFont fWhite, fBlack;
    private TextButton.TextButtonStyle textButtonStyle;
    private Table table;
    private Skin skNewGame;
    private Label laTitle;
    private Label.LabelStyle headingstyle;
    private Music music;
    private Stage stage;
    private CamsGame game;
    private TextButton tbStart, tbExit, tbInstr;

    public Menu(CamsGame game) {
        this.game = game;
        stage = new Stage();
        //bulk of code came from https://www.youtube.com/watch?v=q2qoiTqGsh8
        //creates background music
        //http://www.norakomi.com/tutorial_mambow2_music.php
        music = CamsGame.manager.get("Music/Halo- Menu Music.mp3", Music.class);
        music.setLooping(true);
        music.setVolume(music.getVolume() * 1 / 6);
        music.play();

        //used Bitmap Font Generator to make different fonts
        //http://www.angelcode.com/products/bmfont/
        fWhite = new BitmapFont(Gdx.files.internal("Fonts/white.fnt"));
        fBlack = new BitmapFont(Gdx.files.internal("Fonts/black.fnt"));

        spBack = new Sprite(new Texture("Picnic.jpg"));
        spBack.setSize(1000, 1000);

        //menu button and pack comes from TheDeepDarkTaurock code
        //creates buttons
        taButton = new TextureAtlas("MenuButton.pack");
        skNewGame = new Skin(taButton);
        tButton = new Texture(Gdx.files.internal("expences-button-png-hi.png"));
        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skNewGame.newDrawable("MenuButtonUp");
        textButtonStyle.down = skNewGame.newDrawable("MenuButtonDown");
        textButtonStyle.checked = skNewGame.newDrawable("MenuButtonUp");
        textButtonStyle.over = skNewGame.newDrawable("MenuButtonUp");
        textButtonStyle.font = fWhite;

        //creates title
        headingstyle = new Label.LabelStyle(fBlack, Color.WHITE);
        laTitle = new Label("Ants 'n Stuff", headingstyle);
        laTitle.setFontScale(2);

        //creates start game button
        tbStart = new TextButton("Start", textButtonStyle);
        tbStart.pad(10f);

        tbInstr = new TextButton("Instructions", textButtonStyle);
        tbInstr.pad(10f);

        //creates exit game button
        tbExit = new TextButton("Exit", textButtonStyle);
        tbExit.pad(10f);

        //put everything into a table and onto the stage
        table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.setFillParent(true);
        table.add(laTitle);
        table.row();
        table.add(tbStart);
        table.row();
        table.add(tbInstr);
        table.row();
        table.add(tbExit);
        stage.addActor(table);
    }

    @Override
    public void render(float dt) {
        if(game.getScreen() == this && tbStart.isDisabled() == true) {
            tbExit.setDisabled(false);
            music.play();
        }
        if(game.getScreen() == this){
            Gdx.input.setInputProcessor(stage);
            tbExit.setDisabled(false);
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        spBack.draw(game.batch);
        game.batch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void show() {
            tbStart.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (tbExit.isDisabled() != true) {
                        System.out.println(tbStart.isDisabled());
                        game.playScreen.startGame();
                        tbExit.setDisabled(true);
                        music.stop();
                        ((Game) Gdx.app.getApplicationListener()).setScreen(game.playScreen);
                    }
                }
            });

            tbExit.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (tbExit.isDisabled() != true) {
                        Gdx.app.exit();
                    }
                }
            });

            tbInstr.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (tbExit.isDisabled() != true) {
                    tbExit.setDisabled(true);
                    ((Game) Gdx.app.getApplicationListener()).setScreen(game.instructions);
                }
            }
        });
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }
}
