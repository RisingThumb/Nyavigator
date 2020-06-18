package xyz.risingthumb.navigator.gui;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.text.TextComponentTranslation;
import xyz.risingthumb.navigator.NavigatorMod;
import xyz.risingthumb.navigator.util.Marker;
import xyz.risingthumb.navigator.util.Route;

// This is a utility class to decouple GuiNavigatorScreen from everything else

public class ButtonFunctions {
	
	public void addLocation(GuiTextFieldNumber textFieldX, GuiTextFieldNumber textFieldY, GuiTextFieldNumber textFieldZ, int index) {
		int xray = Integer.valueOf(textFieldX.getText());
		int yankee = Integer.valueOf(textFieldY.getText());
		int zulu = Integer.valueOf(textFieldZ.getText());
		GuiNavigatorScreen.routes.get(index).addLocation(xray, yankee, zulu);
		NavigatorMod.saveConfig();
	}
	
	public void addRoute(GuiTextField textFieldName) {
		GuiNavigatorScreen.routes.add(new Route(textFieldName.getText()));
		textFieldName.setText("");
		NavigatorMod.saveConfig();
	}
	
	public void deleteLocation(int index, int id, GuiNavigatorScreen ref) {
		List<Marker> locations = GuiNavigatorScreen.routes.get(index).getLocations();
		locations.remove(id);
		ref.setSelectedIDLoc(0);
		NavigatorMod.saveConfig();
		NavigatorMod.functions.scrollUpLocation(ref.getCurrentLocationSelection(), ref);
	}
	
	public void deleteRoute(int index, GuiNavigatorScreen ref) {
		GuiNavigatorScreen.routes.remove(index);
		ref.setSelectedID(0);
		NavigatorMod.saveConfig();
		NavigatorMod.functions.scrollUpRoute(ref.getCurrentRouteSelection(), ref);
	}
	
	public void play(boolean b, int index) {
		if(b) {
			Minecraft.getMinecraft().player.sendMessage(new TextComponentTranslation("chat.nyavigator.selectroute"));
			return;
		}
		Minecraft.getMinecraft().player.sendMessage(new TextComponentTranslation("chat.nyavigator.play"));
		NavigatorMod.navigator.play(GuiNavigatorScreen.routes.get(index).getLocations());
		
	}
	
	public void stop() {
		Minecraft.getMinecraft().player.sendMessage(new TextComponentTranslation("chat.nyavigator.stop"));
		NavigatorMod.navigator.stop();
	}
	
	public void pause() {
		Minecraft.getMinecraft().player.sendMessage(new TextComponentTranslation("chat.nyavigator.pause"));
		NavigatorMod.navigator.pause();
	}

	public void scrollUpRoute(int currentRouteSelection, GuiNavigatorScreen ref) {
		if (currentRouteSelection > 0)
			ref.setCurrentRouteSelection(currentRouteSelection-1);
	}
	
	public void scrollUpLocation(int currentLocationSelection, GuiNavigatorScreen ref) {
		if (currentLocationSelection > 0)
			ref.setCurrentLocationSelection(currentLocationSelection-1);
	}
	
}
