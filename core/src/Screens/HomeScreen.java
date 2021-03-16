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
	final CheckBox level2= new CheckBox("Level 2",skin);
	final CheckBox level3= new CheckBox("Level 3",skin);
	final CheckBox level4= new CheckBox("Level 4",skin);
	final CheckBox level5= new CheckBox("Level 5",skin);
	final CheckBox level6= new CheckBox("Level 6",skin);
	final CheckBox soundOnBox= new CheckBox("on",skin);
	final CheckBox soundOffBox= new CheckBox("off",skin);
	final CheckBox musicOnBox= new CheckBox("on",skin);
	final CheckBox musicOffBox= new CheckBox("off",skin);
	Label gameIntro;
	Label levelsLabel;
	Label soundLabel;
	Label musicLabel;
	//private TextButton submit;
	private ImageTextButton submit;
	private int level=-1;
	private boolean isSoundOn=true;
	private boolean isMusicOn=true;
	private boolean allFieldsCompleted=false;
	
	
	public HomeScreen(Launcher game) {
		this.game=game;
		view = new FitViewport(Launcher.width,Launcher.height, new OrthographicCamera());
		stage = new Stage(view,game.batch);
		Gdx.input.setInputProcessor(stage);
		Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(),Color.RED);
		//Skin skin = new Skin(Gdx.files.internal("uiskin.json")); 
		
		gameIntro= new Label("Welcome magician, make your choices and let's enter into the magic world ! ! !",font);
		gameIntro.setFontScale(2);
		gameIntro.setWrap(true);
		gameIntro.setWidth(500);
		
		levelsLabel= new Label("Choose level:",font);
		soundLabel= new Label("Sound",font);
		musicLabel= new Label("Music",font);
		
		TextButtonStyle style= new TextButtonStyle();
		style.pressedOffsetX= 1;
		style.pressedOffsetY= -1;
		style.font=new BitmapFont();
		style.fontColor=Color.ORANGE;
		style.overFontColor=Color.WHITE;
		
		Drawable magicIcon = new TextureRegionDrawable(new TextureRegion(new Texture("magic.png")));
		ImageTextButton.ImageTextButtonStyle buttonStyle = new ImageTextButtonStyle(magicIcon,null,null,new BitmapFont());
		submit = new ImageTextButton("                  Start",buttonStyle);
		submit.setWidth(60);
		submit.setHeight(60);
		//submit= new TextButton("Lets goo",style);
		level1.setChecked(true);
		soundOnBox.setChecked(true);
		musicOnBox.setChecked(true);
		
		this.handleListiners();
		this.makeButtonsMutuallyExclusive();
		layoutOfElements();
		
		
		
		
		
		//initalize stage and all your buttons
		
		
		
		
		
		
		
		
	}
	
	
	public void layoutOfElements() {
		Table table = new Table();
		table.setFillParent(true);
		
		table.add(gameIntro).center().width(500);
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
		table.add(submit).width(60).height(60);
		
		stage.addActor(table);
	}
	
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
	
	public void handleListiners() {
		level1.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				if (level1.isChecked()) {
					level=1;
				}
				Gdx.graphics.setContinuousRendering(level1.isChecked());
			}
		});
		
		level2.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				Gdx.graphics.setContinuousRendering(level2.isChecked());
				if (level2.isChecked()) {
					level=2;
				}
			}
		});
		
		level3.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				Gdx.graphics.setContinuousRendering(level3.isChecked());
				if (level3.isChecked()) {
					level=3;
				}
			}
		});
		
		level4.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				Gdx.graphics.setContinuousRendering(level4.isChecked());
				if (level4.isChecked()) {
					level=4;
				}
			}
		});
		
		level5.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				Gdx.graphics.setContinuousRendering(level5.isChecked());
				if (level5.isChecked()) {
					level=5;
				}
			}
		});
		
		level6.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				Gdx.graphics.setContinuousRendering(level6.isChecked());
				if (level6.isChecked()) {
					level=6;
				}
			}
		});
		
		soundOnBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				Gdx.graphics.setContinuousRendering(soundOnBox.isChecked());
				if (soundOnBox.isChecked()) {
					isSoundOn=true;
				}
			}
		});
		
		soundOffBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				Gdx.graphics.setContinuousRendering(soundOffBox.isChecked());
				if (soundOffBox.isChecked()) {
					isSoundOn=false;
				}
			}
		});
		
		musicOffBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
				Gdx.graphics.setContinuousRendering(musicOffBox.isChecked());
				if (musicOffBox.isChecked()) {
					isMusicOn=false;
				}
			}
		});
		
		musicOnBox.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// TODO Auto-generated method stub
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
	}
	
	
	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		if (this.allFieldsCompleted) {
			game.findLobby(level-1, isSoundOn, isMusicOn);
			dispose();
			
		}
		
		
		Gdx.gl.glClearColor(0,0,0,1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		game.batch.begin();
		game.batch.draw(new Texture("magicalBackground.png"),0,0);
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
