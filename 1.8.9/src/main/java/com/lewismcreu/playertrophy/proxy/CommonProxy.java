package com.lewismcreu.playertrophy.proxy;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.UsernameCache;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.lewismcreu.playertrophy.clan.Clan;
import com.lewismcreu.playertrophy.handler.BlockEventHandler;
import com.lewismcreu.playertrophy.handler.PlayerEventHandler;
import com.lewismcreu.playertrophy.item.ItemBounty;
import com.lewismcreu.playertrophy.item.ItemPlayerTrophy;
import com.lewismcreu.playertrophy.item.ItemScepter;
import com.lewismcreu.playertrophy.player.PlayerData;
import com.lewismcreu.playertrophy.tileentity.TileEntityProtectedChest;
import com.lewismcreu.playertrophy.util.Reference;

/**
 * @author Lewis_McReu
 */
public abstract class CommonProxy implements IProxy
{
	public static ItemPlayerTrophy playerTrophy = new ItemPlayerTrophy();
	public static ItemBounty bounty = new ItemBounty();
	public static ItemScepter scepter = new ItemScepter();

	public void preInit()
	{
		MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
		MinecraftForge.EVENT_BUS.register(new BlockEventHandler());
		registerItems();
		registerRecipes();
		GameRegistry.registerTileEntity(TileEntityProtectedChest.class,
				Reference.MOD_NAME + "ProtectedChest");
	}

	@Override
	public void init()
	{
	}

	public static ItemStack createTrophy(String victim, String slayer, String uuidVictim, String uuidSlayer)
	{
		ItemStack stack = new ItemStack(CommonProxy.playerTrophy, 1);
		if (!stack.hasTagCompound() && victim != null && slayer != null)
		{
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setString("victim", victim);
			stack.getTagCompound().setString("slayer", slayer);
			stack.getTagCompound().setString("uuidVictim", uuidVictim);
			stack.getTagCompound().setString("uuidSlayer", uuidSlayer);
		}
		return stack;
	}

	private void registerItems()
	{
		GameRegistry.registerItem(playerTrophy, playerTrophy.getUnlocalizedName());
		GameRegistry.registerItem(bounty, bounty.getUnlocalizedName());
		GameRegistry.registerItem(scepter, scepter.getUnlocalizedName());
	}

	private void registerRecipes()
	{
		GameRegistry.addRecipe(new ShapedOreRecipe(bounty, false, "ABA", "BCB", "ABA", 'A',
				"gemDiamond", 'B', Items.ender_eye, 'C', Items.paper));
		GameRegistry.addRecipe(new ShapedOreRecipe(scepter, false, "XAB", "XAA", "AXX", 'A',
				"ingotGold", 'B', "gemEmerald"));
	}

	@Override
	public void broadcastChatMessage(String s)
	{
		ChatComponentText x = new ChatComponentText(s);
		ChatStyle c = new ChatStyle();
		c.setColor(EnumChatFormatting.RED);
		x.setChatStyle(c);
		List<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance()
				.getConfigurationManager().playerEntityList;

		for (EntityPlayer p : players)
		{
			p.addChatComponentMessage(x);
		}
	}

	public static EntityPlayer getPlayerOnline(String uuid)
	{
		List<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance()
				.getConfigurationManager().playerEntityList;
		for (EntityPlayer p : players)
		{
			if (p.getUniqueID().equals(UUID.fromString(uuid))) return p;
		}
		return null;
	}

	public static String getNameForUuid(UUID uuid)
	{
		String name = UsernameCache.getLastKnownUsername(uuid);
		return name;
	}

	public static UUID getUuidForName(String name)
	{
		Map<UUID, String> map = UsernameCache.getMap();
		Iterator<Entry<UUID, String>> it = map.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<UUID, String> e = it.next();
			if (e.getValue().equals(name)) return e.getKey();
		}
		// Normally never reached, otherwise a player who never joined or
		// doesn't exist is targeted
		return null;
	}

	public static EntityPlayer getPlayerForUuid(UUID uuid)
	{
		List<EntityPlayerMP> list = FMLCommonHandler.instance().getMinecraftServerInstance()
				.getConfigurationManager().playerEntityList;
		for (EntityPlayer p : list)
		{
			if (p.getUniqueID().equals(uuid)) return p;
		}
		return null;
	}

	public static PlayerData getPlayerData(EntityPlayer player)
	{
		return (PlayerData) player.getExtendedProperties("playertrophy");
	}

	public static Clan getClanForPlayer(EntityPlayer player)
	{
		return getPlayerData(player).getClan();
	}
}
