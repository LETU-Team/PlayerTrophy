package com.lewismcreu.playertrophy.common.commands;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.UsernameCache;

import com.lewismcreu.playertrophy.PlayerTrophy;
import com.lewismcreu.playertrophy.proxy.CommonProxy;

/**
 * @author Lewis_McReu
 */
public class CommandBounty extends LMCommandBase
{
	public CommandBounty()
	{
		super("bounty", false);
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
	{
		if (args.length > 0)
		{
			String action = args[0];
			switch (action)
			{
				case "list":
					handleList(sender, args);
					return;
			}
		}
		else throw new IllegalArgumentException("playertrophy.command.invalidcommand");
	}

	private void handleList(ICommandSender sender, String[] params)
	{
		sender.addChatMessage(new TextComponentString("Bounties:"));
		Iterator<Entry<UUID, Integer>> it = bounties.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<UUID, Integer> x = it.next();
			String displayName = "";
			displayName = UsernameCache.getLastKnownUsername(x.getKey());
			int count = x.getValue();
			sender.addChatMessage(new TextComponentString(displayName + " : " + count));
		}
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
	{
		List<String> out = new LinkedList<String>();
		if (args.length == 1)
		{
			out.add("list");
		}
		if (out.size() == 0) return null;
		return out;
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}

	@Override
	public String getCommandName() {
		return null;
	}
}
