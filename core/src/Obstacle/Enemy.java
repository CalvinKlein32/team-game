package Obstacle;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import Screens.PlayScreen;

public abstract class Enemy extends Sprite{
	
	protected World world;
	protected PlayScreen screen;
	public Body b2body;
	public Vector2 velocity;
	
	public Enemy(PlayScreen screen,float x, float y) {
		this.screen = screen;
		this.world= screen.getWorld();
		//setPosition(x,y);
		
		define();
		velocity = new Vector2(0,0);
		b2body.setTransform(new Vector2(x,y),0);
		 
		
	}
	
	protected abstract void define() ;
	
	public abstract void update(float dt);
	
	public abstract String whichDragon() ;
	
	
	public void reverseDirection(boolean reverseX , boolean reverseY) {
		if (reverseX) {
			velocity.x=-velocity.x;
		}
		
		if (reverseY) {
			velocity.y=-velocity.y;
		}
	}

}
