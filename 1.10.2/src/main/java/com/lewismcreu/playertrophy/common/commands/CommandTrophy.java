package com.lewismcreu.playertrophy.common.commands;

import java.util.LinkedList;
import java.util.List;

import com.lewismcreu.playertrophy.proxy.CommonProxy;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

/**
 * @author Lewis_McReu
 */
public class CommandTrophy extends LMCommandBase
{
	public CommandTrophy()
	{
		super("trophy", false);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (sender instanceof EntityPlayer && args.length == 2)
		{
			EntityPlayer p = (EntityPlayer) sender;
			String victim = args[0];
			String slayer = args[1];
			p.inventory.addItemStackToInventory(CommonProxy.createTrophy(victim, slayer, "", ""));
		}
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) 
	{
		List<String> list = new LinkedList<String>();
		if (args.length < 3)
		{
			addAllPlayers(list, "");
		}
		if (list.size() == 0) return null;
		return list;
	}

	@Override
	public String getCommandName() {
		return null;
	}
}
