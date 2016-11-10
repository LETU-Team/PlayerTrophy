package com.lewismcreu.playertrophy.common.event;

import com.lewismcreu.playertrophy.PlayerTrophy;
import com.lewismcreu.playertrophy.common.data.Clan;
import com.lewismcreu.playertrophy.proxy.CommonProxy;
import com.lewismcreu.playertrophy.util.Config;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Lewis_McReu
 */
@EventBusSubscriber
public class ClanEventHandler
{
	// Territory handling

	@SubscribeEvent
	public void onBlockBreak(BlockEvent.BreakEvent event)
	{
		if (!hasAccess(event.getPlayer(), event.getPos()))
			event.setCanceled(true);
	}

	@SubscribeEvent
	public void onInteract(PlayerInteractEvent event)
	{
		if (!hasAccess(event.getEntityPlayer(), event.getPos())
				&& event.isCancelable())
			event.setCanceled(true);
	}

	public static boolean hasAccess(EntityPlayer player, BlockPos pos)
	{
		Clan c = PlayerTrophy.getInstance().getData().findClan(pos);
		Clan pc = CommonProxy.getClan(player);

		return (c != null && pc == c) || c == null;
	}

	// Member handling

	@SubscribeEvent
	public void onPlayerDamaged(LivingHurtEvent event)
	{
		if (!Config.getFriendlyFire()
				&& event.getEntity() instanceof EntityPlayer
				&& event.getSource()
						.getSourceOfDamage() instanceof EntityPlayer)
		{
			Clan target = CommonProxy.getClan((EntityPlayer) event.getEntity());
			Clan source = CommonProxy.getClan(
					(EntityPlayer) event.getSource().getSourceOfDamage());
			if (target == source) event.setCanceled(true);
		}
	}
}
