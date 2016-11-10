package com.lewismcreu.playertrophy.common.data;

import com.lewismcreu.playertrophy.util.Lang;

/**
 * @author Lewis_McReu
 */
public enum Right
{
	INVITE, KICKDEFAULT, KICK, MANAGE, DISBAND, CLAIM;

	private final String descriptionKey;

	private Right()
	{
		this.descriptionKey = "playertrophy.clan.right."
				+ this.name().toLowerCase() + ".description";
	}

	public String getDescription()
	{
		return Lang.translate(descriptionKey);
	}
}
