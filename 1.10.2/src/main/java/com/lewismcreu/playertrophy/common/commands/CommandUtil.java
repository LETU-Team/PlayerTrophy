package com.lewismcreu.playertrophy.common.commands;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CommandUtil
{
	public static final void addMatchingPlayerNames(MinecraftServer server,
			List<String> out, String start)
	{
		for (String name : server.getAllUsernames())
			if (CommandBase.doesStringStartWith(start, name)) out.add(name);
	}

	public static final void sendErrorMessage(ICommandSender sender,
			String message)
	{
		TextComponentString c = new TextComponentString(message);
		Style style = new Style();
		style.setColor(TextFormatting.RED);
		c.setStyle(style);
		sender.addChatMessage(c);
	}

	public static final void sendMessage(ICommandSender sender, String message)
	{
		TextComponentString c = new TextComponentString(message);
		sender.addChatMessage(c);
	}
}
