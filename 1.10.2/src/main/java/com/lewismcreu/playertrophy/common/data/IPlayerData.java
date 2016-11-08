package com.lewismcreu.playertrophy.common.data;

import java.util.Collection;
import java.util.UUID;

public interface IPlayerData
{
	public Clan getClan();

	public void setClan(Clan clan);

	public UUID getUUID();

	public Collection<Clan> getInvites();

	public void addInvite(Clan clan);

	public default void copy(IPlayerData data)
	{
		setClan(data.getClan());
		for (Clan c : data.getInvites())
			addInvite(c);
	}
}
