package lewismcreu.playertrophy.common.commands;

import lewismcreu.playertrophy.common.item.ItemTrophy;
import lewismcreu.playertrophy.util.Lang;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * @author Lewis_McReu
 */
public class CommandTrophy extends PTCommandBase {
	public CommandTrophy() {
		super("trophy");
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender,
			String[] args) throws CommandException
	{
		if (args.length == 2)
		{
			EntityPlayer p = getCommandSenderAsPlayer(sender);
			EntityPlayer victim = getPlayer(server, sender, args[0]);
			EntityPlayer slayer = getPlayer(server, sender, args[1]);
			if (victim == null) throw new PlayerNotFoundException(
					Lang.translate(
							"command.exception.playertrophy.trophy.playernotfound"),
					args[0]);
			if (slayer == null) throw new PlayerNotFoundException(
					Lang.translate(
							"command.exception.playertrophy.trophy.playernotfound"),
					args[1]);

			p.inventory.addItemStackToInventory(ItemTrophy.create(
					slayer.getPersistentID(), victim.getPersistentID()));
		}
	}

	@Override
	public List<String> getTabCompletionOptions(MinecraftServer server,
			ICommandSender sender, String[] args, BlockPos pos)
	{
		if (args.length < 3) return CommandUtil.getMatchingPlayerNames(server,
				args[args.length - 1]);
		return super.getTabCompletionOptions(server, sender, args, pos);
	}
}
