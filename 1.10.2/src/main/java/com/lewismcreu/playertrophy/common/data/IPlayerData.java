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

	public default void acceptInvite(Clan clan)
	{
		if (!hasClan() && getInvites().contains(clan))
		{
			removeInvitation(clan);
			setClan(clan);
		}
	}

	public default boolean hasClan()
	{
		return getClan() != null;
	}

	public UUID getUUID();

	public Collection<Clan> getInvites();

	public void addInvitation(Clan clan);

	public void removeInvitation(Clan clan);
	
	public void acceptInvitation(Clan clan);

	public void leave();

	public Map<UUID, Long> getLastKills();

	public long getLastBountySetTime();

	public void setLastBountySetTime(long time);

	public default void copy(IPlayerData data)
	{
		setClan(data.getClan());
		for (Clan c : data.getInvites())
			addInvitation(c);
	}
}
