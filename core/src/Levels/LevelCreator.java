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

import Obstacle.BlueDragon;
import Obstacle.Door;
import Obstacle.Dragon;
import Obstacle.Enemy;
import Obstacle.FinishLine;
import Obstacle.Sea;
import Obstacle.Spikes;
import Screens.PlayScreen;
import uc.ac.aston.game.Launcher;

public class LevelCreator {
	private Array<Enemy> dragons;
	
	public LevelCreator(TiledMap map, World world, PlayScreen screen) {
		BodyDef bdef = new BodyDef();
		PolygonShape actor = new PolygonShape();
		FixtureDef f = new  FixtureDef();
		Body body;
		
		for (MapObject object: map.getLayers().get(9).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			new FinishLine(world,map,rect);
		}
		
		for (MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX()+rect.getWidth()/2)/Launcher.PPM, (rect.getY() + rect.getHeight()/2)/Launcher.PPM);
			body = world.createBody(bdef);
			actor.setAsBox((rect.getWidth()/2)/Launcher.PPM, (rect.getHeight()/2)/Launcher.PPM);
			f.shape = actor;
			body.createFixture(f);
			}
		
		for (MapObject object: map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			new Spikes(world,map,rect);
		}
		
		for (MapObject object: map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			new Door(world,map,rect);
		}
		
		for (MapObject object: map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			new Sea(world,map,rect);
		}
		
		for (MapObject object: map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX()+rect.getWidth()/2)/Launcher.PPM, (rect.getY() + rect.getHeight()/2)/Launcher.PPM);
			body = world.createBody(bdef);
			actor.setAsBox((rect.getWidth()/2)/Launcher.PPM, (rect.getHeight()/2)/Launcher.PPM);
			f.shape = actor;
			body.createFixture(f);
			
		}
		
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
		
		dragons = new Array<Enemy>();
		for (MapObject object: map.getLayers().get(11).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			dragons.add(new Dragon(screen,rect.getX()/Launcher.PPM,rect.getY()/Launcher.PPM));
		}
		
		for (MapObject object: map.getLayers().get(12).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			dragons.add(new BlueDragon(screen,rect.getX()/Launcher.PPM,rect.getY()/Launcher.PPM));
		}
		
		
		
		

	}
	
	public Array<Enemy> getDragons(){
		return dragons;
	}
	

}
