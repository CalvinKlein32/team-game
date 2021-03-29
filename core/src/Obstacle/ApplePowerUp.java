package Obstacle;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import uc.ac.aston.game.Launcher;

/**
 * 
 * ApplePowerUp class that represents the apple object in the game world, that allows power up effect on the player.
 *
 */
public class ApplePowerUp {

	    //map variable that would be used to contain a TiledMap, TiledMAp is map made of textures where a basic unit is a Tile.
		private TiledMap map;
		//bounds is the rectangular bounds of the apple object.
		private Rectangle bounds;
		//the body object that would represent the apple.
		private Body body;
		//Fixture associated with the apple.
		private Fixture fixture;
		
		public ApplePowerUp(World world, TiledMap map, Rectangle bounds) {
			this.map=map;
			this.bounds=bounds;
			//creates the body object of the apple surrounding it with a rectangular bounds
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
			setCategoryFilter(Launcher.powerUpBit);
		}
		
		
		/**
		 * Sets the filter category bit to a new filter to be identified as a different fixture.
		 * @param filterBit the filter fixture that would be assigned to the Apple
		 */
		public void setCategoryFilter(short filterBit) {
			Filter filter = new Filter();
			filter.categoryBits=filterBit;
			fixture.setFilterData(filter);
		}
		
		/**
		 * Destroys the physical cells that make the apple power up by taking away the tiles of the apple from the TiledMap.
		 */
		public void destroyDoorCells(){
			setCategoryFilter(Launcher.destroyedBit);
			//index 3 in the TileMap is used for the layer on which the door was constructed on.
			TiledMapTileLayer layer= (TiledMapTileLayer) map.getLayers().get(3);
			int dx=Math.round(bounds.getX()/16);
			int dy=((int)(bounds.getY())/16);
			
			for (int j=0;j<= Math.round(bounds.getHeight())/16;j++) {
				for (int i=0; i<Math.round(bounds.getWidth()/16);i++) {
					if (layer.getCell(dx+i, dy+j)!=null){
						layer.getCell(dx+i, dy+j).setTile(null);
					}
				}
			}
		
		}
}
