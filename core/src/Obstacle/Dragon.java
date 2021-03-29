package Obstacle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;

import Screens.PlayScreen;
import uc.ac.aston.game.Launcher;

/**
 * 
 * Dragon class that represents a type of Enemy that is a red dragon that walks horizontally on surface and  must be avoided by the player.
 *
 */

public class Dragon extends Enemy{
	//stateTime is a float that keeps track of the amount of time the red dragon stays in a particular state.
	private float stateTime;
	//walkingLeft is as an Animation made of a series  textures correlated to when the red dragon is moving left.
	private Animation<TextureRegion> walkingLeft;
	//walkingRight is as an Animation made of a series  textures correlated to when the red dragon is moving right.
	private Animation<TextureRegion> walkingRight;
	//frames is a list of textures that are involved in the animations.
	Array<TextureRegion> frames;
	//atlas is where all the textures are loaded form which particular regions would be selected.
	TextureAtlas atlas;
	

	public Dragon(PlayScreen screen, float x, float y) {
		super(screen, x, y);
		velocity = new Vector2(1,0);
		atlas = new TextureAtlas("Dargons.pack");
		frames = new Array<TextureRegion>();
		for (int i=0; i<4;i++) {
			frames.add(new TextureRegion(atlas.findRegion("walk - pitchfork shield"),i*64,64,64,64));
		}
		walkingRight = new  Animation(0.3f, frames);
		frames.clear();
		
		for (int i=0; i<4;i++) {
			frames.add(new TextureRegion(atlas.findRegion("walk - pitchfork shield"),i*64,192,64,64));
		}
		walkingLeft = new  Animation(0.3f, frames);
		stateTime=0;
		setBounds(0,32,64/Launcher.PPM,64/Launcher.PPM);
	}
	
	/**
	 * updates the movement of the BlueDragon by making sure to change animation and moving towards right direction and right speed.
	 */
	@Override
	public void update(float dt) {
		stateTime +=dt;
		b2body.setLinearVelocity(velocity);
		setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2);
		if (velocity.x==1) {
			setRegion(walkingLeft.getKeyFrame(stateTime, true));
		}else {
			setRegion(walkingRight.getKeyFrame(stateTime, true));
		}
		
	}

	/**
	 * Defines the dragon by creating a Body object of it with the appropriate fixtures. 
	 */
	@Override
	protected void define() {
		BodyDef character = new BodyDef();
		character.position.set(80/Launcher.PPM,150/Launcher.PPM);
		character.type=BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(character);
		
		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(10/Launcher.PPM);
		fdef.filter.categoryBits = Launcher.enemyBit;
		fdef.filter.maskBits = Launcher.DefaultBit|Launcher.playerBit|Launcher.objectBit;
		
		fdef.shape=shape;
		b2body.createFixture(fdef).setUserData(this);
		
		
	}

	/**
	 * specifies the type of Dragon.
	 */
	@Override
	public String whichDragon() {
		return ("red");
	}

}
