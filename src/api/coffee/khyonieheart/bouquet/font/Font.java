package coffee.khyonieheart.bouquet.font;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTBakedChar.Buffer;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class Font implements AutoCloseable
{
	private int fontPoint;

	private int textureHandle;

	public static final int TEXTURE_WIDTH = 1024;
	public static final int TEXTURE_HEIGHT = 512;
	private static final int ASCII_SPACE = (int) ' ';

	private Glyph[] glyphData = new Glyph['~' + 1];

	private float scale;
	private int ascent, descent, lineGap, fontHeight, fontHeightPx; 
	private STBTTFontinfo fontInfo;

	public Font(
		String path,
		int fontPoint
	) {
		if (fontPoint > 128)
		{
			System.err.println("Font \"" + path + "\" is using an unsupported font size " + fontPoint + ". Glyphs may be clipped. Consider changing this to 128 or lower.");
		}

		this.fontPoint = fontPoint;
		byte[] fileBytes;
		try {
			fileBytes = Files.readAllBytes(new File(path).toPath());
		} catch (IOException e) {
			throw new IllegalStateException("Failed to load font \"" + path + "\"", e);
		}

		ByteBuffer bytes = MemoryUtil.memAlloc(fileBytes.length);
		bytes.put(fileBytes);
		bytes.flip();

		Buffer charInfo = STBTTBakedChar.malloc(127);
		ByteBuffer bitmapBuffer = BufferUtils.createByteBuffer(TEXTURE_WIDTH * TEXTURE_HEIGHT);
		STBTruetype.stbtt_BakeFontBitmap(bytes, fontPoint, bitmapBuffer, TEXTURE_WIDTH, TEXTURE_HEIGHT, ASCII_SPACE, charInfo);

		for (char c = ' '; c <= '~'; c++)
		{
			this.glyphData[c] = new Glyph(charInfo.get(c - 32), this, c);
		}
		charInfo.position(0);

		this.textureHandle = GL30.glGenTextures();
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, this.textureHandle);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_S, GL30.GL_REPEAT);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_WRAP_T, GL30.GL_REPEAT);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MIN_FILTER, GL30.GL_LINEAR);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL30.GL_TEXTURE_MAG_FILTER, GL30.GL_LINEAR);
		// Swizzle settings, since OpenGL core doesn't like alpha-only images
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL33.GL_TEXTURE_SWIZZLE_R, GL30.GL_ONE);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL33.GL_TEXTURE_SWIZZLE_G, GL30.GL_ONE);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL33.GL_TEXTURE_SWIZZLE_B, GL30.GL_ONE);
		GL30.glTexParameteri(GL30.GL_TEXTURE_2D, GL33.GL_TEXTURE_SWIZZLE_A, GL30.GL_RED);
		GL30.glTexImage2D(GL30.GL_TEXTURE_2D, 0, GL30.GL_RED, TEXTURE_WIDTH, TEXTURE_HEIGHT, 0, GL30.GL_RED, GL30.GL_UNSIGNED_BYTE, bitmapBuffer);

		try (MemoryStack stack = MemoryStack.stackPush())
		{
			this.fontInfo = STBTTFontinfo.malloc(stack);
			STBTruetype.stbtt_InitFont(fontInfo, bytes);
			
			this.scale = STBTruetype.stbtt_ScaleForPixelHeight(fontInfo, this.fontPoint);
			//this.scale = 1.0f;
			int[] ascentReference = new int[1], descentReference = new int[1], lineGapReference = new int[1];
			STBTruetype.stbtt_GetFontVMetrics(fontInfo, ascentReference, descentReference, lineGapReference);

			this.ascent = Math.round(ascentReference[0]);
			this.descent = Math.round(descentReference[0]);
			this.lineGap = Math.round(lineGapReference[0]);
			this.fontHeight = Math.round(this.ascent - this.descent);
			this.fontHeightPx = Math.round(fontHeight * scale);

			for (int platform = 0; platform < 4; platform++)
			{
				for (int encoding = 0; encoding < 10; encoding++)
				{
					for (int i = 0; i < 9; i++)
					{
						ByteBuffer fontFamilyBytes = STBTruetype.stbtt_GetFontNameString(fontInfo, platform, encoding, STBTruetype.STBTT_MS_LANG_ENGLISH, i);
						if (fontFamilyBytes == null)
						{
							continue;
						}
						byte[] byteArray = new byte[fontFamilyBytes.limit()];
						fontFamilyBytes.get(byteArray);
						String str = new String(byteArray, StandardCharsets.UTF_16BE);
						System.out.println("Font table " + i + " (platform: " + platform + ", encoding: " + encoding + "): " + str);
					}
				}
			}
		}
	}

	public int getStringLength(
		String string
	) {
		float length = 0;

		for (int i = 0; i < string.length(); i++)
		{
			char current = string.charAt(i);

			if (current < 32 || current > 128)
			{
				length += 15f;
				continue;
			}

			length += this.getGlyph(current).getXAdvance();
		}

		return (int) length;
	}

	public void bind()
	{
		GL30.glBindTexture(GL30.GL_TEXTURE_2D, this.textureHandle);
	}

	@Override
	public void close() throws Exception 
	{
		GL30.glDeleteTextures(this.textureHandle);
	}

	public int getFontPoint() 
	{
		return fontPoint;
	}

	public float getScale()
	{
		return this.scale;
	}

	public int getKerning(
		char charA,
		char charB
	) {
		return Math.round(STBTruetype.stbtt_GetCodepointKernAdvance(this.fontInfo, charA, charB));
	}

	public int getAscent() 
	{
		return ascent;
	}

	public int getDescent() 
	{
		return descent;
	}

	public int getLineGap() 
	{
		return lineGap;
	}

	public int getFontHeight() 
	{
		return fontHeight;
	}

	public int getFontHeightPx()
	{
		return this.fontHeightPx;
	}

	public Glyph getGlyph(
		char c
	) {
		return this.glyphData[c];
	}
}
