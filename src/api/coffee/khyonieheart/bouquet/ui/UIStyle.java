package coffee.khyonieheart.bouquet.ui;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import coffee.khyonieheart.bouquet.font.Font;
import coffee.khyonieheart.lilac.LilacDecoder;
import coffee.khyonieheart.lilac.TomlConfiguration;
import coffee.khyonieheart.lilac.TomlDecoder;
import coffee.khyonieheart.lilac.TomlVersion;

/**
 * Container for all the different ways a UI can be styled.
 */
public class UIStyle
{
	private static TomlConfiguration style;
	private static Map<String, Font> loadedFonts = new HashMap<>();

	public static void loadStyle(
		String path
	)
		throws IOException
	{
		TomlDecoder decoder = new LilacDecoder(TomlVersion.V1_1_0);
		File styleFile = new File(path);

		style = new TomlConfiguration(decoder.decode(styleFile));
	}

	public static int getInt(
		String key
	) {
		return style.getInteger(key);
	}
	
	public static String getString(
		String key
	) {
		return style.getString(key);
	}

	public static Font getFont(
		String key
	) {
		if (loadedFonts.containsKey(key))
		{
			return loadedFonts.get(key);
		}

		Map<String, Object> fontData = style.getTable(key);
		long fontPoint = (long) fontData.get("point");

		Font font = new Font((String) fontData.get("path"), (int) fontPoint);
		loadedFonts.put(key, font);

		return font;
	}
}
