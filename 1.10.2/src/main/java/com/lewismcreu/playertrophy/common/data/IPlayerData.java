package com.lewismcreu.playertrophy.common.data;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface IPlayerData
{
	public Clan getClan();

	public void setClan(Clan clan);

	public default void acceptInvite(Clan clan)
	{
		if (!hasClan() && getInvites().contains(clan))
		{
			removeInvite(clan);
			setClan(clan);
		}
	}

	public default boolean hasClan()
	{
		return getClan() != null;
	}

	public UUID getUUID();

	public Collection<Clan> getInvites();

	public void addInvite(Clan clan);

	public void removeInvite(Clan clan);

	public default void copy(IPlayerData data)
	{
		setClan(data.getClan());
		for (Clan c : data.getInvites())
			addInvite(c);
	}

	public Map<UUID, Long> getLastKills();

	// public long getLastBountySetTime();
	//
	// public void setLastBountySetTime(long time);
}
