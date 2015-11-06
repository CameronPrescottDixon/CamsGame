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

    private Integer nTime;
    private Integer nLevel;
    private Integer nMoney;

    Label lcountupLabel;
    Label lMoneyLabel;
    Label lLevelLabel;
    Label lMoney;
    Label lTime;
    Label lLevel;

    public Hud(SpriteBatch spriteBatch){
        nTime = 0;
        nMoney = 0;
        nLevel = 0;

        vpHud = new FitViewport(CamsGame.V_WIDTH, CamsGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(vpHud, spriteBatch);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        lcountupLabel = new Label(String.format("%05d", nTime), new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lLevelLabel = new Label(String.format("%03d", nLevel), new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lMoneyLabel = new Label(String.format("%06d", nMoney), new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lLevel = new Label("Level", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lMoney = new Label("Money", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        lTime = new Label("Time", new Label.LabelStyle(new BitmapFont(), Color.BLUE));

        table.add(lTime).expandX().padTop(5);
        table.add(lMoney).expandX().padTop(5);
        table.add(lLevel).expandX().padTop(5);
        table.row();
        table.add(lcountupLabel).expandX();
        table.add(lMoneyLabel).expandX();
        table.add(lLevelLabel).expandX();

        stage.addActor(table);
    }
}
//https://www.youtube.com/watch?v=7idwNW5a8Qs for the labels and table
