package lewismcreu.playertrophy.world;

import lewismcreu.playertrophy.PlayerTrophy;
import lewismcreu.playertrophy.clan.Clan;
import lewismcreu.playertrophy.proxy.CommonProxy;
import lewismcreu.playertrophy.util.Logger;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Lewis_McReu
 */
public class WorldData
{
	private File worldDataFile;
	private HashMap<String, Clan> clans;
	private HashMap<UUID, Integer> bounties;

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

	private WorldData(File worldDataFile, HashMap<String, Clan> hashSet, HashMap<UUID, Integer> bounties)
	{
		this.worldDataFile = worldDataFile;
		this.clans = hashSet;
		this.bounties = bounties;
		Logger.info("WorldData created");
	}

	public WorldData(File worldDataFile)
	{
		this(worldDataFile, new HashMap<String, Clan>(), new HashMap<UUID, Integer>());
	}

	public void addBounty(UUID uuid)
	{
		Integer i = bounties.put(uuid, 1);
		if (i != null) bounties.put(uuid, i.intValue() + 1);
		PlayerTrophy.proxy.broadcastChatMessage("A bounty was put on " + CommonProxy
				.getNameForUuid(uuid) + ".");
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
			if (c.hasMember(uuid))
			{
				Logger.info("Yippiekayee");
				return c;
			}
		}
		return null;
	}
}
