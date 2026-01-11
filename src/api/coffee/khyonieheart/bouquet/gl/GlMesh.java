package coffee.khyonieheart.bouquet.gl;

import org.lwjgl.opengl.GL30;

public class GlMesh implements AutoCloseable
{
	private int vaoHandle;
	private int indexCount;

	public GlMesh(
		float[] data
	) {
		this.indexCount = data.length / 4;

		System.out.print("Creating new mesh... ");
		this.vaoHandle = GL30.glGenVertexArrays();
		int vboHandle = GL30.glGenBuffers();

		System.out.print("Created buffers... ");
		GL30.glBindVertexArray(this.vaoHandle);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, vboHandle);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, data, GL30.GL_STATIC_DRAW);
		System.out.print("Buffers uploaded... ");

		GL30.glVertexAttribPointer(0, 2, GL30.GL_FLOAT, false, 4 * Float.BYTES, 0);
		GL30.glEnableVertexAttribArray(0);
		GL30.glVertexAttribPointer(1, 2, GL30.GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
		GL30.glEnableVertexAttribArray(1);
		System.out.print("Set attribute pointers... ");

		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, 0);
		GL30.glBindVertexArray(0);
		System.out.println("Done, ID is " + this.vaoHandle);
	}

	public int getVertexCount()
	{
		return this.indexCount;
	}

	public void bind()
	{
		GL30.glBindVertexArray(this.vaoHandle);
	}

	public static void unbind()
	{
		GL30.glBindVertexArray(0);
	}

	@Override
	public void close() throws Exception 
	{
		System.out.println("Deleting mesh " + this.vaoHandle);
		GL30.glDeleteVertexArrays(this.vaoHandle);
	}
}
