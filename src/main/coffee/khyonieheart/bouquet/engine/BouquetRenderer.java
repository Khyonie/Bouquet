package coffee.khyonieheart.bouquet.engine;

import java.nio.FloatBuffer;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL30;

import coffee.khyonieheart.bouquet.data.Message;
import coffee.khyonieheart.bouquet.data.User;
import coffee.khyonieheart.bouquet.font.Font;
import coffee.khyonieheart.bouquet.font.Glyph;
import coffee.khyonieheart.bouquet.gl.GlFramebuffer;
import coffee.khyonieheart.bouquet.gl.GlMesh;
import coffee.khyonieheart.bouquet.gl.GlShader;
import coffee.khyonieheart.bouquet.gl.GlTexture;
import coffee.khyonieheart.bouquet.gl.GlTexture.FilterStrategy;
import coffee.khyonieheart.bouquet.gl.GlTexture.WrapStrategy;
import coffee.khyonieheart.bouquet.math.Matrix4;
import coffee.khyonieheart.bouquet.ui.AnchorPoint;
import coffee.khyonieheart.bouquet.ui.UIElement;
import coffee.khyonieheart.bouquet.ui.data.UIChannelBox;
import coffee.khyonieheart.bouquet.ui.data.UIMessageBox;
import coffee.khyonieheart.bouquet.ui.data.UIServerBar;

public class BouquetRenderer implements Renderer
{
	// Shaders
	private GlShader textureShader, roundedTextureShader, colorShader, roundedShader, glyphShader, multiColorShader;

	// Meshes
	private GlMesh rectangleMesh;
	private int textMeshVAO, textMeshVBO;
	private FloatBuffer textMeshVertices;

	// Matrices
	private Matrix4 orthographic;

	private int drawCalls = 0;

	private UIServerBar serverBar;

	public void init(
		RenderContainer container
	) {
		this.orthographic = Matrix4.orthographic(0, container.getWidth(), 0, container.getHeight(), -1f, 1f);
		GL30.glViewport(0, 0, container.getWidth(), container.getHeight());
		GL30.glClearColor(0f, 0f, 0f, 1f);
		container.addResolutionSubscriber((width, height) -> {
			this.orthographic = Matrix4.orthographic(0, width, 0, height, -1f, 1f);
		});

		this.colorShader = GlShader.loadInternal("shader/vertex/identity.glsl", "shader/fragment/color.glsl");
		this.roundedShader = GlShader.loadInternal("shader/vertex/identity.glsl", "shader/fragment/rounded.glsl");
		this.roundedTextureShader = GlShader.loadInternal("shader/vertex/identity.glsl", "shader/fragment/rounded_texture.glsl");
		this.textureShader = GlShader.loadInternal("shader/vertex/identity.glsl", "shader/fragment/texture.glsl");
		this.glyphShader = GlShader.loadInternal("shader/vertex/identity.glsl", "shader/fragment/glyph.glsl");
		this.multiColorShader = GlShader.loadInternal("shader/vertex/identity.glsl", "shader/fragment/multicolor_rectangle.glsl");

		this.rectangleMesh = new GlMesh(
			new float[] { // Vertices/texcoords
				1.0f, 1.0f, 1f, 1f,
				1.0f, 0.0f, 1f, 0f,
				0.0f, 0.0f, 0f, 0f,
				1.0f, 1.0f, 1f, 1f,
				0.0f, 0.0f, 0f, 0f,
				0.0f, 1.0f, 0f, 1f
			}
		);

		//
		// Dynamic meshes
		//
		this.textMeshVertices = BufferUtils.createFloatBuffer(1024 * 6 * 4);
		this.textMeshVAO = GL30.glGenVertexArrays();
		this.textMeshVBO = GL30.glGenBuffers();
		GL30.glBindVertexArray(this.textMeshVAO);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, this.textMeshVBO);
		GL30.glBufferData(GL30.GL_ARRAY_BUFFER, 1024 * 6 * 4 * Float.BYTES, GL30.GL_DYNAMIC_DRAW);

		GL30.glVertexAttribPointer(0, 2, GL30.GL_FLOAT, false, 4 * Float.BYTES, 0);
        GL30.glEnableVertexAttribArray(0);
        GL30.glVertexAttribPointer(1, 2, GL30.GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
        GL30.glEnableVertexAttribArray(1);

		// Dummy messages
		GlTexture pfpA = new GlTexture("star.png", WrapStrategy.CLAMP_TO_EDGE, FilterStrategy.NEAREST);
		GlTexture pfpB = new GlTexture("star.png", WrapStrategy.CLAMP_TO_EDGE, FilterStrategy.NEAREST);
		GlTexture pfpC = new GlTexture("star.png", WrapStrategy.CLAMP_TO_EDGE, FilterStrategy.NEAREST);
		GlTexture pfpD = new GlTexture("star.png", WrapStrategy.CLAMP_TO_EDGE, FilterStrategy.NEAREST);

		User userA = new User("Matthew", pfpA, 0x83CC85FF);
		User userB = new User("Nicole", pfpB, 0xB083CCFF);
		User userC = new User("Sandra", pfpC, 0x83B7CCFF);
		User userD = new User("Russ", pfpD, 0xCC8385FF);

		User[] users = new User[] { userA, userB, userC, userD };

		List<Message> messages = new ArrayList<>();
		Random random = new Random();
		for (int i = 0; i < 20; i++)
		{
			User selected = users[random.nextInt(users.length)];

			messages.add(new Message(selected, "Please do not the cat", OffsetDateTime.now()));
		}

		// UI
		this.serverBar = new UIServerBar(container);
		UIManager.UI_ROOT = serverBar; // TODO This is gross

		UIChannelBox channelBox = new UIChannelBox(container);
		UIElement messageBox = new UIMessageBox(0, 0, messages, container);

		this.serverBar.anchor(channelBox.setAnchorPoint(AnchorPoint.BOTTOM_EAST));
		channelBox.anchor(messageBox.setAnchorPoint(AnchorPoint.BOTTOM_EAST));
	}

	@Override
	public void render(RenderContainer container) 
	{
		this.resetDrawCalls();
		GLFW.glfwSwapBuffers(container.getHandle());
		GL30.glClear(GL30.GL_COLOR_BUFFER_BIT);

		this.serverBar.render(container, this);
	}

	//############################## 
	// Rendering
	//############################## 

	@Override
	public void drawMesh(GlMesh mesh, GlShader shader) 
	{
		mesh.bind();
		shader.bind();

		GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, mesh.getVertexCount());
		this.drawCalls++;
	}

	@Override
	public void drawRectangle(int x, int y, int width, int height, int red, int green, int blue, int alpha) 
	{
		this.colorShader.bind()
			.setUniformMat4("model", Matrix4.ofTransform(x, y, width, height), true)
			.setUniformMat4("projection", this.orthographic, true)
			.setUniformVec4("color", (float) red / 255f, (float) green / 255f, (float) blue / 255f, (float) alpha / 255f);
		this.rectangleMesh.bind();

		GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, this.rectangleMesh.getVertexCount());
		this.drawCalls++;
	}

	@Override
	public void drawImage(int x, int y, GlTexture texture) 
	{
		this.rectangleMesh.bind();
		texture.bind();
		this.textureShader.bind()
			.setUniformMat4("model", Matrix4.ofTransform(x, y, texture.getWidth(), texture.getHeight()), true)
			.setUniformMat4("projection", this.orthographic, true);

		GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, this.rectangleMesh.getVertexCount());
		this.drawCalls++;
	}

	@Override
	public void drawString(String string, int x, int y, Font font, int red, int green, int blue) 
	{
		this.textMeshVertices.clear();

		float charX = x;
		for (int i = 0; i < string.length(); i++)
		{
			char current = string.charAt(i);
			if (current < 32 || current > 128)
			{
				charX += 15f;
				continue;
			}

			Glyph glyph = font.getGlyph(current);

			float x0 = charX + glyph.getXOffset();
			float y0 = y + (font.getAscent() * font.getScale()) + glyph.getYOffset();
			float x1 = x0 + (glyph.getX1() - glyph.getX0());
			float y1 = y0 + (glyph.getY1() - glyph.getY0());
			
			float s0 = glyph.getX0() / (float) Font.TEXTURE_WIDTH;
            float t0 = glyph.getY0() / (float) Font.TEXTURE_HEIGHT;
            float s1 = glyph.getX1() / (float) Font.TEXTURE_WIDTH;
            float t1 = glyph.getY1() / (float) Font.TEXTURE_HEIGHT;

			float[] coords = new float[]{
                x0, y0, s0, t0,
                x1, y0, s1, t0,
                x1, y1, s1, t1,
                x1, y1, s1, t1,
                x0, y1, s0, t1,
                x0, y0, s0, t0
            };

			textMeshVertices.put(coords);

			charX += glyph.getXAdvance();
		}

		textMeshVertices.flip();
		glyphShader.bind()
			.setUniformVec3("color", (float) red / 255f, (float) green / 255f, (float) blue / 255f)
			.setUniformMat4("model", Matrix4.identity(), true)
			.setUniformMat4("projection", this.orthographic, true);

		GL30.glActiveTexture(GL30.GL_TEXTURE0);
		font.bind();

		GL30.glBindVertexArray(this.textMeshVAO);
		GL30.glBindBuffer(GL30.GL_ARRAY_BUFFER, this.textMeshVBO);
		GL30.glBufferSubData(GL30.GL_ARRAY_BUFFER, 0, this.textMeshVertices);
		GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, this.textMeshVertices.limit() / 4);
		this.drawCalls++;
	}

	@Override
	public void drawRoundedRectangle(int x, int y, int width, int height, int red, int green, int blue, int alpha, float radius, int bRed, int bGreen, int bBlue, float borderSize) {
		this.rectangleMesh.bind();
		this.roundedShader.bind()
			.setUniformMat4("model", Matrix4.ofTransform(x, y, width, height), true)
			.setUniformMat4("projection", this.orthographic, true)
			.setUniformVec2("rectSize", (float) width, (float) height)
			.setUniformFloat1("radius", radius)
			.setUniformFloat1("borderSize", borderSize)
			.setUniformVec3("borderColor", (float) bRed / 255f, (float) bGreen / 255f, (float) bBlue / 255f)
			.setUniformVec4("color", (float) red / 255f, (float) green / 255f, (float) blue / 255f, (float) alpha / 255f);

		GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, this.rectangleMesh.getVertexCount());
		this.drawCalls++;
	}

	@Override
	public void drawRoundedImage(int x, int y, int width, int height, GlTexture texture, float radius) 
	{
		this.rectangleMesh.bind();
		texture.bind();
		this.roundedTextureShader.bind()
			.setUniformMat4("model", Matrix4.ofTransform(x, y, width, height), true)
			.setUniformMat4("projection", this.orthographic, true)
			.setUniformVec2("rectSize", width, height)
			.setUniformFloat1("radius", radius);

		GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, this.rectangleMesh.getVertexCount());
		this.drawCalls++;
	}

	@Override
	public void drawColoredRectangle(int x, int y, int width, int height, int topRightColor, int bottomRightColor, int bottomLeftColor, int topLeftColor, float radius) 
	{
		float trRed = (float) (topRightColor >>> 24) / 255f;
		float trGreen = (float) ((topRightColor & 0x00FF0000) >>> 16) / 255f;
		float trBlue = (float) ((topRightColor & 0x0000FF00) >>> 8) / 255f;
		float trAlpha = (float) (topRightColor & 0x000000FF) / 255f;
		float brRed = (float) (bottomRightColor >>> 24) / 255f;
		float brGreen = (float) ((bottomRightColor & 0x00FF0000) >>> 16) / 255f;
		float brBlue = (float) ((bottomRightColor & 0x0000FF00) >>> 8) / 255f;
		float brAlpha = (float) (bottomRightColor & 0x000000FF) / 255f;
		float blRed = (float) (bottomLeftColor >>> 24) / 255f;
		float blGreen = (float) ((bottomLeftColor & 0x00FF0000) >>> 16) / 255f;
		float blBlue = (float) ((bottomLeftColor & 0x0000FF00) >>> 8) / 255f;
		float blAlpha = (float) (bottomLeftColor & 0x000000FF) / 255f;
		float tlRed = (float) (topLeftColor >>> 24) / 255f;
		float tlGreen = (float) ((topLeftColor & 0x00FF0000) >>> 16) / 255f;
		float tlBlue = (float) ((topLeftColor & 0x0000FF00) >>> 8) / 255f;
		float tlAlpha = (float) (topLeftColor & 0x000000FF) / 255f;

		this.rectangleMesh.bind();
		multiColorShader.bind()
			.setUniformMat4("model", Matrix4.ofTransform(x, y, width, height), true)
			.setUniformMat4("projection", this.orthographic, true)
			.setUniformVec4("tlColor", tlRed, tlGreen, tlBlue, tlAlpha)
			.setUniformVec4("trColor", trRed, trGreen, trBlue, trAlpha)
			.setUniformVec4("blColor", blRed, blGreen, blBlue, blAlpha)
			.setUniformVec4("brColor", brRed, brGreen, brBlue, brAlpha)
			.setUniformVec2("rectSize", width, height)
			.setUniformFloat1("radius", radius);
		
		GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, this.rectangleMesh.getVertexCount());
		this.drawCalls++;
	}

	@Override
	public int getDrawCalls() 
	{
		return this.drawCalls;
	}

	@Override
	public void resetDrawCalls() 
	{
		this.drawCalls = 0;
	}

	@Override
	public void drawFramebuffer(int x, int y, int width, int height, GlFramebuffer framebuffer) 
	{
		this.rectangleMesh.bind();
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, framebuffer.getGlTexture());
		this.textureShader.bind()
			.setUniformMat4("model", Matrix4.ofTransform(x, y, width, height), true)
			.setUniformMat4("projection", this.orthographic, true);

		GL30.glDrawArrays(GL30.GL_TRIANGLES, 0, this.rectangleMesh.getVertexCount());
		this.drawCalls++;
	}
}
