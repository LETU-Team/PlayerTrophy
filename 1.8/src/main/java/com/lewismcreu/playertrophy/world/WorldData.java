package com.lewismcreu.playertrophy.world;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.lewismcreu.playertrophy.PlayerTrophy;
import com.lewismcreu.playertrophy.clan.Clan;
import com.lewismcreu.playertrophy.proxy.CommonProxy;
import com.lewismcreu.playertrophy.util.Chunk;
import com.lewismcreu.playertrophy.util.Logger;

/**
 * @author Lewis_McReu
 */
public class WorldData
{
	private File worldDataFile;
	private HashMap<String, Clan> clans;
	private HashMap<UUID, Integer> bounties;
	private HashMap<Chunk, Clan> chunks;

	public static WorldData getInstance(File worldDataFile)
	{
		if (worldDataFile.exists())
		{
			WorldDataReader r = new WorldDataReader(worldDataFile);
			return new WorldData(worldDataFile, r.getClans(), r.getBounties());
		}
		else
		{
			try
			{
				worldDataFile.createNewFile();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				Logger.error("Couldn't initialize the data save file.");
			}
			return new WorldData(worldDataFile);
		}
	}

	private WorldData(File worldDataFile, HashMap<String, Clan> clans, HashMap<UUID, Integer> bounties)
	{
		this.worldDataFile = worldDataFile;
		this.clans = clans;
		this.bounties = bounties;
		this.chunks = new HashMap<Chunk, Clan>();
		readChunks();
		Logger.info("WorldData loaded");
	}

	private void readChunks()
	{
		for (Clan clan : clans.values())
			for (Chunk chunk : clan.getChunks())
				chunks.put(chunk, clan);
	}

	public WorldData(File worldDataFile)
	{
		this(worldDataFile, new HashMap<String, Clan>(), new HashMap<UUID, Integer>());
	}

	public void addBounty(UUID uuid)
	{
		Integer i = bounties.put(uuid, 1);
		if (i != null) bounties.put(uuid, i.intValue() + 1);
		PlayerTrophy.proxy.broadcastChatMessage(
				"A bounty was put on " + CommonProxy.getNameForUuid(uuid) + ".");
	}

	public HashMap<UUID, Integer> getBounties()
	{
		return bounties;
	}

	public void addBounty(UUID uuid, int count)
	{
		bounties.put(uuid, count);
	}

	public void removeBounty(String uuid)
	{
		bounties.remove(uuid);
	}

	public int getBounty(String uuid)
	{
		return bounties.get(uuid);
	}

	public void saveData()
	{
		worldDataFile.delete();
		try
		{
			worldDataFile.createNewFile();
		}
		catch (IOException e)
		{
			Logger.error(e.getMessage());
		}
		new WorldDataWriter(worldDataFile, this.clans, this.bounties);
	}

	public Map<String, Clan> getClans()
	{
		return this.clans;
	}

	public void removeClan(Clan c)
	{
		this.clans.remove(c.getName());
	}

	public Clan getClan(String name)
	{
		return clans.get(name);
	}

	public Clan createClan(String clanName, UUID uuid)
	{
		if (!clans.containsKey(clanName))
		{
			Clan c = new Clan(clanName, uuid);
			clans.put(clanName, c);
			return c;
		}
		return null;
	}

	public boolean clanExists(Clan clan)
	{
		return this.clans.containsKey(clan.getName());
	}

	public Clan getClanForUUID(UUID uuid)
	{
		for (Clan c : clans.values())
		{
			if (c.hasMember(uuid)) { return c; }
		}
		return null;
	}

	public Clan getChunkClan(Chunk chunk)
	{
		return chunks.get(chunk);
	}

	public boolean isChunkClaimed(Chunk chunk)
	{
		return chunks.containsKey(chunk);
	}

	public void claimChunk(Chunk chunk, Clan clan)
	{
		this.chunks.put(chunk, clan);
	}
}