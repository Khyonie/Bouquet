package coffee.khyonieheart.bouquet.gl;

import org.lwjgl.opengl.GL30;

import coffee.khyonieheart.bouquet.engine.RenderContainer;
import coffee.khyonieheart.bouquet.engine.ResolutionUpdateSubscriber;

public class GlFramebuffer implements AutoCloseable, ResolutionUpdateSubscriber
{
	private int handle;
	private int textureHandle;

	public GlFramebuffer(
		RenderContainer container
	) {
		this.handle = GL30.glGenFramebuffers();

		container.addResolutionSubscriber(this);

		this.textureHandle = GL30.glGenTextures();
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, this.textureHandle);
		GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA, container.getWidth(), container.getHeight(), 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, 0);
		GL30.glTexParameterIi(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
		GL30.glTexParameterIi(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);

		GL30.glFramebufferTexture2D(this.handle, GL30.GL_COLOR_ATTACHMENT0, GL30.GL_TEXTURE_2D, this.textureHandle, 0);
	}

	public void bind()
	{
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, this.handle);
	}

	public static void unbind()
	{
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
	}

	public int getGlTexture()
	{
		return this.textureHandle;
	}

	@Override
	public void close() throws Exception 
	{
		GL30.glDeleteFramebuffers(this.handle);
		GL30.glDeleteTextures(textureHandle);
	}

	@Override
	public void update(int x, int y) 
	{
		//GL30.glBindTexture(GL30.GL_TEXTURE_2D, this.textureHandle);
		//GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RGBA, x, y, 0, GL30.GL_RGBA, GL30.GL_UNSIGNED_BYTE, 0);
	}
}
