package lewismcreu.playertrophy.common.commands;

import lewismcreu.playertrophy.PlayerTrophy;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

/**
 * @author Lewis_McReu
 */
public abstract class PTCommandBase extends CommandBase
{
	private String name;

	public PTCommandBase(String name)
	{
		super();
		this.name = name;
	}

	@Override
	public String getCommandName()
	{
		return name;
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_)
	{
		return "commands." + PlayerTrophy.MODID + "." + name + ".usage";
	}
}
