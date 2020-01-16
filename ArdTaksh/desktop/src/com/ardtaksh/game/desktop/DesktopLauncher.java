package com.ardtaksh.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ardtaksh.game.ArdTakshGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1000;
		config.height = 1000;
		config.forceExit = true;
		new LwjglApplication(new ArdTakshGame(), config);
	}
}
