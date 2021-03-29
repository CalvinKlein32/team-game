package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uc.ac.aston.game.Launcher;

/**
 * Screen that is showed at the end of the game displaying the results.
 *
 */
public class GameOverScreen implements Screen{
	//view handles what is seen in the screen. 
	private Viewport view;
	//stage is the environment that can be viewed.
	private Stage stage;
	// variable containing the Launcher class that handles the different screens;
	private Launcher game;
	//boolean indicating whether the player wants to play again after they finished the game.
	private boolean wantsToPlayAgain=false;
	
	public GameOverScreen(Launcher game, int position, int points, int otherPlayerPoints) {
		this.game=game;
		view = new FitViewport(Launcher.width,Launcher.height, new OrthographicCamera());
		stage = new Stage(view,game.batch);
		
		Gdx.input.setInputProcessor(stage);
		
		Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(),Color.WHITE);
		
		//based on  the player points and the opposing player points result label would display the right message 
		String results;
		if (points==otherPlayerPoints) {
			results= "Congratulations magician you finished the game :), You ended the game with a Tie, you and your opponent both got "+
					+points + " points.";
		}else {
			results= "Congratulations magician you finished the game :), Overall you finished in";
			if (position==1) {
				results+=" 1st ";
			}else {
				results+=" 2nd ";
			}
			results+= " position with "+points+" points, and your opponent finished with "+otherPlayerPoints + " points.";

		}
		//label that shows the results and dimension		
		Label gameResults= new Label(results,font);
		gameResults.setWrap(true);
		gameResults.setWidth(350);
		//label displaying game over.
		Label gameOverText= new Label("Game Over !!!",font);
		
		TextButtonStyle style= new TextButtonStyle();
		style.pressedOffsetX= 1;
		style.pressedOffsetY= -1;
		style.font=new BitmapFont();
		style.fontColor=Color.RED;
		
		
		//playAgain button to allow user to play  again
		TextButton playAgain= new TextButton("Play again!",style);
		playAgain.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				wantsToPlayAgain=true;
			}
		});
		
		//layout specified using a Table structure
		Table table = new Table();
		table.setFillParent(true);
		
		table.add(gameOverText).width(100).expandX();
		table.row();
		table.add(gameResults).width(350).expandX().padTop(20f);
		table.row();
		table.add(playAgain);
		
		stage.addActor(table);
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Renders content of the stage, with a black background.
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (wantsToPlayAgain) {
			game.newGame();
		}
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
		stage.dispose();
		// TODO Auto-generated method stub
		
	}

}
