package com.lewismcreu.playertrophy.util;

import net.minecraft.nbt.NBTTagCompound;

public interface NBTable
{
	/**
	 * Rules for use: the given NBTTagCompound should be directly written to;
	 * the NBTTagCompound should be mapped by the caller method to the key specified if relevant
	 * 
	 * @param nbt
	 *            an NBTTagCompound for the data to be written to
	 */
	public NBTTagCompound writeToNBT(NBTTagCompound nbt);

	/**
	 * Rules for use: the given NBTTagCompound should contain all data contained to be loaded into this object directly
	 * 
	 * @param nbt
	 *            an NBTTagCompound to read the data from
	 */
	public void readFromNBT(NBTTagCompound nbt);

	/**
	 * returns a key for use by classes calling upon writeToNBT or loadFromNBT to use a key preferred for this class
	 * not relevant in the case of NBTTagList containing a number of objects of this type in NBT form
	 * 
	 * @return
	 */
	public String getPreferredKey();
}
