package com.lewismcreu.playertrophy.item;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.lewismcreu.playertrophy.util.Reference;

/**
 * @author Lewis_McReu
 */
public class BaseItem extends Item
{
	public BaseItem(String name)
	{
		setUnlocalizedName(name);
		this.setCreativeTab(CreativeTabs.tabMisc);
	}

	@Override
	public String getUnlocalizedName()
	{
		return super.getUnlocalizedName().substring(super.getUnlocalizedName().indexOf('.') + 1);
	}

	@Override
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining)
	{
		return new ModelResourceLocation(Reference.MOD_ID + ":" + this.getUnlocalizedName(),
				"inventory");
	}
}
