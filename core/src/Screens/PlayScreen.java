package Screens;

import java.io.BufferedReader;
import java.io.FileReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import Collisions.WorldContactListener;
import Levels.LevelCreator;
import Obstacle.Dragon;
import Obstacle.Enemy;
import Player.Player;
//import Player.Player.State;
import Questions.Question;
import uc.ac.aston.game.Launcher;

/**
 *
 *The PlayScreen class is a class that contains the screen that displays all the component of a level including the map, the players, objects for
 *that level.
 *
 */
public class PlayScreen implements Screen{
	
	// variable containing the LAuncher class that handles the different screens;
	private Launcher theGame;
	private Viewport gamePort;
	//cam contains a camera which will be set to follow the player as it moves throughout the map
	private OrthographicCamera cam;
	//status would be set to a playerStatus that would be used to display information about the player and their progress in the game 
	protected PlayerStatus status;
	//map variable that would be used to contain a TiledMap, TiledMAp is map made of textures where a basic unit is a Tile.
	private TiledMap map;
	//TmxMapLoader loads the content of a TiledMap given the destination of this TiledMap.
	private TmxMapLoader mapLoader;
	//mapRender allows a TiledMap to be displayed appropriately in the screen. 
	private OrthogonalTiledMapRenderer mapRender;
	//world is the game world, that would allow interaction between objects.
	private World world;
	// b2 is what is classified as an object in the world.
	private Box2DDebugRenderer b2;
	//player is the Player instance for the main player
	private Player player;
	private TextureAtlas atlas;
	//levels is a variable that when instantiated it handles creation of all the objects for a specific level
	private LevelCreator levels;
	// detectCollision is a listener that listen to when two object collide against each other and based on the objects and their relationship will perform an action.
	WorldContactListener detectCollision;
	//player is the Player instance for the opposing player(opponent to beat)
	private Player player2;
	//player2Position is a variable to keep track of the opposing player.
	private Vector2 player2Position;
	//levelsMaps contains all the URL destinations for the different levels of the game
	private String[] levelsMaps = {"level1.tmx","level2.tmx","level3.tmx","level4.tmx","level5.tmx","level6.tmx"};
	//currentLevel is the integer indicating the currentLevel that is being played
	private int currentLevel;
	//currentScore is the integer indicating the score of the main player.
	private int currentScore;
	//listOfQuestions variable for a Question variable that return a dataset of Maths and English questions.
	private Question listOfQuestions;
	//waitingToMoveLevel is a boolean to check if the main player has finished their level and is waiting for opponent to finish theirs.
	private boolean waitingToMoveLevel=false;
	//signalToMoveLevelReceived is a boolena to check if the signal to move level has been received.
	private boolean signalToMoveLevelReceived=false;
	//boolean to check if the game has ended
	private boolean hasGameEnded=false;
	
	private boolean canMove=false;
	//gameStatus represent the different status the game can be in during the run time of the game.
	private enum gameStatus{WAITING,STARTING,PROGRESSING,FROZEN,ADVANCING,CONCLUDING,DISCONNECTING,POWER_ACTIVATED};
	//currentStaus is the game current State.
	private gameStatus currentStatus;
	//countdownForStart is the initial count down for when the players can start playing the level associated with this screen
	private int countdownForStart = 5;
	//timeCount is the current time on the game world.
	private float timeCount=0;
	//endingResult is the boolean indicating the position in which the player has finished the level
	private int endingResult=-1;
	//concludingCountdown integer indicating a timer to allows the opposing player to finish after the main player has completed the level
	private int concludingCountdown = 20;
	//music is a Music variable representing the sound track of the game.
	private Music music;
	//isLeavingLevel boolean indicating whether the player has decided to leave the level associate with this screen.
	private boolean isLeavingLevel=false;
	
	
	
	
	public PlayScreen(Launcher theGame,int level,int score) {
		currentScore=score;
		atlas=new TextureAtlas("the_player.pack");
		currentLevel=level;
		this.theGame= theGame;
		cam = new OrthographicCamera();
		gamePort = new FitViewport(Launcher.width/Launcher.PPM,Launcher.height/Launcher.PPM,cam);
		status = new PlayerStatus(theGame.batch,currentLevel+1,currentScore);
		mapLoader= new TmxMapLoader();
		map = mapLoader.load(levelsMaps[currentLevel]);
		mapRender = new OrthogonalTiledMapRenderer(map,1/Launcher.PPM);
		cam.position.set(gamePort.getWorldWidth()/2, gamePort.getWorldHeight()/2,0);
		world = new World(new Vector2(0,-10), true);
		b2 = new Box2DDebugRenderer();
		b2.setDrawBodies(false);
		player = new Player(world,this);
		player.b2body.setTransform(new Vector2(1,3),0);
		player2= null;
		levels= new LevelCreator(map,world, this);
		detectCollision= new WorldContactListener(player);
		world.setContactListener(detectCollision);
		listOfQuestions= new Question();
		currentStatus = gameStatus.WAITING;
		music=Launcher.manager.get("music/Background.wav", Music.class);
		
		
		

		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * the render method handle what is seen during the run time of the screen including rendering the map, update scene, rendering the status details
	 * changing to another screen, playing music
	 */
	@Override
	public void render(float delta) {
		//plays music sound track if the user desires for the music to be on.
		if (theGame.isMusicOn) {
			music.setLooping(true);
			music.setVolume(0.1f);
			music.play();
			
		}else {
			music.stop();
		}
		
		if (theGame.returnToHome) {
			theGame.disconnecting();
			this.currentStatus=gameStatus.DISCONNECTING;
			this.isLeavingLevel=true;
		}
		
		//Check whether a player is dead, if is, revives player to the initial position
		if (player.getTimer()==0) {
			player.revivePlayer();
			player.b2body.setTransform(new Vector2(1,3),0);
		}
		update(delta);
		updateStatus(delta);
		
		//set background environment
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//sets the camera
		theGame.batch.setProjectionMatrix(status.stage.getCamera().combined);
		//display status details
		status.stage.draw();
		//renders the map and objects
		mapRender.render();
		b2.render(world, cam.combined);
		//when a door has been hit it changes to the screen to a new QuestionScreen where user would answer questions
		if (detectCollision.getDoGenerateQuestion()) {
			theGame.setScreen(new QuestionScreen(theGame,this,listOfQuestions, currentLevel));
		}
		
		
		//Adjust camera settings
		theGame.batch.setProjectionMatrix(cam.combined);
		theGame.batch.begin();
		//draws the player
		player.draw(theGame.batch);
		if (player2!=null) {
			player2.draw(theGame.batch);
		}
		
		//draws the dragons that are in the level this Screen class is showing
		for (Enemy dragon : levels.getDragons()) {
			dragon.draw(theGame.batch);
		}
		
		theGame.batch.end();
		
	}
	
	public void unlockDoor() {
		player.setCanOpenDoor(true);
	}
	
	/**
	 * Blocks players movement 
	 */
	public void playerFrozen() {
		currentStatus=gameStatus.FROZEN;
		canMove=false;
	}
	
	/**
	 * creates Opposing player and sets initial position
	 */
	public void createPlayer2() {
		player2=  new Player(world,this);
		player2.makeGhost();
		player2Position = new Vector2(1,3);
	}
	
	
	public Vector2 getPlayerPosition() {
		return player.b2body.getWorldCenter();
	}
	
	public void setPlayer2Position(Vector2 pos) {
		player2Position=pos;
	}
	
	/**
	 * Method handleInput checks if UP, LEFT, RIGHT keys have been pressed and move the player accordingly
	 * @return boolean value depending on whether the player has beeen able to move
	 */
	public boolean handleInput() {
		if (!player.getIsDead() && canMove) {
			//if up key has been pressed and player was not already jumping apply a liner impulse to the main player in y direction
			if (Gdx.input.isKeyPressed(Input.Keys.UP) && player.b2body.getLinearVelocity().y==0) {
				//apply sound effect for the jump if the user desires
				if (Launcher.isSoundOn) {
					Launcher.manager.get("music/Jump.wav", Music.class).play();
				}
				//if player has power up the displacement of the jump in the y direction is set to be higher than normal.
				if (player.getHasPowerUp()) {
					player.b2body.applyLinearImpulse(new Vector2(0,6f), player.b2body.getWorldCenter(),true);
				}else {
					player.b2body.applyLinearImpulse(new Vector2(0,4f), player.b2body.getWorldCenter(),true);
				}
				return true;
			}
			//if right key has been pressed and player has not surpassed the speed of 1.0 in the x axis a positive liner impulse is applied to the main player in x direction
			if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)&& player.b2body.getLinearVelocity().x<=1.0) {
				player.b2body.applyLinearImpulse(new Vector2(0.1f,0),player.b2body.getWorldCenter(),true);
				return true;
				//if right key has been pressed and player has not surpassed the speed of -1.0 in the x axis a negative liner impulse is applied to the main player in x direction
			}if (Gdx.input.isKeyPressed(Input.Keys.LEFT)&& player.b2body.getLinearVelocity().x>=-1.0) {
				player.b2body.applyLinearImpulse(new Vector2(-0.1f,0),player.b2body.getWorldCenter(),true);
				return true;
			}
			
		}
		return false;
	}
	
	
	/**
	 * The method update has the role to update objects like the players or enemies movements and status. And also manages what happens when end of
	 * the game is reached.
	 * @param delta time frame of execution
	 */
	public void update (float delta) {
		
		world.step(1/60f, 6, 2);
		handleInput();
		if (this.currentStatus==gameStatus.PROGRESSING){
			theGame.updateServer(delta);
		}
		
		if (currentStatus!=gameStatus.CONCLUDING) {
			if (this.currentStatus!=gameStatus.POWER_ACTIVATED & player.getHasPowerUp()) {
				this.currentStatus=gameStatus.POWER_ACTIVATED;
			}
		}
		
		
		if (player2!=null) {
			player2.b2body.setTransform(player2Position, 0);
		}
		
		player.update(delta);
		
		//updates the position of the dragons by making them move within the range it was specified.
		for (Enemy dragon:levels.getDragons()) {
			dragon.update(delta);
		}
		
		if (player2!=null) {
			player2.update(delta);
		}
		
		//make the camera follow player if is not dead, if it is dead decrement teh timer until it can be respawn.
		if (!player.getIsDead()) {
			cam.position.x=player.b2body.getPosition().x;
		}else {
			player.decrementRespawnTimer();
		}
		
		//check if the player is waiting to move level or has received signal to move level and informs Launcher class, also updates ending position.
		if (!waitingToMoveLevel) {
			if (player.getHasFinishedLevel()) {
				
				
				waitingToMoveLevel=true;
				if (this.currentStatus!=gameStatus.DISCONNECTING){
					if (this.currentStatus!=gameStatus.CONCLUDING) {
						currentStatus=gameStatus.CONCLUDING;
						endingResult=1;
					}else {
						endingResult=2;
					}
					theGame.updateHasPlayerFinished();
				}
			}
		}else if (signalToMoveLevelReceived) {
			if (this.currentStatus!=gameStatus.DISCONNECTING){
				if (this.currentStatus!=gameStatus.ADVANCING) {
					currentStatus=gameStatus.ADVANCING;
				}
			}	
			
		}
		
		//show game over screen when game has reached the end and it is the last level.
		if (hasGameEnded & currentLevel==5) {
			theGame.showGameOverScreen();
		}

		
		cam.update();
		mapRender.setView(cam);
		
	}
	
	public void returnToPlayScreen() {
		detectCollision.resetGenerateQuestion();
		status.enableListeners();
	}
	
	public void dragPlayerBack() {
		Vector2 playerPosition = new Vector2(player.b2body.getWorldCenter().x -0.2f,player.b2body.getWorldCenter().y);
		player.b2body.setTransform(playerPosition, 0);
	}
	

	public void setSignalToMoveLevelReceived(boolean signal) {
		this.signalToMoveLevelReceived = signal;
	}
	
	public boolean getSignalToMoveLevel() {
		return signalToMoveLevelReceived ;
	}

	public int getCurrentSore() {
		return currentScore;
	}
	
	public void setHasGameEnded() {
		hasGameEnded=true;
	}

	public TextureAtlas getAtlass() {
		return atlas;
	}
	
	public World getWorld() {
		return world;
	}
	
	public void allowMovement() {
		currentStatus=gameStatus.STARTING;
	}
	
	public void setEndingresult(int position) {
		endingResult=position;
	}
	
	public void changeToConcluidng() {
		countdownForStart=5;
		if (this.currentStatus!=gameStatus.DISCONNECTING){
			currentStatus=gameStatus.CONCLUDING;
		}
		
	}
	
	public void changeToDisconnecting() {
		currentStatus=gameStatus.DISCONNECTING;
	}
	
	/**
	 * updateStatus performs a specific operation based on the status of the game in the level associated with this screen
	 * @param delta integer indicating update time in the game world
	 */
	public void updateStatus(float delta) {
		String state;
		switch(currentStatus) {
		case DISCONNECTING:
			timeCount+=delta;
			//checks for when 1 second has passed in the game world
			if (timeCount>=1) {
				//if the count down reaches 0 it changes the state to waiting and returns back to the home screen 
				if (this.countdownForStart==0) {
					state="Here we goo";
					countdownForStart=5;
					currentStatus = gameStatus.WAITING;
					theGame.newGame();
				}else {
					//keep decreasing the count down showing an appropriate message on the status bar based on who initiated the disconnection
					canMove=false;
					if (isLeavingLevel) {
						state="Returning to Home page in: "+countdownForStart;
					}else {
						state="current opponent disconnected, moving you to a new lobby in "+countdownForStart;
					}
					countdownForStart--;
					
				}
				status.update(state);
				timeCount=0;
			}
			break;
		case WAITING:
			//shows a message on the status bar notifying the player that the game is waiting for opponent player.
			state="waiting for opponent player to connect ...";
			status.update(state);
			break;
		case STARTING:
			timeCount+=delta;
			//checks for when 1 second has passed in the game world
			if (timeCount>=1) {
				//if the count down reaches 0 it changes the state to progressing allowing the player to move.
				if (this.countdownForStart==0) {
					state="Gooooooooooo";
					currentStatus = gameStatus.PROGRESSING;
					countdownForStart=5;
					canMove=true;
				}else {
					//decreases count down shows a message on the status bar notifying the players when they can start playing the level.
					state="Level starts in: "+countdownForStart;
					countdownForStart--;
					
				}
				status.update(state);
				timeCount=0;
			}
			break;
		case FROZEN:
			timeCount+=delta;
			//checks for when 1 second has passed in the game world
			if (timeCount>=1) {
				//if the count down reaches 0 it changes the state to progressing allowing the main player to move.
				if (this.countdownForStart==0) {
					state="Gooooooooooo";
					currentStatus = gameStatus.PROGRESSING;
					countdownForStart=5;
					canMove=true;
				}else {
					//decreases count down shows a message on the status bar notifying the players when they would be allowed to move after being frozen.
					state="You have been freezed, you will be allowed to move again in: "+countdownForStart;
					countdownForStart--;
					
				}
				status.update(state);
				timeCount=0;
			}
			break;
		case POWER_ACTIVATED:
			timeCount+=delta;
			//checks for when 1 second has passed in the game world
			if (timeCount>=1) {
				if (this.countdownForStart==0) {
					//if the count down reaches 0 it changes the state to progressing and changes the boolean variable hasPowerUp to false.
					state="Gooooooooooo";
					currentStatus = gameStatus.PROGRESSING;
					countdownForStart=5;
					player.setHasPowerUp(false);
				}else {
					//decreases count down shows a message on the status bar notifying about the power up and when its effect disappears.					
					state="You have obtained the super jump power up, effect would be disabled in: "+countdownForStart;
					countdownForStart--;
					
				}
				status.update(state);
				timeCount=0;
			}
			break;
		case CONCLUDING:
			timeCount+=delta;
			if (timeCount>=1) {
				//if the count down reaches 0 it changes the state to advancing disabling movement.
				if (this.concludingCountdown==0) {
					state="End";
					currentStatus = gameStatus.ADVANCING;
					countdownForStart=5;
					canMove=false;
				}else {
					//decreases count down shows a message on the status bar notifying appropriate messages based on who arrived at the finish line first.
					if (this.endingResult==1) {
						state="Well done you finished first, let's give the opponent "+concludingCountdown+"s to finish off the level";
					}else {
						state="Your opponent has finished the level, to gain some points you must finish within : "+concludingCountdown;
					}
					concludingCountdown--;
					
				}
				status.update(state);
				timeCount=0;
			}
			break;
		case ADVANCING:
			timeCount+=delta;
			//checks for when 1 second has passed in the game world
			if (timeCount>=1) {
				//if the count down reaches 0 moves to the next level by calling the nextLevel with the player ending result.
				if (this.countdownForStart==0) {
					theGame.nextLevel(endingResult);
				}else {
					//decreases count down shows a message on the status bar notifying the timer to move to the next level.
					state="Well done, moving to the next level in : "+countdownForStart;
					countdownForStart--;
					status.update(state);
					
				}
				timeCount=0;
			}
			break;
			
			
		default:
			//shows a message on the status bar notifying that the game is in progress.
			state = "Game in progress";
			status.update(state);
		}
	}
	

	@Override
	public void resize(int width, int height) {
		gamePort.update(width, height);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * disposes of all resources
	 */
	@Override
	public void dispose() {
		map.dispose();
		mapRender.dispose();
		world.dispose();
		b2.dispose();
		status.dispose();
		
		
		// TODO Auto-generated method stub
		
	}

}
