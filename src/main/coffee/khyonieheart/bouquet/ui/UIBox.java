package coffee.khyonieheart.bouquet.ui;

import coffee.khyonieheart.bouquet.engine.RenderContainer;
import coffee.khyonieheart.bouquet.engine.Renderer;

public class UIBox extends UIBaseElement
{
	public UIBox(
		int x,
		int y,
		int width,
		int height,
		int layer
	) {
		super(x, y, width, height, layer);
	}

	@Override
	public void render(RenderContainer container, Renderer renderer) 
	{
		this.renderChildren(container, renderer);
	}

	@Override
	public void tick(RenderContainer container, Renderer renderer) 
	{
		this.updateChildren(container, renderer);
	}
}
