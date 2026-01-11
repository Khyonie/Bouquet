package coffee.khyonieheart.bouquet.ui;

import coffee.khyonieheart.bouquet.engine.RenderContainer;
import coffee.khyonieheart.bouquet.engine.Renderer;
import coffee.khyonieheart.bouquet.font.Font;

public class UIText extends UIBaseElement
{
	private String text;
	private int color;
	private Font font;

	public UIText(
		String text,
		int x,
		int y,
		Font font,
		int color,
		int layer
	) {
		super(x, y, font.getStringLength(text), Math.round(font.getFontHeight() * font.getScale()), layer);
		this.text = text;
		this.color = color;
		this.font = font;
	}

	@Override
	public int getWidth() 
	{
		return this.font.getStringLength(this.text);
	}

	@Override
	public int getHeight() 
	{
		return Math.round(this.font.getFontHeight() * this.font.getScale());
	}

	@Override
	public void render(RenderContainer container, Renderer renderer) 
	{
		renderer.drawString(this.text, this.getEffectiveX(), this.getEffectiveY(), this.font, this.color);
		this.renderChildren(container, renderer);
	}

	@Override
	public void tick(RenderContainer container, Renderer renderer) 
	{
		this.updateChildren(container, renderer);
	}
}
