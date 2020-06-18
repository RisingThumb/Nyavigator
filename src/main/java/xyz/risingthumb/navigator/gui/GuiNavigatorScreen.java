package xyz.risingthumb.navigator.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import xyz.risingthumb.navigator.NavigatorMod;
import xyz.risingthumb.navigator.util.Marker;
import xyz.risingthumb.navigator.util.Route;

public class GuiNavigatorScreen extends GuiScreen {
	
	public static List<Route> routes = new ArrayList<>();
	
	ResourceLocation textureBack = new ResourceLocation(NavigatorMod.MODID, "textures/gui/navigator.png");
	private int lMargin=10;
	private int boxHeight = 222;
	private int boxWidth = 176;
	private int topLeftX;
	private int topLeftY;
	private int boxX = 0;
	private int boxY = 0;
	private int splitY = 0;
	private int splitWidth = 8;
	private int splitHeight = 222;
	//Nya variables
	private int currentTick = 0;
	private int currentFrame = 0;
	private boolean frameSwap = false;
	int frameWidth = 28;
	int frameHeight = 33;
	int frameX;
	int frameY = 256 - frameHeight;
	int frameXCoord;
	int frameYCoord;
	private int selectedID = 0;
	private int selectedIDLoc = 0;
	private int centerBoxLeftX;
	private int centerBoxLeftY;
	private int centerBoxWidth = 130;
	private int centerBoxHeight = 182;
	// Scroll box
	private int scrollBoxWidth = 6;
	private int scrollBoxHeight = 180;
	private int scrollButtonX;
	private int scrollButtonY;
	private int scrollBoxX;
	private int scrollBoxY;
	
	private int widthOneUnit = (boxWidth-splitWidth*3)/3;
	private int topLeftSplit;
	
	String[] nyaHoverText = {TextFormatting.BOLD+"NYA!", 
			TextFormatting.GOLD+""+TextFormatting.BOLD+"Hoi Meowster, the left box is for your routes!",
			"",
			TextFormatting.AQUA+"You can add a route under any name.",
			TextFormatting.AQUA+"Once you select a route, information appears to the right!",
			TextFormatting.AQUA+"You can add locations to this route! Nya!",
			"",
			TextFormatting.AQUA+"Set sail to lands galore Meowster! NYA!"};
	
	final int maxInBox = 15;
	private int currentRouteSelection = 0;
	private int currentLocSelection = 0;
	
	private GuiTextField textFieldName;
	final int IDFieldName = 0;
	private GuiTextFieldNumber textFieldX;
	final int IDFieldX = 1;
	private GuiTextFieldNumber textFieldY;
	final int IDFieldY = 2;
	private GuiTextFieldNumber textFieldZ;
	final int IDFieldZ = 3;
	
	final int IDAddRoute = 4;
	private GuiButtonSmall buttonAddLocation;
	final int IDAddLocation = 5;
	private GuiButtonSmall buttonDelRoute;
	final int IDDelRoute = 6;
	private GuiButtonSmall buttonDelLocation;
	final int IDDelLocation = 7;
	
	final int IDPlay = 10;
	final int IDPause = 11;
	final int IDStop = 12;
	
	final int IDUpRoute = 20;
	final int IDDownRoute = 21;
	private GuiButtonSmall buttonScrollUpLocation;
	final int IDUpLocation = 22;
	private GuiButtonSmall buttonScrollDownLocation;
	final int IDDownLocation = 23;
	
	private List<GuiButton> staticButtons = new ArrayList<>();
	
	// Range 0 to 100 for utility buttons
	// Everything below 0 is used for generated location Buttons
	// Everything above 100 is used for generated Route Buttons
	final int unusedIDStart = 100;
	
	public void drawNya(int topLeftX, int topLeftY, int mouseX, int mouseY, int posX, int posY) {
		posY-=frameHeight;
		frameX = 256 - frameWidth*(1+currentFrame);
		frameYCoord = topLeftY-frameHeight+2+currentFrame*2; // This coordinate gets adjusted
		Minecraft.getMinecraft().renderEngine.bindTexture(textureBack);
		currentTick += 1;
		if (currentTick % 200 == 0) {
			frameSwap = true;
			currentTick = 0;
		}
		if (frameSwap) {
			if (currentFrame == 0)
				currentFrame = 1;
			else
				currentFrame = 0;
			frameSwap = false;
		}
		drawTexturedModalRect(posX, posY, frameX, frameY, frameWidth, frameHeight);
		if 		(mouseX <= posX+frameWidth  && mouseX >= posX &&
				 mouseY <= posY+frameHeight && mouseY >= posY)
			drawHoveringText(Arrays.asList(nyaHoverText), mouseX, mouseY);
	}
	
	public void regenerateButtons() {
		Minecraft.getMinecraft().renderEngine.bindTexture(textureBack);
		buttonList.clear();
		buttonList.addAll(staticButtons);
		// Generate buttons from routes arraylist
		int TLY = topLeftY+28;
		int TLX = topLeftX+16;
		for(int i=0; (i<routes.size()) && (i<maxInBox); i++) {
			String text = routes.get(i+currentRouteSelection).getName();
			GuiButton buttonToAdd = new GuiButtonRoute(textureBack, unusedIDStart+i+currentRouteSelection, TLX, TLY+i*12, 128, 12, 32, 244, text);
			buttonList.add(buttonToAdd);
			if (unusedIDStart+i+currentRouteSelection == selectedID)
				((GuiButtonRoute) buttonToAdd).setSelected();
		}
		
		//DelRoute button and location addition
		if (selectedID>=unusedIDStart){
			// Scroll buttons
			buttonList.add(buttonScrollUpLocation);
			buttonList.add(buttonScrollDownLocation);
			if ((selectedID-unusedIDStart-currentRouteSelection)*12 < maxInBox*12 && (selectedID-unusedIDStart-currentRouteSelection)*12 >=0) {
				buttonDelRoute = new GuiButtonSmall(textureBack, IDDelRoute, topLeftX+boxWidth-splitWidth-10, TLY+(selectedID-unusedIDStart-currentRouteSelection)*12, 12, 12, 0, 244, TextFormatting.RED+"-");
				buttonList.add(buttonDelRoute);
			}
			buttonList.add(buttonAddLocation);
			// We also need to display all the positions
			TLX = topLeftX+boxWidth+16;
			List<Marker> locations = routes.get(selectedID-unusedIDStart).getLocations();
			
			for(int i=0; (i<locations.size()) && (i<maxInBox); i++) {
				String text = locations.get(i+currentLocSelection).getCoords();
				GuiButton buttonToAdd = new GuiButtonRoute(textureBack, (-1-i-currentLocSelection), TLX, TLY+i*12, 128, 12, 32, 244, text);
				buttonList.add(buttonToAdd);
				if (-1-i-currentLocSelection == selectedIDLoc)
					((GuiButtonRoute) buttonToAdd).setSelected();
			}
		}
		if (selectedIDLoc<0 && (-(selectedIDLoc+currentLocSelection+1))*12 < maxInBox*12 && (-(selectedIDLoc+currentLocSelection+1))*12 >=0) {
			buttonDelLocation = new GuiButtonSmall(textureBack, IDDelLocation, topLeftX+boxWidth*2-splitWidth-10, TLY+(-(selectedIDLoc+currentLocSelection+1))*12, 12, 12, 0, 244, TextFormatting.RED+"-");
			buttonList.add(buttonDelLocation);
		}
	}
	
	@Override
	public void actionPerformed(GuiButton button) throws IOException {
		
		switch(button.id) {
		case IDAddRoute:
			NavigatorMod.functions.addRoute(textFieldName);
			break;
		case IDAddLocation:
			NavigatorMod.functions.addLocation(textFieldX, textFieldY, textFieldZ, selectedID-unusedIDStart);
			break;
		case IDDelRoute:
			NavigatorMod.functions.deleteRoute(selectedID-unusedIDStart, this);
			break;
		case IDDelLocation:
			NavigatorMod.functions.deleteLocation(selectedID-unusedIDStart, -(selectedIDLoc+1+currentLocSelection), this);
			break;
		case IDPlay:
			NavigatorMod.functions.play(selectedID<unusedIDStart, selectedID-unusedIDStart);
			break;
		case IDPause:
			NavigatorMod.functions.pause();
			break;
		case IDStop:
			NavigatorMod.functions.stop();
			break;
		case IDUpRoute:
			NavigatorMod.functions.scrollUpRoute(currentRouteSelection, this);
			break;
		case IDDownRoute:
			if (currentRouteSelection < routes.size()-maxInBox)
				currentRouteSelection+=1;
			break;
		case IDUpLocation:
			NavigatorMod.functions.scrollUpLocation(currentLocSelection, this);
			break;
		case IDDownLocation:
			if (currentLocSelection < routes.get(selectedID-unusedIDStart).getLocations().size()-maxInBox)
				currentLocSelection+=1;
			break;
		}
		// Properly set selectedIDs
		if (button.id<0)
			selectedIDLoc = button.id;
		if (button.id >=unusedIDStart) {
			if (selectedID != button.id)
				currentLocSelection=0; // Need to reset scrollbar
			selectedID = button.id;
		}
		
		regenerateButtons();
		super.actionPerformed(button);
	}
	
	private void prettyDrawRects(int x, int y, int width, int height) {
		drawRect(x-1, y-1, x+width+1, y+height+1, -6250336); //White
		drawRect(x, y, x+width, y+height, -16777216); //black
	}
	
	private void textFieldInit(GuiTextField textField, String text, int length) {
		textField.setFocused(false);
		textField.setMaxStringLength(length);
		textField.setText(text);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		drawDefaultBackground();
		
		Minecraft.getMinecraft().renderEngine.bindTexture(textureBack);
		// Draw texture GUI
		drawTexturedModalRect(topLeftX,topLeftY, boxX, boxY, boxWidth, boxHeight);
		drawTexturedModalRect(topLeftX+boxWidth, topLeftY, boxX, boxY, boxWidth, boxHeight);
		drawTexturedModalRect(topLeftX+boxWidth-splitWidth/2, topLeftY, boxWidth/2, splitY, splitWidth, splitHeight);
		// Draw boxes
		prettyDrawRects(centerBoxLeftX, centerBoxLeftY, centerBoxWidth, centerBoxHeight);
		prettyDrawRects(centerBoxLeftX+boxWidth, centerBoxLeftY, centerBoxWidth, centerBoxHeight);
		// Draw Scroll Boxes
		prettyDrawRects(scrollBoxX, scrollBoxY, scrollBoxWidth, scrollBoxHeight);
		prettyDrawRects(scrollBoxX+boxWidth, scrollBoxY, scrollBoxWidth, scrollBoxHeight);
		// Draw GUITextField
		textFieldName.drawTextBox();
		textFieldX.drawTextBox();
		textFieldY.drawTextBox();
		textFieldZ.drawTextBox();
		drawString(Minecraft.getMinecraft().fontRenderer,"Name:",topLeftX+splitWidth,topLeftY+splitWidth,Integer.parseInt("FFAA00", 16));
		drawString(Minecraft.getMinecraft().fontRenderer,"X:",topLeftSplit,topLeftY+splitWidth,Integer.parseInt("FFAA00", 16));
		drawString(Minecraft.getMinecraft().fontRenderer,"Y:",topLeftSplit+widthOneUnit,topLeftY+splitWidth,Integer.parseInt("FFAA00", 16));
		drawString(Minecraft.getMinecraft().fontRenderer,"Z:",topLeftSplit+widthOneUnit*2,topLeftY+splitWidth,Integer.parseInt("FFAA00", 16));
		super.drawScreen(mouseX, mouseY, partialTicks);
		
		//Draws nya cat Drawn after screen because tooltip
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		drawString(Minecraft.getMinecraft().fontRenderer,"",0,0,Integer.parseInt("FFFFFF", 16));
		drawNya(topLeftX, topLeftY, mouseX, mouseY, topLeftX+boxWidth*2, topLeftY+boxHeight);
	}
	
	@Override
	public void initGui() {
		// Width is only known once initalised
		topLeftX = (this.width/2)-boxWidth;
		topLeftY = (this.height/2)-boxHeight/2;
		scrollButtonX = topLeftX+centerBoxWidth+15;
		scrollButtonY = topLeftY+26;
		frameXCoord = topLeftX+boxWidth*2-frameWidth-splitWidth;
		centerBoxLeftX = topLeftX+15;
		centerBoxLeftY = topLeftY+27;
		scrollBoxX = centerBoxLeftX+centerBoxWidth+3;
		scrollBoxY = centerBoxLeftY+1;
		topLeftSplit = topLeftX+boxWidth+splitWidth;
		
		textFieldName = new GuiTextField(IDFieldName, Minecraft.getMinecraft().fontRenderer,topLeftX+splitWidth+30,topLeftY+splitWidth, boxWidth-26-splitWidth*4, 10);
		textFieldInit(textFieldName, "Name", 20);
		textFieldX = new GuiTextFieldNumber(IDFieldX, Minecraft.getMinecraft().fontRenderer,topLeftSplit+lMargin,topLeftY+splitWidth, widthOneUnit-lMargin-2, 10);
		textFieldInit(textFieldX, "0", 20);
		textFieldY = new GuiTextFieldNumber(IDFieldY, Minecraft.getMinecraft().fontRenderer,topLeftSplit+lMargin+widthOneUnit,topLeftY+splitWidth, widthOneUnit-lMargin-2, 10);
		textFieldInit(textFieldY, "0", 20);
		textFieldZ = new GuiTextFieldNumber(IDFieldZ, Minecraft.getMinecraft().fontRenderer,topLeftSplit+lMargin+widthOneUnit*2,topLeftY+splitWidth, widthOneUnit-lMargin-2, 10);
		textFieldInit(textFieldZ, "0", 20);

		staticButtons.add(new GuiButtonSmall(textureBack, IDAddRoute, topLeftX+boxWidth-splitWidth-10, topLeftY+splitWidth-1, 12, 12, 0, 244, TextFormatting.GREEN+"+"));
		staticButtons.add(new GuiButtonSmall(textureBack, IDPlay, topLeftX+boxWidth*2-3, topLeftY+splitWidth-1, 16, 12, 160, 244, TextFormatting.GREEN+">"));
		staticButtons.add(new GuiButtonSmall(textureBack, IDPause, topLeftX+boxWidth*2-3, topLeftY+splitWidth+16-1, 16, 12, 160, 244, TextFormatting.GOLD+"|||"));
		staticButtons.add(new GuiButtonSmall(textureBack, IDStop, topLeftX+boxWidth*2-3, topLeftY+splitWidth+32-1, 16, 12, 160, 244, TextFormatting.RED+"#"));
		staticButtons.add(new GuiButtonSmall(textureBack, IDUpRoute, scrollButtonX, scrollButtonY, 12, 12, 0, 244, TextFormatting.GOLD+"+"));
		staticButtons.add(new GuiButtonSmall(textureBack, IDDownRoute, scrollButtonX, scrollButtonY+scrollBoxHeight-8, 12, 12, 0, 244, TextFormatting.GOLD+"-"));
		buttonScrollDownLocation = new GuiButtonSmall(textureBack, IDDownLocation, scrollButtonX+boxWidth, scrollButtonY+scrollBoxHeight-8, 12, 12, 0, 244, TextFormatting.GOLD+"-");
		buttonScrollUpLocation = new GuiButtonSmall(textureBack, IDUpLocation, scrollButtonX+boxWidth, scrollButtonY, 12, 12, 0, 244, TextFormatting.GOLD+"+");
		buttonAddLocation = new GuiButtonSmall(textureBack, IDAddLocation, topLeftX+boxWidth*2-splitWidth-10, topLeftY+splitWidth-1, 12, 12, 0, 244, TextFormatting.GREEN+"+");
		regenerateButtons();
		
		super.initGui();
	}

	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException{
		super.keyTyped(typedChar, keyCode);
		if (textFieldName.isFocused() && typedChar != 59)
			textFieldName.textboxKeyTyped(typedChar, keyCode);
		textFieldX.keyTyped(typedChar, keyCode);
		textFieldY.keyTyped(typedChar, keyCode);
		textFieldZ.keyTyped(typedChar, keyCode);
	}
	
	public void mouseClicked(int i, int j, int k) throws IOException {
		super.mouseClicked(i,j,k);
		textFieldName.mouseClicked(i,j,k);
		textFieldX.mouseClicked(i,j,k);
		textFieldY.mouseClicked(i,j,k);
		textFieldZ.mouseClicked(i,j,k);
	}
	
	// Methods of the stupid ass variety
	@Override
	public boolean doesGuiPauseGame() { return false; }
	public void setCurrentRouteSelection(int i) { this.currentRouteSelection=i; }
	public void setCurrentLocationSelection(int i) { this.currentLocSelection=i; }
	public int getCurrentLocationSelection() { return this.currentLocSelection; }
	public int getCurrentRouteSelection() { return this.currentRouteSelection; }
	public void setSelectedID(int id) { this.selectedID = id; }
	
	public void setSelectedIDLoc(int id) { this.selectedIDLoc = id; }
}
