package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import uc.ac.aston.game.Launcher;

/**
 *
 *PlayerStatus is a Class for displaying players details and their progress during run time of the game.
 */

public class PlayerStatus implements Disposable{
	//stage is the environment that can be viewed.
	public Stage stage;
	//view handles what is seen in the screen. 
	private Viewport view;
	//level Integer indicating player's current Level;
	private Integer level;
	//score Integer indicating player's current Score;
	private Integer score;
	//user string indicating ID of the player in the lobby.
	private String user;
	//labels to display text
	private Label scoreLabel;
	private Label levelLabel;
	private Label statusLabel;
	private Label theScore;
	private Label theLevel;
	private Label status;
	private boolean hasGameStarted =false;
	private String statusText= "waiting for opponent player to connect ...";
	
	public PlayerStatus(SpriteBatch batch, int level, int score){
		this.score=score;
		this.level=level;
		
		view = new FitViewport(Launcher.width,Launcher.height,new OrthographicCamera());
		stage = new Stage (view, batch);
		Gdx.input.setInputProcessor(stage);
		
		Table table = new Table();
		table.top();
		table.setFillParent(true);
		Drawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture("musicOn.png")));
		ImageButton playButton = new ImageButton(drawable);
		playButton.setWidth(30);
		playButton.setHeight(30);
		playButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Launcher.isMusicOn=!Launcher.isMusicOn;
				//Launcher.setMusicOn();
			}
		});
		Drawable drawable2 = new TextureRegionDrawable(new TextureRegion(new Texture("soundOn.png")));
		ImageButton soundButton = new ImageButton(drawable2);
		soundButton.setWidth(30);
		soundButton.setHeight(30);
		soundButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Launcher.isSoundOn=!Launcher.isSoundOn;
				//Launcher.setMusicOn();
			}
		});
		
		Drawable drawable3 = new TextureRegionDrawable(new TextureRegion(new Texture("homePage.png")));
		ImageButton homeButton = new ImageButton(drawable3);
		homeButton.setWidth(30);
		homeButton.setHeight(30);
		homeButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				System.out.println("ok");
				Launcher.returnToHome=true;
				//Launcher.setMusicOn();
			}
		});
		
		
		
		
		//labels to represent the actual values of score and level;
		scoreLabel = new Label (String.format("%03d", score), new Label.LabelStyle(new BitmapFont(),Color.WHITE));
		levelLabel = new Label (String.format("%02d", level), new Label.LabelStyle(new BitmapFont(),Color.WHITE)); 
		//labels to represent the literal text 
		theScore = new Label ("SCORE", new Label.LabelStyle(new BitmapFont(),Color.WHITE));
		theLevel = new Label ("LEVEL", new Label.LabelStyle(new BitmapFont(),Color.WHITE));
		status = new Label ("STATUS", new Label.LabelStyle(new BitmapFont(),Color.WHITE));
		statusLabel= new Label (statusText, new Label.LabelStyle(new BitmapFont(),Color.WHITE));
		statusLabel.setWrap(true);
		statusLabel.setWidth(150);
		
		
		//layout specified using a Table structure
		table.add(theScore).expandX().padTop(10);
		table.add(theLevel).expandX().padTop(10);
		table.add(status).expandX().padTop(10);
		table.row();
		table.add(scoreLabel).expandX();
		table.add(levelLabel).expandX();
		table.add(statusLabel).width(150).expandX();
		table.add(playButton).width(30).height(30).expandX();
		table.add(soundButton).width(30).height(30).expandX();
		table.add(homeButton).width(30).height(30).expandX();
		
		stage.addActor(table);
		
		
		
		
		
	}
	
	public void update(String theStatus) {
		if (!theStatus.equals(statusText)) {
			this.statusLabel.setText(theStatus);
		}
	}
	
	public void enableListeners() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void dispose() {
		stage.dispose();
		
	}
	

}
