package com.lewismcreu.playertrophy.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

/**
 * @author Lewis_McReu
 */
public interface IProtectedTileEntity
{
	public boolean isAccessible(EntityPlayer player);

	public boolean isModifiable(EntityPlayer player);

	public TileEntity getParent();

	public TileEntity remove();
}
