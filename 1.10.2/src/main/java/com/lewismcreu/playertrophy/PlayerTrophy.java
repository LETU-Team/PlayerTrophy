package com.lewismcreu.playertrophy;

import com.lewismcreu.playertrophy.common.commands.CommandClanNew;
import com.lewismcreu.playertrophy.common.data.WorldData;
import com.lewismcreu.playertrophy.proxy.CommonProxy;
import com.lewismcreu.playertrophy.util.Config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

/**
 * @author Lewis_McReu
 */
@Mod(modid = PlayerTrophy.MODID)
public class PlayerTrophy
{
	public static final String MODID = "playertrophy";

	@Instance
	private static PlayerTrophy instance;

	public static PlayerTrophy getInstance()
	{
		return instance;
	}

	@SidedProxy(clientSide = "com.lewismcreu.playertrophy.proxy.ClientProxy",
			serverSide = "com.lewismcreu.playertrophy.proxy.CommonProxy")
	private static CommonProxy proxy;

	public CommonProxy getProxy()
	{
		return proxy;
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Config.init(new Configuration(event.getSuggestedConfigurationFile()));
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init(event);
	}

	private WorldData data;

	public static WorldData getData()
	{
		return instance.data;
	}

	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event)
	{
		data = WorldData.loadFromWorld(event.getServer().getEntityWorld());
		event.registerServerCommand(new CommandClanNew());
	}
}
