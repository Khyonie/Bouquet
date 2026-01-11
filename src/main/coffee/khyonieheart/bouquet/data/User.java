package coffee.khyonieheart.bouquet.data;

import coffee.khyonieheart.bouquet.gl.GlTexture;

public class User
{
	private final String username;
	private String nickname;
	private int color = 0xFFFFFFFF;
	private GlTexture pfp;

	public User(
		String username,
		GlTexture pfp
	) {
		this(username, pfp, 0xFFFFFFFF);
	}

	public User(
		String username,
		GlTexture pfp,
		int color
	) {
		this.username = username;
		this.pfp = pfp;
		this.color = color;
	}

	public String getEffectiveName()
	{
		return this.nickname != null ?
			this.nickname :
			this.username;
	}

	public GlTexture getProfilePicture()
	{
		return this.pfp;
	}

	public int getColor()
	{
		return this.color;
	}

	public void setColor(
		int color
	) {
		this.color = color | 0x000000FF;
	}

	public void setNickname(
		String nickname
	) {
		this.nickname = nickname;
	}
}
