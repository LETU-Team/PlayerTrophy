package com.lewismcreu.playertrophy.common.data;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lewismcreu.playertrophy.PlayerTrophy;
import com.lewismcreu.playertrophy.proxy.CommonProxy;
import com.lewismcreu.playertrophy.util.CollectionUtil;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 * @author Lewis_McReu
 */
public class PlayerData implements IPlayerData
{
	private Clan clan;
	private UUID uuid;
	private Collection<Clan> invites;

	public PlayerData()
	{
		clan = null;
		uuid = null;
		invites = Sets.newHashSet();
		lastKills = Maps.newHashMap();
	}

	public PlayerData(UUID uuid)
	{
		this();
		this.uuid = uuid;
	}

	@Override
	public Clan getClan()
	{
		if (!clan.hasMember(getUUID())) setClan(null);
		return clan;
	}

	@Override
	public void setClan(Clan clan)
	{
		this.clan = clan;
	}

	@Override
	public UUID getUUID()
	{
		return uuid;
	}

	@Override
	public Collection<Clan> getInvites()
	{
		return Collections.unmodifiableCollection(invites);
	}

	@Override
	public void addInvitation(Clan clan)
	{
		invites.add(clan);
		clan.addInvitation(uuid);
	}

	@Override
	public void removeInvitation(Clan clan)
	{
		invites.remove(clan);
		clan.removeInvitation(uuid);
	}

	@Override
	public void acceptInvitation(Clan clan)
	{
		if (invites.contains(clan))
		{
			removeInvitation(clan);
			setClan(clan);
		}
	}

	@Override
	public void leave()
	{
		if (hasClan())
		{
				getClan().removeMember(uuid);
				setClan(null);
		}
	}

	private Map<UUID, Long> lastKills;

	@Override
	public Map<UUID, Long> getLastKills()
	{
		return Collections.unmodifiableMap(lastKills);
	}

	public static class PlayerDataStorage implements IStorage<IPlayerData>
	{
		private static final String clanKey = "clan", inviteKey = "invites";

		@Override
		public NBTBase writeNBT(Capability<IPlayerData> capability, IPlayerData instance, EnumFacing side)
		{
			NBTTagCompound nbt = new NBTTagCompound();
			nbt.setInteger(clanKey, instance.getClan().getId());
			int[] arr = new int[instance.getInvites().size()];
			int i = 0;
			Iterator<Clan> it = instance.getInvites().iterator();
			while (it.hasNext())
			{
				arr[i++] = it.next().getId();
			}
			nbt.setIntArray(inviteKey, arr);
			return nbt;
		}

		@Override
		public void readNBT(Capability<IPlayerData> capability, IPlayerData instance, EnumFacing side, NBTBase nbtbase)
		{
			NBTTagCompound nbt = (NBTTagCompound) nbtbase;
			instance.setClan(PlayerTrophy.getData().findClan(nbt.getInteger(clanKey)));
			Collection<Integer> col = Lists.newArrayList();
			for (int id : nbt.getIntArray(inviteKey))
				col.add(id);
			for (Clan c : CollectionUtil.convert(col, id -> PlayerTrophy.getData().findClan(id)))
				instance.addInvitation(c);
		}
	}

	public static class PlayerDataProvider implements ICapabilitySerializable<NBTTagCompound>
	{
		IPlayerData instance = CommonProxy.playerDataCapability.getDefaultInstance();

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing)
		{
			return CommonProxy.playerDataCapability == capability;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing)
		{
			return hasCapability(capability, facing) ? CommonProxy.playerDataCapability.cast(instance) : null;
		}

		@Override
		public NBTTagCompound serializeNBT()
		{
			return (NBTTagCompound) CommonProxy.playerDataCapability.getStorage().writeNBT(
					CommonProxy.playerDataCapability, instance, null);
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt)
		{
			CommonProxy.playerDataCapability.getStorage().readNBT(CommonProxy.playerDataCapability, instance, null,
					nbt);
		}
	}

	private long lastBountySetTime;

	@Override
	public long getLastBountySetTime()
	{
		return lastBountySetTime;
	}

	@Override
	public void setLastBountySetTime(long lastBountySetTime)
	{
		this.lastBountySetTime = lastBountySetTime;
	}
}
