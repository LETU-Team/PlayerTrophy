package com.lewismcreu.playertrophy.common.commands;

import com.lewismcreu.playertrophy.common.data.Clan;
import com.lewismcreu.playertrophy.common.data.IPlayerData;
import com.lewismcreu.playertrophy.common.data.Right;
import com.lewismcreu.playertrophy.proxy.CommonProxy;
import com.lewismcreu.playertrophy.util.Lang;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public abstract class CommandClanTreeBase extends PTCommandTreeBase
{
	public CommandClanTreeBase(String name)
	{
		super(name);
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender)
	{
		try
		{
			return super.checkPermission(server, sender) && getClan(sender) != null && getClan(sender).hasRight(
					((EntityPlayer) sender).getPersistentID(), getRequiredRight());
		}
		catch (CommandException e)
		{
			return false;
		}
	}

	public abstract Right getRequiredRight();

	public static EntityPlayer checkPlayer(ICommandSender sender) throws CommandException
	{
		if (sender instanceof EntityPlayer) return (EntityPlayer) sender;
		throw new CommandException(Lang.translate("chat.notaplayer"));
	}

	public static Clan getClan(ICommandSender sender) throws CommandException
	{
		checkPlayer(sender);
		return CommonProxy.getClan((EntityPlayer) sender);
	}

	public static IPlayerData getPlayerData(ICommandSender sender) throws CommandException
	{
		return CommonProxy.getPlayerData(checkPlayer(sender));
	}
}
