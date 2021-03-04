package Screens;

import java.io.BufferedReader;
import java.io.FileReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
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
import Questions.Question;
import uc.ac.aston.game.Launcher;

/**
 *
 * @author Los Thunder
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
		

		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * the render method handle what is seen during the run time of the screen including rendering the map, update scene, rendering the status details
	 * changing to another screen 
	 */
	@Override
	public void render(float delta) {
		//Check whether a player is dead, if it is it revives it to the initial position
		if (player.getTimer()==0) {
			player.revivePlayer();
			player.b2body.setTransform(new Vector2(1,3),0);
		}
		update(delta);
		//set background enviroment
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
			theGame.setScreen(new QuestionScreen(theGame,this,listOfQuestions));
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
	 * creates Opposing player and sets initial position
	 */
	public void createPlayer2() {
		player2=  new Player(world,this);
		player2Position = new Vector2(1,1);
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
		if (!player.getIsDead()) {
			//if up key has been pressed and player was not already jumping apply a liner impulse to the main player in y direction
			if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && player.b2body.getLinearVelocity().y==0) {
				player.b2body.applyLinearImpulse(new Vector2(0,4f), player.b2body.getWorldCenter(),true);
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
		theGame.updateServer(delta);
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
		
		//check if the player is waiting to move level or has received signal to move level and informs Launcher class.
		if (!waitingToMoveLevel) {
			if (player.getHasFinishedLevel()) {
				theGame.updateHasPlayerFinished();
				waitingToMoveLevel=true;
			}
		}else if (signalToMoveLevelReceived) {
			signalToMoveLevelReceived=false;
			theGame.nextLevel(1);
			
		}
		
		//show game over screen when game has reached the end
		if (hasGameEnded & currentLevel==5) {
			theGame.showGameOverScreen();
		}

		
		cam.update();
		mapRender.setView(cam);
		
	}
	
	public void returnToPlayScreen() {
		detectCollision.resetGenerateQuestion();
	}
	

	public void setSignalToMoveLevelReceived(boolean signal) {
		this.signalToMoveLevelReceived = signal;
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
