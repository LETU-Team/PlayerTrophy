package com.lewismcreu.playertrophy.rework;

import java.util.UUID;

import com.lewismcreu.playertrophy.util.NBTable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class Bounty implements NBTable
{
	private String target, source;
	private ItemStack[] bounty;

	public Bounty(UUID target, UUID source, ItemStack... bounty)
	{
		this.target = target.toString();
		this.source = source.toString();
		this.bounty = bounty;
	}

	public UUID getTarget()
	{
		return UUID.fromString(target);
	}

	public UUID getSource()
	{
		return UUID.fromString(source);
	}

	public ItemStack[] getBounty()
	{
		return bounty;
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
		return "bounty";
	}
}
