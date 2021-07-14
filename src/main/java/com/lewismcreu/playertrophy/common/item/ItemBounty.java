package com.lewismcreu.playertrophy.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemBounty extends BaseItem
{
	public ItemBounty()
	{
		super("bounty");
		setCreativeTab(CreativeTabs.TOOLS);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack,
			World world, EntityPlayer player, EnumHand hand)
	{
		if (!world.isRemote)
		{
			// TODO open gui for setting a bounty!
			return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
		}
		return super.onItemRightClick(itemStack, world, player, hand);
	}
}
