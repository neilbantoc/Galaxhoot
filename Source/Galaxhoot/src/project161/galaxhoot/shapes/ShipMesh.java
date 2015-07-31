/*
 * ShipMesh.java
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
 * The Class ShipMesh.
 */
public class ShipMesh {

	/** The vertices. */
	private float vertices[] = { 0f, 3f, 0f, // 0, Nguso
			0f, -3f, 1f, // 1, Diamond top
			-2f, -3f, 0f, // 2, Diamond left
			0f, -3f, -1f, // 3, Diamond bottom
			2f, -3f, 0f, // 4, Diamond right

			1.5f, -1.5f, 0f, 5f, -3f, 0f,

			-1.5f, -1.5f, 0f, -5f, -3f, 0f, };

	/** The indices. */
	private short[] indices = { 0, 1, 2, 0, 1, 4, 1, 2, 4, 2, 3, 4, 0, 2, 3, 0,
			4, 3, 5, 6, 4, 7, 8, 2 };

	/** The vertex buffer. */
	private FloatBuffer vertexBuffer;

	/** The index buffer. */
	private ShortBuffer indexBuffer;

	/**
	 * Instantiates a new ship mesh.
	 */
	public ShipMesh() {
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

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		gl.glLineWidth(0.5f);
		gl.glColor4f(0, 0, 0, .5f);
		gl.glDrawElements(GL10.GL_LINE_LOOP, indices.length,
				GL10.GL_UNSIGNED_SHORT, indexBuffer);
		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
	}
}
