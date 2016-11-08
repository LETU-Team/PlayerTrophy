package com.lewismcreu.playertrophy.tileentity;

import com.lewismcreu.playertrophy.PlayerTrophy;
import com.lewismcreu.playertrophy.clan.Clan;
import com.lewismcreu.playertrophy.clan.Right;
import com.lewismcreu.playertrophy.proxy.CommonProxy;

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
	public void writeToNBT(NBTTagCompound nbt)
	{
		if (clan == null)
		{
			this.remove().writeToNBT(nbt);
			return;
		}
		nbt.setString("clan", clan.toString());
		writeOriginalNBT(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		readOriginalNBT(nbt);
		this.clan = PlayerTrophy.instance.worldData.getClan(nbt.getString("clan"));
	}

	@Override
	public boolean isAccessible(EntityPlayer player)
	{
		Clan c = CommonProxy.getClanForPlayer(player);
		return clan.equals(c);
	}

	@Override
	public boolean isModifiable(EntityPlayer player)
	{
		return isAccessible(player) && clan.getMemberRank(player.getUniqueID())
				.hasRight(Right.CLAIM);
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
}
