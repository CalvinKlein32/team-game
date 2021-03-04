package Collisions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;


import Obstacle.Door;
import Obstacle.Dragon;
import Obstacle.Enemy;
import Obstacle.FinishLine;
import Obstacle.InteractiveObstacle;
import Obstacle.Spikes;
import Player.Player;
import uc.ac.aston.game.Launcher;

/**
 * 
 * WorldContactListener is the class that manages the collisions between objects, more specifically when two fixtures associated
 * with Body objects come in contact against each other.
 *
 */
public class WorldContactListener implements ContactListener{
	//The player instance that has been in contact with another object.
	private Player player;
	//doGenerateQuestion boolean variable that checks whether is time to generate questions or not. 
	private boolean doGenerateQuestion;
	
	public WorldContactListener(Player player) {
		this.player=player;
		doGenerateQuestion= false;
	}
	


	/**
	 * beginContact is the method that is called when two objects in the world come in contact, it handles contact between:
	 * the feet fixture of the player and the spike fixture.
	 * the feet fixture of the player and the sea fixture.
	 * the front or back fixture of the player and the spike fixture.
	 * the front or back fixture of the player and the Door fixture.
	 * any fixture of the player against the enemy fixture(which could be blue or red dragon).
	 *  
	 */
	@Override
	public void beginContact(Contact contact) {
		
		//fixA is the first fixture that was involved in the contact between the two objects.
		Fixture fixA= contact.getFixtureA();
		//fixA is the second fixture that was involved in the contact between the two objects.
		Fixture fixB= contact.getFixtureB();
		
		//When one of the fixture is feet of a player we identify which one is it, if the other fixture is an InteractiveObstacle meaning that it 
		//has been in contact with an obstacle the player dies.
		if (fixA.getUserData()=="feet" || fixB.getUserData()=="feet") {
			Fixture feet;
			Fixture obstacle;
			if (fixA.getUserData()=="feet") {
				feet= fixA;
				obstacle = fixB;
			}else {
				feet=fixB;
				obstacle=fixA;
			}
			
			if (obstacle.getUserData()!=null && (obstacle.getUserData() instanceof InteractiveObstacle)) {
				if ((player.b2body.getFixtureList().contains(feet, true))){
					player.destroyPlayer();
				};
			}
		
		//When one of the fixture is side (either left or right) of a player we identify which one is it, and based on what the other object
		//the player has run into we perform an action.
		}else if (fixA.getUserData()=="side" || fixB.getUserData()=="side") {
			Fixture side;
			Fixture obstacle;
			if (fixA.getUserData()=="side") {
				side= fixA;
				obstacle = fixB;
			}else {
				side=fixB;
				obstacle=fixA;
			}
			
			if (player.b2body.getFixtureList().contains(side, true)) {
				//If the Object the player has ran into is a Spike object the player dies.
				if (obstacle.getUserData()!=null && (obstacle.getUserData() instanceof Spikes)) {
					player.destroyPlayer();
					
		        //If the Object the player has ran into is a Door object we can validate question generation.	
				}else if (obstacle.getUserData()!=null && (obstacle.getUserData() instanceof Door)) {
					if (!player.getCanOpenDoor()) {
						doGenerateQuestion=true;
					}else {
						((Door) obstacle.getUserData()).destroyDoorCells();
						player.setCanOpenDoor(false);
					}
				//If the Object the player has ran into is a FinishLine object we can make the player aware that they have finished their level.	
				}else if (obstacle.getUserData()!=null && (obstacle.getUserData() instanceof FinishLine)) {
					player.setHasFinishedLevel(true);
				//If the Object the player has ran into an Enemy object we destroy the player.
				}else if (obstacle.getUserData()!=null && (obstacle.getUserData() instanceof Enemy)) {
					player.destroyPlayer();
				}
				
			}

		//When an Enemy hits an object (which are the Enemy Bounds object), the enemy is told to reverse speed, if it is a red dragon it reverses
		//speed in the X direction, if the dragon is blue it reverses speed in the y direction.
		}else if (fixA.getFilterData().categoryBits==Launcher.enemyBit  || fixB.getFilterData().categoryBits==Launcher.enemyBit ) {
			if (fixA.getFilterData().categoryBits==Launcher.enemyBit && fixB.getFilterData().categoryBits==Launcher.objectBit) {
				if (((Enemy) fixA.getUserData()).whichDragon().equals("red")) {
					((Enemy) fixA.getUserData()).reverseDirection(true, false);
				}else {
					((Enemy) fixA.getUserData()).reverseDirection(false, true);
				}
				
				
			}else if (fixA.getFilterData().categoryBits==Launcher.objectBit){
				if (((Enemy) fixB.getUserData()).whichDragon().equals("red")) {
					((Enemy) fixB.getUserData()).reverseDirection(true, false);
				}else {
					((Enemy) fixB.getUserData()).reverseDirection(false, true);
				}
				
			}
			
		}
		
	}
	
	
	public void resetGenerateQuestion() {
		doGenerateQuestion=false;
	}
	
	public boolean getDoGenerateQuestion() {
		return doGenerateQuestion;
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}

}
