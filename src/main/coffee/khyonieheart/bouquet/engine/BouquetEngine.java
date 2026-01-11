package coffee.khyonieheart.bouquet.engine;

import java.util.ArrayDeque;
import java.util.Deque;

import org.lwjgl.glfw.GLFW;

public class BouquetEngine implements Ticker
{
	public static int framerate = 0;
	private Deque<Integer> frames = new ArrayDeque<>();
	private int frameTicks = 0;
	private long timeDelta = 0;

	@Override
	public void tick(RenderContainer container) 
	{
		tickFrameCounter();

		UIManager.clearHoverEvents();
		container.getInput().poll();

		UIManager.UI_ROOT.tick(container, container.getRenderer());
		TweenManager.tick();

		// Controls
		if (GLFW.glfwGetKey(container.getHandle(), GLFW.GLFW_KEY_ESCAPE) == GLFW.GLFW_PRESS)
		{
			container.close();
			return;
		}
	}

	private void tickFrameCounter()
	{
		if (timeDelta == 0)
		{
			timeDelta = System.currentTimeMillis() + 1000;
		}

		frameTicks++;
		if (System.currentTimeMillis() >= timeDelta)
		{
			frames.push(frameTicks);
			frameTicks = 0;
			timeDelta = System.currentTimeMillis() + 1000;
		}

		if (!frames.isEmpty())
		{
			int totalFrames = 0;
			for (int i : frames)
			{
				totalFrames += i;
			}

			framerate = totalFrames / frames.size();
		}
	}
}
