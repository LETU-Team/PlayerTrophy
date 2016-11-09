package com.lewismcreu.playertrophy.proxy;

import com.lewismcreu.playertrophy.common.data.Clan;
import com.lewismcreu.playertrophy.common.data.IPlayerData;
import com.lewismcreu.playertrophy.common.data.PlayerData;
import com.lewismcreu.playertrophy.common.item.ItemScepter;
import com.lewismcreu.playertrophy.common.item.ItemTrophy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CommonProxy
{
	@CapabilityInject(IPlayerData.class)
	public static final Capability<IPlayerData> playerDataCapability = null;

	public static final ItemScepter scepter = new ItemScepter();
	public static final ItemTrophy trophy = new ItemTrophy();

	public void preInit(FMLPreInitializationEvent event)
	{
		CapabilityManager.INSTANCE.register(IPlayerData.class, new PlayerData.PlayerDataStorage(), PlayerData.class);
		GameRegistry.register(scepter);
		GameRegistry.register(trophy);
	}

	public void init(FMLInitializationEvent event)
	{

	}

	public void postInit(FMLPostInitializationEvent event)
	{

	}

	public static IPlayerData getPlayerData(EntityPlayer player)
	{
		return player.getCapability(playerDataCapability, null);
	}

	public static Clan getClan(EntityPlayer player)
	{
		IPlayerData data = getPlayerData(player);
		return data != null ? data.getClan() : null;
	}
}
