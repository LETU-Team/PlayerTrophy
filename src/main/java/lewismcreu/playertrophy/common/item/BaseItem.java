package lewismcreu.playertrophy.common.item;

import lewismcreu.playertrophy.PlayerTrophy;
import net.minecraft.item.Item;

public class BaseItem extends Item
{
	public BaseItem(String unlocalizedName)
	{
		setUnlocalizedName(unlocalizedName);
		setRegistryName(PlayerTrophy.MODID, unlocalizedName);
	}
}
