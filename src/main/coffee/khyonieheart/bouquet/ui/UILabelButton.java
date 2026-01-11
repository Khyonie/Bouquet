package coffee.khyonieheart.bouquet.ui;

import coffee.khyonieheart.bouquet.engine.RenderContainer;
import coffee.khyonieheart.bouquet.engine.Renderer;
import coffee.khyonieheart.bouquet.engine.UIManager;
import coffee.khyonieheart.bouquet.font.Font;

public class UILabelButton extends UIBaseElement
{
	private Font font;

	private String label;
	private int radius;

	public UILabelButton(
		String label,
		int x, 
		int y,
		int width,
		int height,
		Font font,
		int layer
	) {
		super(x, y, width, height, layer);
		this.label = label;
		this.font = font;
	}

	@Override
	public void render(RenderContainer container, Renderer renderer) 
	{
		int stateFg = switch (this.getState()) {
			case IDLE -> UIStyle.getCurrentStyle().getLabelButtonForegroundIdle();
			case HOVER -> UIStyle.getCurrentStyle().getLabelButtonForegroundIdle();
			case CLICKED -> UIStyle.getCurrentStyle().getLabelButtonForegroundIdle();
			case INACTIVE -> UIStyle.getCurrentStyle().getLabelButtonForegroundIdle();
		};

		int stateBg = switch (this.getState()) {
			case IDLE -> UIStyle.getCurrentStyle().getLabelButtonBackgroundIdle();
			case HOVER -> UIStyle.getCurrentStyle().getLabelButtonBackgroundIdle();
			case CLICKED -> UIStyle.getCurrentStyle().getLabelButtonBackgroundIdle();
			case INACTIVE -> UIStyle.getCurrentStyle().getLabelButtonBackgroundIdle();
		};

		renderer.drawRoundedRectangle(this.getEffectiveX(), this.getEffectiveY(), this.getWidth(), this.getHeight(), stateBg, radius);
		int textX = (this.getEffectiveX() + (this.getWidth() / 2) - (this.font.getStringLength(this.label) / 2));
		int textY = this.getEffectiveY() + (this.getHeight() / 2) - (Math.round(this.font.getFontHeight() * this.font.getScale()));
		renderer.drawString(this.label, textX, textY, this.font, stateFg);
	}

	@Override
	public void tick(RenderContainer container, Renderer renderer) 
	{
		this.updateChildren(container, renderer);

		if (container.getInput().isCursonInRegion(this.getEffectiveX(), this.getEffectiveY(), this.getEffectiveX() + this.getWidth(), this.getEffectiveY() + this.getHeight()))
		{
			UIManager.addHoveredElement(this);
			this.setState(UIState.HOVER);
		} else {
			this.setState(UIState.IDLE);
		}

		if (this.getState() == UIState.HOVER && UIManager.isHighestLayer(this) && container.getInput().isLeftMouseClicked())
		{
			this.setState(UIState.CLICKED);
		}
	}
}
