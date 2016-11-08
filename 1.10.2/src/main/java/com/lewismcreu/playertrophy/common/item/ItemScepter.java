package com.lewismcreu.playertrophy.common.item;

import com.lewismcreu.playertrophy.PlayerTrophy;
import com.lewismcreu.playertrophy.common.data.Clan;
import com.lewismcreu.playertrophy.common.data.Right;
import com.lewismcreu.playertrophy.common.data.WorldData;
import com.lewismcreu.playertrophy.proxy.CommonProxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ItemScepter extends BaseItem
{
	public ItemScepter()
	{
		super("itemScepter");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, EntityPlayer player,
			EnumHand hand)
	{
		// TODO Auto-generated method stub

		if (!world.isRemote)
		{
			if (player.isSneaking())
			{
				// TODO open tribe gui
				return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
			}
			else
			{
				Clan playerClan = CommonProxy.getClan(player);
				if (playerClan != null && playerClan.hasRight(player.getPersistentID(), Right.CLAIM))
				{
					BlockPos pos = player.getPosition();
					Chunk chunk = world.getChunkFromBlockCoords(pos);
					ChunkPos cPos = chunk.getChunkCoordIntPair();
					WorldData data = PlayerTrophy.getInstance().getData();
					Clan claimer = data.findClan(cPos);
					if (claimer == null)
					{
						data.claimChunk(playerClan, cPos);
						return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
					}
					else if (claimer == playerClan) { 
						data.unclaimChunk(claimer, cPos);
						return ActionResult.newResult(EnumActionResult.SUCCESS,
							itemStack); }
					return ActionResult.newResult(EnumActionResult.FAIL, itemStack);
				}
			}
		}

		return super.onItemRightClick(itemStack, world, player, hand);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		return onItemRightClick(stack, worldIn, playerIn, hand).getType();
	}
}
