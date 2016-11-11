package com.lewismcreu.playertrophy.common.commands;

import java.util.Collection;

import com.lewismcreu.playertrophy.PlayerTrophy;
import com.lewismcreu.playertrophy.common.data.Clan;
import com.lewismcreu.playertrophy.common.data.IPlayerData;
import com.lewismcreu.playertrophy.proxy.CommonProxy;
import com.lewismcreu.playertrophy.util.Lang;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandClanNew extends PTCommandTreeBase
{
	public CommandClanNew()
	{
		super("clan");
		addSubcommand(new CommandList());
		addSubcommand(new CommandLeave());
	}

	private class CommandList extends PTCommandBase
	{
		public CommandList()
		{
			super("list");
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
		{
			Collection<Clan> clans = PlayerTrophy.getData().getClans();
			if (clans.isEmpty()) sender.addChatMessage(new TextComponentTranslation("chat.clans.empty"));
			else
			{
				sender.addChatMessage(new TextComponentTranslation("chat.clans", clans.size()));
				for (Clan c : clans)
					sender.addChatMessage(new TextComponentTranslation("chat.clan", c.getName(), c.getMembers()
							.size()));
			}
		}
	}

	private class CommandLeave extends PTCommandBase
	{
		public CommandLeave()
		{
			super("leave");
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
		{
			IPlayerData data = getPlayerData(sender);
			if (!data.hasClan()) throw new CommandException(Lang.translate("command.exception.clan.noclan"));

			Clan c = data.getClan();
			data.leave();
			sender.addChatMessage(new TextComponentTranslation("chat.clan.leave", c.getName()));
		}
	}

	private class CommandCreate extends PTCommandBase
	{
		public CommandCreate()
		{
			super("create");
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
		{
			// TODO Auto-generated method stub

		}
	}

	private class CommandDisband extends PTCommandBase
	{
		public CommandDisband()
		{
			super("disband");
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
		{
			// TODO Auto-generated method stub

		}
	}

	private class CommandInvites extends PTCommandTreeBase
	{
		public CommandInvites()
		{
			super("invites");
		}
	}

	private class CommandKick extends PTCommandBase
	{
		public CommandKick()
		{
			super("kick");
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
		{
			// TODO Auto-generated method stub

		}
	}

	private class CommandAccept extends PTCommandBase
	{
		public CommandAccept()
		{
			super("accept");
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
		{
			// TODO Auto-generated method stub

		}
	}

	private class CommandRank extends PTCommandTreeBase
	{
		public CommandRank()
		{
			super("rank");
		}

		private class CommandRight extends PTCommandTreeBase
		{
			public CommandRight()
			{
				super("right");
			}

			private class CommandAdd extends PTCommandBase
			{
				public CommandAdd()
				{
					super("add");
				}

				@Override
				public void execute(MinecraftServer server, ICommandSender sender, String[] args)
						throws CommandException
				{
					// TODO Auto-generated method stub

				}
			}

			private class CommandRemove extends PTCommandBase
			{
				public CommandRemove()
				{
					super("remove");
				}

				@Override
				public void execute(MinecraftServer server, ICommandSender sender, String[] args)
						throws CommandException
				{
					// TODO Auto-generated method stub

				}
			}
		}

		private class CommandGrant extends PTCommandBase
		{
			public CommandGrant()
			{
				super("grant");
			}

			@Override
			public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
			{
				// TODO Auto-generated method stub

			}
		}

		private class CommandCreate extends PTCommandBase
		{
			public CommandCreate()
			{
				super("create");
			}

			@Override
			public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
			{
				// TODO Auto-generated method stub

			}
		}

		private class CommandRemove extends PTCommandBase
		{
			public CommandRemove()
			{
				super("remove");
			}

			@Override
			public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
			{
				// TODO Auto-generated method stub

			}
		}

		private class CommandDefault extends PTCommandBase
		{
			public CommandDefault()
			{
				super("default");
			}

			@Override
			public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
			{
				// TODO Auto-generated method stub

			}
		}
	}

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
