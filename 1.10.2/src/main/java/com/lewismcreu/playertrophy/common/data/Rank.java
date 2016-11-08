package com.lewismcreu.playertrophy.common.data;

import java.util.Collection;
import java.util.Collections;

import com.google.common.collect.Sets;
import com.lewismcreu.playertrophy.util.NBTable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants.NBT;

/**
 * @author Lewis_McReu
 */
public class Rank implements NBTable<Rank>
{
	private int id;
	private String name;
	private Collection<Right> rights;

	public Rank(String name, Right... rights)
	{
		this();
		this.name = name;
		Collections.addAll(this.rights, rights);
	}

	public Rank()
	{
		rights = Sets.newHashSet();
	}

	void setId(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	public String getName()
	{
		return name;
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

	void removeRight(Right right)
	{
		rights.remove(right);
	}

	void addRight(Right right)
	{
		if (right != null) rights.add(right);
	}

	private static final String idKey = "id", nameKey = "name", rightsKey = "rights";

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
