package com.lewismcreu.playertrophy.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.lewismcreu.playertrophy.PlayerTrophy;
import com.lewismcreu.playertrophy.player.PlayerData;
import com.lewismcreu.playertrophy.util.Language;

/**
 * @author Lewis_McReu
 */
public class ItemBounty extends BaseItem
{
	public ItemBounty()
	{
		super("itemBounty");
		this.setMaxStackSize(64);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world,
			EntityPlayer player)
	{
		if (!world.isRemote)
		{
			PlayerData data = (PlayerData) player
					.getExtendedProperties("playertrophy");

			if (stack.getTagCompound() != null
					&& stack.getTagCompound().getCompoundTag("display") != null
					&& stack.getTagCompound().getCompoundTag("display")
							.getString("Name") != null)
			{
				if (player.getDisplayName().equals(stack.getDisplayName()))
				{
					player.addChatMessage(new ChatComponentText(
							Language.getLocalizedString("playertrophy.bounty.selferror")));
				}
				else if (data.canPlaceBounty())
				{
					List<EntityPlayerMP> list = FMLCommonHandler.instance()
							.getMinecraftServerInstance()
							.getConfigurationManager().playerEntityList;
					EntityPlayer target = null;
					for (EntityPlayer p : list)
					{
						if (p.getDisplayName().equals(stack.getDisplayName()))
						{
							target = p;
							break;
						}
					}
					if (target == null)
					{
						player.addChatMessage(new ChatComponentText(
								Language.getLocalizedString("playertrophy.bounty.offlinetarget")));
					}
					player.inventory.decrStackSize(
							player.inventory.currentItem, 1);
					data.registerBounty();
					PlayerTrophy.instance.worldData.addBounty(target
							.getUniqueID());
				}
				else
				{
					player.addChatMessage(new ChatComponentText(Language
							.getLocalizedString("playertrophy.bounty.tooearly")));
				}
			}
		}

		return stack;
	}
}
