package coffee.khyonieheart.bouquet.ui.data;

import java.time.format.DateTimeFormatter;

import coffee.khyonieheart.bouquet.data.Message;
import coffee.khyonieheart.bouquet.engine.RenderContainer;
import coffee.khyonieheart.bouquet.engine.Renderer;
import coffee.khyonieheart.bouquet.font.Font;
import coffee.khyonieheart.bouquet.ui.AnchorPoint;
import coffee.khyonieheart.bouquet.ui.UIBaseElement;
import coffee.khyonieheart.bouquet.ui.UIImage;
import coffee.khyonieheart.bouquet.ui.UIState;
import coffee.khyonieheart.bouquet.ui.UIStyle;
import coffee.khyonieheart.bouquet.ui.UIText;

public class UIMessage extends UIBaseElement
{
	private final Message message;

	private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm a, EEEE dd");

	public UIMessage(
		Message message,
		int x,
		int y
	) {
		super(x, y, 1000, 64, 1);
		this.message = message;

		UIImage uiPfp = new UIImage(message.getSender().getProfilePicture(), 10, 0, 64, 64, 15, 1);
		UIText uiUsername = new UIText(message.getSender().getEffectiveName(), 84, 0, UIStyle.getFont("message-box.message.font-bold"), message.getSender().getColor(), 1);

		Font messageFont = UIStyle.getFont("message-box.message.font-regular");
		UIText uiMessage = new UIText(message.getMessage(), 84, Math.round(messageFont.getFontHeight() * messageFont.getScale()), messageFont, UIStyle.getInt("message-box.message.color"), 1);
		UIText uiTimestamp = new UIText(message.getTime().format(DATE_TIME_FORMAT), 10, -2, UIStyle.getFont("message-box.message.timestamp.font"), UIStyle.getInt("message-box.message.timestamp.color"), 1);

		this.anchor(uiPfp.setAnchorPoint(AnchorPoint.TOP_LEFT))
			.anchor(uiUsername.setAnchorPoint(AnchorPoint.TOP_LEFT))
			.anchor(uiMessage.setAnchorPoint(AnchorPoint.TOP_LEFT));

		uiUsername.anchor(uiTimestamp.setAnchorPoint(AnchorPoint.BOTTOM_EAST));
	}

	@Override
	public void render(RenderContainer container, Renderer renderer) 
	{
		this.renderChildren(container, renderer);
	}

	@Override
	public void tick(RenderContainer container, Renderer renderer) 
	{
		this.tick(container, renderer);
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

	public Message getMessage()
	{
		return this.message;
	}
}
