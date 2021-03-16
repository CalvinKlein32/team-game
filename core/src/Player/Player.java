package Player;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import Screens.PlayScreen;
import uc.ac.aston.game.Launcher;

public class Player extends Sprite {
	
	//world is the game world in which the player exists.
	public World world;
	//b2body is the Body representing the player.
	public Body b2body;
	//fdef variable to define the fixtures used.  
	private FixtureDef fdef;
	private Fixture f;
	//isDead is the boolean to check whether player is dead or still alive
	private boolean isDead;
	//canOpenDoor is the boolean to check if player has permission to open the door.
	private boolean canOpenDoor;
	//respawnTimer is the timer used to check when a player after they die can be respowned.
	private float respawnTimer;
	//playerStand is the texture that represent when a player is standing firm.
	private TextureRegion playerStand;
	//State represent the state in which the player could be iun during the run time of the game.
	private enum State{JUMPING,STANDING,RUNNING};
	//currentState is the player's current State.
	private State currentState;
	//currentState is the player's previous State.
	private State previousState;
	//playerRuns is as an Animation made of a series  textures correlated to when the player is running.
	private Animation<TextureRegion> playerRuns;
	//playerJumps is as an Animation made of a series  textures correlated to when the player is jumping.
	private Animation<TextureRegion> playerJumps;
	//runningRight boolean to check the direction the player is running at.
	private boolean runningRight;
	//stateTimer is a float that keeps track of the amount of time the player stays in a particular state.
	private float stateTimer;
	//hasFinishedLevel boolean to check whether player has finished the level at which they are playing.
	private boolean hasFinishedLevel=false;
	
	private boolean hasPowerUp=false;
	
	
	public Player(World world, PlayScreen screen) {
		super(screen.getAtlass().findRegion("Woodcutter_walk"));
		this.world=world;
		defineCharacter();
		playerStand=new TextureRegion(getTexture(),1,1,32,65);
		setBounds(0,20,32/Launcher.PPM,64/Launcher.PPM);
		setRegion(playerStand);
		isDead=false;
		respawnTimer=50;
		canOpenDoor=false;
		currentState=State.STANDING;
		previousState=State.STANDING;
		runningRight=true;
		stateTimer=0;
		Array<TextureRegion> frames = new Array<TextureRegion>();
		for (int i=581;i<701;i+=40) {
			frames.add(new TextureRegion(getTexture(),i,151,40,65));
		}
		playerRuns = new Animation<TextureRegion>(0.3f,frames);
		frames.clear();
		
		for (int i=1;i<=201;i+=40) {
			frames.add(new TextureRegion(getTexture(),i,51,40,65));
		}
		playerJumps = new Animation<TextureRegion>(0.02f,frames);
	}
	
	public void update(float dt) {
		setPosition(b2body.getPosition().x-getWidth()/2,b2body.getPosition().y-getHeight()/2);
		setRegion(getFrame(dt));

	}
	
	/**
	 * @param dt float indicating the timeframe that would be used to update state timer.
	 * @return texture region for a particular state.
	 */
	public TextureRegion getFrame(float dt){
		currentState=getState();
		TextureRegion region;
		if (currentState==State.JUMPING) {
			region=playerJumps.getKeyFrame(stateTimer);
		}else if(currentState==State.RUNNING) {
			region=playerRuns.getKeyFrame(stateTimer,true);
		}else {
			region=playerStand;
		}
		//based on the direction the player is running at it flips the region accordingly.
		if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
			region.flip(true, false);
			runningRight=false;
		}else if ((b2body.getLinearVelocity().x>0 || runningRight) && region.isFlipX()) {
			region.flip(true, false);
			runningRight=true;
		}
		
		if (currentState==previousState) {
			stateTimer+=dt;
		}else {
			stateTimer=0;
		}
		previousState=currentState;
		
		
		return region;
	}
	
	/**
	 * 
	 * @return State of the player based on information about the player body current  speed.
	 */
	public State getState() {
		if (b2body.getLinearVelocity().y>0) {
			return State.JUMPING;
		}else if(b2body.getLinearVelocity().x!=0) {
			return State.RUNNING;
		}else {
			return State.STANDING;
		}
	}
	
	/**
	 * Define the player's Body object and the fixture it contains.
	 */
	public void defineCharacter() {
		//Defines player's body object and assigns its position in the world.
		BodyDef character = new BodyDef();
		character.position.set(80/Launcher.PPM,150/Launcher.PPM);
		character.type=BodyDef.BodyType.DynamicBody;
		b2body = world.createBody(character);		
		
		//Defines the fixture of the body by representing it as a cirlce shape.
		fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(10/Launcher.PPM);
		fdef.shape=shape;
		//Defines how the fixture would be identified in the world.
		fdef.filter.categoryBits = Launcher.playerBit;
		//Defines the other fixtures that the player fixture  can collide against the world.
		fdef.filter.maskBits = Launcher.DefaultBit| Launcher.spikeBit| Launcher.seaBit| Launcher.enemyBit| Launcher.doorBit| Launcher.powerUpBit;
		f=b2body.createFixture(fdef);
	
		
		//Create a fixture that represents the bottom side of the player, as a segment below the body of the player.
		EdgeShape feet= new EdgeShape();
		feet.set(new Vector2(-5/Launcher.PPM,-10/Launcher.PPM),new Vector2(+5/Launcher.PPM,-10/Launcher.PPM));
		fdef.shape=feet;
		fdef.isSensor = true;
		b2body.createFixture(fdef).setUserData("feet");
		
		
		//Create a fixture that represents the right side of the player, as a segment positioned on the right side of the body of the player.
		EdgeShape front= new EdgeShape();
		front.set(new Vector2(10/Launcher.PPM,-8/Launcher.PPM),new Vector2(10/Launcher.PPM,8/Launcher.PPM));
		fdef.shape=front;
		fdef.isSensor = true;
		b2body.createFixture(fdef).setUserData("side");
	
		//Create a fixture that represents the left side of the player, as a segment positioned on left side of the body of the player.
		EdgeShape back= new EdgeShape();
		back.set(new Vector2(-10/Launcher.PPM,-8/Launcher.PPM),new Vector2(-10/Launcher.PPM,8/Launcher.PPM));
		fdef.shape=back;
		fdef.isSensor = true;
		b2body.createFixture(fdef).setUserData("side");
		
		//Create a fixture that represents the head of the player, as a segment positioned on the top side of the body of the player.
		EdgeShape head= new EdgeShape();
		head.set(new Vector2(-5/Launcher.PPM,10/Launcher.PPM),new Vector2(+5/Launcher.PPM,10/Launcher.PPM));
		fdef.shape=head;
		fdef.isSensor = true;
		b2body.createFixture(fdef).setUserData("head");
	}
	
	
	
	/**
	 * Destroys player's object by making sure the player object cannot collide with anything and falls out of the screen.
	 */
	public void destroyPlayer() {
		Filter filter= new Filter();
		filter.maskBits= Launcher.destroyedBit;
		isDead=true;
		for (Fixture fixture: b2body.getFixtureList()) {
			fixture.setFilterData(filter);
		}

	}
	
	/**
	 * Revive Player's object by making sure is able to collide again and interact with objects in the world.
	 */
	public void revivePlayer() {
		Filter filter= new Filter();
		filter.maskBits= Launcher.DefaultBit| Launcher.spikeBit| Launcher.seaBit| Launcher.enemyBit| Launcher.doorBit| Launcher.powerUpBit;
		isDead=false;
		respawnTimer=50;
		for (Fixture fixture: b2body.getFixtureList()) {
			fixture.setFilterData(filter);
		}

	}
	
	/**
	 * Allows a player to not collide with anything, Useful when creating the 2nd player but not making it collide and interact with 
	 * objects in the world.
	 */
	public void makeGhost() {
		Filter filter= new Filter();
		filter.maskBits= Launcher.destroyedBit;
		for (Fixture fixture: b2body.getFixtureList()) {
			fixture.setFilterData(filter);
		}
		playerStand=new TextureRegion(getTexture(),911,201,32,65);

	}
	
	public void decrementRespawnTimer() {
		respawnTimer--;
	}
	
	public float getTimer() {
		return respawnTimer;
	}
	
	
	public boolean getIsDead() {
		return isDead;
	}
	
	public void setCanOpenDoor(boolean value) {
		canOpenDoor=value;
	}
	public boolean getCanOpenDoor() {
		return canOpenDoor;
	}

	public boolean getHasFinishedLevel() {
		return hasFinishedLevel;
	}
	
	public void updateWordl(World newWorld) {
		this.world=newWorld;
	}

	public void setHasFinishedLevel(boolean isLevelOver) {
		hasFinishedLevel = isLevelOver;
	}

	public boolean getHasPowerUp() {
		return hasPowerUp;
	}

	public void setHasPowerUp(boolean hasPowerUp) {
		this.hasPowerUp = hasPowerUp;
	}
	


	

	
}
