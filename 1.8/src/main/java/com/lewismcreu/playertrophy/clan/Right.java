package com.lewismcreu.playertrophy.clan;

import com.lewismcreu.playertrophy.util.Language;

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
		return Language.getLocalizedString(this.descriptionKey);
	}
}
