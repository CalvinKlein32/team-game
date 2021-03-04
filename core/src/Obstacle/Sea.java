package Obstacle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import uc.ac.aston.game.Launcher;

/**
* Sea object that represents an obstacles for the player. Fixture are associated with it so it can be identified.
*
*/

public class Sea extends InteractiveObstacle{

	public Sea(World world, TiledMap map, Rectangle bounds) {
		super(world, map, bounds);
		fixture.setUserData(this);
		setCategoryFilter(Launcher.seaBit);
	}



}
