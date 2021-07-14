package com.lewismcreu.playertrophy.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

import com.lewismcreu.playertrophy.clan.Clan;
import com.lewismcreu.playertrophy.clan.Right;
import com.lewismcreu.playertrophy.proxy.CommonProxy;
import com.lewismcreu.playertrophy.tileentity.IProtectedTileEntity;
import com.lewismcreu.playertrophy.tileentity.ProtectedTileEntityFactory;
import com.lewismcreu.playertrophy.util.Logger;

/**
 * @author Lewis_McReu
 */
public class ItemScepter extends BaseItem
{
	public ItemScepter()
	{
		super("itemScepter");
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float xSide, float ySide, float zSide)
	{
		if (!world.isRemote)
		{
			Clan clan = CommonProxy.getClanForPlayer(player);
			Logger.info(clan);
			if (clan != null && clan.getMemberRank(player.getUniqueID()) != null && clan
					.getMemberRank(player.getUniqueID()).hasRight(Right.CLAIM))
			{
				TileEntity original = world.getTileEntity(pos);
				Block block = world.getBlockState(pos).getBlock();
				Logger.info("before: " + world.getTileEntity(pos));
				if (original instanceof IProtectedTileEntity)
				{
					IProtectedTileEntity originalProtected = (IProtectedTileEntity) original;
					if (player.isSneaking() && originalProtected.isModifiable(player))
					{
						world.removeTileEntity(pos);
						originalProtected.remove();
						player.addChatMessage(new ChatComponentText("Unclaimed " + block
								.getLocalizedName()));
						Logger.info("removed: " + world.getTileEntity(pos));
					}
				}
				else if (original != null)
				{
					TileEntity entity = (TileEntity) ProtectedTileEntityFactory
							.createProtectedTileEntity(clan, original);
					world.removeTileEntity(pos);
					world.setTileEntity(pos, (TileEntity) entity);
					player.addChatMessage(new ChatComponentText("Claimed " + block
							.getLocalizedName()));
					Logger.info("added: " + world.getTileEntity(pos));
				}
				Logger.info("after: " + world.getTileEntity(pos));
				return true;
			}
			else player.addChatMessage(new ChatComponentText("You can't claim anything!"));
		}
		return false;
	}
}
