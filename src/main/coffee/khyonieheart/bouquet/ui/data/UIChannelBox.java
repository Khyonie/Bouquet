package coffee.khyonieheart.bouquet.ui.data;

import coffee.khyonieheart.bouquet.engine.RenderContainer;
import coffee.khyonieheart.bouquet.engine.Renderer;
import coffee.khyonieheart.bouquet.ui.UIBaseElement;
import coffee.khyonieheart.bouquet.ui.UIState;

public class UIChannelBox extends UIBaseElement
{
	private RenderContainer container;
	private UIState state = UIState.IDLE;

	public UIChannelBox(
		RenderContainer container
	) {
		super(0, 0, 300, container.getHeight(), 0);
		this.container = container;
	}

	public int getHeight()
	{
		return this.container.getHeight();
	}

	@Override
	public void render(RenderContainer container, Renderer renderer) 
	{
		renderer.drawRectangle(this.getEffectiveX(), this.getEffectiveY(), this.getWidth(), this.getHeight(), 0x363636FF);
		renderer.drawRectangle(this.getEffectiveX() + this.getWidth() - 1, this.getEffectiveY(), 1, this.getHeight(), 0x101010FF);
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
