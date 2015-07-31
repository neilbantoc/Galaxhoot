/*
 * Game.java
 * 
 * @author Neil Bantoc and James Plaras 
 */
package project161.galaxhoot;

import java.util.ArrayList;
import java.util.Random;

import project161.galaxhoot.objects.Ship;

// TODO: Auto-generated Javadoc
/**
 * The Class Game.
 */
public class Game implements Runnable, Constants {

	/** The enemies. */
	private ArrayList<Ship> enemies = new ArrayList<Ship>();

	/** The main ship. */
	private Ship mainShip = new Ship(0, -30);

	/** The paused. */
	private boolean paused = false;

	/** The thread. */
	private Thread thread = new Thread(this);

	/** The gen. */
	Random gen = new Random(System.currentTimeMillis());

	/** The view_mode. */
	private int view_mode;

	/** The spawn locations. */
	private int[] spawnLocations = { -15, 15, -30, 30, -45, 45, -60, 60 };

	/** The activity. */
	private MainActivity activity;

	/** The ship_scale. */
	private float ship_scale = 1.5f;

	/**
	 * Instantiates a new game.
	 * 
	 * @param activity
	 *            the activity
	 */
	public Game(MainActivity activity) {
		this.view_mode = Constants.TOP_VIEW;
		this.activity = activity;

		this.mainShip.setScale(ship_scale);
	}

	/**
	 * Starts the.
	 */
	public void start() {
		thread.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (mainShip != null) {
			if (!paused) {
				if (enemies.size() == 0)
					try {
						Thread.sleep(100);
						activity.setScore("Ready");
						activity.playLowBeepSound();
						Thread.sleep(1000);
						activity.setScore("Set");
						activity.playLowBeepSound();
						Thread.sleep(1000);
						activity.refreshScore();
						activity.playHighBeepSound();
						spawnEnemies();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				fireMainShip();
				fireEnemiesShip();
				moveEnemiesShip();

			}

		}
		if (mainShip == null)
			activity.gameOver(Constants.GAME_LOST);
		/*
		 * else if (enemies.size() == 0) activity.gameOver(Constants.GAME_WON);
		 */
	}

	/**
	 * Spawn enemies.
	 */
	private void spawnEnemies() {
		// TODO Auto-generated method stub
		int numberOfSpawns = gen.nextInt(spawnLocations.length) + 1;

		for (int i = 0; i < numberOfSpawns; i++) {
			Ship ship = new Ship(
					spawnLocations[gen.nextInt(spawnLocations.length)],
					20 + 5 * gen.nextInt(4));
			ship.setScale(ship_scale);
			enemies.add(ship);
		}
	}

	/**
	 * Move enemies ship.
	 */
	private synchronized void moveEnemiesShip() {

		float move_buffer;
		for (Ship enemy : enemies) {
			move_buffer = enemy.getMoveBuffer();
			if (move_buffer == 0) {
				enemy.setMoveBuffer((gen.nextInt(50) - 24));
			} else {
				if (move_buffer > 0) {
					enemy.setMoveBuffer(--move_buffer);
					move_buffer = -1;
				} else {
					enemy.setMoveBuffer(++move_buffer);
					move_buffer = +1;
				}
				float moveX = (enemy.getCenterX() + move_buffer);
				if (moveX < minX)
					moveX = minX;
				else if (moveX > maxX)
					moveX = maxX;
				enemy.setCenterX(moveX);
			}
		}
	}

	/**
	 * Fire enemies ship.
	 */
	public synchronized void fireEnemiesShip() {
		Ship min = null;

		for (Ship candidates : enemies) {
			if (min == null)
				min = candidates;
			else if (Math.abs(min.getCenterX() - mainShip.getCenterX()) > Math
					.abs(candidates.getCenterX() - mainShip.getCenterX()))
				min = candidates;
		}

		if (min == null)
			return;

		float bulletX = min.getCenterX();
		float bulletY;

		activity.playBulletSound();

		for (bulletY = min.getCenterY(); bulletY >= minY; bulletY -= 2) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			moveEnemiesShip();
			min.setBulletX(bulletX);
			min.setBulletY(bulletY);
			if (mainShip.isHit(bulletX, bulletY)) {

				activity.playExplosionSound();
				activity.vibrate(2000);

				mainShip = null;
				bulletX = NIL;
				bulletY = NIL;
				min.setBulletX(bulletX);
				min.setBulletY(bulletY);
				return;
			}
		}
		bulletX = NIL;
		bulletY = NIL;
		min.setBulletX(bulletX);
		min.setBulletY(bulletY);

	}

	/**
	 * Fire main ship.
	 */
	public synchronized void fireMainShip() {

		float bulletX = mainShip.getCenterX();
		float bulletY;

		activity.playBulletSound();

		for (bulletY = mainShip.getCenterY(); bulletY <= maxY; bulletY += 2) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			moveEnemiesShip();
			mainShip.setBulletX(bulletX);
			mainShip.setBulletY(bulletY);
			for (Ship enemy : enemies) {
				if (enemy.isHit(bulletX, bulletY)) {

					activity.playExplosionSound();
					activity.vibrate(Constants.VIBRATE_LENGTH);

					enemies.remove(enemy);
					bulletX = NIL;
					bulletY = NIL;
					mainShip.setBulletX(bulletX);
					mainShip.setBulletY(bulletY);

					activity.addScore(200);

					return;
				}
			}

		}
		bulletX = NIL;
		bulletY = NIL;
		mainShip.setBulletX(bulletX);
		mainShip.setBulletY(bulletY);

	}

	/**
	 * Gets the enemies.
	 * 
	 * @return the enemies
	 */
	public ArrayList<Ship> getEnemies() {
		return enemies;
	}

	/**
	 * Sets the enemies.
	 * 
	 * @param enemies
	 *            the new enemies
	 */
	public void setEnemies(ArrayList<Ship> enemies) {
		this.enemies = enemies;
	}

	/**
	 * Gets the main ship.
	 * 
	 * @return the main ship
	 */
	public Ship getMainShip() {
		return (mainShip != null) ? mainShip : new Ship(-500, -500);
	}

	/**
	 * Sets the main ship.
	 * 
	 * @param mainShip
	 *            the new main ship
	 */
	public void setMainShip(Ship mainShip) {
		this.mainShip = mainShip;
	}

	/**
	 * Pause.
	 */
	public void pause() {
		paused = true;
	}

	/**
	 * Resume.
	 */
	public void resume() {
		paused = false;
	}

	/**
	 * Gets the view mode.
	 * 
	 * @return the view mode
	 */
	public int getViewMode() {
		return view_mode;
	}

	/**
	 * Sets the view mode.
	 * 
	 * @param new_view_mode
	 *            the new view mode
	 */
	public void setViewMode(int new_view_mode) {
		this.view_mode = new_view_mode;
	}

}
