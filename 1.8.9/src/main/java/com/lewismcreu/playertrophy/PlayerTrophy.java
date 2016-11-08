package com.lewismcreu.playertrophy;

import java.io.File;

import com.lewismcreu.playertrophy.command.CommandBounty;
import com.lewismcreu.playertrophy.command.CommandClan;
import com.lewismcreu.playertrophy.command.CommandTrophy;
import com.lewismcreu.playertrophy.proxy.IProxy;
import com.lewismcreu.playertrophy.util.Config;
import com.lewismcreu.playertrophy.util.Reference;
import com.lewismcreu.playertrophy.world.WorldData;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;

/**
 * @author Lewis_McReu
 */
@Mod(modid = Reference.MOD_ID, name = Reference.MOD_NAME, version = Reference.MOD_VERSION)
public class PlayerTrophy
{
	@Mod.Instance(Reference.MOD_ID)
	public static PlayerTrophy instance;

	@SidedProxy(clientSide = Reference.CLIENT_PROXY_CLASS, serverSide = Reference.SERVER_PROXY_CLASS)
	public static IProxy proxy;

	public WorldData worldData;
	public Config config;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration conf = new Configuration(event.getSuggestedConfigurationFile());
		config = new Config(conf);
		proxy.preInit();
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init();
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
	}

	@Mod.EventHandler
	public void onServerStarting(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandTrophy());
		if (event.getServer().isDedicatedServer())
		{
			event.registerServerCommand(new CommandBounty());
			event.registerServerCommand(new CommandClan());
			String filePath = event.getServer().getFolderName() + "/playertrophy.xml";
			File worldDataFile = event.getServer().getFile(filePath);
			worldData = WorldData.getInstance(worldDataFile);
		}
	}

	@Mod.EventHandler
	public void onServerStopping(FMLServerStoppingEvent event)
	{
		if (worldData != null) worldData.saveData();
	}
}
