package coffee.khyonieheart.bouquet.ui.data;

import java.util.ArrayList;
import java.util.List;

import coffee.khyonieheart.bouquet.data.Message;
import coffee.khyonieheart.bouquet.data.User;
import coffee.khyonieheart.bouquet.engine.RenderContainer;
import coffee.khyonieheart.bouquet.engine.Renderer;
import coffee.khyonieheart.bouquet.ui.AnchorPoint;
import coffee.khyonieheart.bouquet.ui.UIBaseElement;
import coffee.khyonieheart.bouquet.ui.UIStyle;
import coffee.khyonieheart.bouquet.ui.UIText;

@SuppressWarnings("unused")
public class UIMessageBox extends UIBaseElement
{
	private List<Message> messages = new ArrayList<>();
	private RenderContainer container;

	public UIMessageBox(
		int x,
		int y,
		List<Message> messages,
		RenderContainer container
	) {
		super(x, y, 2000, 1450, 1);
		this.container = container;
		int offset = 0;
		User previousSender = null;
		UIText previousConsolidatedText = null;
		UIMessage previousMessage = null;
		for (Message m : messages)
		{
			if (previousSender == m.getSender())
			{
				UIText text;
				if (previousConsolidatedText == null)
				{
					text = new UIText(m.getMessage(), 84, 69, UIStyle.getStyle().getMessageFont(), UIStyle.getStyle().getMessageColor(), 1);
					text.setAnchorPoint(AnchorPoint.TOP_LEFT);
					previousMessage.anchor(text);
				} else {
					text = new UIText(m.getMessage(), 0, 5, UIStyle.getStyle().getMessageFont(), UIStyle.getStyle().getMessageColor(), 1);
					text.setAnchorPoint(AnchorPoint.SOUTH_LEFT);
					previousConsolidatedText.anchor(text);
				}

				offset += 37;
				previousConsolidatedText = text;
				previousMessage = null;
				continue;
			}

			UIMessage uiMessage = (UIMessage) new UIMessage(m, 0, offset)
				.setAnchorPoint(AnchorPoint.TOP_LEFT);

			this.anchor(uiMessage);
			offset += 84;
			previousSender = m.getSender();
			previousMessage = uiMessage;
			previousConsolidatedText = null;
		}
	}

	@Override
	public int getHeight()
	{
		return container.getHeight() - 94;
	}

	@Override
	public void render(RenderContainer container, Renderer renderer) 
	{
		UIStyle style = UIStyle.getStyle();
		renderer.drawColoredRectangle(this.getEffectiveX(), this.getEffectiveY(), this.getWidth(), this.getHeight(), style.getBackgroundTopRight(), style.getBackgroundTopLeft(), style.getBackgroundBottomRight(), style.getBackgroundBottomLeft());
		renderer.drawRoundedRectangle(this.getEffectiveX() + 10, this.getEffectiveY() + this.getHeight() - 69, this.getWidth() - 20, 64, 0x7F7F7FA0, 16f);
		this.renderChildren(container, renderer);
	}

	@Override
	public void tick(RenderContainer container, Renderer renderer) 
	{
		this.renderChildren(container, renderer);
	}
}
