/*
 * MainActivity.java
 * 
 * @author Neil Bantoc and James Plaras 
 */
package project161.galaxhoot;

import project161.galaxhoot.openGL.OpenGLRenderer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class MainActivity.
 */
public class MainActivity extends Activity implements OnClickListener {

	/** The sensor_manager. */
	private SensorManager sensor_manager;

	/** The sensor. */
	private Sensor sensor;

	/** The score. */
	private TextView score;

	/** The frame. */
	private FrameLayout frame;

	/** The total_score. */
	private int total_score = 0;

	/** The values. */
	private float[] values;

	/** The flipped_values. */
	private float[] flipped_values;

	/** The alpha. */
	private float alpha = 0.5f;

	/** The sound pool. */
	private SoundPool soundPool;

	/** The bg. */
	private int explosion_sound, laser_sound, beep_low, beep_high, bg;

	/** The vibrator. */
	private Vibrator vibrator;

	/** The game. */
	private Game game = new Game(this);

	/** The galaxy bitmap. */
	private Bitmap galaxyBitmap;

	/** The rotation. */
	private int rotation;

	/** The mp. */
	private MediaPlayer mp;

	/** The change_view. */
	private ImageButton change_view;

	/** The view. */
	private GLSurfaceView view;

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		galaxyBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.galaxy);
		game.setViewMode(this.getIntent().getIntExtra("view_mode",
				Constants.TOP_VIEW));

		soundPool = new SoundPool(6, AudioManager.STREAM_MUSIC, 0);
		explosion_sound = soundPool.load(this, R.raw.explosion, 1);
		laser_sound = soundPool.load(this, R.raw.laser, 1);
		beep_low = soundPool.load(this, R.raw.beep_low, 1);
		beep_high = soundPool.load(this, R.raw.beep_high, 1);

		vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

		mp = MediaPlayer.create(this, R.raw.nyan_cat);
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		float volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		mp.setVolume(volume * 100f, volume * 100f);
		mp.setLooping(true);
		mp.start();

		view = new GLSurfaceView(this);
		view.setOnClickListener(this);
		view.setRenderer(new OpenGLRenderer(this));

		frame = new FrameLayout(this);

		LinearLayout top_buttons = new LinearLayout(this);

		top_buttons.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));

		change_view = new ImageButton(this);
		change_view.setImageResource(R.drawable.video);
		change_view.setBackgroundDrawable(null);
		change_view.setOnClickListener(this);

		score = new TextView(this);
		score.setId(100);
		score.setText("0");
		score.setTextSize(30);

		frame.addView(view);
		top_buttons.addView(change_view);
		top_buttons.addView(score);
		frame.addView(top_buttons);
		frame.setKeepScreenOn(true);

		sensor_manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		sensor = sensor_manager.getSensorList(Sensor.TYPE_ACCELEROMETER).get(0);

		values = new float[3];
		flipped_values = new float[3];
		flipped_values[0] = flipped_values[1] = flipped_values[2] = values[0] = values[1] = values[2] = 0;

		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE))
				.getDefaultDisplay();
		rotation = display.getOrientation();

		game.start();

		setContentView(frame);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		sensor_manager.registerListener(accelerationListener, sensor,
				SensorManager.SENSOR_DELAY_GAME);
		game.resume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		sensor_manager.unregisterListener(accelerationListener);
		game.pause();
		super.onStop();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mp.stop();
		mp.release();
	}

	/**
	 * Gets the game.
	 * 
	 * @return the game
	 */
	public Game getGame() {
		return game;
	}

	/** The acceleration listener. */
	private SensorEventListener accelerationListener = new SensorEventListener() {
		@Override
		public void onAccuracyChanged(Sensor sensor, int acc) {
		}

		@Override
		public void onSensorChanged(SensorEvent event) {

			/*
			 * if (Math.abs(values[0] - event.values[0]) < 0.1f) alpha = 0.1f;
			 * else if (Math.abs(values[0] - event.values[0]) < 0.3f) alpha =
			 * 0.3f; else if (Math.abs(values[0] - event.values[0]) < 0.5f)
			 * alpha = 0.5f;
			 */

			values[0] = values[0] + alpha * (event.values[0] - values[0]);

			/*
			 * if (Math.abs(values[1] - event.values[1]) < 0.1f) alpha = 0.1f;
			 * else if (Math.abs(values[1] - event.values[1]) < 0.2f) alpha =
			 * 0.3f; else if (Math.abs(values[2] - event.values[1]) < 0.3f)
			 * alpha = 0.5f; else if (Math.abs(values[2] - event.values[1]) <
			 * 0.4f) alpha = 0.7f; else alpha = 0.9f;
			 */

			values[1] = values[1] + alpha * (event.values[1] - values[1]);

			/*
			 * if (Math.abs(values[2] - event.values[2]) < 0.1f) alpha = 0.1f;
			 * else if (Math.abs(values[2] - event.values[2]) < 0.3f) alpha =
			 * 0.3f; else if (Math.abs(values[2] - event.values[2]) < 0.5f)
			 * alpha = 0.5f;
			 */
			values[2] = values[2] + 0.1f * (event.values[2] - values[2]);
		}
	};

	/**
	 * Gets the sensor values.
	 * 
	 * @return the sensor values
	 */
	public float[] getSensorValues() {
		if (rotation == Surface.ROTATION_0) {
			flipped_values[1] = -values[0];
			flipped_values[0] = -values[1];
			flipped_values[2] = values[2];
			return flipped_values;
		} else if (rotation == Surface.ROTATION_90)
			return values;

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if (v == change_view) {
			if (game.getViewMode() == Constants.TOP_VIEW)
				game.setViewMode(Constants.THREE_DIMENSIONAL_VIEW);
			else if (game.getViewMode() == Constants.THREE_DIMENSIONAL_VIEW)
				game.setViewMode(Constants.CHASE_CAM);
			else if (game.getViewMode() == Constants.CHASE_CAM)
				game.setViewMode(Constants.TOP_VIEW);
		} else if (v == view) {
			// game.fireMainShip();
		}
	}

	/**
	 * Play bg sound.
	 */
	public void playBGSound() {
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		float volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		soundPool.play(bg, volume, volume, 1, 0, 1f);
	}

	/**
	 * Play bullet sound.
	 */
	public void playBulletSound() {
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		float volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		soundPool.play(beep_low, volume, volume, 1, 0, 1f);
	}

	/**
	 * Play explosion sound.
	 */
	public void playExplosionSound() {
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		float volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		soundPool.play(explosion_sound, volume, volume, 1, 0, 1f);
	}

	/**
	 * Play high beep sound.
	 */
	public void playHighBeepSound() {
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		float volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		soundPool.play(beep_high, volume, volume, 1, 0, 1f);
	}

	/**
	 * Play low beep sound.
	 */
	public void playLowBeepSound() {
		AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
		float volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
		soundPool.play(laser_sound, volume, volume, 1, 0, 1f);
	}

	/**
	 * Vibrate.
	 * 
	 * @param millisecs
	 *            the millisecs
	 */
	public void vibrate(long millisecs) {
		vibrator.vibrate(millisecs);
	}

	/**
	 * Game over.
	 * 
	 * @param result
	 *            the result
	 */
	public void gameOver(int result) {
		Intent intent = new Intent(this, GameOverActivity.class);

		if (result == Constants.GAME_LOST)
			intent.putExtra("result", "You have been shot down.");
		else
			intent.putExtra("result",
					"Congratulations! You shot down all enemies!");

		intent.putExtra("score", total_score);

		intent.putExtra("view_mode", game.getViewMode());
		this.startActivity(intent);
		this.finish();
	}

	/**
	 * Adds the score.
	 * 
	 * @param i
	 *            the i
	 */
	public void addScore(int i) {
		total_score += i;
		score.post(new Runnable() {
			@Override
			public void run() {
				score.setText(total_score + "");
			}
		});

	}

	/**
	 * Sets the score.
	 * 
	 * @param new_string
	 *            the new score
	 */
	public void setScore(final String new_string) {
		score.post(new Runnable() {
			@Override
			public void run() {
				score.setText(new_string);
			}
		});

	}

	/**
	 * Refresh score.
	 */
	public void refreshScore() {
		score.post(new Runnable() {
			@Override
			public void run() {
				score.setText(total_score + "");
			}
		});

	}

	/**
	 * Gets the galaxy image.
	 * 
	 * @return the galaxy image
	 */
	public Bitmap getGalaxyImage() {
		return galaxyBitmap;
	}

}
