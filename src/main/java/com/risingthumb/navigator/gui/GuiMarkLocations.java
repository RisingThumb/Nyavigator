package com.risingthumb.navigator.gui;

import java.io.IOException;

import com.risingthumb.navigator.NavigatorMod;
import com.risingthumb.navigator.classes.Marker;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

public class GuiMarkLocations extends GuiScreen {
	
	public static int markX = 0;
	public static int markY = 0;
	public static int markZ = 0;
	private String name = "Waypoint";
	
	int guiWidth = 175;
	int guiHeight = 228;
	
	GuiButton button0;
	GuiButton buttonAdd;
	GuiButton buttonRemove;
	
	final int BUTTON0 = 999; // This will always be the last one in the list
	final int BUTTONADD = 1000;
	final int BUTTONREMOVE = 1001;
	final int UTILITYBUTTONNUM = 3;
	
	GuiTextField text;
	
	private GuiTextField textFieldX;
	private GuiTextField textFieldY;
	private GuiTextField textFieldZ;
	private GuiTextField textFieldName;
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		super.drawScreen(mouseX, mouseY, partialTicks);
		textFieldName.drawTextBox();
		drawString(Minecraft.getMinecraft().fontRenderer,"Name:",1,6,Integer.parseInt("FFAA00", 16));
		textFieldX.drawTextBox();
		drawString(Minecraft.getMinecraft().fontRenderer,"X:",1,27,Integer.parseInt("FFAA00", 16));
		textFieldY.drawTextBox();
		drawString(Minecraft.getMinecraft().fontRenderer,"Y:",1,47,Integer.parseInt("FFAA00", 16));
		textFieldZ.drawTextBox();
		drawString(Minecraft.getMinecraft().fontRenderer,"Z:",1,67,Integer.parseInt("FFAA00", 16));
		drawString(Minecraft.getMinecraft().fontRenderer,"Reopen this window if you remove/add waypoints",1,this.height-16,Integer.parseInt("FFAA00", 16));
	}
	
	@Override
	public void initGui() {
		markX = GuiTutorial.marks[0];
		markZ = GuiTutorial.marks[1];
		markY = GuiTutorial.marks[2];
		name = "Waypoint";
		
		buttonsClearCreate();
		
		textFieldName = new GuiTextField(-4, Minecraft.getMinecraft().fontRenderer,30,0, 80, 20);
		textFieldName.setFocused(false);
		textFieldName.setMaxStringLength(20);
		textFieldName.setText(name);
		
		textFieldX = new GuiTextField(-2, Minecraft.getMinecraft().fontRenderer,30,21, 40, 20);
		textFieldX.setFocused(false);
		textFieldX.setMaxStringLength(5);
		textFieldX.setText(""+markX);
		
		textFieldY = new GuiTextField(-3, Minecraft.getMinecraft().fontRenderer,30,41, 40, 20);
		textFieldY.setFocused(false);
		textFieldY.setMaxStringLength(3);
		textFieldY.setText(""+markY);
		
		textFieldZ = new GuiTextField(-3, Minecraft.getMinecraft().fontRenderer,30,61, 40, 20);
		textFieldZ.setFocused(false);
		textFieldZ.setMaxStringLength(5);
		textFieldZ.setText(""+markZ);
		
		
		super.initGui();
	}
	
	public void updateButtons() {
		
		for (GuiButton button : buttonList) {
			button.enabled = true;
		}
		
	}
	
	private void buttonsClearCreate() {
		buttonList.clear();
		
		int xPos = (this.width-160)/2;
		int yPos = (this.height-guiHeight)/2 + 4;
		
		for(int i = 0; i < GuiTutorial.waypoints.size(); i++) {
			buttonList.add(new GuiButton(i, xPos+80*(i/10), yPos+20*(i%10), 80, 20, GuiTutorial.waypoints.get(i).getString()));
		}
		
		int yPosClose = this.height-yPos-20;
		
		buttonList.add(button0=new GuiButton(BUTTON0,xPos+20, yPosClose, 60, 20, "Close" ));
		buttonList.add(buttonAdd=new GuiButton(BUTTONADD,xPos+80, yPosClose, 60, 20, "Add" ));
		buttonList.add(buttonRemove=new GuiButton(BUTTONREMOVE,xPos+140, yPosClose, 100, 20, "Remove Selected" ));
		
		if (GuiTutorial.selected!=-10) {
			buttonList.get(GuiTutorial.selected).enabled = false;
		}
	}
	
	@Override
	public void actionPerformed(GuiButton button) throws IOException {
		
		updateButtons();
		
		switch(button.id) {
		case BUTTON0:
			NavigatorMod.saveConfig();
			Minecraft.getMinecraft().displayGuiScreen(null);
			break;
		case BUTTONADD:
			name = textFieldName.getText();
			try {
				markX = Integer.parseInt(textFieldX.getText());
			}
			catch (NumberFormatException nfe) {}
			try {
				markZ = Integer.parseInt(textFieldZ.getText());
			}
			catch (NumberFormatException nfe) {}
			try {
				markY = Integer.parseInt(textFieldY.getText());
			}
			catch (NumberFormatException nfe) {}
			
			
			if (!GuiTutorial.waypoints.contains(new Marker(name,markX,markY,markZ))) {
				GuiTutorial.waypoints.add(new Marker(name,markX,markY,markZ));
			}
			break;
		case BUTTONREMOVE:
			for (int i = 0; i<GuiTutorial.waypoints.size(); i++) {
				Marker m = GuiTutorial.waypoints.get(i);
				if ((m.getString()==name && m.getX()==markX && m.getY()==markY && m.getZ()==markZ) ||i==GuiTutorial.selected) {
					GuiTutorial.waypoints.remove(i);
					GuiTutorial.selected = -10;
					break;
				}
			}
			break;
		default:
			if (button.id>=0 && button.id < GuiTutorial.waypoints.size()) {
				GuiTutorial.selected = button.id;
				name = GuiTutorial.waypoints.get(button.id).getString();
				markX = GuiTutorial.waypoints.get(button.id).getX();
				markY = GuiTutorial.waypoints.get(button.id).getY();
				markZ = GuiTutorial.waypoints.get(button.id).getZ();
				GuiTutorial.marks[0] = markX;
				GuiTutorial.marks[1] = markZ;
				GuiTutorial.marks[2] = markY;
			}
			break;
		}
		super.actionPerformed(button);
		buttonsClearCreate();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException{
		super.keyTyped(typedChar, keyCode);
		
		if (textFieldName.isFocused()) {
			textFieldName.textboxKeyTyped(typedChar, keyCode);
		}
		if (textFieldX.isFocused()) {
			if ((typedChar >= 48 && typedChar <= 57) || typedChar==8) {
				textFieldX.textboxKeyTyped(typedChar, keyCode);
			}
			if (typedChar=='-' && textFieldX.getText().length() == 0) {
				textFieldX.textboxKeyTyped(typedChar, keyCode);
			}
		}
		if (textFieldZ.isFocused()) {
			if ((typedChar >= 48 && typedChar <= 57) || typedChar==8) {
				textFieldZ.textboxKeyTyped(typedChar, keyCode);
			}
			if (typedChar=='-' && textFieldZ.getText().length() == 0) {
				textFieldZ.textboxKeyTyped(typedChar, keyCode);
			}
		}
		if (textFieldY.isFocused()) {
			if ((typedChar >= 48 && typedChar <= 57) || typedChar==8) {
				textFieldY.textboxKeyTyped(typedChar, keyCode);
			}
		}
		/*
		name = textFieldName.getText();
		try {
			markX = Integer.parseInt(textFieldX.getText());
			GuiTutorial.marks[0] = Integer.parseInt(textFieldX.getText());
		}
		catch (NumberFormatException nfe) {}
		try {
			markZ = Integer.parseInt(textFieldZ.getText());
			GuiTutorial.marks[1] = Integer.parseInt(textFieldZ.getText());
		}
		catch (NumberFormatException nfe) {}
		try {
			markY = Integer.parseInt(textFieldY.getText());
			GuiTutorial.marks[2] = Integer.parseInt(textFieldY.getText());
		}
		catch (NumberFormatException nfe) {}
		*/
	}
	
	public void mouseClicked(int i, int j, int k) throws IOException {
		super.mouseClicked(i,j,k);
		textFieldName.mouseClicked(i,j,k);
		textFieldX.mouseClicked(i,j,k);
		textFieldY.mouseClicked(i,j,k);
		textFieldZ.mouseClicked(i,j,k);
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
