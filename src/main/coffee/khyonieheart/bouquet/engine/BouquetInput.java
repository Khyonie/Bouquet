package coffee.khyonieheart.bouquet.engine;

import org.lwjgl.glfw.GLFW;

public class BouquetInput implements Input
{
	private final RenderContainer container;
	private int cursorX, cursorY;

	public BouquetInput(
		RenderContainer container
	) {
		this.container = container;
	}

	@Override
	public void poll() 
	{
		double[] cursorXPointer = new double[1];
		double[] cursorYPointer = new double[1];

		GLFW.glfwGetCursorPos(this.container.getHandle(), cursorXPointer, cursorYPointer);
		this.cursorX = (int) Math.round(cursorXPointer[0]);
		this.cursorY = (int) Math.round(cursorYPointer[0]);
	}

	@Override
	public int getCursorX() 
	{
		return this.cursorX;
	}

	@Override
	public int getCursorY() 
	{
		return this.cursorY;
	}

	@Override
	public boolean isLeftMouseClicked() 
	{
		return GLFW.glfwGetMouseButton(this.container.getHandle(), GLFW.GLFW_MOUSE_BUTTON_1) == GLFW.GLFW_PRESS;
	}
}
