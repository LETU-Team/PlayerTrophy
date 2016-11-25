package com.lewismcreu.playertrophy.client.gui;

import java.io.IOException;
import java.util.UUID;

import com.lewismcreu.playertrophy.common.data.IPlayerData;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiClan extends GuiScreen
{
	private IPlayerData player;

	private GuiScreen createClanGui;
	private GuiScreen manageClanGui;

	public GuiClan(IPlayerData player)
	{
		this.player = player;
	}

	private int buttonId = 0;

	@Override
	public void initGui()
	{
		super.initGui();
		createClanGui = new GuiCreateClan();
		manageClanGui = new GuiManageClan();

	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		super.drawScreen(mouseX, mouseY, partialTicks);

		if (player.hasClan()) ;
		else createClanGui.drawScreen(mouseX, mouseY, partialTicks);
	}

	private class GuiCreateClan extends GuiScreen
	{
		GuiTextField clanNameField;

		public GuiCreateClan()
		{
			// TODO Auto-generated constructor stub
			clanNameField = new GuiTextField(buttonId++, fontRendererObj,
					(width - 200) / 2, 100, 200, 40);
			addButton(new GuiButtonExt(buttonId++, 100, 100, "Create"));
			GuiLabel label = new GuiLabel(fontRendererObj, 0, 0, 0, 0, 0, 0);
			label.setCentered();
			label.addLine("Create a clan");
			labelList.add(label);
		}

		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks)
		{
			// TODO Auto-generated method stub

			clanNameField.drawTextBox();

			super.drawScreen(mouseX, mouseY, partialTicks);
		}

		@Override
		protected void actionPerformed(GuiButton button) throws IOException
		{
			super.actionPerformed(button);
			String clanName = clanNameField.getText();
			
		}
	}

	private class GuiManageClan extends GuiScreen
	{
		public GuiManageClan()
		{
			// TODO Auto-generated constructor stub
		}
	}
}
