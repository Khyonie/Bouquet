package coffee.khyonieheart.bouquet.gl;

import java.io.File;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;

public class GlTexture implements AutoCloseable
{
	private int handle;
	private int width, height, channels;

	public GlTexture(
		String imagePath,
		WrapStrategy wrapping,
		FilterStrategy filtering
	) {
		int[] widthBuffer = new int[1];
		int[] heightBuffer = new int[1];
		int[] channelBuffer = new int[1];
		
		if (!(new File(imagePath).exists()))
		{
			throw new NullPointerException("No such file \"" + imagePath + "\"");
		}

		// Image loading
		ByteBuffer data = STBImage.stbi_load(imagePath, widthBuffer, heightBuffer, channelBuffer, 0);

		this.width = widthBuffer[0];
		this.height = heightBuffer[0];
		this.channels = channelBuffer[0];

		// Texture buffering
		this.handle = GL30.glGenTextures();
		this.bind();
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, wrapping.getEnumBinding());
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_T, wrapping.getEnumBinding());
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, filtering.getEnumBinding());
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, filtering.getEnumBinding());

		GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA, width, height, 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, data);
		GL30.glGenerateMipmap(GL30.GL_TEXTURE_2D);

		STBImage.stbi_image_free(data); // Cleanup
	}

	public void bind()
	{
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, this.handle);
	}

	public static void unbind()
	{
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, 0);
	}

	public int getWidth() 
	{
		return width;
	}

	public int getHeight() 
	{
		return height;
	}

	public int getChannels() 
	{
		return channels;
	}

	@Override
	public void close() throws Exception 
	{
		if (this.handle == 0)
		{
			return;
		}
		GL30.glDeleteTextures(this.handle);
	}

	/**
	 * Bindings for texture-wrapping GLenums.
	 */
	public static enum WrapStrategy implements GlEnumBinding
	{
		REPEAT,
		MIRRORED_REPEAT,
		CLAMP_TO_EDGE,
		CLAMP_TO_BORDER
		;

		@Override
		public int getEnumBinding() 
		{
			return switch (this) {
				case REPEAT -> GL30.GL_REPEAT;
				case CLAMP_TO_BORDER -> GL30.GL_CLAMP_TO_BORDER;
				case CLAMP_TO_EDGE -> GL30.GL_CLAMP_TO_EDGE;
				case MIRRORED_REPEAT -> GL30.GL_MIRRORED_REPEAT;
			};
		}
	}

	/**
	 * Bindings for texture-filtering GLenums.
	 */
	public static enum FilterStrategy implements GlEnumBinding 
	{
		NEAREST,
		LINEAR
		;

		@Override
		public int getEnumBinding() 
		{
			return switch (this) {
				case NEAREST -> GL30.GL_NEAREST;
				case LINEAR -> GL30.GL_LINEAR;
			};
		}
	}
}
