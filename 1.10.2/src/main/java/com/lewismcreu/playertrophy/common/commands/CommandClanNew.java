package com.lewismcreu.playertrophy.common.commands;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;
import com.lewismcreu.playertrophy.PlayerTrophy;
import com.lewismcreu.playertrophy.common.data.Clan;
import com.lewismcreu.playertrophy.common.data.IPlayerData;
import com.lewismcreu.playertrophy.common.data.Right;
import com.lewismcreu.playertrophy.proxy.CommonProxy;
import com.lewismcreu.playertrophy.util.Lang;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.UsernameCache;

public class CommandClanNew extends PTCommandTreeBase
{
	private static final String noClan = "command.exception.clan.noclan";
	private static final String inClan = "command.exception.clan.inclan";
	private static final String noName = "command.exception.clan.noname";
	private static final String noPerm = "command.exception.clan.noperm";
	private static final String createClan = "chat.clan.created";
	private static final String notAPlayer = "command.exception.notaplayer";
	private static final String noRight = "command.exception.clan.noright";
	private static final String badParams = "command.exception.badparams";
	private static final String clanDesc = "chat.clan";
	private static final String clans = "chat.clans";
	private static final String clansEmpty = "chat.clans.empty";
	private static final String leaveClan = "chat.clan.leave";
	private static final String disbandClan = "chat.clan.disband";
	private static final String invites = "chat.clan.invites";

	public CommandClanNew()
	{
		super("clan");
		addSubcommand(new CommandCreate());
		addSubcommand(new CommandDisband());
		addSubcommand(new CommandInvites());
		addSubcommand(new CommandKick());
		addSubcommand(new CommandAccept());
		addSubcommand(new CommandRank());
		addSubcommand(new CommandList());
		addSubcommand(new CommandLeave());
		addSubcommand(new CommandView());
	}

	private class CommandView extends PTCommandBase
	{
		public CommandView()
		{
			super("view");
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender,
				String[] args) throws CommandException
		{
			if (args.length == 0)
			{
				Clan c = checkClan(sender);
				sendClanMessage(sender, c);
			}
			else if (args.length == 1)
			{
				String name = args[0];
				Collection<Clan> clans = PlayerTrophy.getData().findClan(name);
				if (clans.isEmpty())
					CommandUtil.sendMessage(sender, Lang.translate(clansEmpty));
				else
				{
					CommandUtil.sendMessage(sender,
							Lang.format(CommandClanNew.clans, clans.size()));
					for (Clan c : clans)
						sendClanMessage(sender, c);
				}
			}
			else badParams();
		}

		@Override
		public List<String> getTabCompletionOptions(MinecraftServer server,
				ICommandSender sender, String[] args, BlockPos pos)
		{
			List<String> list = Lists.newArrayList();
			if (args.length == 1)
			{
				CommandUtil.addMatchingClanNames(list, args[0]);
			}
			return list;
		}
	}

	private class CommandCreate extends PTCommandBase
	{
		public CommandCreate()
		{
			super("create");
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender,
				String[] args) throws CommandException
		{
			Clan c = getClan(sender);
			IPlayerData data = getPlayerData(sender);
			if (c != null) throw new CommandException(Lang.translate(inClan));

			if (args.length != 1)
				throw new CommandException(Lang.translate(noName));

			String name = args[0];
			UUID uuid = ((EntityPlayer) sender).getPersistentID();
			c = PlayerTrophy.getData().createClan(uuid);
			c.setName(name);
			data.setClan(c);
			CommandUtil.sendMessage(sender,
					Lang.format(createClan, c.getName()));
		}
	}

	private class CommandDisband extends PTCommandBase
	{
		public CommandDisband()
		{
			super("disband");
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender,
				String[] args) throws CommandException
		{
			Clan c = checkClan(sender);
			UUID uuid = ((EntityPlayer) sender).getPersistentID();
			checkRight(uuid, c, Right.DISBAND);
			PlayerTrophy.getData().disbandClan(c);
			CommandUtil.sendMessage(sender,
					Lang.format(disbandClan, c.getName()));
		}
	}

	private class CommandInvites extends PTCommandTreeBase
	{
		public CommandInvites()
		{
			super("invite");
			addSubcommand(new CommandAdd());
			addSubcommand(new CommandRemove());
			addSubcommand(new CommandList());
		}

		private class CommandList extends PTCommandBase
		{
			public CommandList()
			{
				super("list");
			}

			@Override
			public void execute(MinecraftServer server, ICommandSender sender,
					String[] args) throws CommandException
			{
				Clan clan = checkClan(sender);
				UUID uuid = ((EntityPlayer) sender).getPersistentID();
				checkRight(uuid, clan, Right.INVITE);

				CommandUtil.sendMessage(sender, Lang.translate(invites));
				for (UUID inv : clan.getInvitations())
					CommandUtil.sendMessage(sender,
							UsernameCache.getLastKnownUsername(inv));
			}
		}

		private class CommandAdd extends PTCommandBase
		{
			public CommandAdd()
			{
				super("add");
			}

			@Override
			public void execute(MinecraftServer server, ICommandSender sender,
					String[] args) throws CommandException
			{
				if (args.length == 1)
				{
					Clan clan = checkClan(sender);
					UUID uuid = ((EntityPlayer) sender).getPersistentID();
					checkRight(uuid, clan, Right.INVITE);

					EntityPlayer player = getPlayer(server, sender, args[0]);
					IPlayerData target = getPlayerData(player);
					target.addInvitation(clan);
				}
				else badParams();
			}

			@Override
			public List<String> getTabCompletionOptions(MinecraftServer server,
					ICommandSender sender, String[] args, BlockPos pos)
			{
				if (args.length == 1)
				{
					List<String> list = Lists.newArrayList();
					CommandUtil.addMatchingPlayerNames(server, list, args[0]);
					return list;
				}
				return super.getTabCompletionOptions(server, sender, args, pos);
			}
		}

		private class CommandRemove extends PTCommandBase
		{
			public CommandRemove()
			{
				super("remove");
			}

			@Override
			public void execute(MinecraftServer server, ICommandSender sender,
					String[] args) throws CommandException
			{
				if (args.length == 1)
				{
					Clan clan = checkClan(sender);
					UUID uuid = ((EntityPlayer) sender).getPersistentID();
					checkRight(uuid, clan, Right.INVITE);

					EntityPlayer player = getPlayer(server, sender, args[0]);
					IPlayerData target = getPlayerData(player);
					target.removeInvitation(clan);
				}
				else badParams();

			}

			@Override
			public List<String> getTabCompletionOptions(MinecraftServer server,
					ICommandSender sender, String[] args, BlockPos pos)
			{
				if (args.length == 1)
				{
					List<String> list = Lists.newArrayList();
					CommandUtil.addMatchingPlayerNames(server, list, args[0]);
					return list;
				}
				return super.getTabCompletionOptions(server, sender, args, pos);
			}
		}
	}

	private class CommandKick extends PTCommandBase
	{
		public CommandKick()
		{
			super("kick");
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender,
				String[] args) throws CommandException
		{
			// TODO
		}

		public List<String> getTabCompletionOptions(MinecraftServer server,
				ICommandSender sender, String[] args, BlockPos pos)
		{
			if (args.length == 1)
			{
				List<String> list = Lists.newArrayList();
				CommandUtil.addMatchingPlayerNames(server, list, args[0]);
				return list;
			}
			return super.getTabCompletionOptions(server, sender, args, pos);
		}
	}

	private class CommandAccept extends PTCommandBase
	{
		public CommandAccept()
		{
			super("accept");
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender,
				String[] args) throws CommandException
		{
			// TODO Auto-generated method stub

		}

		@Override
		public boolean checkPermission(MinecraftServer server,
				ICommandSender sender)
		{
			try
			{
				return super.checkPermission(server, sender)
						&& getClan(sender) == null;
			}
			catch (CommandException e)
			{
				return false;
			}
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
				public void execute(MinecraftServer server,
						ICommandSender sender, String[] args)
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
				public void execute(MinecraftServer server,
						ICommandSender sender, String[] args)
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
			public void execute(MinecraftServer server, ICommandSender sender,
					String[] args) throws CommandException
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
			public void execute(MinecraftServer server, ICommandSender sender,
					String[] args) throws CommandException
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
			public void execute(MinecraftServer server, ICommandSender sender,
					String[] args) throws CommandException
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
			public void execute(MinecraftServer server, ICommandSender sender,
					String[] args) throws CommandException
			{
				// TODO Auto-generated method stub

			}
		}
	}

	private class CommandList extends PTCommandBase
	{
		public CommandList()
		{
			super("list");
		}

		@Override
		public void execute(MinecraftServer server, ICommandSender sender,
				String[] args) throws CommandException
		{
			Collection<Clan> clans = PlayerTrophy.getData().getClans();
			if (clans.isEmpty()) sender.addChatMessage(
					new TextComponentTranslation("chat.clans.empty"));
			else
			{
				sender.addChatMessage(new TextComponentTranslation("chat.clans",
						clans.size()));
				for (Clan c : clans)
					sender.addChatMessage(new TextComponentTranslation(
							"chat.clan", c.getName(), c.getMembers().size()));
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
		public void execute(MinecraftServer server, ICommandSender sender,
				String[] args) throws CommandException
		{
			IPlayerData data = getPlayerData(sender);
			if (!data.hasClan()) throw new CommandException(
					Lang.translate("command.exception.clan.noclan"));

			Clan c = data.getClan();
			data.leave();
			sender.addChatMessage(new TextComponentTranslation(
					"chat.clan.leave", c.getName()));
		}
	}

	private static void checkRight(UUID uuid, Clan clan, Right right)
			throws CommandException
	{
		if (!clan.hasRight(uuid, right))
			throw new CommandException(Lang.format(noRight, right.name()));
	}

	private static Clan checkClan(ICommandSender sender) throws CommandException
	{
		Clan c = getClan(sender);
		if (c == null) throw new CommandException(Lang.translate(noClan));
		return c;
	}

	private static Clan getClan(ICommandSender sender) throws CommandException
	{
		if (sender instanceof EntityPlayer)
			return CommonProxy.getClan((EntityPlayer) sender);
		else throw new CommandException(Lang.translate(notAPlayer));
	}

	private static IPlayerData getPlayerData(ICommandSender sender)
			throws CommandException
	{
		if (sender instanceof EntityPlayer)
			return CommonProxy.getPlayerData((EntityPlayer) sender);
		else throw new CommandException(Lang.translate(notAPlayer));
	}

	private static void sendClanMessage(ICommandSender sender, Clan c)
	{
		CommandUtil.sendMessage(sender,
				Lang.format(clanDesc, c.getName(), c.getMembers().size()));
	}

	private static void badParams() throws CommandException
	{
		throw new CommandException(Lang.translate(badParams));
	}
}
