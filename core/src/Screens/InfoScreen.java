package Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uc.ac.aston.game.Launcher;

/**
 * InfoScreen is a Screen class that displays the instructions for the game.
 *
 */
public class InfoScreen implements Screen {
	private Viewport view;
	//stage is the environment that can be viewed.
	private Stage stage;
	// variable containing the Launcher class that handles the different screens;
	private Launcher game;
	//Menu TextButton is the button to go back to the Menu.
	private TextButton Menu;
	//backgroundTexture is the texture that would be shown at the background of this screen
	private Texture backgroundTexture;
	//isMenuClicked is a boolean variable to check whether the Menu button has been clicked.
	private boolean isMenuClicked = false;
	
	public InfoScreen(Launcher game, HomeScreen home) {
		this.game = game;
		view = new FitViewport(Launcher.width,Launcher.height, new OrthographicCamera());
		stage = new Stage(view,game.batch);
		Gdx.input.setInputProcessor(stage);
		Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(),Color.RED);
		
		TextButtonStyle style= new TextButtonStyle();
		style.pressedOffsetX= 1;
		style.pressedOffsetY= -1;
		style.font=new BitmapFont();
		style.fontColor=Color.RED;
		
		Menu = new TextButton("Back to Home Page!", style);
		
		layoutofElemnts();
		handleListiners();

	}
	
	/** 
	 * Defines the structure of the screen through a table layout where each element is in particular row of the table.
	 */
	public void layoutofElemnts() {
		Table Title = new Table();
		Title.setFillParent(true);
		
		Title.add(Menu).padTop(450);
		Title.row();
		stage.addActor(Title);
	}
	
	/**
	 * handleListiners is a method that based on the action performed on a particular button it would change its state
	 */
	public void handleListiners() {
		Menu.addListener(new ClickListener() {
			public void clicked (InputEvent event, float x, float y) {
				isMenuClicked=true;
			}
			
		});
	}
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		backgroundTexture = new Texture(Gdx.files.internal("info.png"));

	}
	 
	@Override
	public void render(float delta) {
		
		//changes to the homeScreen based on the value of isMenuClicked.
		if(this.isMenuClicked) {
			game.setScreen(new HomeScreen(game));
		}
		
		//draws background image
		game.batch.begin();
		game.batch.draw(backgroundTexture,0, 0);
		game.batch.end();
		
		
	   
		stage.draw();
	
	
	}

	@Override
	public void resize(int width, int height) {
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

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		stage.dispose();
	}



}
