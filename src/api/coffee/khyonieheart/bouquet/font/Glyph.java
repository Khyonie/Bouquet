package coffee.khyonieheart.bouquet.font;

import org.lwjgl.stb.STBTTBakedChar;

public class Glyph
{
	private final Font font;
	private final char backedChar;

	private float x0, x1, y0, y1;
	private float xOffset, yOffset;
	private float xAdvance;

	public Glyph(
		STBTTBakedChar charData,
		Font font,
		char backedChar
	) {
		this.font = font;
		this.backedChar = backedChar;

		this.x0 = charData.x0();
		this.x1 = charData.x1();
		this.y0 = charData.y0();
		this.y1 = charData.y1();

		this.xOffset = charData.xoff();
		this.yOffset = charData.yoff();

		this.xAdvance = charData.xadvance();
	}

	public Font getFont() {
		return font;
	}

	public float getX0() {
		return x0;
	}

	public float getX1() {
		return x1;
	}

	public float getY0() {
		return y0;
	}

	public float getY1() {
		return y1;
	}

	public float getXOffset() {
		return xOffset;
	}

	public float getYOffset() 
	{
		return yOffset;
	}

	public float getXAdvance() 
	{
		return xAdvance;
	}

	public char getChar()
	{
		return this.backedChar;
	}
}

