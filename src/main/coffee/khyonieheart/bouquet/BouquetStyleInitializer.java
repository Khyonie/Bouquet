package coffee.khyonieheart.bouquet;

import coffee.khyonieheart.bouquet.engine.GlInitializeSubscriber;
import coffee.khyonieheart.bouquet.engine.RenderContainer;
import coffee.khyonieheart.bouquet.ui.UIStyle;

public class BouquetStyleInitializer implements GlInitializeSubscriber
{
	@Override
	public void onInitialize(RenderContainer container) 
	{
		try {
			UIStyle.loadStyle("style.toml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
