package coffee.khyonieheart.bouquet.engine;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import coffee.khyonieheart.bouquet.ui.UIElement;

public class UIManager
{
	private static List<UIElement> hoveredElements = new ArrayList<>();

	public static UIElement UI_ROOT = null;

	public static void addHoveredElement(
		UIElement element
	) {
		hoveredElements.add(element);
	}

	public static void clearHoverEvents()
	{
		hoveredElements.clear();
	}

	public static void sortHoverEvents()
	{
		hoveredElements.sort(Comparator.naturalOrder());
	}

	public static boolean isHighestLayer(
		UIElement element
	) {
		if (hoveredElements.isEmpty())
		{
			return false;
		}

		return hoveredElements.getLast() == element;
	}
}
