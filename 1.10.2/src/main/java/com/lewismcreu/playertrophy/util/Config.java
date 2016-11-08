package com.lewismcreu.playertrophy.util;

import net.minecraftforge.common.config.Configuration;

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
	private static final String trophyTimeC =
			"set the minimum time (in hours) between getting trophies from killing the same player";
	private static final int trophyTimeDefault = 24;

	private static boolean friendlyfire;
	private static int bountyTimeRec;
	private static int trophyTimeRec;

	public static void init(Configuration config)
	{
		config.load();
		config.setCategoryRequiresMcRestart(cat, true);
		friendlyfire = config.getBoolean(clanFF, cat, clanFFDefault, clanFFC);
		bountyTimeRec = config.getInt(bountyTime, cat, bountyTimeDefault, minGen, maxGen, bountyTimeC);
		trophyTimeRec = config.getInt(trophyTime, cat, trophyTimeDefault, minGen, maxGen, trophyTimeC);
	}

	public static boolean getFriendlyFire()
	{
		return friendlyfire;
	}

	public static int getBountyTime()
	{
		return bountyTimeRec;
	}

	public static int getTrophyTime()
	{
		return trophyTimeRec;
	}
}
