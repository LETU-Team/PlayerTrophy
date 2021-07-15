package lewismcreu.playertrophy.common.commands;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lewismcreu.playertrophy.PlayerTrophy;
import lewismcreu.playertrophy.common.data.Clan;
import lewismcreu.playertrophy.util.CollectionUtil;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class CommandUtil {
	public static final List<String> getMatchingPlayerNames(
			MinecraftServer server, String start) {
		return CollectionUtil.filter(
				Lists.newArrayList(server.getAllUsernames()),
				l -> l.startsWith(start));
	}

	public static final List<String> getMatchingClanNames(String start)
	{
		Set<String> set = Sets.newHashSet();
		for (Clan c : PlayerTrophy.getData().getClans())
			if (c.getName().startsWith(start)) set.add(c.getName());
		return Lists.newArrayList(set);
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

	public static final List<String> filterMatches(Collection<String> list,
			String match)
	{
		return CollectionUtil.filter(list, l -> l.startsWith(match));
	}
}
