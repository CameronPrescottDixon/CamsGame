package com.cam.camsgame.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cam.camsgame.CamsGame;

/**
 * Created by Cameron on 2015-11-04.
 */
public class Hud {
    public Stage stage;
    private Viewport vpHud;

    private float fTime;
    private int nWorldTime,nMoney,nLevel;

    Label lblCountUpLabel;
    Label lMoneyLabel;
    Label lLevelLabel;
    Label lMoney;
    Label lTime;
    Label lLevel;

    public Hud(SpriteBatch spriteBatch){
        nMoney = 500;
        nLevel = 0;
        nWorldTime = 0;

        vpHud = new FitViewport(CamsGame.V_WIDTH, CamsGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(vpHud, spriteBatch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        //Set the labels for each
        lblCountUpLabel = new Label(String.format("%05d", nWorldTime), new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lLevelLabel = new Label(String.format("%03d", nLevel), new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lMoneyLabel = new Label(String.format("%06d", nMoney), new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lLevel = new Label("Level", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lMoney = new Label("Money", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lTime = new Label("Time", new Label.LabelStyle(new BitmapFont(), Color.BLUE));

        //Give each label 1/3 of the width of the table
        table.add(lTime).expandX().padTop(5);
        table.add(lMoney).expandX().padTop(5);
        table.add(lLevel).expandX().padTop(5);
        //add a row underneath the first row and give each label 1/3 of the width of the table
        table.row();
        table.add(lblCountUpLabel).expandX();
        table.add(lMoneyLabel).expandX();
        table.add(lLevelLabel).expandX();
        //Add the table to the stage after it's been created
        stage.addActor(table);
    }
    public void update(float dt){
        fTime += dt;
        if(fTime >=1){//This makes the time only go up every second
            nWorldTime++;
            lblCountUpLabel.setText(String.format("%05d", nWorldTime));
            fTime = 0;
        }
    }
    public void addMoney(int money){
        nMoney+=money;
        lMoneyLabel.setText(String.format("%06d", nMoney));
    }
    public void subtMoney(int money){
        nMoney-=money;
        lMoneyLabel.setText(String.format("%06d", nMoney));
    }
}
//https://www.youtube.com/watch?v=7idwNW5a8Qs for the labels and table
