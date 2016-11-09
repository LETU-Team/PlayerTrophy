package com.lewismcreu.playertrophy.common.item;

import java.util.List;
import java.util.UUID;

import com.lewismcreu.playertrophy.proxy.CommonProxy;
import com.lewismcreu.playertrophy.util.Lang;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.UsernameCache;

public class ItemTrophy extends BaseItem
{
	public ItemTrophy()
	{
		super("itemPlayerTrophy");
		setMaxStackSize(1);
	}

	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
	{
		super.addInformation(stack, playerIn, tooltip, advanced);
		UUID victim = getVictim(stack);
		UUID slayer = getSlayer(stack);

		if (victim != null)
		{
			String name = UsernameCache.getLastKnownUsername(victim);
			if (name != null) tooltip.add(ChatFormatting.DARK_RED.toString() + String.format(Lang.format(
					"playertrophy.trophy.description.victim", victim)));
		}
		if (slayer != null)
		{
			String name = UsernameCache.getLastKnownUsername(slayer);
			if (name != null) tooltip.add(ChatFormatting.DARK_RED.toString() + String.format(Lang.format(
					"playertrophy.trophy.description.slayer", slayer)));
		}
	}

	public static final String keyVictim = "victim", keySlayer = "slayer";

	public static ItemStack setVictim(ItemStack stack, UUID victim)
	{
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());

		stack.getTagCompound().setUniqueId(keyVictim, victim);

		return stack;
	}

	public static UUID getVictim(ItemStack stack)
	{
		if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey(keyVictim)) return null;
		return stack.getTagCompound().getUniqueId(keyVictim);
	}

	public static ItemStack setSlayer(ItemStack stack, UUID slayer)
	{
		if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());

		stack.getTagCompound().setUniqueId(keySlayer, slayer);

		return stack;
	}

	public static UUID getSlayer(ItemStack stack)
	{
		if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey(keySlayer)) return null;
		return stack.getTagCompound().getUniqueId(keySlayer);
	}

	public static ItemStack create(UUID slayer, UUID victim)
	{
		return setSlayer(setVictim(new ItemStack(CommonProxy.trophy), victim), slayer);
	}
}
