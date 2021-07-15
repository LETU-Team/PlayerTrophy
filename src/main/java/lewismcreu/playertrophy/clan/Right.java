package lewismcreu.playertrophy.clan;

import lewismcreu.playertrophy.util.Lang;

/**
 * @author Lewis_McReu
 */
public enum Right
{
	INVITE, KICKDEFAULT, KICK, MANAGE, DISBAND, CLAIM;

	private String descriptionKey;

	private Right()
	{
		this.descriptionKey = "playertrophy.clan.right."
				+ this.toString().toLowerCase() + ".description";
	}

	public String getDescriptionKey()
	{
		return this.descriptionKey;
	}

	public String getLocalizedDescription()
	{
		return Lang.translate(this.descriptionKey);
	}
}
