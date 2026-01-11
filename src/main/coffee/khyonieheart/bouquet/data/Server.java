package coffee.khyonieheart.bouquet.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Server
{
	private Map<String, ServerChannel> channels = new LinkedHashMap<>();
	private List<User> users = new ArrayList<>();

	private String name;

	public Server(
		String name
	) {
		this.name = name;
	}

	public Map<String, ServerChannel> getChannels() 
	{
		return channels;
	}

	public List<User> getUsers() 
	{
		return users;
	}

	public String getName() 
	{
		return name;
	}

	public void addUser(
		User user
	) {
		this.users.add(user);
	}

	public void addChannel(
		String name
	) {
		this.channels.put(name, new ServerChannel(name));
	}
}
