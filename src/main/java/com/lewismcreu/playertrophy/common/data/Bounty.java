package com.lewismcreu.playertrophy.common.data;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.lewismcreu.playertrophy.PlayerTrophy;
import com.lewismcreu.playertrophy.util.NBTable;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * @author Lewis_McReu
 */
public class Bounty implements NBTable<Bounty>
{
	private UUID uuid;
	private Collection<BountyReward> rewards;

	public Bounty()
	{
		rewards = Lists.newArrayList();
	}

	public Bounty(UUID uuid)
	{
		this();
		this.uuid = uuid;
	}

	public void setUuid(UUID uuid)
	{
		this.uuid = uuid;
	}

	public UUID getUuid()
	{
		return uuid;
	}

	public Collection<BountyReward> getRewards()
	{
		return Collections.unmodifiableCollection(rewards);
	}

	public void addReward(BountyReward reward)
	{
		rewards.add(reward);
		markDirty();
	}

	private static final String uuidKey = "uuid", rewardKey = "rewards";

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setUniqueId(uuidKey, uuid);
		NBTTagList list = new NBTTagList();
		for (BountyReward br : rewards)
			list.appendTag(br.writeToNBT());
		nbt.setTag(rewardKey, list);
	}

	@Override
	public Bounty readFromNBT(NBTTagCompound nbt)
	{
		setUuid(nbt.getUniqueId(uuidKey));
		NBTTagList list = nbt.getTagList(rewardKey, NBT.TAG_COMPOUND);
		for (int i = 0; i < list.tagCount(); i++)
			rewards.add(
					new BountyReward().readFromNBT(list.getCompoundTagAt(i)));

		return this;
	}

	public class BountyReward
			implements Iterable<ItemStack>, NBTable<BountyReward>
	{
		private Collection<ItemStack> stacks;

		public BountyReward()
		{
			stacks = Lists.newArrayList();
		}

		public BountyReward(Collection<ItemStack> reward)
		{
			this();
			stacks.addAll(reward);
		}

		public BountyReward(ItemStack... reward)
		{
			this();
			for (ItemStack i : reward)
				stacks.add(i);
		}

		public Collection<ItemStack> getStacks()
		{
			return Collections.unmodifiableCollection(stacks);
		}

		@Override
		public Iterator<ItemStack> iterator()
		{
			return getStacks().iterator();
		}

		private static final String inventoryKey = "inventory";

		@Override
		public void writeToNBT(NBTTagCompound nbt)
		{
			NBTTagList list = new NBTTagList();
			for (ItemStack i : stacks)
				list.appendTag(i.writeToNBT(new NBTTagCompound()));
			nbt.setTag(inventoryKey, list);
		}

		@Override
		public BountyReward readFromNBT(NBTTagCompound nbt)
		{
			NBTTagList list = nbt.getTagList(inventoryKey, NBT.TAG_COMPOUND);
			for (int i = 0; i < list.tagCount(); i++)
				stacks.add(ItemStack
						.loadItemStackFromNBT(list.getCompoundTagAt(i)));

			return this;
		}
	}

	public void markDirty()
	{
		PlayerTrophy.getData().markDirty();
	}
}
