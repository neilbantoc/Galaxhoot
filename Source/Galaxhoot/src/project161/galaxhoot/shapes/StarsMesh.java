/*
 * StarsMesh.java
 * 
 * @author Neil Bantoc and James Plaras 
 */
package project161.galaxhoot.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

// TODO: Auto-generated Javadoc
/**
 * The Class StarsMesh.
 */
public class StarsMesh {

	/** The vertices. */
	protected float vertices[];

	/** The indices. */
	protected short[] indices;

	/** The vertex buffer. */
	protected FloatBuffer vertexBuffer;

	/** The index buffer. */
	protected ShortBuffer indexBuffer;

	/** The star_count. */
	private int star_count = 500;

	/**
	 * Instantiates a new stars mesh.
	 */
	public StarsMesh() {

		vertices = new float[star_count * 3];
		indices = new short[star_count];

		Random gen = new Random(System.currentTimeMillis());

		for (int x = 0; x < star_count * 3; x += 3) {
			int rand;

			while ((rand = gen.nextInt(250) - 125) == 0)
				;
			vertices[x] = rand;

			while ((rand = gen.nextInt(130) - 65) == 0)
				;
			vertices[x + 1] = rand;

			while ((rand = gen.nextInt(200) - 100) == 0)
				;
			vertices[x + 2] = rand;

			// vertices[x+2] = 0;

			indices[x / 3] = (short) ((short) x / 3);
		}

		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		ByteBuffer ibb = ByteBuffer.allocateDirect(indices.length * 2);
		ibb.order(ByteOrder.nativeOrder());
		indexBuffer = ibb.asShortBuffer();
		indexBuffer.put(indices);
		indexBuffer.position(0);
	}

	/**
	 * Draw.
	 * 
	 * @param gl
	 *            the gl
	 */
	public void draw(GL10 gl) {
		/*
		 * // Counter-clockwise winding. gl.glFrontFace(GL10.GL_CCW); // Enable
		 * face culling. gl.glEnable(GL10.GL_CULL_FACE); // What faces to remove
		 * with the face culling. gl.glCullFace(GL10.GL_BACK);
		 */

		// Enabled the vertices buffer for writing and to be used during
		// rendering.
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		// Specifies the location and data format of an array of vertex
		// coordinates to use when rendering.
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

		gl.glDrawElements(GL10.GL_POINTS, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);

		// Disable the vertices buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY); // OpenGL docs

		/*
		 * // Disable face culling. gl.glDisable(GL10.GL_CULL_FACE); // OpenGL
		 * docs
		 */
	}
}
