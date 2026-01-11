package coffee.khyonieheart.bouquet.data;

import java.util.ArrayList;
import java.util.List;

public class ServerChannel
{
	private String name;
	private List<Message> messages = new ArrayList<>();

	public ServerChannel(
		String name
	) {
		this.name = name;
	}

	public String getName() 
	{
		return name;
	}

	public List<Message> getMessages() 
	{
		return messages;
	}
}
