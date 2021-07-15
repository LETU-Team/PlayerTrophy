package lewismcreu.playertrophy.util;

import net.minecraft.nbt.NBTTagCompound;

/**
 * @author Lewis_McReu
 * @param <E>
 *            the implementing class
 */
public interface NBTable<E extends NBTable<E>>
{
	/**
	 * Rules for use: the given NBTTagCompound should be directly written to;
	 * the implementation should only provide the writing to the NBTTagCompound, in the assumption there will be no other data but the data of this class stored in the NBTTagCompound
	 * 
	 * @param nbt
	 *            an NBTTagCompound for the data to be written to
	 */
	public void writeToNBT(NBTTagCompound nbt);

	/**
	 * Utility method that will create a new NBTTagCompound, call writeToNBT(nbt) on it, and return the result.
	 * 
	 * @return the newly created NBTTagCompound
	 */
	public default NBTTagCompound writeToNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		writeToNBT(nbt);
		return nbt;
	}

	/**
	 * Rules for use: the given NBTTagCompound should contain all data contained to be loaded into this object directly
	 * the implementation should only provide the reading to the class, in the assumption there will be no other data but the data of this class stored in the NBTTagCompound
	 * 
	 * @param nbt
	 *            an NBTTagCompound to read the data from
	 * @return should return the instance of NBTable that was loaded from nbt
	 */
	public E readFromNBT(NBTTagCompound nbt);
}
