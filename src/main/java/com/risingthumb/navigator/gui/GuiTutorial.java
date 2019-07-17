package com.risingthumb.navigator.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.risingthumb.navigator.NavigatorMod;
import com.risingthumb.navigator.classes.Marker;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiTutorial extends GuiScreen {
	
	public static int[] marks = {0, 0, 0};
	
	public static boolean cancel = false;
	public static boolean moveToLoc = false;
	
	public static int selected = -10;

	public static List<Marker> waypoints = new ArrayList<>();
	
	private List<String> getList() {
		List<String> values = new ArrayList<>();
		values.add("moveToWaypoint");
		values.add("cancelMovement");
		values.add("close");
		return values;
	}
	
	ResourceLocation texture = new ResourceLocation(NavigatorMod.MODID, "textures/gui/book.png");
	int guiWidth = 175;
	int guiHeight = 228;
	
	GuiButton button2;
	GuiButton button1;
	GuiButton button0;
	
	final int BUTTON1 = 0;
	final int BUTTON2 = 1;
	final int BUTTON0 = BUTTON2+1; // This will always be the last one in the list
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		int centerX = (this.width-guiWidth)/2;
		int centerY = (this.height-guiHeight)/2;
		drawDefaultBackground();
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		drawTexturedModalRect(centerX, centerY, 0, 0, guiWidth, guiHeight);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void initGui() {
		buttonList.clear();
		
		int xPos = (this.width-100)/2;
		int yPos = (this.height-guiHeight)/2 + 4;
		
		for (int i = 0; i < getList().size()-1; i++) {
			buttonList.add(new GuiButton(i,xPos,yPos+20*i, 100,20, getList().get(i)    ));
		}
		
		int yPosClose = this.height-yPos-20;
		
		buttonList.add(button0=new GuiButton(BUTTON0,xPos+20, yPosClose, 60, 20, getList().get(getList().size()-1) ));
		
		super.initGui();
	}
	
	public void updateButtons() {
		
	}
	
	@Override
	public void actionPerformed(GuiButton button) throws IOException {
		switch(button.id) {
		case BUTTON2:
			cancel = !cancel;
			break;
		case BUTTON1:
			moveToLoc = !moveToLoc;
			break;
		case BUTTON0:
			NavigatorMod.saveConfig();
			Minecraft.getMinecraft().displayGuiScreen(null);
			break;
		default:
			break;
		}
		updateButtons();
		super.actionPerformed(button);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException{
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
