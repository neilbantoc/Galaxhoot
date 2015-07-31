/*
 * Ship.java
 * 
 * @author Neil Bantoc and James Plaras 
 */
package project161.galaxhoot.objects;

// TODO: Auto-generated Javadoc
/**
 * The Class Ship.
 */
public class Ship {

	/** The center y. */
	float centerX, centerY;

	/** The bullet y. */
	float bulletX = -100, bulletY = -100;

	/** The move_buffer. */
	float move_buffer = 0;

	/** The scale. */
	float scale = 1;

	/**
	 * Instantiates a new ship.
	 * 
	 * @param centerX
	 *            the center x
	 * @param centerY
	 *            the center y
	 */
	public Ship(float centerX, float centerY) {
		super();
		this.centerX = centerX;
		this.centerY = centerY;
	}

	/**
	 * Checks if is hit.
	 * 
	 * @param bulletX
	 *            the bullet x
	 * @param bulletY
	 *            the bullet y
	 * @return true, if is hit
	 */
	public boolean isHit(float bulletX, float bulletY) {
		return (centerX - (5 * scale) < bulletX
				&& centerX + (5 * scale) > bulletX
				&& centerY - (3 * scale) < bulletY && centerY + (3 * scale) > bulletY);
	}

	/**
	 * Gets the center x.
	 * 
	 * @return the center x
	 */
	public float getCenterX() {
		return centerX;
	}

	/**
	 * Gets the center y.
	 * 
	 * @return the center y
	 */
	public float getCenterY() {
		return centerY;
	}

	/**
	 * Sets the center x.
	 * 
	 * @param centerX
	 *            the new center x
	 */
	public void setCenterX(float centerX) {
		this.centerX = centerX;
	}

	/**
	 * Sets the center y.
	 * 
	 * @param centerY
	 *            the new center y
	 */
	public void setCenterY(float centerY) {
		this.centerY = centerY;
	}

	/**
	 * Gets the bullet x.
	 * 
	 * @return the bullet x
	 */
	public float getBulletX() {
		return bulletX;
	}

	/**
	 * Sets the bullet x.
	 * 
	 * @param bulletX
	 *            the new bullet x
	 */
	public void setBulletX(float bulletX) {
		this.bulletX = bulletX;
	}

	/**
	 * Gets the bullet y.
	 * 
	 * @return the bullet y
	 */
	public float getBulletY() {
		return bulletY;
	}

	/**
	 * Sets the bullet y.
	 * 
	 * @param bulletY
	 *            the new bullet y
	 */
	public void setBulletY(int bulletY) {
		this.bulletY = bulletY;
	}

	/**
	 * Sets the bullet y.
	 * 
	 * @param bulletY
	 *            the new bullet y
	 */
	public void setBulletY(float bulletY) {
		this.bulletY = bulletY;
	}

	/**
	 * Sets the move buffer.
	 * 
	 * @param move_buffer
	 *            the new move buffer
	 */
	public void setMoveBuffer(float move_buffer) {
		this.move_buffer = move_buffer;
	}

	/**
	 * Gets the move buffer.
	 * 
	 * @return the move buffer
	 */
	public float getMoveBuffer() {
		return this.move_buffer;
	}

	/**
	 * Sets the scale.
	 * 
	 * @param scale
	 *            the new scale
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}

	/**
	 * Gets the scale.
	 * 
	 * @return the scale
	 */
	public float getScale() {
		return scale;
	}
}
