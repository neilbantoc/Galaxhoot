/*
 * OpenGLRenderer.java
 * 
 * @author Neil Bantoc and James Plaras 
 */
package project161.galaxhoot.openGL;

import java.util.ConcurrentModificationException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import project161.galaxhoot.Constants;
import project161.galaxhoot.Game;
import project161.galaxhoot.MainActivity;
import project161.galaxhoot.objects.Ship;
import project161.galaxhoot.shapes.BulletMesh;
import project161.galaxhoot.shapes.GalaxhootFieldMesh;
import project161.galaxhoot.shapes.HollowSquare;
import project161.galaxhoot.shapes.ShipMesh;
import project161.galaxhoot.shapes.StarsMesh;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

// TODO: Auto-generated Javadoc
/**
 * The Class OpenGLRenderer.
 */
public class OpenGLRenderer implements Renderer, Constants {

	/** The game. */
	Game game;

	/** The activity. */
	MainActivity activity;

	/** The galaxhoot field mesh. */
	GalaxhootFieldMesh galaxhootFieldMesh;

	/** The ship draw. */
	ShipMesh shipDraw;

	/** The bullet draw. */
	BulletMesh bulletDraw;

	/** The stars. */
	StarsMesh stars;

	/** The hollow_square. */
	HollowSquare hollow_square;

	/**
	 * Instantiates a new open gl renderer.
	 * 
	 * @param activity
	 *            the activity
	 */
	public OpenGLRenderer(MainActivity activity) {
		this.activity = activity;
		game = activity.getGame();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.opengl.GLSurfaceView.Renderer#onSurfaceCreated(javax.microedition
	 * .khronos.opengles.GL10, javax.microedition.khronos.egl.EGLConfig)
	 */
	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		shipDraw = new ShipMesh();

		galaxhootFieldMesh = new GalaxhootFieldMesh(activity.getGalaxyImage());
		bulletDraw = new BulletMesh();
		hollow_square = new HollowSquare();

		stars = new StarsMesh();
	}

	/*
	 * Screen rotation (non-Javadoc)
	 * 
	 * @see
	 * android.opengl.GLSurfaceView.Renderer#onSurfaceChanged(javax.microedition
	 * .khronos.opengles.GL10, int, int)
	 */
	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height) {
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 1f, 500f);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		gl.glEnable(GL10.GL_LINE_SMOOTH);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.opengl.GLSurfaceView.Renderer#onDrawFrame(javax.microedition.
	 * khronos.opengles.GL10)
	 */
	@Override
	public synchronized void onDrawFrame(GL10 gl) {

		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glLoadIdentity();
		float[] values = activity.getSensorValues();

		Float angle = (float) (100 * (values[2] / 9.81));
		angle = angle > 99 ? 99 : angle;

		float centerX;
		float centerY;

		if (game.getMainShip() != null) {
			centerX = (values[1] / 9.81f) * 65 * Constants.SHIP_SENSITIVITY;
			centerX = centerX > Constants.maxX ? Constants.maxX : centerX;
			centerX = centerX < Constants.minX ? Constants.minX : centerX;
			game.getMainShip().setCenterX(centerX);
			centerY = game.getMainShip().getCenterY();
		} else {
			centerX = 0;
			centerY = -30;
		}
		gl.glColor4f(1, 1, 1, 1);
		if (game.getViewMode() == TOP_VIEW) {
			GLU.gluLookAt(gl, 0, 0, 100, 0, 0, 0, 0, 1, 0);
			galaxhootFieldMesh.draw(gl);
		} else if (game.getViewMode() == THREE_DIMENSIONAL_VIEW)
			GLU.gluLookAt(gl, 0, -100 + angle, 75 + angle, 0, 0, 0, 0, 0,
					100 + angle);
		else if (game.getViewMode() == CHASE_CAM)
			GLU.gluLookAt(gl, centerX > 65 ? 65 : centerX, centerY - 10, 10,
					centerX > 65 ? 65 : centerX, 0, -5, 0, 0, 1);

		stars.draw(gl);

		gl.glLineWidth(3f);
		hollow_square.draw(gl);

		if (game.getMainShip() != null) {

			gl.glPushMatrix();
			gl.glTranslatef(centerX, centerY, 0);
			gl.glScalef(game.getMainShip().getScale(), game.getMainShip()
					.getScale(), game.getMainShip().getScale());
			shipDraw.draw(gl);
			gl.glPopMatrix();

			if (game.getMainShip().getBulletX() != NIL
					&& game.getMainShip().getBulletY() != NIL) {
				gl.glPushMatrix();
				gl.glTranslatef(game.getMainShip().getBulletX(), game
						.getMainShip().getBulletY(), 0);
				gl.glColor4f(0, 1, 1, 1);
				bulletDraw.draw(gl);
				gl.glPopMatrix();
			}
		}
		try {
			for (Ship enemy : game.getEnemies()) {
				gl.glColor4f(1, 0, 0, 1);
				centerX = enemy.getCenterX();
				centerY = enemy.getCenterY();
				gl.glPushMatrix();

				gl.glTranslatef(centerX, centerY, 0);
				gl.glRotatef(180, 0, 0, 1);
				gl.glScalef(enemy.getScale(), enemy.getScale(),
						enemy.getScale());
				shipDraw.draw(gl);
				gl.glPopMatrix();

				gl.glColor4f(1, 1, 0, 1);
				if (enemy.getBulletX() != NIL && enemy.getBulletY() != NIL) {

					gl.glPushMatrix();
					gl.glTranslatef(enemy.getBulletX(), enemy.getBulletY(), 0);
					bulletDraw.draw(gl);
					gl.glPopMatrix();
				}
			}
		} catch (ConcurrentModificationException e) {
		}
	}

}
