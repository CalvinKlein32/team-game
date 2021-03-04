package uc.ac.aston.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import uc.ac.aston.game.Launcher;
/**
 * 
 * @author Los Thunder Team
 * 
 * The class is responsible for the window configurations on which the  game will be run. 
 *
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title="Los thunder";
		config.width=600;
		config.height=600;
		config.resizable=true;
		new LwjglApplication(new Launcher(), config);
	}
}
