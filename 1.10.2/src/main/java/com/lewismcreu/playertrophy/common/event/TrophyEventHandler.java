package com.lewismcreu.playertrophy.common.event;

import com.lewismcreu.playertrophy.common.data.IPlayerData;
import com.lewismcreu.playertrophy.common.item.ItemTrophy;
import com.lewismcreu.playertrophy.proxy.CommonProxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber
public class TrophyEventHandler
{
	@SubscribeEvent
	public void onPlayerDeath(LivingDeathEvent event)
	{
		if (event.getEntityLiving() instanceof EntityPlayer && event.getSource()
				.getSourceOfDamage() instanceof EntityPlayer)
		{
			EntityPlayer source = (EntityPlayer) event.getSource().getSourceOfDamage();
			EntityPlayer target = (EntityPlayer) event.getEntityLiving();

			IPlayerData sourceData = source.getCapability(CommonProxy.playerDataCapability, null);

			if (!sourceData.getLastKills().containsKey(target.getPersistentID()))
			{
				ItemStack out = ItemTrophy.create(source.getPersistentID(), target.getPersistentID());
				if (!source.inventory.addItemStackToInventory(out)) target.entityDropItem(out, 0.1f);
			}
		}
	}
}
