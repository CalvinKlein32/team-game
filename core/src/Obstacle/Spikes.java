package Obstacle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;

import uc.ac.aston.game.Launcher;

/**
 * Spikes object that represents an obstacles for the player. Fixture are associated with it so it can be identified.
 *
 */
public class Spikes extends InteractiveObstacle{

	public Spikes(World world, TiledMap map, Rectangle bounds) {
		super(world, map, bounds);
		fixture.setUserData(this);
		setCategoryFilter(Launcher.spikeBit);
	}


	
	
}
