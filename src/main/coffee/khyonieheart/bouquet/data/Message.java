package coffee.khyonieheart.bouquet.data;

import java.time.OffsetDateTime;

public class Message
{
	private final User sender;
	private final String message;
	private final OffsetDateTime time;

	public Message(
		User sender,
		String message,
		OffsetDateTime time
	) {
		this.sender = sender;
		this.message = message;
		this.time = time;
	}

	public User getSender() 
	{
		return sender;
	}

	public String getMessage() 
	{
		return message;
	}

	public OffsetDateTime getTime() 
	{
		return time;
	}
}
