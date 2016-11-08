package com.lewismcreu.playertrophy.common.item;

import com.lewismcreu.playertrophy.PlayerTrophy;

import net.minecraft.item.Item;

public class BaseItem extends Item
{
	public BaseItem(String unlocalizedName)
	{
		setUnlocalizedName(unlocalizedName);
		setRegistryName(PlayerTrophy.MODID, unlocalizedName);
	}
}
