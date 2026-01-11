package coffee.khyonieheart.bouquet.ui.data;

import coffee.khyonieheart.bouquet.engine.RenderContainer;
import coffee.khyonieheart.bouquet.engine.Renderer;
import coffee.khyonieheart.bouquet.ui.AnchorPoint;
import coffee.khyonieheart.bouquet.ui.UIBaseElement;
import coffee.khyonieheart.bouquet.ui.UIState;
import coffee.khyonieheart.bouquet.ui.UIStyle;
import coffee.khyonieheart.bouquet.ui.UIText;

public class UIServerBarEntryHover extends UIBaseElement
{
	private UIServerBarEntry entry;
	private UIText label;
	public UIServerBarEntryHover(
		UIServerBarEntry entry
	) {
		super(8, 0, 20 + UIStyle.getCurrentStyle().getMessageFontBold().getStringLength(entry.getServer().getName()), 10 + UIStyle.getCurrentStyle().getMessageFont().getFontHeight(), 2);
		this.entry = entry;

		label = new UIText(entry.getServer().getName(), 0, 0, UIStyle.getCurrentStyle().getMessageFontBold(), 0xCCCCCCFF, 2);
		this.anchor(label.setAnchorPoint(AnchorPoint.CENTER));
	}

	@Override
	public void render(RenderContainer container, Renderer renderer) 
	{
		if (entry.getState() == UIState.HOVER || entry.getState() == UIState.CLICKED)
		{
			renderer.drawRoundedRectangle(this.getEffectiveX(), this.getEffectiveY(), 20 + label.getWidth(), 10 + label.getHeight(), 0x404040FF, 5f, 0x808080FF, 1);
			this.renderChildren(container, renderer);
		}
	}

	@Override
	public void tick(RenderContainer container, Renderer renderer) 
	{
	}
}
