package com.lewismcreu.playertrophy.common.data;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.lewismcreu.playertrophy.util.CollectionUtil;
import com.lewismcreu.playertrophy.util.NBTable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * @author Lewis_McReu
 */
public class Clan implements NBTable<Clan>
{
	static Clan createClan(UUID creator)
	{
		Clan c = new Clan();
		c.addRank(new Rank("Default"));
		c.addRank(new Rank("Owner", Right.values()));
		c.addMember(creator);
		c.findMember(creator).rank = c.findRank("Owner");
		return c;
	}

	private int rankIdCounter = 0;

	private int id;
	private Set<Member> members;
	private Set<Rank> ranks;
	private Rank defaultRank;
	private Set<ChunkPos> claimedChunks;

	protected Clan()
	{
		members = new HashSet<>();
		ranks = new HashSet<>();
		claimedChunks = new HashSet<>();
	}

	public int getId()
	{
		return id;
	}

	void setId(int id)
	{
		this.id = id;
	}

	public void addMember(UUID uuid)
	{
		if (findMember(uuid) == null) members.add(new Member(uuid, defaultRank));
	}

	public Member findMember(UUID uuid)
	{
		return CollectionUtil.find(members, m -> m.uuid, uuid);
	}

	public void setMemberTitle(UUID uuid, String title)
	{
		Member m = findMember(uuid);
		if (m != null) m.setTitle(title);
	}

	public void setMemberRank(UUID uuid, Rank rank)
	{
		Member m = findMember(uuid);
		if (m != null) m.setRank(rank);
	}

	public Set<Member> getMembers()
	{
		return Collections.unmodifiableSet(members);
	}

	public void addRank(Rank rank)
	{
		rank.setId(rankIdCounter++);
		ranks.add(rank);
	}

	public void removeRank(Rank rank)
	{
		ranks.remove(rank);
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

	public void removeRankRight(String name, Right right)
	{
		Rank r = findRank(name);
		if (r != null) r.removeRight(right);
	}

	public Rank getDefaultRank()
	{
		return defaultRank;
	}

	public void setDefaultRank(Rank defaultRank)
	{
		this.defaultRank = defaultRank;
	}

	public boolean hasRight(UUID uuid, Right right)
	{
		Member m = findMember(uuid);
		if (m != null) return m.hasRight(right);
		return false;
	}

	public void claimChunk(ChunkPos pos)
	{
		claimedChunks.add(pos);
	}

	public void unclaimChunk(ChunkPos pos)
	{
		claimedChunks.remove(pos);
	}

	public Set<ChunkPos> getClaimedChunks()
	{
		return Collections.unmodifiableSet(claimedChunks);
	}

	private static final String idKey = "id", membersKey = "members", ranksKey = "ranks", rankCounterKey =
			"rankcounter", defaultRankKey = "defaultrank", claimedChunksKey = "claimedchunks", xKey = "x", zKey = "z";

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger(idKey, id);
		nbt.setInteger(rankCounterKey, rankIdCounter);

		NBTTagList rankList = new NBTTagList();
		for (Rank r : ranks)
			rankList.appendTag(r.writeToNBT());
		nbt.setTag(ranksKey, rankList);

		NBTTagList memberList = new NBTTagList();
		for (Member m : members)
			memberList.appendTag(m.writeToNBT());
		nbt.setTag(membersKey, memberList);

		NBTTagList chunksList = new NBTTagList();
		for (ChunkPos p : claimedChunks)
		{
			NBTTagCompound n = new NBTTagCompound();
			n.setInteger(xKey, p.chunkXPos);
			n.setInteger(zKey, p.chunkZPos);
		}
		nbt.setTag(claimedChunksKey, chunksList);

		nbt.setInteger(defaultRankKey, defaultRank.getId());
	}

	@Override
	public Clan readFromNBT(NBTTagCompound nbt)
	{
		id = nbt.getInteger(idKey);
		rankIdCounter = nbt.getInteger(rankCounterKey);

		NBTTagList rankList = nbt.getTagList(ranksKey, NBT.TAG_COMPOUND);
		for (int i = 0; i < rankList.tagCount(); i++)
			ranks.add(new Rank().readFromNBT(rankList.getCompoundTagAt(i)));

		NBTTagList memberList = nbt.getTagList(membersKey, NBT.TAG_COMPOUND);
		for (int i = 0; i < memberList.tagCount(); i++)
			members.add(new Member().readFromNBT(memberList.getCompoundTagAt(i)));

		NBTTagList chunksList = nbt.getTagList(claimedChunksKey, NBT.TAG_COMPOUND);
		for (int i = 0; i < chunksList.tagCount(); i++)
			claimedChunks.add(new ChunkPos(chunksList.getCompoundTagAt(i).getInteger(xKey), chunksList.getCompoundTagAt(
					i).getInteger(zKey)));

		defaultRank = findRank(nbt.getInteger(defaultRankKey));

		return this;
	}

	public class Member implements NBTable<Member>
	{
		private static final String uuidKey = "uuid", rankKey = "rank", titleKey = "title";

		private UUID uuid;
		private Rank rank;
		private String title;

		public Member(UUID uuid, Rank rank)
		{
			this(uuid, rank, "");
		}

		public Member(UUID uuid, Rank rank, String title)
		{
			this();
			this.uuid = uuid;
			this.rank = rank;
			this.title = title;
		}

		public Member()
		{}

		public UUID getUuid()
		{
			return uuid;
		}

		public Rank getRank()
		{
			return rank;
		}

		public String getTitle()
		{
			return title;
		}

		private void setTitle(String title)
		{
			this.title = title;
		}

		private void setRank(Rank rank)
		{
			this.rank = rank;
		}

		public boolean hasRight(Right right)
		{
			return rank.hasRight(right);
		}

		@Override
		public void writeToNBT(NBTTagCompound nbt)
		{
			nbt.setUniqueId(uuidKey, uuid);
			nbt.setInteger(rankKey, rank.getId());
			nbt.setString(titleKey, title);
		}

		@Override
		public Member readFromNBT(NBTTagCompound nbt)
		{
			uuid = nbt.getUniqueId(uuidKey);
			rank = findRank(nbt.getInteger(rankKey));
			title = nbt.getString(titleKey);
			return this;
		}
	}
}
