package com.lewismcreu.playertrophy.rework;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.lewismcreu.playertrophy.clan.Clan;
import com.lewismcreu.playertrophy.util.Reference;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;

public class GlobalData extends WorldSavedData
{
	public static GlobalData loadFromWorld(World world)
	{
		GlobalData data = (GlobalData) world.getMapStorage().loadData(GlobalData.class, "playertrophy");
		if (data == null)
		{
			data = new GlobalData();
			world.getMapStorage().setData("playertrophy", data);
		}
		return data;
	}

	private Map<String, Clan> clans;
	private Set<Bounty> bounties;

	public GlobalData()
	{
		super(Reference.MOD_ID);
		clans = new HashMap<>();
		bounties = new HashSet<>();
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		// TODO Auto-generated method stub

	}

	public static class WorldData extends WorldSavedData
	{
		public static WorldData loadFromWorld(World world)
		{
			WorldData data = (WorldData) world.getPerWorldStorage().loadData(WorldData.class, "playertrophy");
			if (data == null)
			{
				data = new WorldData();
				world.getMapStorage().setData("playertrophy", data);
			}
			return data;
		}

		private Set<ClaimedChunk> claimedChunks;

		public WorldData()
		{
			super(Reference.MOD_ID);
			claimedChunks = new HashSet<>();
		}

		@Override
		public void readFromNBT(NBTTagCompound nbt)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void writeToNBT(NBTTagCompound nbt)
		{
			// TODO Auto-generated method stub

		}
	}
}
