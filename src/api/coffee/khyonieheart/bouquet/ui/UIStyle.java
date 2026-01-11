package coffee.khyonieheart.bouquet.ui;

import java.io.File;
import java.io.IOException;

import coffee.khyonieheart.lilac.LilacDecoder;
import coffee.khyonieheart.lilac.TomlConfiguration;
import coffee.khyonieheart.lilac.TomlDecoder;
import coffee.khyonieheart.lilac.TomlVersion;

/**
 * Container for all the different ways a UI can be styled.
 */
public class UIStyle
{
	private static UIStyle currentStyle;

	private TomlConfiguration style;

	public static void loadStyle(
		String path
	)
		throws IOException
	{
		TomlDecoder decoder = new LilacDecoder(TomlVersion.V1_1_0);
		File styleFile = new File(path);

		TomlConfiguration config = new TomlConfiguration(decoder.decode(styleFile));

		config.getString("welcome.to.hell");
	}
}
