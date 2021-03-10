package Screens;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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

import Questions.Question;
import uc.ac.aston.game.Launcher;

/**
 * 
 * This class is a Screen that displays a Question, allowing player to select an Answer from the given option by clicking to the right
 * button. Waits for user interaction before going back to the main playing screen notifying whether player has answered correctly or not.
 *
 */
public class QuestionScreen implements Screen{
	//view handles what is seen in the screen.
	private Viewport view;
	//stage is the environment that can be viewed.
	private Stage stage;
	// variable containing the Launcher class that handles the different screens;
	private Launcher game;
	// variable containing the previous screen from which it will return to after player has answered the question.
	private PlayScreen playScreen;
	//boolean indicating whether the answer is correct.
	private boolean isAnswerCorrect;
	//boolean indicating whether the user has selected an answer.
	private boolean hasButtonBeenPressed;
	//ArrayList of string made of the question text, followed by 4 possible the options and the correct option.
	private ArrayList<String> question;
	
	public QuestionScreen(Launcher game, PlayScreen playScreen, Question listOfQuestions) {
		this.game=game;
		view = new FitViewport(Launcher.width,Launcher.height, new OrthographicCamera());
		this.playScreen = playScreen;
		isAnswerCorrect=false;
		stage = new Stage(view,game.batch);
		Gdx.input.setInputProcessor(stage);
		Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(),Color.WHITE);
		//question is selected randomly.
		question = listOfQuestions.randomQuestionGenerator();
		
		Table table = new Table();
		table.setFillParent(true);
		
		//label displaying the question text.
		Label questionLabel = new Label(question.get(0),font);
		questionLabel.setWrap(true);
		questionLabel.setWidth(200);
		
		//info label displaying displaying some additional information to the user.
		Label info = new Label("Press Q to go back to the playing screen",font);
		
		TextButtonStyle style= new TextButtonStyle();
		style.pressedOffsetX= 1;
		style.pressedOffsetY= -1;
		style.font=new BitmapFont();
			
		//Button with text attached indicating the first option, listening for the event of user clicking it.
		//when clicked checks if that option is the right answer.
		TextButton answer1= new TextButton("A: "+question.get(1),style);
		answer1.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				isAnswerCorrect=question.get(1).equals(question.get(5));
				hasButtonBeenPressed=true;
			}
		});
		
		//Button with text attached indicating the second option, listening for the event of user clicking it.
		//when clicked checks if that option is the right answer.
		TextButton answer2= new TextButton("B: "+question.get(2),style);
		answer2.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				isAnswerCorrect=question.get(2).equals(question.get(5));
				hasButtonBeenPressed=true;
			}
		});
		

		//Button with text attached indicating the third option, listening for the event of user clicking it.
		//when clicked checks if that option is the right answer.
		TextButton answer3= new TextButton("C: "+question.get(3),style);
		answer3.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				isAnswerCorrect=question.get(3).equals(question.get(5));;
				hasButtonBeenPressed=true;
			}
		});
		
		//Button with text attached indicating the fourth option, listening for the event of user clicking it.
		//when clicked checks if that option is the right answer.
		TextButton answer4= new TextButton("D: "+question.get(4),style);
		answer4.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				isAnswerCorrect=question.get(4).equals(question.get(5));;
				hasButtonBeenPressed=true;
			}
		});
		
		//layout specified using a Table structure
		table.add(questionLabel).width(200).expandX();
		table.row();
		table.add(answer1).width(10).expandX();
		table.row();
		table.add(answer2).width(10).expandX();
		table.row();
		table.add(answer3).width(10).expandX();
		table.row();
		table.add(answer4).width(10).expandX();
		table.row();
		table.add(info).expandX().padTop(60f);
		
		stage.addActor(table);
		
	}

	private void clicked() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Renders the stage on a black background, but also listens to user input, if Q key or a button is pressed returns to the PlayScreen, notifying
	 * whether a correct answer has been given by the player or not.
	 */
	@Override
	public void render(float delta) {
		if (Gdx.input.isKeyPressed(Input.Keys.Q)|(hasButtonBeenPressed)){
			if (isAnswerCorrect) {
				if (Launcher.isSoundOn) {
					Launcher.manager.get("music/CorrectAnswer.wav", Music.class).play();
				}
				playScreen.unlockDoor();
			}else {
				playScreen.playerFrozen();
				if (Launcher.isSoundOn) {
					Launcher.manager.get("music/WrongAnswer.wav", Music.class).play();
				}
			}
			playScreen.returnToPlayScreen();
			game.setScreen(playScreen);
			dispose();
		}
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
		
	}

}
