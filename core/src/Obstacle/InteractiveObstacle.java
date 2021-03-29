package Obstacle;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import uc.ac.aston.game.Launcher;

public abstract class InteractiveObstacle {
	//the body object that would represent the obstacle.
	protected Body body;
	//Fixture associated with the obstacle.
	protected Fixture fixture;
	
	public InteractiveObstacle(World world,TiledMap map, Rectangle bounds) {
		//creates the body object for a particular obstacle.
		BodyDef bdef = new BodyDef();
		PolygonShape actor = new PolygonShape();
		FixtureDef f = new  FixtureDef();
		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set((bounds.getX()+bounds.getWidth()/2)/Launcher.PPM, (bounds.getY() + bounds.getHeight()/2)/Launcher.PPM);
		body = world.createBody(bdef);
		actor.setAsBox((bounds.getWidth()/2)/Launcher.PPM, (bounds.getHeight()/2)/Launcher.PPM);
		f.shape = actor;
		fixture = body.createFixture(f);
		
	}
	
	
	/**
	 * Sets the fixture of the obstacle to be identified differently.
	 * @param filterBit is a short value that would change the property of the fixture.
	 */
	public void setCategoryFilter(short filterBit) {
		Filter filter = new Filter();
		filter.categoryBits= filterBit;
		fixture.setFilterData(filter);
		
	}
	
	/**
	 * Allows to perform a sound effect when obstacle is hit.
	 */
	public abstract void collisionSound();

}
