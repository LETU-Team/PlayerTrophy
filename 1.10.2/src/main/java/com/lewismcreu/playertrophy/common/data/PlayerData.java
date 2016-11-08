package com.lewismcreu.playertrophy.common.data;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.UUID;

import com.google.common.collect.Lists;
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
	}

	public PlayerData(UUID uuid)
	{
		this();
		this.uuid = uuid;
	}

	@Override
	public Clan getClan()
	{
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
	public void addInvite(Clan clan)
	{
		invites.add(clan);
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
			// TODO Auto-generated method stub
			NBTTagCompound nbt = (NBTTagCompound) nbtbase;
			instance.setClan(PlayerTrophy.getInstance().getData().findClan(nbt.getInteger(clanKey)));
			Collection<Integer> col = Lists.newArrayList();
			for (int id : nbt.getIntArray(inviteKey))
				col.add(id);
			for (Clan c : CollectionUtil.convert(col, id -> PlayerTrophy.getInstance().getData().findClan(id)))
				instance.addInvite(c);
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
}
