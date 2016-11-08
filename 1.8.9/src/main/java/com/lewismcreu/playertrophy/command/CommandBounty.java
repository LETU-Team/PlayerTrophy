package com.lewismcreu.playertrophy.command;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import com.lewismcreu.playertrophy.PlayerTrophy;
import com.lewismcreu.playertrophy.proxy.CommonProxy;

/**
 * @author Lewis_McReu
 */
public class CommandBounty extends LMCommandBase
{
	public CommandBounty()
	{
		super("bounty");
	}

	@Override
	public void processCommand(ICommandSender sender, String[] params)
	{
		if (params.length > 0)
		{
			String action = params[0];
			switch (action)
			{
				case "list":
					handleList(sender, params);
					return;
			}
		}
		else throw new IllegalArgumentException("playertrophy.command.invalidcommand");
	}

	private void handleList(ICommandSender sender, String[] params)
	{
		sender.addChatMessage(new ChatComponentText("Bounties:"));
		HashMap<UUID, Integer> bounties = PlayerTrophy.instance.worldData.getBounties();
		Iterator<Entry<UUID, Integer>> it = bounties.entrySet().iterator();
		while (it.hasNext())
		{
			Entry<UUID, Integer> x = it.next();
			String displayName = "";
			displayName = CommonProxy.getNameForUuid(x.getKey());
			int count = x.getValue();
			sender.addChatMessage(new ChatComponentText(displayName + " : " + count));
		}
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] params, BlockPos pos)
	{
		List<String> out = new LinkedList<String>();
		if (params.length == 1)
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
}
