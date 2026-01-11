package coffee.khyonieheart.bouquet.ui;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import coffee.khyonieheart.bouquet.engine.RenderContainer;
import coffee.khyonieheart.bouquet.engine.Renderer;

public interface UIElement extends Comparable<UIElement>
{
	public void render(
		RenderContainer container,
		Renderer renderer
	);

	public void tick(
		RenderContainer container,
		Renderer renderer
	);

	public int getWidth();

	public int getHeight();

	public int getEffectiveX();

	public int getEffectiveY();

	/**
	 * Gets this element's X position, either an absolute pixel position or an X offset from an anchor.
	 */
	public int getX();

	/**
	 * Gets this element's Y position, either an absolute pixel position or an Y offset from an anchor.
	 */
	public int getY();

	public boolean isFocused();

	public boolean setFocused(
		boolean focused
	);

	public UIState setState(
		UIState state
	);

	public UIState getState();

	public int getLayer();

	public UIElement anchor(
		UIElement child
	);

	public UIElement setParent(
		UIElement parent	
	);

	public UIElement getAnchor();

	public UIElement setAnchorPoint(
		AnchorPoint anchor
	);

	public Collection<UIElement> getAnchoredElements();

	/**
	 * Renders all of an element's children. This method should not be called outside of an element's render() function.
	 */
	public default void renderChildren(
		RenderContainer container,
		Renderer renderer
	) {
		for (UIElement element : this.getAnchoredElements())
		{
			element.render(container, renderer);
		}
	}

	/**
	 * Ticks all of an element's children. This method should not be called outside of an element's tick() function.
	 */
	public default void updateChildren(
		RenderContainer container,
		Renderer renderer
	) {
		for (UIElement element : this.getAnchoredElements())
		{
			element.tick(container, renderer);
		}
	}

	public default void printChildren()
	{
		System.out.println(this.getClass().getSimpleName() + " (anchored: " + (this.getAnchor() == null ? "null" : this.getAnchor().getClass().getSimpleName()) + ")");
		this.printChildren(1);
	}

	default void printChildren(
		int depth
	) {
		for (UIElement e : this.getAnchoredElements())
		{
			System.out.println("- ".repeat(depth) + e.getClass().getSimpleName() + " (anchored: " + (this.getAnchor() == null ? "null" : this.getAnchor().getClass().getSimpleName() + ", EY: " + this.getAnchor().getEffectiveY()) + ")");
			e.printChildren(depth + 1);
		}
	}

	public default int compareTo(
		UIElement element
	) {
		return this.getLayer() - element.getLayer();
	}
}
