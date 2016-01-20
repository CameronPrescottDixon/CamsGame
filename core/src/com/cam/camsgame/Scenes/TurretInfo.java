package com.cam.camsgame.Scenes;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.cam.camsgame.CamsGame;
import java.awt.Label;

/**
 * Created by Cameron on 2016-01-20.
 */
public class TurretInfo implements Disposable{
    private Viewport vpTurrInfo;
    private Label lblDamage, lblRange, lblFireRate, lblCost, lblUpgraded;
    private int nDamage, nRange, nFireRate, nCost, nUpgraded;
    private  Stage stage;

    public TurretInfo(SpriteBatch spriteBatch){
        vpTurrInfo = new FitViewport(CamsGame.V_WIDTH, CamsGame.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(vpTurrInfo, spriteBatch);
        Table table = new Table();
        table.right();
    }

    @Override
    public void dispose() {

    }
}
