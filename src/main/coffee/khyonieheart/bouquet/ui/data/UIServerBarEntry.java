package coffee.khyonieheart.bouquet.ui.data;

import java.util.Random;

import coffee.khyonieheart.bouquet.Color;
import coffee.khyonieheart.bouquet.data.Server;
import coffee.khyonieheart.bouquet.engine.RenderContainer;
import coffee.khyonieheart.bouquet.engine.Renderer;
import coffee.khyonieheart.bouquet.engine.UIManager;
import coffee.khyonieheart.bouquet.font.Font;
import coffee.khyonieheart.bouquet.ui.AnchorPoint;
import coffee.khyonieheart.bouquet.ui.UIBaseElement;
import coffee.khyonieheart.bouquet.ui.UIState;
import coffee.khyonieheart.bouquet.ui.UIStyle;
import coffee.khyonieheart.bouquet.ui.UIText;

public class UIServerBarEntry extends UIBaseElement
{
	private Server server;
	private String shortName;

	private int topColorIdle, bottomLeftColorIdle, bottomRightColorIdle,
		topColorHover, bottomLeftColorHover, bottomRightColorHover,
		topColorClicked, bottomLeftColorClicked, bottomRightColorClicked,
		topColorInactive, bottomLeftColorInactive, bottomRightColorInactive;

	private final int BASE_SERVER_COLOR = 0x82CEE0FF;

	public UIServerBarEntry(
		Server server,
		int x,
		int y
	) {
		super(x, y, 64, 64, 1);
		this.server = server;

		String name = server.getName();
		StringBuilder builder = new StringBuilder();
		for (String s : name.split(" ")) 
		{
			if (s.length() == 0)
			{
				continue;
			}
			builder.append(Character.toUpperCase(s.charAt(0)));
		}

		this.shortName = builder.substring(0, Math.min(builder.length(), 3));

		float hueShift = new Random().nextFloat(360f);

		//-------------------------------------------------------------------------------- 
		// Colors
		//-------------------------------------------------------------------------------- 
		float[] hsvColor = Color.rgbToHsv(BASE_SERVER_COLOR);
		Color.hueShift(hsvColor, hueShift);
		float[] desaturatedColor = new float[] { hsvColor[0], hsvColor[1] * 0.1f, hsvColor[2] };
		float[] hueAdjustedColor = Color.rgbToHsv(BASE_SERVER_COLOR);
		Color.hueShift(hueAdjustedColor, hueShift + 60f);

		// Idle
		this.topColorIdle = Color.hsvToRgb(hsvColor); // Primary color
		this.bottomLeftColorIdle = Color.hsvToRgb(desaturatedColor);
		this.bottomRightColorIdle = Color.hsvToRgb(hueAdjustedColor);

		// Hover
		this.topColorHover = Color.hsvToRgb(new float[] { hsvColor[0], hsvColor[1] * 0.8f, 1f });
		this.bottomLeftColorHover = Color.hsvToRgb(new float[] { desaturatedColor[0], desaturatedColor[1] * 0.8f, 1f });
		this.bottomRightColorHover = Color.hsvToRgb(new float[] { hueAdjustedColor[0], hueAdjustedColor[1] * 0.8f, 1f });
		
		// Clicked
		this.topColorClicked = Color.hsvToRgb(new float[] { hsvColor[0], hsvColor[1] * 0.5f, 1f });
		this.bottomLeftColorClicked = Color.hsvToRgb(new float[] { desaturatedColor[0], desaturatedColor[1] * 0.5f, 1f });
		this.bottomRightColorClicked = Color.hsvToRgb(new float[] { hueAdjustedColor[0], hueAdjustedColor[1] * 0.5f, 1f });
		
		// Inactive
		this.topColorInactive = Color.hsvToRgb(new float[] { hsvColor[0], hsvColor[1] * 0.5f, hsvColor[2] * 0.8f });
		this.bottomLeftColorInactive = Color.hsvToRgb(new float[] { desaturatedColor[0], desaturatedColor[1] * 0.5f, desaturatedColor[2] * 0.8f });
		this.bottomRightColorInactive = Color.hsvToRgb(new float[] { hueAdjustedColor[0], hueAdjustedColor[1] * 0.5f, hueAdjustedColor[2] * 0.8f });
		//--------------------------------------------------------------------------------

		// Label
		UIText text = new UIText(shortName, 0, 0, UIStyle.getStyle().getMessageFont(), 0x505050FF, 1);
		UIServerBarEntryHover hover = new UIServerBarEntryHover(this);
		this.anchor(text.setAnchorPoint(AnchorPoint.CENTER))
			.anchor(hover.setAnchorPoint(AnchorPoint.EAST));
	}

	@Override
	public void render(RenderContainer container, Renderer renderer) 
	{
		int topColor = switch (this.getState()) {
			case CLICKED -> this.topColorClicked;
			case HOVER -> this.topColorHover;
			case IDLE -> this.topColorIdle;
			case INACTIVE -> this.topColorInactive;
		};

		int bottomRightColor = switch (this.getState()) {
			case CLICKED -> this.bottomRightColorClicked;
			case HOVER -> this.bottomRightColorHover;
			case IDLE -> this.bottomRightColorIdle;
			case INACTIVE -> this.bottomRightColorInactive;
		};

		int bottomLeftColor = switch (this.getState()) {
			case CLICKED -> this.bottomLeftColorClicked;
			case HOVER -> this.bottomLeftColorHover;
			case IDLE -> this.bottomLeftColorIdle;
			case INACTIVE -> this.bottomLeftColorInactive;
		};

		renderer.drawColoredRectangle(this.getEffectiveX(), this.getEffectiveY(), this.getWidth(), this.getHeight(), topColor, bottomRightColor, bottomLeftColor, topColor, 32);
		renderer.drawRectangle(this.getEffectiveX() - 2, this.getEffectiveY() + 74, 68, 1, 0x404040FF);

		this.renderChildren(container, renderer);
	}

	@Override
	public void tick(RenderContainer container, Renderer renderer) 
	{
		this.updateChildren(container, renderer);

		if (container.getInput().isCursonInRegion(this.getEffectiveX(), this.getEffectiveY(), this.getEffectiveX() + this.getWidth(), this.getEffectiveY() + this.getHeight()))
		{
			UIManager.addHoveredElement(this);
			if (UIManager.isHighestLayer(this))
			{
				this.setState(UIState.HOVER);
			}
		} else {
			this.setState(UIState.IDLE);
		}

		if (this.getState() == UIState.HOVER && UIManager.isHighestLayer(this) && container.getInput().isLeftMouseClicked())
		{
			this.setState(UIState.CLICKED);
		}
	}
	
	public Server getServer()
	{
		return this.server;
	}
}
