package com.lewismcreu.playertrophy.tileentity;

import com.lewismcreu.playertrophy.PlayerTrophy;
import com.lewismcreu.playertrophy.common.data.Clan;
import com.lewismcreu.playertrophy.common.data.Right;
import com.lewismcreu.playertrophy.proxy.CommonProxy;
import com.lewismcreu.playertrophy.util.Logger;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;

/**
 * @author Lewis_McReu
 */
public class TileEntityProtectedChest extends TileEntityChest implements IProtectedTileEntity
{
	private Clan clan;

	public TileEntityProtectedChest()
	{
	}

	public TileEntityProtectedChest(Clan clan, TileEntityChest parent)
	{
		this.clan = clan;
		NBTTagCompound nbt = new NBTTagCompound();
		parent.writeToNBT(nbt);
		readOriginalNBT(nbt);
	}

	private void readOriginalNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
	}

	private void writeOriginalNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt)
	{
		if (clan == null)
		{
			this.remove().writeToNBT(nbt);
			return nbt;
		}
		nbt.setString("clan", clan.toString());
		writeOriginalNBT(nbt);
		return nbt;
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		readOriginalNBT(nbt);
		PlayerTrophy.getInstance();
		this.clan = (Clan) PlayerTrophy.getData().getClans();
	}

	@Override
	public boolean isAccessible(EntityPlayer player)
	{
		Clan c = CommonProxy.getClan(player);
		return clan.equals(c);
	}

	@Override
	public boolean isModifiable(EntityPlayer player)
	{
		return isAccessible(player) && clan.getMember(player.getUniqueID()).hasRight(
				Right.CLAIM);
	}

	@Override
	public TileEntity getParent()
	{
		TileEntity entity = new TileEntityChest();
		NBTTagCompound nbt = new NBTTagCompound();
		writeOriginalNBT(nbt);
		entity.readFromNBT(nbt);
		return entity;
	}

	@Override
	public TileEntity remove()
	{
		TileEntity parent = getParent();
		this.worldObj.removeTileEntity(pos);
		this.worldObj.setTileEntity(pos, parent);
		return parent;
	}

	@Override
	public void update()
	{
		Logger.info("update");
		super.update();
	}
}
