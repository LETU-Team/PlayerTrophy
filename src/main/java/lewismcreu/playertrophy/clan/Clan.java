package lewismcreu.playertrophy.clan;

import lewismcreu.playertrophy.PlayerTrophy;
import lewismcreu.playertrophy.player.PlayerData;
import lewismcreu.playertrophy.proxy.CommonProxy;
import lewismcreu.playertrophy.util.Logger;
import net.minecraft.entity.player.EntityPlayer;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author Lewis_McReu
 */
public class Clan
{
	private String name;

	private Map<UUID, Rank> members;
	private Set<Rank> ranks;
	private Rank defaultRank;

	public Clan(String name, UUID founder)
	{
		this.members = new HashMap<UUID, Rank>();
		this.ranks = new HashSet<Rank>();
		this.setName(name);
		Rank r = new Rank("Admin");
		for (Right right : Right.values())
		{
			r.addRight(right);
		}
		this.ranks.add(r);
		this.setDefaultRank(new Rank("Member"));
		this.ranks.add(this.getDefaultRank());
		this.addMember(founder, r);
	}

	public Clan(String name, Collection<Rank> ranks, Map<UUID, String> members, Rank defaultRank)
	{
		this.setName(name);
		this.ranks = new HashSet<Rank>(ranks);
		this.defaultRank = defaultRank;
		this.members = new HashMap<UUID, Rank>();
		for (Entry<UUID, String> e : members.entrySet())
		{
			this.members.put(e.getKey(), getRank(e.getValue()));
		}
	}

	public String getName()
	{
		return name;
	}

	private void setName(String name)
	{
		this.name = name;
	}

	public Rank getDefaultRank()
	{
		return defaultRank;
	}

	public void setDefaultRank(Rank defaultRank)
	{
		this.defaultRank = defaultRank;
	}

	private void addMember(UUID uuid, Rank r)
	{
		members.put(uuid, r);
	}

	private boolean checkPermission(Right right, UUID source)
	{
		return members.get(source) != null && members.get(source).hasRight(right);
	}

	public void addRank(Rank rank, UUID source)
	{
		if (checkPermission(Right.MANAGE, source)) if (!this.ranks.add(rank)) throw new IllegalArgumentException(
				"playertrophy.command.rankfound");
		else throw new IllegalArgumentException("playertrophy.command.invalidpermissions");
	}

	public void updateRank(Rank rank, UUID source)
	{
		if (checkPermission(Right.MANAGE, source))
		{
			this.ranks.remove(rank);
			this.ranks.add(rank);
		}
		else throw new IllegalArgumentException("playertrophy.command.invalidpermissions");
	}

	public void setDefaultRank(Rank rank, UUID source)
	{
		if (checkPermission(Right.MANAGE, source)) this.defaultRank = rank;
		else throw new IllegalArgumentException("playertrophy.command.invalidpermissions");
	}

	public void removeRank(Rank rank, UUID source)
	{
		if (checkPermission(Right.MANAGE, source))
		{
			if (!rank.equals(defaultRank))
			{
				Iterator<Entry<UUID, Rank>> it = members.entrySet().iterator();
				while (it.hasNext())
				{
					Entry<UUID, Rank> e = it.next();
					if (e.getValue().equals(rank)) e.setValue(defaultRank);
				}
				this.ranks.remove(rank);
			}
			else throw new IllegalArgumentException("playertrophy.command.requiredefaultrank");
		}
		else throw new IllegalArgumentException("playertrophy.command.invalidpermissions");
	}

	public Rank getRank(String name)
	{
		for (Rank r : ranks)
		{
			if (r.getName().equals(name)) return r;
		}
		throw new IllegalArgumentException("playertrophy.command.ranknotfound");
	}

	public Iterable<Rank> getRanks()
	{
		return this.ranks;
	}

	public void setPlayerRank(UUID target, Rank rank, UUID source)
	{
		if (checkPermission(Right.MANAGE, source) && this.members.containsKey(target)) this.members
				.put(target, rank);
		else throw new IllegalArgumentException("playertrophy.command.invalidpermissions");
	}

	public void addMember(UUID uuid)
	{
		members.put(uuid, defaultRank);
	}

	public boolean removeMember(UUID target, UUID source)
	{
		if (target.equals(source) || members.get(source).hasRight(Right.KICK) || (members.get(
				target).equals(defaultRank) && checkPermission(Right.KICKDEFAULT, source)))
		{
			Logger.info(target.toString());
			Logger.info(members.containsKey(target));
			members.remove(target);
			return true;
		}
		return false;
	}

	public Collection<Entry<UUID, Rank>> getMembers()
	{
		return this.members.entrySet();
	}

	public Rank getMemberRank(UUID uuid)
	{
		Rank rank = this.members.get(uuid);
		if (rank == null) rank = this.defaultRank;
		return rank;
	}

	public boolean delete(UUID source)
	{
		if (checkPermission(Right.DISBAND, source))
		{
			delete();
			return true;
		}
		return false;
	}

	public void delete()
	{
		PlayerTrophy.getInstance().getData().removeClan(this);
		for (UUID uuid : members.keySet())
		{
			EntityPlayer player = CommonProxy.getPlayerData(uuid);
			if (player != null)
			{
				PlayerData data = (PlayerData) player.getExtendedProperties("playertrophy");
				data.leaveClan();
			}
		}
	}

	public boolean hasMember(UUID uuid)
	{
		Logger.info(uuid.toString());
		return this.members.containsKey(uuid);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Clan) return this.getName().equals(((Clan) obj).getName());
		return false;
	}

	@Override
	public String toString()
	{
		return this.name;
	}
}
