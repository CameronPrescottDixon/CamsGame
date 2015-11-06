package com.cam.camsgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.cam.camsgame.CamsGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = CamsGame.V_HEIGHT;
		config.width = CamsGame.V_WIDTH;
		config.title = "Cams Game";
		new LwjglApplication(new CamsGame(), config);
	}
}
