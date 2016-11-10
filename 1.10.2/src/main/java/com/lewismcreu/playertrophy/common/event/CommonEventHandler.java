package com.lewismcreu.playertrophy.common.event;

import com.lewismcreu.playertrophy.PlayerTrophy;
import com.lewismcreu.playertrophy.common.data.PlayerData;
import com.lewismcreu.playertrophy.proxy.CommonProxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Lewis_McReu
 */
@EventBusSubscriber
public class CommonEventHandler
{
	@SubscribeEvent
	public void onAttachCapabilities(
			AttachCapabilitiesEvent<EntityPlayer> event)
	{
		event.addCapability(
				new ResourceLocation(PlayerTrophy.MODID, "playerdata"),
				new PlayerData.PlayerDataProvider());
	}

	@SubscribeEvent
	public void clonePlayer(PlayerEvent.Clone event)
	{
		CommonProxy.getPlayerData(event.getEntityPlayer())
				.copy(CommonProxy.getPlayerData(event.getOriginal()));
	}
}
