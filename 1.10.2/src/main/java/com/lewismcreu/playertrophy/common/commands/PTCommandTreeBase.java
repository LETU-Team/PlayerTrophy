package com.lewismcreu.playertrophy.common.commands;

import com.lewismcreu.playertrophy.PlayerTrophy;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;


/**
 * @author Lewis_McReu
 */
public abstract class PTCommandTreeBase extends CommandTreeBase
{
	private String name;

	public PTCommandTreeBase(String name)
	{
		super();
		this.name = name;
	}

	@Override
	public String getCommandName()
	{
		return name;
	}

	@Override
	public String getCommandUsage(ICommandSender sender)
	{
		return "command." + PlayerTrophy.MODID + "." + name + ".usage";
	}
}
