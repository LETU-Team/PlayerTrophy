package com.lewismcreu.playertrophy;

import com.lewismcreu.playertrophy.common.commands.CommandClanNew;
import com.lewismcreu.playertrophy.common.data.WorldData;
import com.lewismcreu.playertrophy.proxy.IProxy;
import com.lewismcreu.playertrophy.util.Config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

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
	public static IProxy proxy;

	private SimpleNetworkWrapper channel;

	public SimpleNetworkWrapper channel()
	{
		return channel;
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Config.init(new Configuration(event.getSuggestedConfigurationFile()));
		proxy.preInit();
		channel = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init();
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
