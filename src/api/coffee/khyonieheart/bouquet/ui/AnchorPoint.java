package coffee.khyonieheart.bouquet.ui;

/**
 * Anchor points for UI elements to attach to each other.
 *
 * Absolute directions are positioned on the <b>interior</b> of the parent.
 * Cardinal direciotns are positioned on the <b>exterior</b> of the parent.
 */
public enum AnchorPoint
{
	// Interior
	CENTER,
	TOP,
	TOP_RIGHT,
	RIGHT,
	BOTTOM_RIGHT,
	BOTTOM,
	BOTTOM_LEFT,
	LEFT,
	TOP_LEFT,

	// Exterior
	NORTH,
	NORTH_EAST,
	EAST,
	SOUTH_EAST,
	SOUTH,
	SOUTH_WEST,
	WEST,
	NORTH_WEST,

	// Cursed combinations of interior/exterior
	NORTH_RIGHT,
	TOP_EAST,
	BOTTOM_EAST,
	SOUTH_RIGHT,
	SOUTH_LEFT,
	BOTTOM_WEST,
	TOP_WEST,
	NORTH_LEFT
	;

	public int getYPosition(
		UIElement parent,
		UIElement child
	) {
		return switch (this) {
			// Absolute (interior)
			case BOTTOM, BOTTOM_LEFT, BOTTOM_RIGHT, BOTTOM_EAST, BOTTOM_WEST -> (parent.getEffectiveY() + parent.getHeight() - child.getHeight()) + child.getY(); 
			case TOP, TOP_LEFT, TOP_RIGHT, TOP_EAST, TOP_WEST -> parent.getEffectiveY() + child.getY();

			// Cardinal (exterior)
			case NORTH, NORTH_EAST, NORTH_WEST, NORTH_RIGHT, NORTH_LEFT -> (parent.getEffectiveY() - child.getHeight()) + child.getY();
			case SOUTH, SOUTH_EAST, SOUTH_WEST, SOUTH_RIGHT, SOUTH_LEFT -> (parent.getEffectiveY() + parent.getHeight()) + child.getY();

			case CENTER, LEFT, RIGHT, EAST, WEST -> (parent.getEffectiveY() + parent.getHeight() / 2) - (child.getHeight() / 2) + child.getY();
		};
	}

	public int getXPosition(
		UIElement parent,
		UIElement child
	) {
		return switch (this) {
			// Absolute (Interior)
			case TOP_RIGHT, RIGHT, BOTTOM_RIGHT, NORTH_RIGHT, SOUTH_RIGHT -> (parent.getEffectiveX() + parent.getWidth() - child.getWidth()) + child.getX();
			case TOP_LEFT, LEFT, BOTTOM_LEFT, NORTH_LEFT, SOUTH_LEFT -> parent.getEffectiveX() + child.getX();
			
			// Cardinal (exterior)
			case NORTH_EAST, EAST, SOUTH_EAST, TOP_EAST, BOTTOM_EAST -> (parent.getEffectiveX() + parent.getWidth()) + child.getX();
			case NORTH_WEST, WEST, SOUTH_WEST, TOP_WEST, BOTTOM_WEST -> (parent.getEffectiveX() - child.getWidth()) + child.getX();

			case CENTER, TOP, BOTTOM, NORTH, SOUTH -> ((parent.getEffectiveX() + parent.getWidth() / 2) - (child.getWidth() / 2)) + child.getX();
		};
	}
}
