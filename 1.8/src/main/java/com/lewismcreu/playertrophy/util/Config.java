package com.lewismcreu.playertrophy.util;

import net.minecraftforge.common.config.Configuration;

import com.lewismcreu.playertrophy.handler.PlayerEventHandler;
import com.lewismcreu.playertrophy.player.PlayerData;

/**
 * @author Lewis_McReu
 */
public class Config
{
	private static final String cat = "playertrophy";
	private static final int minGen = 1;
	private static final int maxGen = Integer.MAX_VALUE;

	private static final String clanFF = "friendlyfire";
	private static final String clanFFC = "enable friendly fire for clans";
	private static final boolean clanFFDefault = false;

	private static final String bountyTime = "bountytime";
	private static final String bountyTimeC = "set the minimum time (in hours) between placing bounties";
	private static final int bountyTimeDefault = 2;

	private static final String trophyTime = "trophytime";
	private static final String trophyTimeC = "set the minimum time (in hours) between getting trophies from killing the same player";
	private static final int trophyTimeDefault = 24;

	private Configuration conf;

	public boolean friendlyfire;
	public int bountyTimeRec;
	public int trophyTimeRec;

	public Config(Configuration config)
	{
		this.conf = config;
		this.init(config);
	}

	private void init(Configuration config)
	{
		config.load();

		boolean friendlyfireRec = config.getBoolean(clanFF, cat, clanFFDefault, clanFFC);
		int bountyTimeRec = config.getInt(bountyTime, cat, bountyTimeDefault, minGen, maxGen,
				bountyTimeC);
		int trophyTimeRec = config.getInt(trophyTime, cat, trophyTimeDefault, minGen, maxGen,
				trophyTimeC);

		this.friendlyfire = friendlyfireRec;
		this.bountyTimeRec = bountyTimeRec;
		this.trophyTimeRec = trophyTimeRec;

		PlayerEventHandler.friendlyfire = friendlyfireRec;
		PlayerData.bountyTime = bountyTimeRec;
		PlayerData.trophyTime = trophyTimeRec;

		config.save();
	}
}
