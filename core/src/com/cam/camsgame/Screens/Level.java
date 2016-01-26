package com.cam.camsgame.Screens;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
 * Created by Stefan on 25/01/2016.
 */
public class Level extends ApplicationAdapter implements Screen {
    private Stage stage;
    private Table table;
    private Texture tBack, tButton;
    private BitmapFont fWhite, fBlack;
    private Label laTitle, laLevel;
    private Label.LabelStyle headingstyle;
    private TextButton tbMenu, tbNext, tbBack;
    private TextButton.TextButtonStyle textButtonStyle;
    private TextureAtlas taButton;
    private Skin skNewGame;
    private int nNum = 0;
    private CamsGame game;

    public Level(CamsGame game) {
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

        tBack = new Texture("Misc/Picnic.jpg");

        headingstyle = new Label.LabelStyle(fBlack, Color.WHITE);
        laTitle = new Label("Current Starting Level:", headingstyle);
        laTitle.setFontScale(2);

        headingstyle = new Label.LabelStyle(fBlack, Color.WHITE);
        //laLevel = new Label(String.format("%01d", nNum), headingstyle);
        //laLevel.setFontScale(2);

        tbMenu = new TextButton("Menu", textButtonStyle);
        tbMenu.pad(10f);
        tbMenu.setPosition(0, 500);

        tbNext = new TextButton("Increase Starting Level", textButtonStyle);
        tbNext.pad(10f);
        tbNext.setPosition(500, 0);

        tbBack = new TextButton("Decrease Starting Level", textButtonStyle);
        tbBack.pad(10f);
        tbBack.setPosition(0, 0);

        table = new Table();
        /*table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.setFillParent(true);
        table.add(laTitle);
        table.row();
        table.add(laLevel);
        stage.addActor(table);*/
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
                    if(nNum>0){
                        nNum-=10;
                    }
                    else{
                        nNum=0;
                    }
                    System.out.println(nNum);
                }
            }
        });
        tbNext.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (tbBack.isDisabled() != true) {
                    nNum+=10;
                    System.out.println(nNum);
                }
            }
        });
        tbMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (tbBack.isDisabled() != true) {
                    game.setMenu();
                    game.setLevel(nNum);
                }
            }
        });
    }

    @Override
    public void render(float delta) {
        if (game.getScreen() == this) {
            Gdx.input.setInputProcessor(stage);
            tbBack.setDisabled(false);
        } else {
            tbBack.setDisabled(true);
        }
        laLevel = new Label(String.format("%01d", nNum), headingstyle);
        laLevel.setFontScale(2);
        table = new Table();
        table.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.setFillParent(true);
        table.add(laTitle);
        table.row();
        table.add(laLevel);
        stage.addActor(table);
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
