package com.lewismcreu.playertrophy.handler;

import com.lewismcreu.playertrophy.tileentity.IProtectedTileEntity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Lewis_McReu
 */
public class BlockEventHandler
{
	@SubscribeEvent
	public void onBlockBreak(BreakEvent event)
	{
		World w = event.world;
		EntityPlayer p = event.getPlayer();
		TileEntity e = w.getTileEntity(event.pos);
		if (e instanceof IProtectedTileEntity)
		{
			IProtectedTileEntity eP = (IProtectedTileEntity) e;
			if (!eP.isModifiable(p))
			{
				p.addChatComponentMessage(
						new ChatComponentText("You don't have access to this block."));
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if (event.action.equals(Action.RIGHT_CLICK_BLOCK) || event.action
				.equals(Action.LEFT_CLICK_BLOCK))
		{
			World w = event.world;
			EntityPlayer p = event.entityPlayer;
			TileEntity e = w.getTileEntity(event.pos);
			if (e instanceof IProtectedTileEntity)
			{
				IProtectedTileEntity eP = (IProtectedTileEntity) e;
				if (!eP.isAccessible(p))
				{
					p.addChatComponentMessage(
							new ChatComponentText("You don't have access to this block."));
					event.setCanceled(true);
					event.setResult(Result.DENY);
				}
			}
		}
	}
}
