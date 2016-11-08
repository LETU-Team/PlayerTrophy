package com.lewismcreu.playertrophy.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;

import com.lewismcreu.playertrophy.clan.Clan;

/**
 * @author Lewis_McReu
 */
public class ProtectedTileEntityFactory
{
	public static IProtectedTileEntity createProtectedTileEntity(Clan clan,
			TileEntity tileEntity)
	{
		if (tileEntity instanceof TileEntityChest)
		{
			return new TileEntityProtectedChest(clan, (TileEntityChest) tileEntity);
		}
		else return null;
	}
}
