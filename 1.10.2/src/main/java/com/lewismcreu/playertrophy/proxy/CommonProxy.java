package com.lewismcreu.playertrophy.proxy;

import com.lewismcreu.playertrophy.common.data.Clan;
import com.lewismcreu.playertrophy.common.data.IPlayerData;
import com.lewismcreu.playertrophy.common.data.PlayerData;
import com.lewismcreu.playertrophy.common.event.ClanEventHandler;
import com.lewismcreu.playertrophy.common.event.CommonEventHandler;
import com.lewismcreu.playertrophy.common.event.TrophyEventHandler;
import com.lewismcreu.playertrophy.common.item.ItemBounty;
import com.lewismcreu.playertrophy.common.item.ItemScepter;
import com.lewismcreu.playertrophy.common.item.ItemTrophy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * @author Lewis_McReu
 */
public class CommonProxy
{
	@CapabilityInject(IPlayerData.class)
	public static final Capability<IPlayerData> playerDataCapability = null;

	public static final ItemScepter scepter = new ItemScepter();
	public static final ItemTrophy trophy = new ItemTrophy();
	public static final ItemBounty bounty = new ItemBounty();

	public void preInit(FMLPreInitializationEvent event)
	{
		CapabilityManager.INSTANCE.register(IPlayerData.class,
				new PlayerData.PlayerDataStorage(), PlayerData.class);
		GameRegistry.register(scepter);
		GameRegistry.register(trophy);
		GameRegistry.register(bounty);
		MinecraftForge.EVENT_BUS.register(new ClanEventHandler());
		MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
		MinecraftForge.EVENT_BUS.register(new TrophyEventHandler());
	}

	public void init(FMLInitializationEvent event)
	{

	}

	public void postInit(FMLPostInitializationEvent event)
	{

	}

	public static IPlayerData getPlayerData(EntityPlayer player)
	{
		IPlayerData data = player.getCapability(playerDataCapability, null);
		if (data != null && data.getUUID() == null)
			data.setUUID(player.getPersistentID());
		return data;
	}

	public static Clan getClan(EntityPlayer player)
	{
		IPlayerData data = getPlayerData(player);
		return data != null ? data.getClan() : null;
	}
}
