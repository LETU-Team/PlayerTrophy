package com.lewismcreu.playertrophy.rework;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.lewismcreu.playertrophy.clan.Rank;
import com.lewismcreu.playertrophy.clan.Right;
import com.lewismcreu.playertrophy.util.NBTable;

import net.minecraft.nbt.NBTTagCompound;

public class Clan implements NBTable
{
	private static Rank DEFAULT = new Rank("Member");
	private static Rank ADMIN = new Rank("Owner");
	static
	{
		ADMIN.addRight(Right.CLAIM, Right.DISBAND, Right.INVITE, Right.KICK, Right.KICKDEFAULT, Right.MANAGE);
	}

	private String name;
	private Map<String, Rank> members;
	private Set<Rank> ranks;
	private Rank defaultRank;

	public Clan()
	{
		this.members = new HashMap<>();
		this.ranks = new HashSet<>();
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public Map<String, Rank> getMembers()
	{
		return members;
	}

	public void addMember(UUID uuid)
	{
		this.members.put(uuid.toString(), getDefaultRank());
	}

	public Set<Rank> getRanks()
	{
		return ranks;
	}

	public boolean addRank(Rank rank)
	{
		return this.ranks.add(rank);
	}

	public Rank getDefaultRank()
	{
		return defaultRank;
	}

	public void setDefaultRank(Rank defaultRank)
	{
		this.defaultRank = defaultRank;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public String getPreferredKey()
	{
		return "clan";
	}
}
