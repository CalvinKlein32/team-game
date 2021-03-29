package uc.ac.aston.game;

/**
 * Launcher class of the game, which run the game by managing the different screens to be displayed in the run time of the game.
 * The Launcher class also manages communication to the server acting as a socket.io client connecting to a node.js server that is found
 * at the following URL https://arcane-taiga-94757.herokuapp.com/
 */

import java.net.URISyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import Questions.Question;
//import io.socket.emitter.Emitter;
//import io.socket.client.IO;
import io.socket.client.*;
import io.socket.emitter.Emitter;
import Screens.GameOverScreen;
import Screens.HomeScreen;
import Screens.PlayScreen;

public class Launcher extends Game {
	//dimensions of the window
	public static final int width =600;
	public static final int height =600;
	//A SpriteBatch is an environment that allows sprites and 2D textures to be drawn.
	public SpriteBatch batch;
	public static final float PPM=100;
	//series of constant, where each is used in identifying an object in the game world essential when managing collisions.
	public static final short DefaultBit=1;
	public static final short playerBit=2;
	public static final short spikeBit=4;
	public static final short seaBit=8;
	public static final short doorBit = 16;
	public static final short destroyedBit=32;
	public static final short enemyBit=64;
	public static final short objectBit=128;
	public static final short powerUpBit=256;
	//socket class that would be used as a client to connect to a node.js server
	private Socket socket;
	//current game screen that the Launcher is using to run the game. For each level there is a Screen, this variable is used to switch between the appropriate screens.
	private PlayScreen screen;
	//time frame by which updates are handled.
	private final float UPDATE_TIME = 1/60f;
	//current timer related to the update_TIME.
	private float timer;
	//Integer indicating the Lobby in which player is in.
	private int lobbyNum=-1;
	//Boolean to check if the current player has finished their current level.
	private boolean hasPlyerFiniehed=false;
	//Boolean to check if opponent player that is in the same lobby has finished the level.
	private boolean hasTheOtrerPlyerFiniehed=false;
	//Integer that indicates the currentLevel the player is in.
	private int currentLevel=0;
	//The points which the player possesses, (incremented after completing each level)
	private int finalPoints=0;
	//The points which the opponent player possesses, (incremented after completing each level)
	private int opponentFinalPoints=0;
	//AssetManager that contains some of the assets that would be used during run time of the game contains mainly sound effects and music data.
	public static AssetManager manager;
	//numPlayer is an integer that indicates teh number the player has been assigned in the lobby if first player to join lobby it would be 1 if 2nd 2. if it hasn't joined a lobby yet -1
	private int numPlayer=-1;
	//boolean to indicate whether sound effects is turned on.
	public static boolean isSoundOn=true;
	//boolean to indicate whether music is turned on.
	public static boolean isMusicOn=true;
	//boolean to indicate whether player has decided to leave their current level to return to home page.
	public static boolean returnToHome=false;
	
	
	
	/**
	 * Displays the first screen of the game, which consist of the HomePage screen. Initiates command to connect the socket to the server
	 * configures events.
	 */
	@Override
	public void create () {
		batch = new SpriteBatch();
		manager=new AssetManager();
		
		//loads music file from the asset folder inside the core directory ready to be used through the manager variable 
		manager.load("music/Background.wav", Music.class);
		manager.load("music/CorrectAnswer.wav", Music.class);
		manager.load("music/Dead.mp3", Music.class);
		manager.load("music/Jump.wav", Music.class);
		manager.load("music/WaterDrop.mp3", Music.class);
		manager.load("music/WrongAnswer.wav", Music.class);
		manager.finishLoading();
		setScreen(new HomeScreen(this));
		connectSocket();
		configSocketEvents();
		
		

		
	}
	
	//Renders a screen.
	@Override
	public void render () {
		super.render();

		
	}
	
	
	/**
	 * Searches for a lobby based on the preferences of the user by moving to the appropriate screen associated with that level
	 * and notifying the server through a findLobbyByLevel event specifying the chosen level so that server can 
	 * find an appropriate lobby.
	 * 
	 * @param levelChoosed integer indicating the chosen level the user wants to play in.
	 * @param isSoundOn boolean indicates whether uses prefers the sound effects to be on.
	 * @param isMusicOn boolean indicates whether uses prefers the music to be on. 
	 */
	public void findLobby(int levelChoosed, boolean isSoundOn, boolean isMusicOn) {
		this.currentLevel=levelChoosed;
		this.isSoundOn=isSoundOn;
		this.isMusicOn=isMusicOn;
		if (screen!=null) {
			screen.dispose();
		}
		screen = new PlayScreen(this,currentLevel,0);
		setScreen(screen);
		JSONObject data = new JSONObject();
		
		try {
			data.put("level", currentLevel);
			socket.emit("findLobbyByLevel", data);
		}catch(Exception e) {
			System.out.println(e);
		}
			
		
		
	}
	/**
	 * disconnecting informs the server that the player wants to disconnect from a particular level through the leaveLevel event
	 */
	public void disconnecting() {
		socket.emit("leaveLevel");
		this.returnToHome=false;
	}
	
	/**
	 * Displays the GameOver Screen with information on points of both the player and its opponent. 
	 */
	public void showGameOverScreen() {
		int finalPosition;
		if (finalPoints>opponentFinalPoints) {
			setScreen(new GameOverScreen(this,1,finalPoints, opponentFinalPoints));
		}else {
			setScreen(new GameOverScreen(this,2,finalPoints, opponentFinalPoints));
		}
	}
	
	/**
	 * Moves the playing environment to the next level, By changing the screen to next levels, updating points. ALos notifies the server that they have finished.
	 * @param position Int indicating the position on which the player finished their current level.
	 */
	public void nextLevel(int position) {
		int score=0;
		if (position==1) {
			score=screen.getCurrentSore()+100;
		}else if (position==2) {
			score=screen.getCurrentSore()+50;
		}else{
			score=screen.getCurrentSore();
		}
		System.out.print(currentLevel);
		if (currentLevel<6) {
			screen = new PlayScreen(this,currentLevel,score);
			setScreen(screen);
			screen.createPlayer2();
			screen.allowMovement();
			//System.out.println("Congratulations you finished the level in position: "+position);
		}else {
			finalPoints=score;
			JSONObject data = new JSONObject();
			try{
				data.put("playerScore", score);
				data.put("lobbyNum", lobbyNum);
				socket.emit("gameEnded", data);
			}catch(JSONException e) {
				Gdx.app.log("SocketIO", "Error in sending has player finished ");
			}
			screen.setHasGameEnded();
			System.out.println("Congratulations you finished the Game");
		}
		hasPlyerFiniehed=false;
		hasTheOtrerPlyerFiniehed=false;

	}
	
	/**
	 * Updates server with the player current position through the playerMoved event.
	 * @param dt delta time to check whether is within the time frame of the game .
	 */
	public void updateServer(float dt) {
		timer+=dt;
		if (timer>=UPDATE_TIME) {
			JSONObject data = new JSONObject();
			try{
				data.put("x", screen.getPlayerPosition().x);
				data.put("y", screen.getPlayerPosition().y);
				data.put("lobbyNum", lobbyNum);
				socket.emit("playerMoved", data);
			}catch(JSONException e) {
				Gdx.app.log("SocketIO", "Error in sending update position ");
			}
		}
	}
	
	/**
	 * Event that is called when the current player finishes its levels, it notifies the server about it, if the other player had already finished 
	 * it moves to the next level.
	 */
	public void updateHasPlayerFinished() {
		hasPlyerFiniehed=true;
		if (hasTheOtrerPlyerFiniehed) {
			screen.setEndingresult(2);
			screen.setSignalToMoveLevelReceived(true);
		}else {
			currentLevel++;
		}
		JSONObject data = new JSONObject();
		try{
			data.put("playerFinished", true);
			data.put("lobbyNum", lobbyNum);
			socket.emit("playerCompletedLevel", data);
		}catch(JSONException e) {
			Gdx.app.log("SocketIO", "Error in sending has player finished ");
		}
		
		
		
		
	}
	
	/**
	 * Connects the socket to the server via URL addresses. 
	 */
	public void connectSocket() {
		try{
			//socket =IO.socket("http://localhost:8080");
			socket =IO.socket("https://arcane-taiga-94757.herokuapp.com/");
			socket.connect();
		}catch(Exception e) {
			System.out.println(e);
		}
	}
	
	
	
	/**
	 * newGame return to the HomePage allowing users to chose their preferences and start a new game after they have disconnected or their 
	 * opponent has disconnected.
	 */
	public void newGame() {
		setScreen(new HomeScreen(this));
	}
	
	
	
	/**
	 * Handles the various events it receives from the server with adequate action.
	 * the events it responds to are the following:
	 * socketID: when each ID is send to the appropriate client.
	 * newPlayer: event received when a user has notified the server about their preferences and the server has assigned a lobby to that user.
	 * playerDisconeccted: when a player disconnect to the server.
	 * readyTostart: event received when the 2nd player in the same lobby joins so that the player that was waiting can start the level.
	 * playerMoved: when opposing player has moved.
	 * playerCompletedLevel: when opposing player has completed their level.
	 * gameEnded: When opposing player has finished the game.
	 */
	public void configSocketEvents() {
		socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
			
			@Override
			public void call(Object... args) {
				Gdx.app.log("SocketIO", "Connected");
				
			}
		}).on("socketID", new Emitter.Listener() {
			
			//When this client receive a socketID event it prints the ID by which this socket is stored in the server side.
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try{
					String id = data.getString("id");
					Gdx.app.log("SocketIO", "My ID: "+id);
				}catch(JSONException e) {
					Gdx.app.log("SocketIO", "Error in fetching id ");
				}
				
				
			}
		}).on("newPlayer", new Emitter.Listener() {
			
			//When the client receives a newPlayer event it store the lobbyNum assigned by the server 
			//if the lobby says there are two player in the lobby it emits a readyTostart event to the server 
			//and creates the instance for the opposing player in their current screen 
			//allowing the game to start. 
			
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try{
					int count=data.getInt("playersInLobby");
					lobbyNum=data.getInt("lobby");
					System.out.println("this is player number "+count+" in lobby number "+lobbyNum);
					if (count==2) {
						data.put("lobbyNum", lobbyNum);
						socket.emit("readyTostart", data);
						screen.createPlayer2();
						screen.allowMovement();
					}
				}catch(JSONException e) {
					Gdx.app.log("SocketIO", "Error in fetching new player");
				}
				
			}
		}).on("readyTostart", new Emitter.Listener() {
			
			//When the client receives a readyTostart event it checks whether they are on the same lobby and if they are .
			// it creates the instance for the opposing player in their current screen and allows the game to start.
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try{
					int count=data.getInt("lobbyNum");
					//System.out.println("this is player number "+count+" in lobby number "+lobbyNum);
					if (count==lobbyNum) {
						screen.createPlayer2();
						screen.allowMovement();
					}
				}catch(JSONException e) {
					Gdx.app.log("SocketIO", "Error in fetching new player");
				}
				
			}
		}).on("playerDisconeccted", new Emitter.Listener() {
			
			//When the client receives a playerDisconeccted event it sets lobbyNum and numPlayer to null values which are -1 and notifies 
			// the playScreen instance of the level they are playing to start disconnecting
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try{
					int lobbyTocheck= data.getInt("oldLobby");
					if (lobbyTocheck==lobbyNum) {
						lobbyNum = -1;
						numPlayer= -1;
						screen.changeToDisconnecting();
					}
				}catch(JSONException e) {
					Gdx.app.log("SocketIO", "Error in fetching disconneccted player");
				}
				
			}
		}).on("playerMoved", new Emitter.Listener() {
			
			//When the client receives a playerMoved event it displays the opposing player movement information on their current screen/
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try{
					
					int lobby = data.getInt("lobbyNum");
					if (lobbyNum==lobby) {
						String id = data.getString("id");
						Double x = data.getDouble("x");
						Double y = data.getDouble("y");
						Vector2 position = new Vector2(x.floatValue(),y.floatValue());
						screen.setPlayer2Position(position);
					}
					
					
				}catch(JSONException e) {
					Gdx.app.log("SocketIO", "Error in getting players movement");
				}
				
				
			}
		}).on("playerCompletedLevel", new Emitter.Listener() {
			
			// When the client receives a playerCompletedLevel event it check if the opponent player has finished their level and update value of hasTheOtrerPlyerFiniehed
			// if the current player has also finished its level both players move to the next level.
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try{
					int lobby = data.getInt("lobbyNum");
					if (lobbyNum==lobby) {
						hasTheOtrerPlyerFiniehed=data.getBoolean("playerFinished");
						if (hasPlyerFiniehed) {
							System.out.println("Ready to move to the next level. came in 1st position");
							screen.setSignalToMoveLevelReceived(true);
							//currentLevel++;
							
						}else {
							currentLevel++;
							screen.changeToConcluidng();
						}	
						
					}		
				}catch(JSONException e) {
					Gdx.app.log("SocketIO", "Error in getting players movement");
				}
				
				
			}
		}).on("gameEnded", new Emitter.Listener() {
			
			//When the client receives a gameEnded event it updates opponentFinalPoints based on the data received and notifies the current player.
			@Override
			public void call(Object... args) {
				JSONObject data = (JSONObject) args[0];
				try{
					int lobby = data.getInt("lobbyNum");
					if (lobbyNum==lobby) {
						opponentFinalPoints=data.getInt("playerScore");
						screen.setHasGameEnded();
					}		
				}catch(JSONException e) {
					Gdx.app.log("SocketIO", "Error in getting players movement");
				}
				
				
			}
		});
	}
	
	
	
	
	

}
	
