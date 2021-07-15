package lewismcreu.playertrophy.util;

import net.minecraft.client.resources.I18n;

/**
 * @author Lewis_McReu
 */
public class Lang
{
	public static String translate(String key)
	{
		return format(key);
	}

	public static String format(String key, Object... args)
	{
		return I18n.format(key, args);
	}
}
