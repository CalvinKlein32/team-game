package Obstacle;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import Screens.PlayScreen;

public abstract class Enemy extends Sprite{
	
	//world is the World object in which enemy object will be present.
	protected World world;
	//screen is the screen in which the enemies will be present in.
	protected PlayScreen screen;
	//b2body is the Body object that would represent the enemy.
	public Body b2body;
	//the velocity is the speed at which the enemy is moving.
	public Vector2 velocity;
	
	public Enemy(PlayScreen screen,float x, float y) {
		this.screen = screen;
		this.world= screen.getWorld();
		
		define();
		velocity = new Vector2(0,0);
		b2body.setTransform(new Vector2(x,y),0);
		 
		
	}
	
	/**
	 * Defines the enemy by creating a Body object of it.
	 */
	protected abstract void define() ;
	
	/**
	 * 
	 * @param dt is the timeframe used in updating the enemy movements.
	 */
	public abstract void update(float dt);
	
	/**
	 * Displays the type of dragon that particular enemy is.
	 * @return A string indicating the colour of the Dragon
	 */
	public abstract String whichDragon() ;
	
	
	/**
	 * reverseDirection allows to change the direction of the speed of the enemy to the opposite direction.
	 * @param reverseX is a boolean that determines whether the enemy has to reverse speed in the  x direction.
	 * @param reverseY is a boolean that determines whether the enemy has to reverse speed in the y direction.
	 */
	public void reverseDirection(boolean reverseX , boolean reverseY) {
		if (reverseX) {
			velocity.x=-velocity.x;
		}
		
		if (reverseY) {
			velocity.y=-velocity.y;
		}
	}

}
