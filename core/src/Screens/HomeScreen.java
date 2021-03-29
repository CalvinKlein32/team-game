package Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton.ImageTextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import uc.ac.aston.game.Launcher;

public class HomeScreen implements Screen{
	
	//view handles what is seen in the screen. 
	private Viewport view;
	//stage is the environment that can be viewed.
	private Stage stage;
	// variable containing the Launcher class that handles the different screens;
	private Launcher game;
	
	private Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
	
	private final CheckBox level1= new CheckBox("Level 1",skin);
	//the following are a series of CheckBox variable used in the UI design for the user to make a particular selection based on their preference.
	final CheckBox level2= new CheckBox("Level 2",skin);
	final CheckBox level3= new CheckBox("Level 3",skin);
	final CheckBox level4= new CheckBox("Level 4",skin);
	final CheckBox level5= new CheckBox("Level 5",skin);
	final CheckBox level6= new CheckBox("Level 6",skin);
	final CheckBox soundOnBox= new CheckBox("on",skin);
	final CheckBox soundOffBox= new CheckBox("off",skin);
	final CheckBox musicOnBox= new CheckBox("on",skin);
	final CheckBox musicOffBox= new CheckBox("off",skin);
	//The following are a list of labels to display a particular message to the used, to make them understand a choice they must make.
	private Label levelsLabel;
	private Label soundLabel;
	private Label musicLabel;
	//submit is an ImageTextButton composed of an image alongside a Label specifying what is the purpose of the button
	private ImageTextButton submit;
	//info is an ImageTextButton composed of an image alongside a Label specifying what is the purpose of the button
	private ImageTextButton info;
	//level is the level that the user has selected they would like to play in
	private int level=-1;
	//isSoundOn boolean indicating whether the player choose for the sound effects to be on or off.
	private boolean isSoundOn=true;
	//isMusicOn boolean indicating whether the player choose for the music to be on or off.
	private boolean isMusicOn=true;
	//allFieldsCompleted boolean indicating whether all fields have been completed
	private boolean allFieldsCompleted=false;
	//infoClicked boolean indicating whether the user has requested to read the information of the game.
	private boolean infoClicked=false;
	//the following are a series of images representing the sample preview of a level of the game.
	private Texture level1BG,level2BG,level3BG,level4BG,level5BG,level6BG;
	//the following boolean variable represent whether a particular level has been clicked or not.
	private boolean level1Checked, level2Checked, level3Checked, level4Checked, level5Checked, level6Checked;
	
	public HomeScreen(Launcher game) {
		this.game=game;
		view = new FitViewport(Launcher.width,Launcher.height, new OrthographicCamera());
		stage = new Stage(view,game.batch);
		Gdx.input.setInputProcessor(stage);
		Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(),Color.GOLD);
		
		
		levelsLabel= new Label("Choose level:",font);
		soundLabel= new Label("Sound",font);
		musicLabel= new Label("Music",font);
		
		TextButtonStyle style= new TextButtonStyle();
		style.pressedOffsetX= 1;
		style.pressedOffsetY= -1;
		style.font=new BitmapFont();
		style.fontColor=Color.ORANGE;
		style.overFontColor=Color.WHITE;
		
		//Creates the submit ImageTextButton by giving it a label and attaching an image next to it.
		Drawable magicIcon = new TextureRegionDrawable(new TextureRegion(new Texture("magic.png")));
		ImageTextButton.ImageTextButtonStyle buttonStyle = new ImageTextButtonStyle(magicIcon,null,null,new BitmapFont());
		submit = new ImageTextButton("                  Start",buttonStyle);
		submit.setWidth(60);
		submit.setHeight(60);
		
		//Creates the info ImageTextButton by giving it a label and attaching an image next to it.
		Drawable infoIcon = new TextureRegionDrawable(new TextureRegion(new Texture("infoIcon.png")));
		ImageTextButton.ImageTextButtonStyle buttonStyle2 = new ImageTextButtonStyle(infoIcon,null,null,new BitmapFont());
		info = new ImageTextButton("                                How to play",buttonStyle2);
		info.setWidth(40);
		info.setHeight(40); 
		
		level1.setChecked(true);
		soundOnBox.setChecked(true);
		musicOnBox.setChecked(true);
		
		this.handleListiners();
		this.makeButtonsMutuallyExclusive();
		layoutOfElements();
		
		
		
		
		
		
		
		
		
		
		
		
		
		
	}
	
	/**
	 * Defines the structure of the screen through a table layout where each element is in particular row of the table.
	 */
	public void layoutOfElements() {
		Table table = new Table();
		table.setFillParent(true);
		
		table.row().padTop(50);
		table.add(levelsLabel).padRight(120);
		table.row();
		table.add(level1);
		table.row();
		table.add(level2);
		table.row();
		table.add(level3);
		table.row();
		table.add(level4);
		table.row();
		table.add(level5);
		table.row();
		table.add(level6);
		table.row().padTop(50);
		table.add(soundLabel).padRight(150);
		table.add(soundOnBox);
		table.add(soundOffBox);
		table.row();
		table.add(musicLabel).padRight(150);
		table.add(musicOnBox);
		table.add(musicOffBox);
		table.row();
		table.add(info).width(40).height(40);
		table.row();
		table.add(submit).width(60).height(60);
		
		
		stage.addActor(table);
	}
	
	/**
	 * makeButtonsMutuallyExclusive uses combines a series of CheckBox variables into particular groups based on their purpose
	 * ending with a level ButtonGroup, music choice ButtonGroup and sound effects choice ButtonGroup.
	 * the elements in a particular group are mutually exclusive where only one choice is allowed.
	 */
	
	public void makeButtonsMutuallyExclusive() {
		ButtonGroup buttonGroup = new ButtonGroup(level1,level2,level3, level4, level5, level6);
		//next set the max and min amount to be checked
		buttonGroup.setMaxCheckCount(1);
		buttonGroup.setMinCheckCount(1);
			
		ButtonGroup sound = new ButtonGroup(soundOnBox,soundOffBox);
		//next set the max and min amount to be checked
		sound.setMaxCheckCount(1);
		sound.setMinCheckCount(1);
		
		ButtonGroup music = new ButtonGroup(musicOnBox,musicOffBox);
		//next set the max and min amount to be checked
		music.setMaxCheckCount(1);
		music.setMinCheckCount(1);
	}
	
	/**
	 * handleListiners is a method that based on the action performed on a particular button it would change its state
	 */
	public void handleListiners() {
		level1.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (level1.isChecked()) {
					level=1;
					resetChecked();
					level1Checked = true;
				}
				Gdx.graphics.setContinuousRendering(level1.isChecked());
			}
		});
		
		level2.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.graphics.setContinuousRendering(level2.isChecked());
				if (level2.isChecked()) {
					level=2;
					resetChecked();
					level2Checked =true;
				}
			}
		});
		
		level3.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.graphics.setContinuousRendering(level3.isChecked());
				if (level3.isChecked()) {
					level=3;
					resetChecked();
					level3Checked = true;
				}
			}
		});
		
		level4.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.graphics.setContinuousRendering(level4.isChecked());
				if (level4.isChecked()) {
					level=4;
					resetChecked();
					level4Checked = true;
				}
			}
		});
		
		level5.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.graphics.setContinuousRendering(level5.isChecked());
				if (level5.isChecked()) {
					level=5;
					resetChecked();
					level5Checked = true;
				}
			}
		});
		
		level6.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.graphics.setContinuousRendering(level6.isChecked());
				if (level6.isChecked()) {
					level=6;
					resetChecked();
					level6Checked = true;
				}
			}
		});
		
		soundOnBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.graphics.setContinuousRendering(soundOnBox.isChecked());
				if (soundOnBox.isChecked()) {
					isSoundOn=true;
				}
			}
		});
		
		soundOffBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.graphics.setContinuousRendering(soundOffBox.isChecked());
				if (soundOffBox.isChecked()) {
					isSoundOn=false;
				}
			}
		});
		
		musicOffBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.graphics.setContinuousRendering(musicOffBox.isChecked());
				if (musicOffBox.isChecked()) {
					isMusicOn=false;
				}
			}
		});
		
		musicOnBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.graphics.setContinuousRendering(musicOnBox.isChecked());
				if (musicOnBox.isChecked()) {
					isMusicOn=true;
				}
			}
		});
		
		submit.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				allFieldsCompleted=true;
			}
		});
		info.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				infoClicked=true;
				   game.setScreen(new InfoScreen(game,null));
			}
		});
	}
	
	public void resetChecked() {
		level1Checked = false;
		level2Checked = false;
		level3Checked = false;
		level4Checked = false;
		level5Checked = false;
		level6Checked = false;
	}
	
	@Override
	public void show() {
		level1BG = new Texture(Gdx.files.internal("level1.png"));
		level2BG = new Texture(Gdx.files.internal("level2.png"));
		level3BG = new Texture(Gdx.files.internal("level3.png"));
		level4BG = new Texture(Gdx.files.internal("level4.png"));
		level5BG = new Texture(Gdx.files.internal("level5.png"));
		level6BG = new Texture(Gdx.files.internal("level6.png"));
		
	}

	@Override
	public void render(float delta) {
		
		if (this.allFieldsCompleted) {
			game.findLobby(level-1, isSoundOn, isMusicOn);
			dispose();
			
			
		}
		
		if (infoClicked) {
			infoClicked=false;
			game.setScreen(new InfoScreen(game,this));
		
		}
		
		
		game.batch.begin();
		//based on the level that has been selected an appropriate background image would be drawn.
		if(level1Checked) {
			game.batch.draw(level1BG,0,0);
		} 
		if(level2Checked) {
			game.batch.draw(level2BG,0,0);
		}
		if(level3Checked) {
			game.batch.draw(level3BG,0,0);
		}
		if (level4Checked) {
			game.batch.draw(level4BG,0,0);
		}
		if (level5Checked) {
			game.batch.draw(level5BG,0,0);
		}
		if (level6Checked) {
			game.batch.draw(level6BG,0,0);
		}
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
		stage.dispose();
		// TODO Auto-generated method stub
		
	}

}