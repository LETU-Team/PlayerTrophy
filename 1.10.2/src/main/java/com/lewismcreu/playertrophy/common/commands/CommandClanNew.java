package com.lewismcreu.playertrophy.common.commands;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.lewismcreu.playertrophy.PlayerTrophy;
import com.lewismcreu.playertrophy.common.data.Clan;
import com.lewismcreu.playertrophy.common.data.IPlayerData;
import com.lewismcreu.playertrophy.common.data.Right;
import com.lewismcreu.playertrophy.util.Lang;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandClanNew extends CommandClanTreeBase
{
	public CommandClanNew()
	{
		super("clan");
		addSubcommand(new CommandList());
		addSubcommand(new CommandLeave());
		addSubcommand(new CommandInvites());
	}

	private class CommandCreate extends CommandClanBase
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

		@Override
		public boolean checkPermission(MinecraftServer server, ICommandSender sender)
		{
			try
			{
				return getClan(sender) == null && super.checkPermission(server, sender);
			}
			catch (CommandException e)
			{
				return false;
			}
		}

		@Override
		public Right getRequiredRight()
		{
			return Right.NONE;
		}
	}

	private class CommandDisband extends CommandClanBase
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

		@Override
		public Right getRequiredRight()
		{
			return Right.MANAGE;
		}
	}

	private class CommandInvites extends CommandClanTreeBase
	{
		public CommandInvites()
		{
			super("invite");
			addSubcommand(new CommandAdd());
			addSubcommand(new CommandRemove());
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
		{
			if (args.length < 1)
			{
				Clan c = getClan(sender);
				for ()
			}
			else super.execute(server, sender, args);
		}

		private class CommandAdd extends CommandClanBase
		{
			public CommandAdd()
			{
				super("add");
			}

			@Override
			public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
			{
				IPlayerData data = getPlayerData(sender);
				if (args.length == 1)
				{
					EntityPlayer player = getPlayer(server, sender, args[0]);
					IPlayerData target = getPlayerData(player);
					target.addInvitation(data.getClan());

				}
			}

			@Override
			public boolean checkPermission(MinecraftServer server, ICommandSender sender)
			{
				try
				{
					return super.checkPermission(server, sender) && getClan(sender).hasRight(((EntityPlayer) sender)
							.getPersistentID(), Right.INVITE);
				}
				catch (CommandException e)
				{
					return false;
				}
			}

			@Override
			public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
					BlockPos pos)
			{
				if (args.length == 1)
				{
					List<String> list = Lists.newArrayList();
					CommandUtil.addMatchingPlayerNames(server, list, args[0]);
					return list;
				}
				return super.getTabCompletionOptions(server, sender, args, pos);
			}

			@Override
			public Right getRequiredRight()
			{
				return Right.INVITE;
			}
		}

		private class CommandRemove extends CommandClanBase
		{
			public CommandRemove()
			{
				super("remove");
			}

			@Override
			public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
			{
				IPlayerData data = getPlayerData(sender);
				if (args.length == 1)
				{
					EntityPlayer player = getPlayer(server, sender, args[0]);
					IPlayerData target = getPlayerData(player);
					target.removeInvitation(data.getClan());
				}

			}

			@Override
			public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args,
					BlockPos pos)
			{
				if (args.length == 1)
				{
					List<String> list = Lists.newArrayList();
					CommandUtil.addMatchingPlayerNames(server, list, args[0]);
					return list;
				}
				return super.getTabCompletionOptions(server, sender, args, pos);
			}

			@Override
			public Right getRequiredRight()
			{
				return Right.INVITE;
			}
		}

		@Override
		public Right getRequiredRight()
		{
			return Right.INVITE;
		}
	}

	private class CommandKick extends CommandClanBase
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

		@Override
		public Right getRequiredRight()
		{
			// TODO Auto-generated method stub
			return null;
		}
	}

	private class CommandAccept extends CommandClanBase
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

		@Override
		public boolean checkPermission(MinecraftServer server, ICommandSender sender)
		{
			try
			{
				return super.checkPermission(server, sender) && getClan(sender) == null;
			}
			catch (CommandException e)
			{
				return false;
			}
		}

		@Override
		public Right getRequiredRight()
		{
			return Right.NONE;
		}
	}

	private class CommandRank extends CommandClanTreeBase
	{
		public CommandRank()
		{
			super("rank");
		}

		private class CommandRight extends CommandClanTreeBase
		{
			public CommandRight()
			{
				super("right");
			}

			private class CommandAdd extends CommandClanBase
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

				@Override
				public Right getRequiredRight()
				{
					// TODO Auto-generated method stub
					return null;
				}
			}

			private class CommandRemove extends CommandClanBase
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

				@Override
				public Right getRequiredRight()
				{
					// TODO Auto-generated method stub
					return null;
				}
			}

			@Override
			public Right getRequiredRight()
			{
				// TODO Auto-generated method stub
				return null;
			}
		}

		private class CommandGrant extends CommandClanBase
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

			@Override
			public Right getRequiredRight()
			{
				// TODO Auto-generated method stub
				return null;
			}
		}

		private class CommandCreate extends CommandClanBase
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

			@Override
			public Right getRequiredRight()
			{
				// TODO Auto-generated method stub
				return null;
			}
		}

		private class CommandRemove extends CommandClanBase
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

			@Override
			public Right getRequiredRight()
			{
				// TODO Auto-generated method stub
				return null;
			}
		}

		private class CommandDefault extends CommandClanBase
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

			@Override
			public Right getRequiredRight()
			{
				// TODO Auto-generated method stub
				return null;
			}
		}

		@Override
		public Right getRequiredRight()
		{
			return Right.MANAGE;
		}
	}

	@Override
	public Right getRequiredRight()
	{
		return Right.NONE;
	}

	private class CommandList extends CommandClanBase
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

		@Override
		public Right getRequiredRight()
		{
			return Right.NONE;
		}
	}

	private class CommandLeave extends CommandClanBase
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

		@Override
		public Right getRequiredRight()
		{
			return Right.NONE;
		}
	}
}
