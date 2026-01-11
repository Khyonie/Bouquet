package coffee.khyonieheart.bouquet;

import coffee.khyonieheart.bouquet.engine.GlInitializeSubscriber;
import coffee.khyonieheart.bouquet.engine.RenderContainer;
import coffee.khyonieheart.bouquet.font.Font;
import coffee.khyonieheart.bouquet.ui.UIStyle;

public class BouquetStyleInitializer implements GlInitializeSubscriber
{
	@Override
	public void onInitialize(RenderContainer container) 
	{
		UIStyle style = new UIStyle();
		
		// Font
		Font messageRegular = new Font("font/Nunito-Regular.ttf", 32);
		Font messageBold = new Font("font/Nunito-Bold.ttf", 32);
		Font messageItalic = new Font("font/Nunito-Italic.ttf", 32);
		Font messageBoldItalic = new Font("font/Nunito-BoldItalic.ttf", 32);
		
		Font usernameFont = messageBold;
		Font timestampFont = new Font("font/Nunito-LightItalic.ttf", 24);

		style.setMessageFonts(messageRegular, messageBold, messageItalic, messageBoldItalic)
			.setMessageColor(0xD0D0D0FF)
			.setUserFonts(usernameFont, timestampFont)
			.setTimestampColor(0xA0A0A0FF)
			.setMessageBoxBackgroundColor(0x363636FF, 0x363636FF, 0x565656FF, 0x4B394DFF);

		UIStyle.setStyle(style);
	}
}
