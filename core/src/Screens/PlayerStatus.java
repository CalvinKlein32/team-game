package Screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
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
	private Label theScore;
	private Label theLevel;
	
	public PlayerStatus(SpriteBatch batch, int level, int score){
		this.score=score;
		this.level=level;
		
		view = new FitViewport(Launcher.width,Launcher.height,new OrthographicCamera());
		stage = new Stage (view, batch);
		
		Table table = new Table();
		table.top();
		table.setFillParent(true);
		//labels to represent the actual values of score and level;
		scoreLabel = new Label (String.format("%03d", score), new Label.LabelStyle(new BitmapFont(),Color.WHITE));
		levelLabel = new Label (String.format("%02d", level), new Label.LabelStyle(new BitmapFont(),Color.WHITE));
		//labels to represent the literal text 
		theScore = new Label ("SCORE", new Label.LabelStyle(new BitmapFont(),Color.WHITE));
		theLevel = new Label ("LEVEL", new Label.LabelStyle(new BitmapFont(),Color.WHITE));
		
		//layout specified using a Table structure
		table.add(theScore).expandX().padTop(10);
		table.add(theLevel).expandX().padTop(10);
		table.row();
		table.add(scoreLabel).expandX();
		table.add(levelLabel).expandX();
		
		stage.addActor(table);
		
		
		
		
		
	}

	@Override
	public void dispose() {
		stage.dispose();
		
	}
	

}
