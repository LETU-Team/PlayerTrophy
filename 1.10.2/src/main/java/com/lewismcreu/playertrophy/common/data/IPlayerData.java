package com.lewismcreu.playertrophy.common.data;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

/**
 * @author Lewis_McReu
 */
public interface IPlayerData
{
	public Clan getClan();

	public void setClan(Clan clan);

	public default boolean hasRight(Right right)
	{
		Clan c = getClan();
		if (c == null) return false;
		return c.hasRight(getUUID(), right);
	}

	public default void acceptInvitation(Clan clan)
	{
		if (!hasClan() && getInvitations().contains(clan))
		{
			removeInvitation(clan);
			setClan(clan);
		}
	}

	public default void acceptInvitation(Collection<Clan> possibleClans)
	{
		for (Clan p : possibleClans)
			for (Clan c : getInvitations())
				if (p == c) acceptInvitation(p);
	}

	public default boolean hasClan()
	{
		return getClan() != null;
	}

	public UUID getUUID();

	public Collection<Clan> getInvitations();

	public void addInvitation(Clan clan);

	public void removeInvitation(Clan clan);

	public void leave();

	public Map<UUID, Long> getLastKills();

	public long getLastBountySetTime();

	public void setLastBountySetTime(long time);

	public default void copy(IPlayerData data)
	{
		setClan(data.getClan());
		for (Clan c : data.getInvitations())
			addInvitation(c);
	}

	public void setUUID(UUID uuid);
}
