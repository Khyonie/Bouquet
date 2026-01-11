package coffee.khyonieheart.bouquet.engine;

public interface Input
{
	public void poll();

	public int getCursorX();

	public int getCursorY();

	public boolean isLeftMouseClicked();

	public default boolean isCursonInRegion(
		int minX,
		int minY,
		int maxX,
		int maxY
	) {
		return (this.getCursorX() >= minX && this.getCursorX() <= maxX) && (this.getCursorY() >= minY && this.getCursorY() <= maxY);
	}
}
