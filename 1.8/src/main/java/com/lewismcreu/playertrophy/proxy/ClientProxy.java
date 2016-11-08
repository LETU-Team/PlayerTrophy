package com.lewismcreu.playertrophy.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.lewismcreu.playertrophy.util.Reference;

/**
 * @author Lewis_McReu
 */
@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
	public void init()
	{
		super.init();
		registerItemModel(CommonProxy.bounty, "itemBounty");
		registerItemModel(CommonProxy.playerTrophy, "itemPlayerTrophy");
		registerItemModel(CommonProxy.scepter, "itemScepter");
	}

	public static void registerItemModel(Item item, String name)
	{
		Minecraft
				.getMinecraft()
				.getRenderItem()
				.getItemModelMesher()
				.register(item, 0,
						new ModelResourceLocation(Reference.MOD_ID + ":" + name, "inventory"));
		ModelBakery.addVariantName(item, Reference.MOD_ID + ":" + name);
	}
}
