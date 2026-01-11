package coffee.khyonieheart.bouquet.engine;

import static org.lwjgl.system.MemoryUtil.NULL;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLDebugMessageCallback;

public class BouquetContainer implements RenderContainer
{
	private long handle;
	private int width, height, framerate;
	private String title;
	private boolean started;
	private boolean enableGlDebug = false;

	private Ticker ticker;
	private Renderer renderer;
	private Input input;

	private Deque<ResolutionUpdateSubscriber> resolutionUpdateSubscribers = new ArrayDeque<>();
	private Deque<GlInitializeSubscriber> initializeSubscribers = new ArrayDeque<>();

	@Override
	public long getHandle() 
	{
		return this.handle;
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

	@Override
	public String getTitle() 
	{
		return this.title;
	}

	@Override
	public Renderer getRenderer()
	{
		return this.renderer;
	}

	@Override
	public BouquetContainer setDimensions(int width, int height) 
	{
		if (width < 1)
		{
			throw new IllegalArgumentException("Width must be greater than 0");
		}

		if (height < 1)
		{
			throw new IllegalArgumentException("Height must be greater than 0");
		}

		this.width = width;
		this.height = height;
		return this;
	}

	@Override
	public BouquetContainer setTitle(String title) 
	{
		if (title == null)
		{
			title = "null";
		}

		this.title = title;
		return this;
	}

	@Override
	public void start() 
	{
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_DEBUG_CONTEXT, GLFW.GLFW_TRUE);
		System.out.print("Creating window... ");
		this.handle = GLFW.glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
		if (this.handle == NULL)
		{
			System.out.println("Failed, code: " + GLFW.glfwGetError(null));
			return;
		}
		System.out.println("Done, handle is $" + Long.toHexString(this.handle).toUpperCase());
		System.out.print("Making context current and loading capabilities... ");
		GLFW.glfwMakeContextCurrent(this.handle);
		GL.createCapabilities();
		System.out.println("Done");
		System.out.println("OpenGL version: " + GL30.glGetString(GL30.GL_VERSION));
		GL30.glEnable(GL30.GL_BLEND);
		GL30.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
		GL30.glDisable(GL30.GL_CULL_FACE);
		GL30.glDisable(GL30.GL_DEPTH_TEST);

		if (this.framerate == 0)
		{
			System.out.print("No refresh rate set, setting automatically... ");
			GLFWVidMode mode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
			this.framerate = mode.refreshRate();
			System.out.println("Set refresh rate to " + this.framerate + " FPS");
		}

		if (this.enableGlDebug)
		{
			GL43.glDebugMessageCallback((source, type, id, severity, length, message, userParam) -> {
				System.err.println("GL DEBUG sev " + severity + " type " + type + ": " + GLDebugMessageCallback.getMessage(length, message));
			}, 0);
		}

		GLFW.glfwSetFramebufferSizeCallback(this.handle, (window, width, height) -> {
			System.out.println("Updating resolution for window " + Long.toHexString(this.handle).toUpperCase() + ": " + width + "x" + height + "px");
			this.width = width;
			this.height = height;
			this.resolutionUpdateSubscribers.forEach(target -> target.update(width, height));
			GL30.glViewport(0, 0, width, height);
		});

		/*
		GLFW.glfwSetWindowSizeCallback(this.handle, (window, width, height) -> {
			System.out.println("Updating resolution for window " + Long.toHexString(this.handle).toUpperCase() + ": " + width + "x" + height + "px");
			this.width = width;
			this.height = height;
			this.resolutionUpdateSubscribers.forEach(target -> target.update(width, height));
			GL30.glViewport(0, 0, width, height);
		});
		*/

		this.started = true;

		long waitTime = 1000 / this.framerate;

		// Run initializers
		this.initializeSubscribers.forEach((subscriber) -> subscriber.onInitialize(this));

		// Render-update loop
		while (this.started)
		{
			try {
				this.ticker.tick(this);

				if (!this.started) // Check again, in case the ticker decides to close the window
				{
					break;
				}

				this.renderer.render(this);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				Thread.sleep(waitTime); // Yes, bad, I know. I just want to lock framerate.
			} catch (InterruptedException e) {
				System.out.println("Container thread interrupted");
			}

			GLFW.glfwPollEvents();
		}
	}

	@Override
	public void close() 
	{
		if (this.handle != NULL)
		{
			GLFW.glfwSetWindowShouldClose(this.handle, true);
			this.handle = NULL;
		}
		this.started = false;
	}

	@Override
	public boolean isRunning() 
	{
		return this.started;
	}

	@Override
	public BouquetContainer setTicker(Ticker ticker) 
	{
		this.ticker = Objects.requireNonNull(ticker);

		return this;
	}

	@Override
	public BouquetContainer setRenderer(Renderer renderer) 
	{
		this.renderer = Objects.requireNonNull(renderer);

		return this;
	}

	@Override
	public int getTargetFramerate() 
	{
		return this.framerate;
	}

	@Override
	public BouquetContainer setTargetFramerate(int framerate) 
	{
		if (framerate <= 30)
		{
			throw new IllegalArgumentException("Framerate must be 30 or greater");
		}

		this.framerate = framerate;

		return this;
	}

	@Override
	public void updateDimensions(int x, int y) 
	{
		this.width = x;
		this.height = y;
	}

	@Override
	public void addResolutionSubscriber(ResolutionUpdateSubscriber subscriber) 
	{
		Objects.requireNonNull(subscriber);

		this.resolutionUpdateSubscribers.push(subscriber);
	}

	@Override
	public BouquetContainer addInitializeSubscriber(GlInitializeSubscriber subscriber) 
	{
		Objects.requireNonNull(subscriber);

		this.initializeSubscribers.push(subscriber);
		return this;
	}

	public void enableGlDebug()
	{
		this.enableGlDebug = true;
	}

	@Override
	public Input getInput() 
	{
		return this.input;
	}

	@Override
	public RenderContainer setInput(Input input) 
	{
		this.input = input;

		return this;
	}
}
