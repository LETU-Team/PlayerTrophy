package com.lewismcreu.playertrophy.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

import com.lewismcreu.playertrophy.PlayerTrophy;
import com.lewismcreu.playertrophy.clan.Clan;
import com.lewismcreu.playertrophy.clan.Rank;
import com.lewismcreu.playertrophy.clan.Right;
import com.lewismcreu.playertrophy.player.PlayerData;
import com.lewismcreu.playertrophy.proxy.CommonProxy;
import com.lewismcreu.playertrophy.util.Language;

/**
 * @author Lewis_McReu
 */
public class CommandClan extends LMCommandBase
{
	private List<String> firstParams;
	private List<String> rankParams;
	private List<String> rightParams;

	public CommandClan()
	{
		super("clan");
		firstParams = new ArrayList<String>();
		firstParams.add("list");
		firstParams.add("create");
		firstParams.add("disband");
		firstParams.add("invite");
		firstParams.add("kick");
		firstParams.add("members");
		firstParams.add("invites");
		firstParams.add("accept");
		firstParams.add("rank");
		firstParams.add("leave");
		Collections.sort(firstParams);
		rankParams = new ArrayList<String>();
		rankParams.add("create");
		rankParams.add("grant");
		rankParams.add("right");
		rankParams.add("remove");
		rankParams.add("default");
		Collections.sort(rankParams);
		rightParams = new ArrayList<String>();
		rightParams.add("add");
		rightParams.add("remove");
		Collections.sort(rightParams);
	}

	@Override
	public void processCommand(ICommandSender sender, String[] params)
	{
		try
		{
			if (params.length > 0)
			{
				switch (params[0])
				{
					case "list":
						handleList(sender);
						break;
					case "create":
						handleCreate(sender, params);
						break;
					case "disband":
						handleDisband(sender, params);
						break;
					case "invite":
						handleInvite(sender, params);
						break;
					case "kick":
						handleKick(sender, params);
						break;
					case "members":
						handleMembers(sender);
						break;
					case "accept":
						handleAccept(sender, params);
						break;
					case "invites":
						handleInvites(sender);
						break;
					case "rank":
						handleRank(sender, params);
						break;
					case "leave":
						handleLeave(sender, params);
						break;

					default:
						throw new IllegalArgumentException("playertrophy.command.invalidcommand");
				}
			}
			else
			{
				Clan c = getClan(sender);
				if (c != null)
				{
					sendMessage(
							sender,
							c.getName() + " - " + c.getMemberRank(((EntityPlayer) sender)
									.getUniqueID()));
					return;
				}
				else throw new IllegalArgumentException("playertrophy.command.noclan");
			}
		}
		catch (IllegalArgumentException | PlayerNotFoundException e)
		{
			sendErrorMessage(sender, Language.getLocalizedString(e.getMessage()));
		}
	}

	private void handleLeave(ICommandSender sender, String[] params)
	{
		Clan c = getClan(sender);
		if (c != null)
		{
			getPlayerData(sender).leaveClan();
			sender.addChatMessage(new ChatComponentText(String.format(
					Language.getLocalizedString("playertrophy.command.clanleave"), c.getName())));
		}
		else throw new IllegalArgumentException("playertrophy.command.noclan");
	}

	private void handleList(ICommandSender sender)
	{
		Map<String, Clan> clans = PlayerTrophy.instance.worldData.getClans();
		if (clans.size() > 0)
		{
			sender.addChatMessage(new ChatComponentText(Language
					.getLocalizedString("playertrophy.clan.clans") + ":"));
			for (Clan c : clans.values())
			{
				sender.addChatMessage(new ChatComponentText(c.getName() + " - " + c.getMembers()
						.size() + " " + Language.getLocalizedString("playertrophy.clan.members")));
			}
		}
		else throw new IllegalArgumentException("playertrophy.command.noclansfound");
	}

	private void handleCreate(ICommandSender sender, String[] params)
	{
		Clan c = getClan(sender);
		if (c == null)
		{
			EntityPlayer p = (EntityPlayer) sender;
			UUID uuid = p.getUniqueID();
			if (params.length > 1)
			{
				String clanName = params[1];
				for (int i = 2; i < params.length; i++)
				{
					clanName = clanName + " " + params[i];
				}
				if (!PlayerTrophy.instance.worldData.getClans().containsKey(clanName))
				{
					Clan clan = PlayerTrophy.instance.worldData.createClan(clanName, uuid);
					if (clan != null)
					{
						PlayerData data = (PlayerData) ((EntityPlayer) sender)
								.getExtendedProperties("playertrophy");
						data.setClan(clan);
						sender.addChatMessage(new ChatComponentText(String.format(
								Language.getLocalizedString("playertrophy.command.clancreate"),
								clan.getName())));
					}
					else throw new IllegalArgumentException("playertrophy.command.clanexists");
				}
				else throw new IllegalArgumentException("playertrophy.command.clanexists");
			}
			else throw new IllegalArgumentException("playertrophy.command.invalidcommand");
		}
		else throw new IllegalArgumentException("playertrophy.command.alreadyinclan");
	}

	private void handleDisband(ICommandSender sender, String[] params)
	{
		Clan c = getClan(sender);
		if (c != null && c.delete(((EntityPlayer) sender).getUniqueID()))
		{
			sender.addChatMessage(new ChatComponentText(String.format(
					Language.getLocalizedString("playertrophy.command.clandisband"), c.getName())));
			return;
		}
		else throw new IllegalArgumentException("playertrophy.command.invalidpermissions");
	}

	private void handleInvite(ICommandSender sender, String[] params)
	{
		Clan c = getClan(sender);
		if (params.length > 1)
		{
			String targetName = params[1];
			UUID targetUuid = CommonProxy.getUuidForName(targetName);
			EntityPlayer player = CommonProxy.getPlayerForUuid(targetUuid);
			if (targetUuid != null && !c.hasMember(targetUuid) && player != null)
			{
				PlayerData data = (PlayerData) player.getExtendedProperties("playertrophy");
				data.invite(c);
				player.addChatMessage(new ChatComponentText(String.format(
						Language.getLocalizedString("playertrophy.command.invitenotification"),
						c.getName())));
				sender.addChatMessage(new ChatComponentText(String.format(
						Language.getLocalizedString("playertrophy.command.inviteconfirmation"),
						player.getName())));
			}
			else throw new IllegalArgumentException("playertrophy.command.playernotfoundorinclan");
		}
		else throw new IllegalArgumentException("playertrophy.command.invalidcommand");
	}

	private void handleKick(ICommandSender sender, String[] params)
	{
		if (params.length > 1)
		{
			Clan c = getClan(sender);
			String targetName = params[1];
			UUID uuid = CommonProxy.getUuidForName(targetName);
			if (c.hasMember(uuid))
			{
				if (c.removeMember(uuid, ((EntityPlayer) sender).getUniqueID()))
				{
					PlayerTrophy.proxy.broadcastChatMessage(String.format(
							Language.getLocalizedString("playertrophy.command.bannednotification"),
							targetName, c.getName()));
					return;
				}
				else throw new IllegalArgumentException("playertrophy.command.invalidpermissions");
			}
			else throw new IllegalArgumentException("playertrophy.command.notamember");
		}
		else throw new IllegalArgumentException("playertrophy.command.invalidcommand");
	}

	private void handleMembers(ICommandSender sender)
	{
		Clan c = getClan(sender);
		if (c != null)
		{
			Iterator<Entry<UUID, Rank>> mem = c.getMembers().iterator();
			while (mem.hasNext())
			{
				Entry<UUID, Rank> m = mem.next();
				sender.addChatMessage(new ChatComponentText(
						CommonProxy.getNameForUuid(m.getKey()) + " - " + m.getValue().toString() + " - " + m
								.getKey().toString()));
			}
			return;
		}
		throw new IllegalArgumentException("playertrophy.command.noclan");
	}

	private void handleAccept(ICommandSender sender, String[] params)
	{
		if (params.length > 1)
		{
			String clanName = params[1];
			for (int i = 2; i < params.length; i++)
			{
				clanName = clanName + " " + params[i];
			}
			Clan x = PlayerTrophy.instance.worldData.getClan(clanName);
			EntityPlayer p = (EntityPlayer) sender;
			PlayerData pdata = (PlayerData) p.getExtendedProperties("playertrophy");
			List<Clan> clans = pdata.getInvites();
			if (!pdata.isInClan())
			{
				if (clans.contains(x))
				{
					pdata.accept(x);
					sender.addChatMessage(new ChatComponentText(String.format(
							Language.getLocalizedString("playertrophy.command.clanjoined"),
							clanName)));
					return;
				}
				else throw new IllegalArgumentException("playertrophy.command.clannotfound");
			}
			else throw new IllegalArgumentException("playertrophy.command.alreadyinclan");
		}
		else throw new IllegalArgumentException("playertrophy.command.invalidcommand");
	}

	private void handleInvites(ICommandSender sender)
	{
		EntityPlayer p = (EntityPlayer) sender;
		PlayerData pdata = (PlayerData) p.getExtendedProperties("playertrophy");
		List<Clan> clans = pdata.getInvites();
		if (clans.size() > 0)
		{
			for (Clan c : clans)
			{
				sendMessage(sender, c.getName());
			}
		}
		else sendMessage(sender, Language.getLocalizedString("playertrophy.command.noinvites"));
	}

	private void handleRank(ICommandSender sender, String[] params) throws PlayerNotFoundException
	{
		Clan c = getClan(sender);
		if (params.length > 1)
		{
			if (c != null) switch (params[1])
			{
				case "create":
					handleCreateRank(sender, params);
					break;
				case "grant":
					handleGrantRank(sender, params);
					break;
				case "right":
					handleRightRank(sender, params);
					break;
				case "remove":
					handleRemoveRank(sender, params);
					break;
				case "default":
					handleDefaultRank(sender, params);
					break;
				default:
					throw new IllegalArgumentException("playertrophy.command.invalidcommand");
			}
		}
		else
		{
			if (c != null)
			{
				for (Rank r : c.getRanks())
				{
					sendMessage(sender, r.getName());
				}
			}
			else throw new IllegalArgumentException("playertrophy.command.noclan");
		}
	}

	private void handleDefaultRank(ICommandSender sender, String[] params)
	{
		if (params.length > 2)
		{
			Clan c = getClan(sender);
			c.setDefaultRank(c.getRank(params[2]));
		}
		else sendMessage(sender, getClan(sender).getDefaultRank().toString());
	}

	private void handleRemoveRank(ICommandSender sender, String[] params) throws PlayerNotFoundException
	{
		if (params.length > 2)
		{
			Clan c = getClan(sender);
			Rank r = c.getRank(params[2]);
			c.removeRank(r, getCommandSenderAsPlayer(sender).getUniqueID());
		}
		else throw new IllegalArgumentException("playertrophy.command.invalidcommand");
	}

	private void handleRightRank(ICommandSender sender, String[] params)
	{
		if (params.length > 2)
		{
			switch (params[2])
			{
				case "add":
					handleAddRightRank(sender, params);
					break;
				case "remove":
					handleRemoveRightRank(sender, params);
					break;
			}
		}
		else throw new IllegalArgumentException("playertrophy.command.invalidcommand");
	}

	private void handleRemoveRightRank(ICommandSender sender, String[] params)
	{
		if (params.length > 3)
		{
			Clan c = getClan(sender);
			Rank rank = c.getRank(params[3]);
			Right right = Right.valueOf(params[4].toUpperCase());
			if (rank != null)
			{
				rank.removeRight(right);
				return;
			}
		}
		throw new IllegalArgumentException("playertrophy.command.invalidcommand");
	}

	private void handleAddRightRank(ICommandSender sender, String[] params)
	{
		if (params.length > 3)
		{
			Clan c = getClan(sender);
			Rank rank = c.getRank(params[3]);
			Right right = Right.valueOf(params[4].toUpperCase());
			if (rank != null)
			{
				rank.addRight(right);
				return;
			}
		}
		throw new IllegalArgumentException("playertrophy.command.invalidcommand");
	}

	private void handleGrantRank(ICommandSender sender, String[] params) throws PlayerNotFoundException
	{
		if (params.length > 3)
		{
			Clan c = getClan(sender);
			PlayerData data = getPlayerData(sender);
			Rank rank = c.getRank(params[2]);
			UUID uuid = CommonProxy.getUuidForName(params[3]);
			if (rank == null) throw new IllegalArgumentException(
					"playertrophy.command.ranknotfound");
			if (uuid == null) throw new IllegalArgumentException(
					"playertrophy.command.playernotfound");
			c.setPlayerRank(uuid, rank, getCommandSenderAsPlayer(sender).getUniqueID());
		}
		else throw new IllegalArgumentException("playertrophy.command.invalidcommand");
	}

	private void handleCreateRank(ICommandSender sender, String[] params) throws PlayerNotFoundException
	{
		if (params.length > 2)
		{
			String rankName = params[2];
			Clan c = getClan(sender);
			Rank r = new Rank(rankName);
			c.addRank(r, getCommandSenderAsPlayer(sender).getUniqueID());
		}
		else throw new IllegalArgumentException("playertrophy.command.invalidcommand");
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] params, BlockPos pos)
	{
		List<String> out = new LinkedList<String>();
		switch (params.length)
		{
			case 1:
				String par1start = params[0];
				for (String s : firstParams)
				{
					if (doesStringStartWith(par1start, s))
					{
						out.add(s);
					}
				}
				break;
			case 2:
				switch (params[0])
				{
					case "list":
					case "create":
					case "remove":
					case "rank":
						String rankStart = params[1];
						for (String s : rankParams)
						{
							if (doesStringStartWith(rankStart, s))
							{
								out.add(s);
							}
						}
					case "invite":
						addAllPlayers(out, params[1]);
					case "kick":
						addClanMembers(sender, out, params[1]);
					default:
						return out;
				}
			case 3:
				switch (params[1])
				{
					case "grant":
						addClanRanks(sender, out, params[2]);
						break;
					case "remove":
						addClanRanks(sender, out, params[2]);
						break;
					case "right":
						String start = params[2];
						for (String s : rightParams)
						{
							if (doesStringStartWith(start, s))
							{
								out.add(s);
							}
						}
						break;
				}
				break;
			case 4:
				switch (params[1])
				{
					case "grant":
						addClanMembers(sender, out, params[3]);
						break;
					case "right":
						for (Rank r : getClan(sender).getRanks())
						{
							if (doesStringStartWith(params[3], r.toString()))
							{
								out.add(r.toString());
							}
						}
						break;
				}
				break;
			case 5:
				switch (params[1])
				{
					case "right":
						for (Right r : Right.values())
						{
							if (doesStringStartWith(params[4], r.toString()))
							{
								out.add(r.toString());
							}
						}
						break;
				}
		}
		if (out.size() == 0) return null;
		return out;
	}

	private void addClanRanks(ICommandSender sender, Collection<String> out, String start)
	{
		Clan clan = getClan(sender);
		Iterable<Rank> ranks = clan.getRanks();
		for (Rank rank : ranks)
		{
			String rankName = rank.toString();
			if (doesStringStartWith(start, rankName)) out.add(rankName);
		}
	}

	private void addClanMembers(ICommandSender sender, Collection<String> out, String start)
	{
		Clan clan = getClan(sender);
		if (clan != null)
		{
			Collection<Entry<UUID, Rank>> list = clan.getMembers();
			for (Entry<UUID, Rank> e : list)
			{
				String name = CommonProxy.getNameForUuid(e.getKey());
				if (doesStringStartWith(start, name)) out.add(name);
			}
		}
	}

	private Clan getClan(ICommandSender sender)
	{
		if (sender instanceof EntityPlayer) return CommonProxy
				.getClanForPlayer((EntityPlayer) sender);
		else return null;
	}

	private PlayerData getPlayerData(ICommandSender sender)
	{
		if (sender instanceof EntityPlayer) return CommonProxy.getPlayerData((EntityPlayer) sender);
		else return null;
	}

	@Override
	public int getRequiredPermissionLevel()
	{
		return 0;
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_)
	{
		return true;
	}
}
