package lewismcreu.playertrophy.player;

import lewismcreu.playertrophy.PlayerTrophy;
import lewismcreu.playertrophy.clan.Clan;
import lewismcreu.playertrophy.util.Logger;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author Lewis_McReu
 */
public class PlayerData implements IExtendedEntityProperties
{
	public static int bountyTime;
	public static int trophyTime;

	private long lastBountyTime;
	private HashMap<String, Long> lastPlayersKilled;
	private Clan clan;
	private UUID persistentUuid;
	private List<Clan> invites = new ArrayList<Clan>();

	public PlayerData(UUID persistentUuid)
	{
		Logger.info("Called constructor");
		this.persistentUuid = persistentUuid;
		lastBountyTime = 0;
		lastPlayersKilled = new HashMap<String, Long>(10);
		if (FMLCommonHandler.instance().getSide().isServer())
		{
			clan = PlayerTrophy.getInstance().getData().getClanForUUID(persistentUuid);
			Logger.info("Motherf*cker");
		}
		Logger.info(clan);
	}

	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		NBTTagCompound data = new NBTTagCompound();
		data.setLong("lastBountyTime", lastBountyTime);
		data.setString("lastPlayersKilled", lastKillsToString());
	}

	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		NBTTagCompound data = compound.getCompoundTag("playertrophy");
		lastBountyTime = data.getLong("lastBountyTime");
		parseLastKills(data.getString("lastPlayersKilled"));
	}

	@Override
	public void init(Entity entity, World world)
	{
		Logger.info("Called init");
	}

	public void registerKill(String nameSource)
	{
		this.lastPlayersKilled.put(nameSource, System.currentTimeMillis());
	}

	public void registerBounty()
	{
		this.lastBountyTime = System.currentTimeMillis();
	}

	public boolean canPlaceBounty()
	{
		return System.currentTimeMillis() - PlayerData.bountyTime * 60 * 60 * 1000 >= this.lastBountyTime;
	}

	private String lastKillsToString()
	{
		String out = "";
		Iterator<Entry<String, Long>> i = lastPlayersKilled.entrySet().iterator();

		while (i.hasNext())
		{
			Entry<String, Long> e = i.next();
			out += e.getKey() + "=" + e.getValue().toString();
			if (i.hasNext()) out += ",";
		}

		return out;
	}

	private void parseLastKills(String string)
	{
		Scanner s = new Scanner(string);
		s.useDelimiter(",");
		while (s.hasNext())
		{
			Scanner s2 = new Scanner(s.next());
			s2.useDelimiter("=");
			lastPlayersKilled.put(s2.next(), Long.parseLong(s2.next()));
			s2.close();
		}
		s.close();
	}

	public boolean canGetTrophy(String uuid)
	{
		return !lastPlayersKilled.containsKey(uuid) || (lastPlayersKilled.get(uuid).longValue() < Minecraft
				.getSystemTime() - trophyTime * 60 * 60 * 1000);
	}

	public List<String> getLastPlayersKilled()
	{
		return new ArrayList<String>(lastPlayersKilled.keySet());
	}

	public Clan getClan()
	{
		return clan;
	}

	public void setClan(Clan clan)
	{
		this.clan = clan;
	}

	public void leaveClan()
	{
		if (clan != null)
		{
			this.clan.removeMember(persistentUuid, persistentUuid);
			if (clan.getMembers().size() == 0) clan.delete();
			this.removeClan();
		}
	}

	public boolean isInClan()
	{
		return clan != null;
	}

	public void invite(Clan c)
	{
		this.invites.add(c);
	}

	public void removeClan()
	{
		this.clan = null;
	}

	public List<Clan> getInvites()
	{
		return this.invites;
	}

	public void accept(Clan clan)
	{
		this.clan = clan;
		this.invites.remove(clan);
		clan.addMember(this.persistentUuid);
	}

	public void setUUID(UUID uuid)
	{
		this.persistentUuid = uuid;
	}
}
