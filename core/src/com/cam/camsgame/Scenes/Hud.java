package com.cam.camsgame.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cam.camsgame.CamsGame;

/**
 * Created by Cameron on 2015-11-04.
 */
public class Hud implements Disposable {
    public Stage stage;
    private Viewport vpHud;

    private float fTime;
    public int nWorldTime, nMoney, nLevel, nHP;

    Label lblCountUpLabel;
    Label lblMoneyAmount;
    Label lblRoundNumber;
    Label lblMoney;
    Label lblTime;
    Label lblRound;
    Label lblHealth;
    Label lblHP;


    public Hud(SpriteBatch spriteBatch) { //Hud class, displays labels in a table and can add + subtract money, also increases time based on deltaTime
        nMoney = 1000;
        nHP = 100;
        nLevel = 0;
        nWorldTime = 0;

        vpHud = new FitViewport(CamsGame.V_WIDTH, CamsGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(vpHud, spriteBatch);

        Table table = new Table();
        table.top(); // Sets table to the top of the screen
        table.setFillParent(true);

        //Set the labels for each
        lblCountUpLabel = new Label(String.format("%02d", nWorldTime), new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lblHP = new Label(String.format("%03d", nHP), new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lblRoundNumber = new Label(String.format("%01d", nLevel), new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lblMoneyAmount = new Label(String.format("%03d", nMoney), new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lblRound = new Label("Round", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lblMoney = new Label("Money", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lblTime = new Label("Time", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lblHealth = new Label("Health", new Label.LabelStyle(new BitmapFont(), Color.BLUE));

        //Give each label 1/3 of the width of the table
        table.add(lblTime).expandX().padTop(5);
        table.add(lblMoney).expandX().padTop(5);
        table.add(lblRound).expandX().padTop(5);
        table.add(lblHealth).expandX().padTop(5);
        //add a row underneath the first row and give each label 1/3 of the width of the table
        table.row();
        table.add(lblCountUpLabel).expandX();
        table.add(lblMoneyAmount).expandX();
        table.add(lblRoundNumber).expandX();
        table.add(lblHP).expandX();
        //Add the table to the stage after it's been created
        stage.addActor(table);
    }

    public void updateTime(float dt) { //updates time
        fTime += dt;
        if (fTime >= 1) {//This makes the time only go up every second
            nWorldTime++;
            lblCountUpLabel.setText(String.format("%02d", nWorldTime));
            fTime = 0;
        }
    }

    public void addMoney(int money) {//adds money
        nMoney += money;
        lblMoneyAmount.setText(String.format("%03d", nMoney));
    }

    public void subtMoney(int money) {//subtracts money
        nMoney -= money;
        if (nMoney > 0) {
            lblMoneyAmount.setText(String.format("%03d", nMoney));

        } else if (nMoney == 0) {
            lblMoneyAmount.setText(String.format("%03d", 0));
        }
    }

    public void nextRound() {
        nLevel++;
        lblRoundNumber.setText(String.format("%01d", nLevel));
    }

    public void loseHP(int nLose) {
        nHP -= nLose;
        lblHP.setText(String.format("%03d", nHP));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
//https://www.youtube.com/watch?v=7idwNW5a8Qs for the labels and table
