package com.cam.camsgame.Scenes;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cam.camsgame.CamsGame;
import com.cam.camsgame.Screens.PlayScreen;

/**
 * Created by Cameron on 2016-01-20.
 */
public class TurretInfo extends ApplicationAdapter implements Disposable{
    private Viewport vpTurrInfo;
    private Label lblDamage, lblRange, lblFireRate, lblCost, lblUpgraded;
    private Label lblDamage2, lblRange2, lblFireRate2, lblCost2, lblUpgraded2;
    private int nDamage, nRange, nCost, nUpgraded;
    private long nFireRate;
    public Stage stage;
    private Texture tInst1, tButton;
    private BitmapFont fWhite, fBlack;
    public TextButton tbSell, tbUpgrade;
    private TextButton.TextButtonStyle textButtonStyle;
    private TextureAtlas taButton;
    private Skin skNewGame;

    public TurretInfo(SpriteBatch spriteBatch, final PlayScreen playScreen) {
        nDamage = 0;
        nRange = 0;
        nFireRate = 0;
        nCost = 0;
        nUpgraded = 0;

        fWhite = new BitmapFont(Gdx.files.internal("Fonts/white.fnt"));
        fBlack = new BitmapFont(Gdx.files.internal("Fonts/black.fnt"));

        tInst1 = new Texture(Gdx.files.internal("Misc/instp1.jpg"));

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

        tbSell = new TextButton("Sell", textButtonStyle);
        tbSell.pad(10f);

        tbUpgrade = new TextButton("Upgrade", textButtonStyle);
        tbUpgrade.pad(10f);
        vpTurrInfo = new StretchViewport(CamsGame.V_WIDTH, CamsGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(vpTurrInfo, spriteBatch);
        Table table = new Table();
        table.right();
        table.setFillParent(true);
        //Set the labels for each
        lblDamage = new Label(String.format("%03d", nDamage), new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lblRange = new Label(String.format("%04d", nRange), new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lblFireRate = new Label(String.format("%01d", nFireRate), new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lblCost = new Label(String.format("%03d", nCost), new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lblUpgraded = new Label(String.format("%01d", nUpgraded), new Label.LabelStyle(new BitmapFont(), Color.BLUE));

        lblDamage2 = new Label("Damage", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lblRange2 = new Label("Range", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lblFireRate2 = new Label("Fire Rate", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lblCost2 = new Label("Cost", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lblUpgraded2 = new Label("Upgraded", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        //Give each label 1/3 of the width of the table
        table.add(tbSell).width(50).height(50).padBottom(30);
        table.row();
        table.add(tbUpgrade).height(50).width(75).padBottom(30);
        table.row();
        table.add(lblDamage2);
        table.row();
        table.add(lblDamage).padBottom(30);
        table.row();
        table.add(lblRange2);
        table.row();
        table.add(lblRange).padBottom(30);
        table.row();
        //add a row underneath the first row and give each label 1/3 of the width of the table
        table.row();
        table.add(lblFireRate2);
        table.row();
        table.add(lblFireRate).padBottom(30);
        table.row();
        table.add(lblCost2);
        table.row();
        table.add(lblCost).padBottom(30);
        table.row();
        table.add(lblUpgraded2);
        table.row();
        table.add(lblUpgraded);
        table.padRight(15);
        stage.addActor(table);
    }

    public void giveVals(int nFireRate, int nDamage, int nRange, int nCost, int nUpgraded) {
        this.nFireRate = (1000000000 - nFireRate) / 1000000000;
        this.nDamage = nDamage;
        this.nRange = nRange;
        this.nCost = nCost;
        this.nUpgraded = nUpgraded;
        update();
    }

    public void update() {
        updateCost();
        updateDamage();
        updateFireRate();
        updateRange();
        updateUpgraded();
    }

    public void updateFireRate() {
        lblFireRate.setText(String.format("%01d", nFireRate));
    }

    public void updateDamage() {
        lblDamage.setText(String.format("%03d", nDamage));
    }

    public void updateRange() {
        lblRange.setText(String.format("%04d", nRange));
    }

    public void updateCost() {
        lblCost.setText(String.format("%03d", nCost));
    }

    public void updateUpgraded() {
        lblUpgraded.setText(String.format("%01d", nUpgraded));
    }


    @Override
    public void dispose() {

    }
}
