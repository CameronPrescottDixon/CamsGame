package com.cam.camsgame.Screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

/**
 * Created by Cameron on 2015-12-15.
 */
public class Menu extends ApplicationAdapter implements Screen {

    private static final float BUTTON_WIDTH = 300f;
    private static final float BUTTON_HEIGHT = 30f;
    public boolean bStart = false;
    private Viewport vpHud;
    SpriteBatch batch;
    Texture tBack, tButton;
    Stage stage;
    TextureAtlas taButton;
    BitmapFont fWhite, fBlack;
    TextButton tbStart, tbExit;
    TextButton.TextButtonStyle textButtonStyle;
    Table table;
    Skin skNewGame;
    Label laTitle;
    Label.LabelStyle headingstyle;
    Music music;
    int nChoi = 0;
    private CamsGame game;
    public Menu(CamsGame game) {
this.game = game;
    }

    @Override
    public void create() {
        //bulk of code came from https://www.youtube.com/watch?v=q2qoiTqGsh8
        vpHud = new FitViewport(CamsGame.V_WIDTH, CamsGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(vpHud, game.batch);

        //creates background music
        //http://www.norakomi.com/tutorial_mambow2_music.php
        music = Gdx.audio.newMusic(Gdx.files.internal("Music/Halo- Menu Music.mp3"));
        music.setLooping(true);
        music.setVolume(music.getVolume() * 1 / 6);
        music.play();

        //used Bitmap Font Generator to make different fonts
        //http://www.angelcode.com/products/bmfont/
        fWhite = new BitmapFont(Gdx.files.internal("Fonts/white.fnt"));
        fBlack = new BitmapFont(Gdx.files.internal("Fonts/black.fnt"));

        tBack = new Texture(Gdx.files.internal("Picnic.jpg"));

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
        tbStart.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        tbStart.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            game.setScreen(new PlayScreen(game));
            }
        });

        //creates exit game button
        tbExit = new TextButton("Exit", textButtonStyle);
        tbExit.pad(10f);
        tbExit.setSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        tbExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        //put everything into a table and onto the stage
        table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.setFillParent(true);
        table.add(laTitle);
        table.row();
        table.add(tbStart);
        table.row();
        table.add(tbExit);
        stage.addActor(table);
    }

    @Override
    public void render(float dt) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
