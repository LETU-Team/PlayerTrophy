package com.lewismcreu.playertrophy.common.commands;

import java.util.Collection;

import com.lewismcreu.playertrophy.PlayerTrophy;
import com.lewismcreu.playertrophy.common.data.Bounty;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.UsernameCache;

/**
 * @author Lewis_McReu
 */
public class CommandBounty extends PTCommandTreeBase
{
	public CommandBounty()
	{
		super("bounty");
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender,
			String[] args) throws CommandException
	{
		Collection<Bounty> bounties = PlayerTrophy.getData().getBounties();
		if (bounties.isEmpty()) sender.addChatMessage(
				new TextComponentTranslation("chat.bounty.empty"));
		else
		{
			sender.addChatMessage(new TextComponentTranslation("chat.bounties",
					bounties.size()));
			for (Bounty b : bounties)
				sender.addChatMessage(new TextComponentTranslation(
						"chat.bounty",
						UsernameCache.getLastKnownUsername(b.getUuid())));
		}
	}
}
