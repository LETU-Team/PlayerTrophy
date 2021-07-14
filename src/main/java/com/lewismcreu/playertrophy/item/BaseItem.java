package com.lewismcreu.playertrophy.item;

import com.lewismcreu.playertrophy.PlayerTrophy;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * @author Lewis_McReu
 */
public class BaseItem extends Item
{
	public BaseItem(String name)
	{
		setUnlocalizedName(name);
		this.setCreativeTab(CreativeTabs.MISC);
	}

	@Override
	public String getUnlocalizedName()
	{
		return super.getUnlocalizedName().substring(super.getUnlocalizedName().indexOf('.') + 1);
	}
	
	@Override
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining)
	{
		return new ModelResourceLocation(PlayerTrophy.MODID + ":" + this.getUnlocalizedName(),
				"inventory");
	}
}
