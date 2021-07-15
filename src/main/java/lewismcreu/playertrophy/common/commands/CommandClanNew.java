package lewismcreu.playertrophy.common.commands;

import lewismcreu.playertrophy.PlayerTrophy;
import lewismcreu.playertrophy.common.data.Clan;
import lewismcreu.playertrophy.common.data.Clan.Member;
import lewismcreu.playertrophy.common.data.Clan.Rank;
import lewismcreu.playertrophy.common.data.IPlayerData;
import lewismcreu.playertrophy.common.data.Right;
import lewismcreu.playertrophy.proxy.CommonProxy;
import lewismcreu.playertrophy.util.CollectionUtil;
import lewismcreu.playertrophy.util.Lang;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.UsernameCache;

import java.util.*;

public class CommandClanNew extends PTCommandTreeBase {
	private static final String noClan = "command.exception.clan.noclan";
	private static final String inClan = "command.exception.clan.inclan";
	private static final String noName = "command.exception.clan.noname";
	private static final String notAPlayer = "command.exception.notaplayer";
	private static final String rankExists =
			"command.exception.clan.rankexists";
	private static final String isDefault = "command.exception.clan.isdefault";
	private static final String rankNotFound = "command.exception.clan.norank";
	private static final String createClan = "chat.clan.created";
	private static final String noRight = "command.exception.clan.noright";
	private static final String badParams = "command.exception.badparams";
	private static final String clanDesc = "chat.clan";
	private static final String clans = "chat.clans";
	private static final String clansEmpty = "chat.clans.empty";
	private static final String leaveClan = "chat.clan.leave";
	private static final String disbandClan = "chat.clan.disband";
	private static final String invites = "chat.clan.invites";
	private static final String invited = "chat.clan.invited";

	public CommandClanNew()
	{
		super("clan");
		addSubcommand(new CommandCreate());
		addSubcommand(new CommandDisband());
		addSubcommand(new CommandInvites());
		addSubcommand(new CommandKick());
		addSubcommand(new CommandRank());
		addSubcommand(new CommandList());
		addSubcommand(new CommandLeave());
		addSubcommand(new CommandView());
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
			IPlayerData data = checkPlayerData(sender);
			if (data.hasClan())
				throw new CommandException(Lang.translate(inClan));

			if (args.length != 1)
				throw new CommandException(Lang.translate(noName));

			String name = args[0];
			UUID uuid = ((EntityPlayer) sender).getPersistentID();
			Clan c = PlayerTrophy.getData().createClan(uuid);
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
			checkRight(sender, Right.DISBAND);
			PlayerTrophy.getData().disbandClan(c);
			CommandUtil.sendMessage(sender,
					Lang.format(disbandClan, c.getName()));
		}
	}

	private class CommandInvites extends PTCommandTreeBase
	{
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
				if (args.length != 1) badParams();
				IPlayerData data = checkPlayerData(sender);
				if (data.hasClan())
					throw new CommandException(Lang.translate(inClan));
				data.acceptInvitation(PlayerTrophy.getData().findClan(args[0]));
			}

			@Override
			public List<String> getTabCompletionOptions(MinecraftServer server,
					ICommandSender sender, String[] args, BlockPos pos)
			{
				if (args.length == 1) try
				{
					IPlayerData data = checkPlayerData(sender);
					return CommandUtil.filterMatches(CollectionUtil.convert(
							data.getInvitations(), c -> c.getName()), args[0]);
				}
				catch (CommandException e)
				{}
				return Collections.emptyList();
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
					checkRight(sender, Right.INVITE);

					EntityPlayer player = getPlayer(server, sender, args[0]);
					IPlayerData target = checkPlayerData(player);
					target.addInvitation(clan);
					CommandUtil.sendMessage(player,
							Lang.format(invited, clan.getName()));
				}
				else badParams();
			}

			@Override
			public List<String> getTabCompletionOptions(MinecraftServer server,
					ICommandSender sender, String[] args, BlockPos pos)
			{
				if (args.length == 1)
					return CommandUtil.getMatchingPlayerNames(server, args[0]);
				return super.getTabCompletionOptions(server, sender, args, pos);
			}
		}

		private class CommandList extends PTCommandTreeBase
		{
			private class CommandClan extends PTCommandBase
			{
				public CommandClan()
				{
					super("clan");
				}

				@Override
				public void execute(MinecraftServer server,
						ICommandSender sender, String[] args)
						throws CommandException
				{
					if (args.length == 0)
					{
						Clan clan = checkClan(sender);
						checkRight(sender, Right.INVITE);

						CommandUtil.sendMessage(sender,
								Lang.translate(invites));
						for (UUID inv : clan.getInvitations())
							CommandUtil.sendMessage(sender,
									UsernameCache.getLastKnownUsername(inv));
					}
					else badParams();
				}
			}

			private class CommandSelf extends PTCommandBase
			{
				public CommandSelf()
				{
					super("self");
				}

				@Override
				public void execute(MinecraftServer server,
						ICommandSender sender, String[] args)
						throws CommandException
				{
					if (args.length == 0)
					{
						IPlayerData data = checkPlayerData(sender);

						CommandUtil.sendMessage(sender,
								Lang.translate(invites));
						for (Clan clan : data.getInvitations())
							CommandUtil.sendMessage(sender, clan.getName());
					}
					else badParams();
				}
			}

			public CommandList()
			{
				super("list");
				addSubcommand(new CommandSelf());
				addSubcommand(new CommandClan());
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
					checkRight(sender, Right.INVITE);

					EntityPlayer player = getPlayer(server, sender, args[0]);
					IPlayerData target = checkPlayerData(player);
					target.removeInvitation(clan);
				}
				else badParams();

			}

			@Override
			public List<String> getTabCompletionOptions(MinecraftServer server,
					ICommandSender sender, String[] args, BlockPos pos)
			{
				if (args.length == 1)
					return CommandUtil.getMatchingPlayerNames(server, args[0]);
				return super.getTabCompletionOptions(server, sender, args, pos);
			}
		}

		public CommandInvites()
		{
			super("invite");
			addSubcommand(new CommandAdd());
			addSubcommand(new CommandRemove());
			addSubcommand(new CommandList());
			addSubcommand(new CommandAccept());
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
			if (args.length != 1) badParams();
			else
			{
				Clan clan = checkClan(sender);
				UUID uuid = findUUID(args[0]);
				clan.removeMember(uuid);
			}
		}

		public List<String> getTabCompletionOptions(MinecraftServer server,
				ICommandSender sender, String[] args, BlockPos pos)
		{
			if (args.length == 1)
				return CommandUtil.getMatchingPlayerNames(server, args[0]);
			return super.getTabCompletionOptions(server, sender, args, pos);
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
			IPlayerData data = checkPlayerData(sender);
			if (!data.hasClan())
				throw new CommandException(Lang.translate(noClan));

			Clan c = data.getClan();
			data.leave();
			sender.addChatMessage(
					new TextComponentTranslation(leaveClan, c.getName()));
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

	private class CommandRank extends PTCommandTreeBase
	{
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
				if (args.length != 1) badParams();

				checkRight(sender, Right.MANAGE);

				Clan c = checkClan(sender);
				if (c.getRank(args[0]) != null)
					throw new CommandException(Lang.translate(rankExists));

				Rank r = c.new Rank();
				r.setName(args[0]);
				c.addRank(r);
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
				if (args.length != 1) badParams();
				checkRight(sender, Right.MANAGE);
				Clan c = checkClan(sender);
				Rank r = c.getRank(args[0]);
				if (r == null)
					throw new CommandException(Lang.translate(rankNotFound));
				c.setDefaultRank(r);
			}

			@Override
			public List<String> getTabCompletionOptions(MinecraftServer server,
					ICommandSender sender, String[] args, BlockPos pos)
			{
				try
				{
					Clan c = checkClan(sender);
					if (args.length == 1) return CollectionUtil.filter(
							CollectionUtil.convert(c.getRanks(),
									r -> r.getName()),
							l -> l.startsWith(args[0]));
				}
				catch (CommandException e)
				{}
				return super.getTabCompletionOptions(server, sender, args, pos);
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
				if (args.length != 2) badParams();
				checkRight(sender, Right.MANAGE);

				IPlayerData data = checkPlayerData(sender);
				Rank r = data.getClan().getRank(args[0]);
				Member m = data.getClan().getMember(findUUID(args[1]));
				if (r == null)
					throw new CommandException(Lang.translate(rankNotFound));
				if (m == null) throw new PlayerNotFoundException();
				m.setRank(r);
			}

			@Override
			public List<String> getTabCompletionOptions(MinecraftServer server,
					ICommandSender sender, String[] args, BlockPos pos)
			{
				try
				{
					Clan c = checkClan(sender);
					if (args.length == 1) return CollectionUtil.filter(
							CollectionUtil.convert(c.getRanks(),
									r -> r.getName()),
							l -> l.startsWith(args[0]));
					else if (args.length == 2) return CommandUtil
							.getMatchingPlayerNames(server, args[1]);
				}
				catch (CommandException e)
				{}
				return super.getTabCompletionOptions(server, sender, args, pos);
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
				if (args.length != 0) badParams();
				checkRight(sender, Right.MANAGE);
				Collection<Rank> ranks = checkClan(sender).getRanks();
				for (Rank r : ranks)
				{
					String s = r.getName();
					Collection<Right> rights = r.getRights();
					if (!rights.isEmpty())
					{
						s += " : ";
						Iterator<Right> ri = rights.iterator();
						while (ri.hasNext())
						{
							s += ri.next().name();
							if (ri.hasNext()) s += ", ";
						}
					}
					CommandUtil.sendMessage(sender, s);
				}
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
				if (args.length != 1) badParams();

				checkRight(sender, Right.MANAGE);

				Clan c = checkClan(sender);

				Rank r = c.getRank(args[0]);
				if (r == c.getDefaultRank())
					throw new CommandException(Lang.translate(isDefault));
				c.removeRank(r);
			}

			@Override
			public List<String> getTabCompletionOptions(MinecraftServer server,
					ICommandSender sender, String[] args, BlockPos pos)
			{
				try
				{
					Clan c = checkClan(sender);
					if (args.length == 1) return CollectionUtil.filter(
							CollectionUtil.convert(c.getRanks(),
									r -> r.getName()),
							l -> l.startsWith(args[0]));
				}
				catch (CommandException e)
				{}
				return super.getTabCompletionOptions(server, sender, args, pos);
			}
		}

		private class CommandRight extends PTCommandTreeBase
		{
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
					if (args.length != 2) badParams();

					checkRight(sender, Right.MANAGE);
					IPlayerData data = checkPlayerData(sender);
					Rank r = data.getClan().getRank(args[0]);
					if (r == null) throw new CommandException(
							Lang.translate(rankNotFound));
					r.addRight(Right.valueOf(args[1].toUpperCase()));
				}

				@Override
				public List<String> getTabCompletionOptions(
						MinecraftServer server, ICommandSender sender,
						String[] args, BlockPos pos)
				{
					try
					{
						Clan c = checkClan(sender);
						if (args.length == 1) return CollectionUtil.filter(
								CollectionUtil.convert(c.getRanks(),
										r -> r.getName()),
								l -> l.startsWith(args[0]));
						else if (args.length == 2) return CollectionUtil.filter(
								CollectionUtil.convert(
										Arrays.asList(Right.values()),
										r -> r.name()),
								l -> l.startsWith(args[1]));
					}
					catch (CommandException e)
					{}
					return super.getTabCompletionOptions(server, sender, args,
							pos);
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
					if (args.length != 2) badParams();

					checkRight(sender, Right.MANAGE);

					IPlayerData data = checkPlayerData(sender);
					Rank r = data.getClan().getRank(args[0]);
					if (r == null) throw new CommandException(
							Lang.translate(rankNotFound));
					r.removeRight(Right.valueOf(args[1].toUpperCase()));
				}

				@Override
				public List<String> getTabCompletionOptions(
						MinecraftServer server, ICommandSender sender,
						String[] args, BlockPos pos)
				{
					try
					{
						Clan c = checkClan(sender);
						if (args.length == 1) return CollectionUtil.filter(
								CollectionUtil.convert(c.getRanks(),
										r -> r.getName()),
								l -> l.startsWith(args[0]));
						else if (args.length == 2) return CollectionUtil.filter(
								CollectionUtil.convert(
										Arrays.asList(Right.values()),
										r -> r.name()),
								l -> l.startsWith(args[1]));
					}
					catch (CommandException e)
					{}
					return super.getTabCompletionOptions(server, sender, args,
							pos);
				}
			}

			public CommandRight()
			{
				super("right");
				addSubcommand(new CommandAdd());
				addSubcommand(new CommandRemove());
			}
		}

		public CommandRank()
		{
			super("rank");
			addSubcommand(new CommandRight());
			addSubcommand(new CommandCreate());
			addSubcommand(new CommandDefault());
			addSubcommand(new CommandGrant());
			addSubcommand(new CommandRemove());
			addSubcommand(new CommandList());
		}
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
			if (args.length == 1) { return CommandUtil
					.getMatchingClanNames(args[0]); }
			return Collections.emptyList();
		}
	}

	private static void badParams() throws CommandException
	{
		throw new CommandException(Lang.translate(badParams));
	}

	private static Clan checkClan(ICommandSender sender) throws CommandException
	{
		Clan c = getClan(sender);
		if (c == null) throw new CommandException(Lang.translate(noClan));
		return c;
	}

	private static IPlayerData checkPlayerData(ICommandSender sender)
			throws CommandException
	{
		IPlayerData data = getPlayerData(sender);
		if (data == null)
			throw new CommandException(Lang.translate(notAPlayer));
		return data;
	}

	private static void checkRight(ICommandSender sender, Right right)
			throws CommandException
	{
		IPlayerData data = checkPlayerData(sender);
		if (!data.hasRight(right))
			throw new CommandException(Lang.format(noRight, right.name()));
	}

	private static UUID findUUID(String name)
	{
		Map.Entry<UUID, String> e = CollectionUtil.find(
				UsernameCache.getMap().entrySet(), en -> en.getValue(), name);
		return e != null ? e.getKey() : null;
	}

	private static Clan getClan(ICommandSender sender) throws CommandException
	{
		return CommonProxy.getClan(getCommandSenderAsPlayer(sender));
	}

	private static IPlayerData getPlayerData(ICommandSender sender)
			throws CommandException
	{
		return CommonProxy.getPlayerData(getCommandSenderAsPlayer(sender));
	}

	private static void sendClanMessage(ICommandSender sender, Clan c)
	{
		CommandUtil.sendMessage(sender,
				Lang.format(clanDesc, c.getName(), c.getMembers().size()));
	}
}
