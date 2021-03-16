package Levels;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import Obstacle.ApplePowerUp;
import Obstacle.BlueDragon;
import Obstacle.Door;
import Obstacle.Dragon;
import Obstacle.Enemy;
import Obstacle.FinishLine;
import Obstacle.Sea;
import Obstacle.Spikes;
import Screens.PlayScreen;
import uc.ac.aston.game.Launcher;

/**
 * LevelCreator class reads all the object components from a tile Map and reproduces them in the game world appropriately, so that the 
 * player can interact with it.
 */
public class LevelCreator {
	//enemies is a list that would be used to contain enemies(red and blue dragons) obtained from the TiledMap.
	private Array<Enemy> enemies=new Array<Enemy>();;
	
	public LevelCreator(TiledMap map, World world, PlayScreen screen) {
		BodyDef bdef = new BodyDef();
		PolygonShape actor = new PolygonShape();
		FixtureDef f = new  FixtureDef();
		Body body;
		
		//From the tileMap at index 9 there is the Finish Line object we need from that tiledMap, and create a Body object and fixture for it
		for (MapObject object: map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			new FinishLine(world,map,rect);
		}
		
		//From the tileMap at index 5 there are all the the Ground object we need for that tiledMap, and create a Body object and fixture for it.
		for (MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX()+rect.getWidth()/2)/Launcher.PPM, (rect.getY() + rect.getHeight()/2)/Launcher.PPM);
			body = world.createBody(bdef);
			actor.setAsBox((rect.getWidth()/2)/Launcher.PPM, (rect.getHeight()/2)/Launcher.PPM);
			f.shape = actor;
			body.createFixture(f);
			}
		
		//From the tileMap at index 7 there are all Spikes object we need for that tiledMap, and create a Body object and fixture for it.
		for (MapObject object: map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			new Spikes(world,map,rect);
		}
		
		//From the tileMap at index  6 there are all Door object we need for that tiledMap, and create a Body object and fixture for it.
		for (MapObject object: map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			new Door(world,map,rect);
		}
		
		//From the tileMap at index 8 there are all Sea object we need for that tiledMap, and create a Body object and fixture for it.
		for (MapObject object: map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			new Sea(world,map,rect);
		}
		
		//From the tileMap at index 4 there are all Boundaries object we need for that tiledMap, to prevent player from moving past the boundaries
		//of the map, and create a Body object and fixture for it.
		for (MapObject object: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX()+rect.getWidth()/2)/Launcher.PPM, (rect.getY() + rect.getHeight()/2)/Launcher.PPM);
			body = world.createBody(bdef);
			actor.setAsBox((rect.getWidth()/2)/Launcher.PPM, (rect.getHeight()/2)/Launcher.PPM);
			f.shape = actor;
			body.createFixture(f);
		}
		
		//From the tileMap at index 10 there are all Enemy Bounds object we need for that tiledMap, to confine enemy on a particular space of the map
		//creates a Body object and fixture for it.
		for (MapObject object: map.getLayers().get(10).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX()+rect.getWidth()/2)/Launcher.PPM, (rect.getY() + rect.getHeight()/2)/Launcher.PPM);
			body = world.createBody(bdef);
			actor.setAsBox((rect.getWidth()/2)/Launcher.PPM, (rect.getHeight()/2)/Launcher.PPM);
			f.shape = actor;
			f.filter.categoryBits=Launcher.objectBit;
			body.createFixture(f);
			
		}
		
		//From the tileMap at index 11 there are all the the (red) Dragon object we need for that tiledMap, and create a Body object and fixture for it.
		for (MapObject object: map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			enemies.add(new Dragon(screen,rect.getX()/Launcher.PPM,rect.getY()/Launcher.PPM));
		}
		
		//From the tileMap at index 12 there are all the the BlueDragon object we need for that tiledMap, and create a Body object and fixture for it.
		for (MapObject object: map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			enemies.add(new BlueDragon(screen,rect.getX()/Launcher.PPM,rect.getY()/Launcher.PPM));
		}
		
		for (MapObject object: map.getLayers().get(13).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			new ApplePowerUp(world,map,rect);
		}
		
		
		
		

	}
	
	
	/**
	 * 
	 * @return a list of all the enemies objects obtained from the TiledMAp
	 */
	public Array<Enemy> getDragons(){
		return enemies;
	}
	

}
