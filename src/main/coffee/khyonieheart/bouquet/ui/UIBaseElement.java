package coffee.khyonieheart.bouquet.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import coffee.khyonieheart.bouquet.anim.Animatable;
import coffee.khyonieheart.bouquet.anim.AnimationProperty;
import coffee.khyonieheart.bouquet.state.Stateful;

public abstract class UIBaseElement implements UIElement, Animatable, Stateful
{
	private int x, y, width, height, layer, foregroundColor, backgroundColor;

	private boolean isFocused = false;
	private UIState state = UIState.IDLE;

	private UIElement parent;
	private AnchorPoint anchorPoint;
	private List<UIElement> childElements = new ArrayList<>();

	public UIBaseElement(
		int x,
		int y,
		int width,
		int height,
		int layer
	) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.layer = layer;
	}

	public void animate(
		AnimationProperty property,
		int value
	) {
		switch (property)
		{
			case X_POS -> this.x = value;
			case Y_POS -> this.y = value;
			case BG_COLOR -> this.backgroundColor = value;
			case FG_COLOR -> this.foregroundColor = value;
			case HEIGHT -> this.height = value;
			case WIDTH -> this.width = value;
		}
	}

	@Override
	public int getEffectiveX() 
	{
		return this.parent != null ? 
			this.anchorPoint.getXPosition(this.parent, this) :
			this.x;
	}

	@Override
	public int getEffectiveY() {
		return this.parent != null ? 
			this.anchorPoint.getYPosition(this.parent, this) :
			this.y;
	}

	@Override
	public int getX()
	{
		return this.x;
	}

	@Override
	public int getY()
	{
		return this.y;
	}

	@Override
	public int getWidth()
	{
		return this.width;
	}

	@Override
	public int getHeight()
	{
		return this.height;
	}

	public int getLayer()
	{
		return this.layer;
	}

	@Override
	public boolean isFocused() 
	{
		return this.isFocused;
	}

	@Override
	public boolean setFocused(boolean focused) 
	{
		return this.isFocused = focused;
	}

	@Override
	public UIState getState() 
	{
		return this.state;
	}

	@Override
	public UIState setState(UIState state) 
	{
		return this.state = state;
	}

	@Override
	public UIElement anchor(UIElement child) 
	{
		this.childElements.add(child);
		Collections.sort(this.childElements);
		child.setParent(this);
		return this;
	}

	@Override
	public UIElement getAnchor() 
	{
		return this.parent;
	}

	@Override
	public Collection<UIElement> getAnchoredElements() 
	{
		return this.childElements;
	}

	@Override
	public UIElement setAnchorPoint(AnchorPoint anchor) 
	{
		this.anchorPoint = anchor;

		return this;
	}

	@Override
	public UIElement setParent(UIElement parent) 
	{
		this.parent = parent;

		return this;
	}

	public int getBackgroundColor() 
	{
		return backgroundColor;
	}

	public int getForegroundColor() 
	{
		return foregroundColor;
	}
}
