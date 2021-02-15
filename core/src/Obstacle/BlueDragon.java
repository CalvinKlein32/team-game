package Obstacle;

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

public class BlueDragon extends Enemy{
	
	private float stateTime;
	private Animation<TextureRegion> flying;
	Array<TextureRegion> frames;
	TextureAtlas atlas;

	public BlueDragon(PlayScreen screen, float x, float y) {
		super(screen, x, y);
		velocity = new Vector2(0,-1);
		atlas = new TextureAtlas("blueDragons.pack");
		frames = new Array<TextureRegion>();
		for (int i=0; i<4;i++) {
			frames.add(new TextureRegion(atlas.findRegion("attack - sword"),i*64,128,64,64));
		}
		flying = new  Animation(0.3f, frames);
		stateTime=0;
		setBounds(0,32,64/Launcher.PPM,64/Launcher.PPM);
	
	}

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

	@Override
	public void update(float dt) {
		stateTime +=dt;
		b2body.setLinearVelocity(velocity);
		setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2);
		setRegion(flying.getKeyFrame(stateTime, true));
		
	}

	@Override
	public String whichDragon() {
		return ("blue");
	}

}
