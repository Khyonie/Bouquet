package coffee.khyonieheart.bouquet;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;

import coffee.khyonieheart.bouquet.engine.BouquetContainer;
import coffee.khyonieheart.bouquet.engine.BouquetEngine;
import coffee.khyonieheart.bouquet.engine.BouquetInput;
import coffee.khyonieheart.bouquet.engine.BouquetRenderer;
import coffee.khyonieheart.bouquet.engine.RenderContainer;

public class BouquetApp
{
	public static void main(String[] args) 
	{
		System.out.print("Initializing GLFW... ");
		if (!GLFW.glfwInit())
		{
			System.out.println("Failed");
			return;
		}
		System.out.println("Done");

		System.out.print("Setting GLFW error callback... ");
		GLFWErrorCallback.createPrint(System.err).set();

		System.out.println("Done");

		try (RenderContainer container = new BouquetContainer())
		{
			BouquetEngine engine = new BouquetEngine();
			BouquetRenderer renderer = new BouquetRenderer();
			container.setDimensions(800, 600)
				.setTitle("Bouquet")
				.setTicker(engine)
				.setRenderer(renderer)
				.setInput(new BouquetInput(container))
				.addInitializeSubscriber((c) -> renderer.init(c))
				.addInitializeSubscriber((c) -> new BouquetStyleInitializer().onInitialize(c))
				.start();
		} catch (Exception e) {
			System.out.print("An exception occurred outside of container bounds: ");
			e.printStackTrace();
		} finally {
			GLFW.glfwTerminate();
			System.out.println("GLFW terminated");
		}
	}
}
