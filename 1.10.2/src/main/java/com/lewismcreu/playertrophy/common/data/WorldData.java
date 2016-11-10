package com.lewismcreu.playertrophy.common.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import com.lewismcreu.playertrophy.common.data.Bounty.BountyReward;
import com.lewismcreu.playertrophy.util.CollectionUtil;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * @author Lewis_McReu
 */
public class WorldData extends WorldSavedData
{
	public static WorldData loadFromWorld(World world)
	{
		WorldData data = (WorldData) world.getMapStorage()
				.getOrLoadData(WorldData.class, "playertrophy");
		if (data == null)
		{
			data = new WorldData();
			world.getMapStorage().setData("playertrophy", data);
		}
		return data;
	}

	public WorldData()
	{
		super("playertrophy");
		clanIdCounter = 0;
		clans = new ArrayList<>();
		claimedChunks = new TreeSet<>(new Comparator<ChunkPos>()
		{
			@Override
			public int compare(ChunkPos arg0, ChunkPos arg1)
			{
				return arg0.chunkXPos * arg1.chunkZPos;
			}
		});
	}

	private int clanIdCounter;

	public int nextClanId()
	{
		int id = clanIdCounter++;
		clanIdCounter++;
		return id;
	}

	private Collection<Clan> clans;
	private Set<ChunkPos> claimedChunks;

	public Collection<ChunkPos> getClaimedChunks()
	{
		return Collections.unmodifiableSet(claimedChunks);
	}

	public void claimChunk(Clan clan, ChunkPos pos)
	{
		claimedChunks.add(pos);
		clan.claimChunk(pos);
		markDirty();
	}

	public void unclaimChunk(Clan clan, ChunkPos pos)
	{
		claimedChunks.remove(pos);
		clan.unclaimChunk(pos);
		markDirty();
	}

	public Clan createClan(UUID creator)
	{
		Clan c = Clan.createClan(creator);
		clans.add(c);
		markDirty();
		return c;
	}

	public Clan findClan(int id)
	{
		return CollectionUtil.find(clans, c -> c.getId(), id);
	}

	public Clan findClan(ChunkPos pos)
	{
		if (!claimedChunks.contains(pos)) for (Clan c : clans)
			if (c.getClaimedChunks().contains(pos)) return c;

		return null;
	}

	public Clan findClan(BlockPos pos)
	{
		return findClan(new ChunkPos(pos));
	}

	public Collection<Clan> getClans()
	{
		return Collections.unmodifiableCollection(clans);
	}

	private Collection<Bounty> bounties;

	public Collection<Bounty> getBounties()
	{
		return Collections.unmodifiableCollection(bounties);
	}

	public void addBounty(UUID uuid, Collection<ItemStack> reward)
	{
		Bounty b = findBounty(uuid);
		if (b == null) b = new Bounty(uuid);
		BountyReward br = b.new BountyReward(reward);
		b.addReward(br);
		markDirty();
	}

	public Bounty findBounty(UUID uuid)
	{
		return CollectionUtil.find(bounties, b -> b.getUuid(), uuid);
	}

	private static final String clanCounterKey = "clancounter",
			clanKey = "clans", bountyKey = "bounties";

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		clanIdCounter = nbt.getInteger(clanCounterKey);
		NBTTagList clanList = nbt.getTagList(clanKey, NBT.TAG_COMPOUND);
		for (int i = 0; i < clanList.tagCount(); i++)
			clans.add(new Clan().readFromNBT(clanList.getCompoundTagAt(i)));

		NBTTagList bountyList = nbt.getTagList(bountyKey, NBT.TAG_COMPOUND);
		for (int i = 0; i < bountyList.tagCount(); i++)
			bounties.add(
					new Bounty().readFromNBT(bountyList.getCompoundTagAt(i)));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.setInteger(clanCounterKey, clanIdCounter);
		NBTTagList clanList = new NBTTagList();
		for (Clan c : clans)
			clanList.appendTag(c.writeToNBT());
		compound.setTag(clanKey, clanList);

		NBTTagList bountyList = new NBTTagList();
		for (Bounty b : bounties)
			bountyList.appendTag(b.writeToNBT());
		compound.setTag(bountyKey, bountyList);

		return compound;
	}
}
