package coffee.khyonieheart.bouquet.ui;

import coffee.khyonieheart.bouquet.engine.RenderContainer;
import coffee.khyonieheart.bouquet.engine.Renderer;
import coffee.khyonieheart.bouquet.gl.GlTexture;

public class UIImage extends UIBaseElement
{
	private GlTexture texture;
	private float radius;

	public UIImage(
		GlTexture texture,
		int x,
		int y,
		int width,
		int height,
		int layer
	) {
		this(texture, x, y, width, height, 0f, layer);
	}

	public UIImage(
		GlTexture texture,
		int x,
		int y,
		int width,
		int height,
		float radius,
		int layer
	) {
		super(x, y, width, height, layer);
		this.texture = texture;
		this.radius = radius;
	}

	@Override
	public void render(RenderContainer container, Renderer renderer) 
	{
		renderer.drawRoundedImage(this.getEffectiveX(), this.getEffectiveY(), this.getWidth(), this.getHeight(), this.texture, radius);
		this.renderChildren(container, renderer);
	}

	@Override
	public void tick(RenderContainer container, Renderer renderer) 
	{
		this.updateChildren(container, renderer);
	}

	@Override
	public void changeState(UIState state) 
	{
		switch (state)
		{
			case CLICKED -> {}
			case HOVER -> {}
			case IDLE -> {}
			case INACTIVE -> {}
		}
	}
}
