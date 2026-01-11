package coffee.khyonieheart.bouquet.ui.style;

import java.util.HashMap;

public class Style 
{
	private final HashMap<String, Object> styleData;
	private final HashMap<String, Object> cachedStyleData = new HashMap<>();

	public Style(
		HashMap<String, Object> styleData
	) {
		this.styleData = styleData;
	}

	public int getIntProperty(
		String key
	) {
		return (int) get(key);
	}

	@SuppressWarnings("unchecked")
	private Object get(
		String key
	) {
		if (cachedStyleData.containsKey(key))
		{
			return cachedStyleData.get(key);
		}
		String[] subkeys = key.split("\\.");
		HashMap<String, Object> current = styleData;
		for (int i = 0; i < subkeys.length - 1; i++)
		{
			current = (HashMap<String, Object>) current.get(subkeys[i]);
		}

		return current.get(subkeys[subkeys.length - 1]);
	}
}
