package coffee.khyonieheart.bouquet.engine;

@FunctionalInterface
public interface GlInitializeSubscriber
{
	public void onInitialize(
		RenderContainer container
	);
}
