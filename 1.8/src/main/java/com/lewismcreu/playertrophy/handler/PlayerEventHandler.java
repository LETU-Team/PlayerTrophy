package com.lewismcreu.playertrophy.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.WorldEvent.Save;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.lewismcreu.playertrophy.PlayerTrophy;
import com.lewismcreu.playertrophy.clan.Clan;
import com.lewismcreu.playertrophy.player.PlayerData;
import com.lewismcreu.playertrophy.proxy.CommonProxy;

/**
 * @author Lewis_McReu
 */
public class PlayerEventHandler
{
	public static boolean friendlyfire = false;

	@SubscribeEvent
	public void onPlayerJoinWorld(EntityJoinWorldEvent event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			EntityPlayer p = (EntityPlayer) event.entity;
			if (p.getExtendedProperties("playertrophy") == null)
			{
				PlayerData data = new PlayerData(p.getUniqueID());
				p.registerExtendedProperties("playertrophy", data);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerDeath(LivingDeathEvent event)
	{
		if (event.entityLiving instanceof EntityPlayer && event.source.damageType.equals("player"))
		{
			EntityPlayer source = (EntityPlayer) event.source.getEntity();
			EntityPlayer victim = (EntityPlayer) event.entityLiving;

			PlayerData data = (PlayerData) source.getExtendedProperties("playertrophy");

			String nameVictim = victim.getDisplayNameString();
			String nameSource = source.getDisplayNameString();

			String uuidVictim = victim.getUniqueID().toString();
			String uuidSource = source.getUniqueID().toString();

			if (PlayerTrophy.instance.worldData.getBounties().containsKey(uuidVictim))
			{
				ItemStack diamond = new ItemStack(Items.diamond, 1);
				int count = PlayerTrophy.instance.worldData.getBounty(uuidVictim);
				for (int i = 0; i < count; i++)
				{
					source.inventory.addItemStackToInventory(diamond);
				}
				PlayerTrophy.instance.worldData.removeBounty(uuidVictim);
			}
			if (data.canGetTrophy(uuidVictim))
			{
				ItemStack stack = CommonProxy.createTrophy(nameVictim, nameSource, uuidVictim,
						uuidSource);
				source.inventory.addItemStackToInventory(stack);
			}
			data.registerKill(uuidVictim);
		}
	}

	@SubscribeEvent
	public void onPlayerDamaged(LivingHurtEvent event)
	{
		if (friendlyfire)
		{
			if (event.entity instanceof EntityPlayer && event.source.damageType.equals("player"))
			{
				Clan target = ((PlayerData) ((EntityPlayer) event.entity)
						.getExtendedProperties("playertrophy")).getClan();
				Clan source = ((PlayerData) ((EntityPlayer) event.source.getEntity())
						.getExtendedProperties("playertrophy")).getClan();
				if (target != null && target.equals(source)) event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public void onWorldSave(Save event)
	{
		if (PlayerTrophy.instance.worldData != null) PlayerTrophy.instance.worldData.saveData();
	}
}