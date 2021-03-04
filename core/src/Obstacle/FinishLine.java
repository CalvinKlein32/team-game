package Obstacle;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import uc.ac.aston.game.Launcher;

/**
 * Finish Line object that represents end of a level for a player. The Finish Line object is created and the fixtures are defined
 *
 */
public class FinishLine {
	//the body object that would represent the finish line.
	private Body body;
	//Fixture associated with the finish line.
	private Fixture fixture;
	
	public FinishLine(World world,TiledMap map, Rectangle bounds) {
		BodyDef bdef = new BodyDef();
		PolygonShape actor = new PolygonShape();
		FixtureDef f = new  FixtureDef();
		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set((bounds.getX()+bounds.getWidth()/2)/Launcher.PPM, (bounds.getY() + bounds.getHeight()/2)/Launcher.PPM);
		body = world.createBody(bdef);
		actor.setAsBox((bounds.getWidth()/2)/Launcher.PPM, (bounds.getHeight()/2)/Launcher.PPM);
		f.shape = actor;
		fixture = body.createFixture(f);
		fixture.setUserData(this);
	}
	
}
