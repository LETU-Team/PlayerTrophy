package com.lewismcreu.playertrophy.util;

import net.minecraftforge.fml.common.registry.LanguageRegistry;

/**
 * @author Lewis_McReu
 */
public class Language
{
	public static String getLocalizedString(String key)
	{
		return LanguageRegistry.instance().getStringLocalization(key);
	}
}
