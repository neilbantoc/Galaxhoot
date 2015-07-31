/*
 * GalaxhootFieldMesh.java
 * 
 * @author Neil Bantoc and James Plaras 
 */
package project161.galaxhoot.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import android.graphics.Bitmap;
import android.opengl.GLUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class GalaxhootFieldMesh.
 */
public class GalaxhootFieldMesh {

	/** The vertices. */
	protected float vertices[] = { -68.0f, 41.0f, -2.0f, // 0, Top Left
			-68.0f, -41.0f, -2.0f, // 1, Bottom Left
			68.0f, -41.0f, -2.0f, // 2, Bottom Right
			68.0f, 41.0f, -2.0f, // 3, Top Right
	};

	/** The texture coords. */
	float textureCoords[] = { 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f };

	/** The indices. */
	protected short[] indices = { 0, 1, 2, 2, 3, 0 };

	/** The vertex buffer. */
	protected FloatBuffer vertexBuffer;

	/** The texture buffer. */
	protected FloatBuffer textureBuffer;

	/** The index buffer. */
	protected ShortBuffer indexBuffer;

	/** The bitmap image. */
	Bitmap bitmapImage;

	/** The textures. */
	int[] textures = new int[1];

	/**
	 * Instantiates a new galaxhoot field mesh.
	 * 
	 * @param bitmapImage
	 *            the bitmap image
	 */
	public GalaxhootFieldMesh(Bitmap bitmapImage) {
		this.bitmapImage = bitmapImage;

		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		ByteBuffer byteBuf = ByteBuffer
				.allocateDirect(textureCoords.length * 4);
		byteBuf.order(ByteOrder.nativeOrder());
		textureBuffer = byteBuf.asFloatBuffer();
		textureBuffer.put(textureCoords);
		textureBuffer.position(0);

		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}

	/** The texture loaded. */
	boolean textureLoaded = false;

	/**
	 * Draw.
	 * 
	 * @param gl
	 *            the gl
	 */
	public void draw(GL10 gl) {

		if (!textureLoaded) {
			loadGLTexture(gl);
			textureLoaded = true;
		}
		// Enabled the vertices buffer for writing and to be used during
		// rendering.
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		// Specifies the location and data format of an array of vertex
		// coordinates to use when rendering.
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		// Disable the vertices buffer.

		/*
		 * // Disable face culling. gl.glDisable(GL10.GL_CULL_FACE); // OpenGL
		 * docs
		 */

		// Telling OpenGL to enable textures.
		gl.glEnable(GL10.GL_TEXTURE_2D);
		// Tell OpenGL where our texture is located.
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[0]);
		// Tell OpenGL to enable the use of UV coordinates.
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		// Telling OpenGL where our UV coordinates are.
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);

		// Disable the use of UV coordinates.
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		// Disable the use of textures.
		gl.glDisable(GL10.GL_TEXTURE_2D);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY); // OpenGL docs

	}

	/**
	 * Load gl texture.
	 * 
	 * @param gl
	 *            the gl
	 */
	private void loadGLTexture(GL10 gl) {
		// Generate one texture pointer...

		gl.glGenTextures(1, textures, 0);
		int textureId = textures[0];

		// ...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

		// Create Nearest Filtered Texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_LINEAR);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_LINEAR);

		// Different possible texture parameters, e.g. GL10.GL_CLAMP_TO_EDGE
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_REPEAT);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_WRAP_T,
				GL10.GL_REPEAT);

		// Use the Android GLUtils to specify a two-dimensional texture image
		// from our bitmap
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmapImage, 0);
	}
}
