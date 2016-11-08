package com.lewismcreu.playertrophy.command;

import java.util.LinkedList;
import java.util.List;

import com.lewismcreu.playertrophy.proxy.CommonProxy;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

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
	public void execute(ICommandSender sender, String[] parameters)
	{
		if (sender instanceof EntityPlayer && parameters.length == 2)
		{
			EntityPlayer p = (EntityPlayer) sender;
			String victim = parameters[0];
			String slayer = parameters[1];
			p.inventory.addItemStackToInventory(CommonProxy.createTrophy(victim, slayer, "", ""));
		}
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] parameters, BlockPos pos)
	{
		List<String> list = new LinkedList<String>();
		if (parameters.length < 3)
		{
			addAllPlayers(list, "");
		}
		if (list.size() == 0) return null;
		return list;
	}
}
