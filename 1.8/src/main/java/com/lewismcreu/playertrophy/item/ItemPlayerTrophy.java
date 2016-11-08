package com.lewismcreu.playertrophy.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.lewismcreu.playertrophy.util.Language;

/**
 * @author Lewis_McReu
 */
public class ItemPlayerTrophy extends BaseItem
{
	public ItemPlayerTrophy()
	{
		super("itemPlayerTrophy");
		this.setMaxStackSize(1);
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack)
	{
		return true;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par)
	{
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("victim"))
		{
			String victim = stack.getTagCompound().getString("victim");
			String slayer = stack.getTagCompound().getString("slayer");

			list.add(EnumChatFormatting.DARK_RED.toString() + String.format(
					Language.getLocalizedString("playertrophy.trophy.description.target"), victim));
			list.add(EnumChatFormatting.DARK_RED.toString() + String.format(
					Language.getLocalizedString("playertrophy.trophy.description.slain"), slayer));
		}
	}
}
