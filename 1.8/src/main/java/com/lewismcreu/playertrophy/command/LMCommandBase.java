package com.lewismcreu.playertrophy.command;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
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

	@Override
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
		ChatComponentText c = new ChatComponentText(message);
		ChatStyle style = new ChatStyle();
		style.setColor(EnumChatFormatting.RED);
		c.setChatStyle(style);
		sender.addChatMessage(c);
	}

	protected final void sendMessage(ICommandSender sender, String message)
	{
		ChatComponentText c = new ChatComponentText(message);
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
