/*
 * BulletMesh.java
 * 
 * @author Neil Bantoc and James Plaras 
 */
package project161.galaxhoot.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

// TODO: Auto-generated Javadoc
/**
 * The Class BulletMesh.
 */
public class BulletMesh {

	/** The vertices. */
	protected float vertices[] = { -0.5f, 1.0f, 0.0f, // 0, Top Left
			-0.5f, -1.0f, 0.0f, // 1, Bottom Left
			0.5f, -1.0f, 0.0f, // 2, Bottom Right
			0.5f, 1.0f, 0.0f, // 3, Top Right
	};

	/** The indices. */
	protected short[] indices = { 0, 1, 2, 0, 2, 3 };

	/** The vertex buffer. */
	protected FloatBuffer vertexBuffer;

	/** The index buffer. */
	protected ShortBuffer indexBuffer;

	/**
	 * Instantiates a new bullet mesh.
	 */
	public BulletMesh() {
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

		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);

		// Disable the vertices buffer.
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY); // OpenGL docs

		/*
		 * // Disable face culling. gl.glDisable(GL10.GL_CULL_FACE); // OpenGL
		 * docs
		 */
	}

}
