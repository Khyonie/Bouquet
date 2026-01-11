package coffee.khyonieheart.bouquet.engine;

import coffee.khyonieheart.bouquet.font.Font;
import coffee.khyonieheart.bouquet.gl.GlFramebuffer;
import coffee.khyonieheart.bouquet.gl.GlMesh;
import coffee.khyonieheart.bouquet.gl.GlShader;
import coffee.khyonieheart.bouquet.gl.GlTexture;

public interface Renderer
{
	public void render(
		RenderContainer container
	);

	public void drawMesh(
		GlMesh mesh,
		GlShader shader
	);

	public void drawRectangle(
		int x,
		int y,
		int width,
		int height,
		int red,
		int green,
		int blue,
		int alpha
	);

	public default void drawRectangle(
		int x,
		int y,
		int width,
		int height,
		int color
	) {
		int red   = (color & 0xFF000000) >>> 24;
		int green = (color & 0x00FF0000) >>> 16;
		int blue  = (color & 0x0000FF00) >>> 8;
		int alpha = color & 0x000000FF;

		drawRectangle(x, y, width, height, red, green, blue, alpha);
	}

	public default void drawColoredRectangle(
		int x,
		int y,
		int width,
		int height,
		int topRightColor,
		int bottomRightColor,
		int bottomLeftColor,
		int topLeftColor
	) {
		this.drawColoredRectangle(x, y, width, height, topRightColor, bottomRightColor, bottomLeftColor, topLeftColor, 0f);
	}

	public void drawColoredRectangle(
		int x,
		int y,
		int width,
		int height,
		int topRightColor,
		int bottomRightColor,
		int bottomLeftColor,
		int topLeftColor,
		float radius
	);

	public void drawImage(
		int x,
		int y,
		GlTexture texture
	);

	public void drawString(
		String string, 
		int x, 
		int y, 
		Font font,
		int red,
		int green,
		int blue
	);

	public default void drawString(String string, int x, int y, Font font)
	{
		this.drawString(string, x, y, font, 255, 255, 255);
	}

	public default void drawString(
		String string,
		int x,
		int y,
		Font font,
		int color
	) {
		int red   = (color & 0xFF000000) >>> 24;
		int green = (color & 0x00FF0000) >>> 16;
		int blue  = (color & 0x0000FF00) >>> 8;

		this.drawString(string, x, y, font, red, green, blue);
	}

	public default void drawRoundedImage(
		int x,
		int y,
		GlTexture texture,
		float radius
	) {
		this.drawRoundedImage(x, y, texture.getWidth(), texture.getHeight(), texture, radius);
	}

	public void drawRoundedImage(
		int x,
		int y,
		int width,
		int height,
		GlTexture texture,
		float radius
	);

	public void drawRoundedRectangle(
		int x,
		int y,
		int width,
		int height,
		int red,
		int green,
		int blue,
		int alpha,
		float radius,
		int borderRed,
		int borderGreen,
		int borderBlue,
		float borderSize
	);

	public default void drawRoundedRectangle(
		int x,
		int y,
		int width,
		int height,
		int color,
		float radius
	) {
		this.drawRoundedRectangle(x, y, width, height, color, radius, color, 0);
	}

	public default void drawRoundedRectangle(
		int x,
		int y,
		int width,
		int height,
		int color,
		float radius,
		int borderColor,
		float borderSize
	) {
		int red   = (color & 0xFF000000) >>> 24;
		int green = (color & 0x00FF0000) >>> 16;
		int blue  = (color & 0x0000FF00) >>> 8;
		int alpha = (color & 0x000000FF);

		int bRed   = (borderColor & 0xFF000000) >>> 24;
		int bGreen = (borderColor & 0x00FF0000) >>> 16;
		int bBlue  = (borderColor & 0x0000FF00) >>> 8;

		drawRoundedRectangle(x, y, width, height, red, green, blue, alpha, radius, bRed, bGreen, bBlue, borderSize);
	}

	public void drawFramebuffer(int x, int y, int width, int height, GlFramebuffer framebuffer);

	//
	// Profiling tools
	//
	public int getDrawCalls();

	public void resetDrawCalls();
}
