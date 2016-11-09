package com.lewismcreu.playertrophy.common.commands;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * @author Lewis_McReu 
 */
public abstract class LMCommandBase extends CommandBase
{
	private String name;

	protected Map<String, SubCommand> subCommands;

	public LMCommandBase(String name, boolean sub)
	{
		super();
		this.setName(name);
		if (sub) this.subCommands = new HashMap<>();
	}

	public String getName()
	{
		return name;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_)
	{
		return "commands." + name + ".usage";
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 2;
	}

	private void setName(String name)
	{
		this.name = name;
	}

	protected final void sendErrorMessage(ICommandSender sender, String message)
	{
		TextComponentString c = new TextComponentString(message);
		Style style = new Style();
		style.setColor(TextFormatting.RED);
		c.setStyle(style);
		sender.addChatMessage(c);
	}

	protected final void sendMessage(ICommandSender sender, String message)
	{
		TextComponentString c = new TextComponentString(message);
		sender.addChatMessage(c);
	}

	protected final void addAllPlayers(List<String> out, String start)
	{
		String[] usernames = FMLCommonHandler.instance().getMinecraftServerInstance()
				.getAllUsernames();
		for (String s : usernames)
		{
			if (doesStringStartWith(start, s)) out.add(s);
		}
	}

	@FunctionalInterface
	public static interface SubCommand
	{
		void execute(ICommandSender sender, String... parameters);
	}
}
