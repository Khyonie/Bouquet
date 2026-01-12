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
