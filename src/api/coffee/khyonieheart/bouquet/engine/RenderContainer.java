package coffee.khyonieheart.bouquet.engine;

public interface RenderContainer extends AutoCloseable
{
	public long getHandle();

	public int getWidth();

	public int getHeight();

	public int getTargetFramerate();

	public String getTitle();

	public Input getInput();

	public Renderer getRenderer();

	public void updateDimensions(
		int x,
		int y
	);

	/**
	 * Adds a subscriber object to this container, to be called when the container resolution changes.
	 *
	 * @param subscriber Subscriber
	 */
	public void addResolutionSubscriber(
		ResolutionUpdateSubscriber subscriber
	);

	/**
	 * Adds a subscriber object to this container, to be called after GLFW and OpenGL have been initialized.
	 *
	 * @param subscriber Subscriber
	 */
	public RenderContainer addInitializeSubscriber(
		GlInitializeSubscriber subscriber
	);

	public RenderContainer setDimensions(
		int width,
		int height
	);

	public RenderContainer setTargetFramerate(
		int framerate
	);

	public RenderContainer setTitle(
		String title
	);

	public RenderContainer setTicker(
		Ticker ticker
	);

	public RenderContainer setRenderer(
		Renderer renderer
	);

	public RenderContainer setInput(
		Input input
	);

	public void start();

	public void close();

	public boolean isRunning();
}
