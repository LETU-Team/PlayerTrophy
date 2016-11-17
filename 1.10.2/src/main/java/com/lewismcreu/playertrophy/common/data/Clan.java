package com.lewismcreu.playertrophy.common.data;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.Sets;
import com.lewismcreu.playertrophy.PlayerTrophy;
import com.lewismcreu.playertrophy.util.CollectionUtil;
import com.lewismcreu.playertrophy.util.NBTable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
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
		c.addRank(c.new Rank("Default"));
		c.addRank(c.new Rank("Owner", Right.values()));
		c.addMember(creator);
		c.findMember(creator).rank = c.findRank("Owner");
		c.setId(PlayerTrophy.getData().nextClanId());
		return c;
	}

	private int rankIdCounter = 0;

	private int id;
	private String name;
	private Set<Member> members;
	private Set<Rank> ranks;
	private Rank defaultRank;
	private Set<ChunkPos> claimedChunks;
	private Set<UUID> invitations;

	protected Clan()
	{
		members = new HashSet<>();
		ranks = new HashSet<>();
		claimedChunks = new HashSet<>();
		invitations = new HashSet<>();
	}

	public int getId()
	{
		return id;
	}

	void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
		markDirty();
	}

	public Set<UUID> getInvitations()
	{
		return Collections.unmodifiableSet(invitations);
	}

	public void addInvitation(UUID uuid)
	{
		if (invitations.add(uuid)) markDirty();
	}

	public void removeInvitation(UUID uuid)
	{
		if (invitations.remove(uuid)) markDirty();
	}

	private void addMember(UUID uuid)
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
		Member m = findMember(uuid);
		if (m != null) return right == Right.NONE || m.hasRight(right);
		return false;
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

	private static final String idKey = "id", membersKey = "members",
			ranksKey = "ranks", rankCounterKey = "rankcounter",
			defaultRankKey = "defaultrank", claimedChunksKey = "claimedchunks",
			xKey = "x", zKey = "z";

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
			members.add(
					new Member().readFromNBT(memberList.getCompoundTagAt(i)));

		NBTTagList chunksList =
				nbt.getTagList(claimedChunksKey, NBT.TAG_COMPOUND);
		for (int i = 0; i < chunksList.tagCount(); i++)
			claimedChunks.add(new ChunkPos(
					chunksList.getCompoundTagAt(i).getInteger(xKey),
					chunksList.getCompoundTagAt(i).getInteger(zKey)));

		defaultRank = findRank(nbt.getInteger(defaultRankKey));

		return this;
	}

	public class Member implements NBTable<Member>
	{
		private static final String uuidKey = "uuid", rankKey = "rank",
				titleKey = "title";

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

		public void setTitle(String title)
		{
			this.title = title;
			markDirty();
		}

		public void setRank(Rank rank)
		{
			if (rank == null) rank = defaultRank;
			this.rank = rank;
			markDirty();
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

	public class Rank implements NBTable<Rank>
	{
		private int id;
		private String name;
		private Collection<Right> rights;

		public Rank(String name, Right... rights)
		{
			this();
			this.id = rankIdCounter++;
			this.name = name;
			Collections.addAll(this.rights, rights);
		}

		public Rank()
		{
			rights = Sets.newHashSet();
		}

		public int getId()
		{
			return id;
		}

		public String getName()
		{
			return name;
		}

		public void setName(String name)
		{
			this.name = name;
			markDirty();
		}

		/**
		 * @return an unmodifiable collection of all rights
		 */
		public Collection<Right> getRights()
		{
			return Collections.unmodifiableCollection(rights);
		}

		public boolean hasRight(Right right)
		{
			return rights.contains(right);
		}

		public void removeRight(Right right)
		{
			if (rights.remove(right)) markDirty();
		}

		public void addRight(Right right)
		{
			if (right != null)
			{
				rights.add(right);
				markDirty();
			}
		}

		private static final String idKey = "id", nameKey = "name",
				rightsKey = "rights";

		@Override
		public void writeToNBT(NBTTagCompound nbt)
		{
			nbt.setInteger(idKey, id);
			nbt.setString(nameKey, name);

			NBTTagList rightList = new NBTTagList();
			for (Right r : rights)
				rightList.appendTag(new NBTTagString(r.name()));
			nbt.setTag(rightsKey, rightList);
		}

		@Override
		public Rank readFromNBT(NBTTagCompound nbt)
		{
			id = nbt.getInteger(idKey);
			name = nbt.getString(nameKey);
			NBTTagList rightList = nbt.getTagList(rightsKey, NBT.TAG_STRING);
			for (int i = 0; i < rightList.tagCount(); i++)
				rights.add(Right.valueOf(rightList.getStringTagAt(i)));

			return this;
		}
	}

	public void markDirty()
	{
		PlayerTrophy.getData().markDirty();
	}
}
