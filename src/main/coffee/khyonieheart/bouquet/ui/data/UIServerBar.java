package coffee.khyonieheart.bouquet.ui.data;

import java.util.Random;

import coffee.khyonieheart.bouquet.data.Server;
import coffee.khyonieheart.bouquet.engine.RenderContainer;
import coffee.khyonieheart.bouquet.engine.Renderer;
import coffee.khyonieheart.bouquet.engine.UIManager;
import coffee.khyonieheart.bouquet.ui.AnchorPoint;
import coffee.khyonieheart.bouquet.ui.UIBaseElement;
import coffee.khyonieheart.bouquet.ui.UIState;

public class UIServerBar extends UIBaseElement
{	
	private RenderContainer container;

	private final String[] WORD_CORPUS = { "Trout", "Salmon", "Bass", "Lilac", "Delta", "Charlie", "Zulu", "Yankee" };

	public UIServerBar(
		RenderContainer container
	) {
		super(0, 0, 94, container.getHeight(), 0);
		this.container = container;

		UIServerBarEntry previousEntry = null;
		Server server;

		Random random = new Random();
		for (int i = 0; i < 25; i++)
		{
			StringBuilder testName = new StringBuilder();
			for (int j = 0; j < 1 + random.nextInt(20); j++)
			{
				testName.append(WORD_CORPUS[random.nextInt(WORD_CORPUS.length)]);
				testName.append(' ');
			}
			String serverName = testName.toString().trim();
			server = new Server(serverName);

			if (previousEntry == null)
			{
				UIServerBarEntry entry = new UIServerBarEntry(server, 15, 30);
				this.anchor(entry.setAnchorPoint(AnchorPoint.TOP_LEFT));
				previousEntry = entry;

				continue;
			}

			UIServerBarEntry entry = new UIServerBarEntry(server, 0, 20);
			previousEntry.anchor(entry.setAnchorPoint(AnchorPoint.SOUTH_LEFT));
			previousEntry = entry;
		}
	}

	@Override
	public int getHeight()
	{
		return this.container.getHeight();
	}

	@Override
	public void render(RenderContainer container, Renderer renderer) 
	{
		int color = this.getState() == UIState.HOVER && UIManager.isHighestLayer(this) ? 0x220000FF : 0x262626FF;
		renderer.drawRectangle(this.getEffectiveX(), this.getEffectiveY(), this.getWidth(), this.getHeight(), color);
		renderer.drawRectangle(this.getEffectiveX() + this.getWidth() - 1, this.getEffectiveY(), 1, this.getHeight(), 0x101010FF);
		this.renderChildren(container, renderer);
	}

	@Override
	public void tick(RenderContainer container, Renderer renderer) 
	{
		this.updateChildren(container, renderer);

		if (container.getInput().isCursonInRegion(this.getEffectiveX(), this.getEffectiveY(), this.getEffectiveX() + this.getWidth(), this.getEffectiveY() + this.getHeight()))
		{
			UIManager.addHoveredElement(this);
			if (UIManager.isHighestLayer(this))
			{
				this.setState(UIState.HOVER);
			}
		} else {
			this.setState(UIState.IDLE);
		}

		if (this.getState() == UIState.HOVER && UIManager.isHighestLayer(this) && container.getInput().isLeftMouseClicked())
		{
			this.setState(UIState.CLICKED);
		}
	}

	@Override
	public void changeState(UIState state) 
	{
		switch (state)
		{
			case CLICKED -> {}
			case HOVER -> {}
			case IDLE -> {}
			case INACTIVE -> {}
		}
	}
}
