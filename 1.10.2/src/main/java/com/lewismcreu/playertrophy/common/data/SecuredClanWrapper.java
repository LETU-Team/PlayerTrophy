package com.lewismcreu.playertrophy.common.data;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import com.lewismcreu.playertrophy.common.data.Clan.Member;
import com.lewismcreu.playertrophy.common.data.Clan.Rank;
import com.lewismcreu.playertrophy.util.CollectionUtil;

import net.minecraft.util.math.ChunkPos;

public class SecuredClanWrapper
{
	private final Clan clan;

	public SecuredClanWrapper(Clan clan)
	{
		this.clan = clan;
	}

	public int getId()
	{
		return clan.getId();
	}

	public String getName()
	{
		return clan.getName();
	}

	public void setName(UUID executor, String name)
	{
		if (hasRight(executor, Right.MANAGE)) clan.setName(name);
	}

	public void addMember(UUID uuid)
	{
		if (!hasMember(uuid))
		{
			members.add(new Member(uuid, defaultRank));
			markDirty();
		}
	}

	public boolean hasMember(UUID uuid)
	{
		return findMember(uuid) != null;
	}

	public Member findMember(UUID uuid)
	{
		return CollectionUtil.find(members, m -> m.uuid, uuid);
	}

	public Set<Member> getMembers()
	{
		return Collections.unmodifiableSet(members);
	}

	public void removeMember(UUID uuid)
	{
		Member m = findMember(uuid);
		if (members.remove(m)) markDirty();
	}

	public void addRank(Rank rank)
	{
		ranks.add(rank);
		markDirty();
	}

	public void removeRank(Rank rank)
	{
		ranks.remove(rank);
		markDirty();
	}

	public Set<Rank> getRanks()
	{
		return Collections.unmodifiableSet(ranks);
	}

	public Rank findRank(int id)
	{
		return CollectionUtil.find(ranks, r -> r.getId(), id);
	}

	public Rank findRank(String name)
	{
		return CollectionUtil.find(ranks, r -> r.getName(), name);
	}

	public Rank getDefaultRank()
	{
		return defaultRank;
	}

	public void setDefaultRank(Rank defaultRank)
	{
		this.defaultRank = defaultRank;
		markDirty();
	}

	public boolean hasRight(UUID uuid, Right right)
	{
		return clan.hasRight(uuid, right);
	}

	public void claimChunk(ChunkPos pos)
	{
		claimedChunks.add(pos);
		markDirty();
	}

	public void unclaimChunk(ChunkPos pos)
	{
		claimedChunks.remove(pos);
		markDirty();
	}

	public Set<ChunkPos> getClaimedChunks()
	{
		return Collections.unmodifiableSet(claimedChunks);
	}

	public class SecuredRankWrapper
	{

	}

	public class SecuredMemberWrapper
	{

	}
}
