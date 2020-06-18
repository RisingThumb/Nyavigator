package xyz.risingthumb.navigator.navigation;

import java.util.ArrayList;
import java.util.List;

import baritone.api.BaritoneAPI;
import baritone.api.event.listener.AbstractGameEventListener;
import baritone.api.pathing.goals.GoalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentTranslation;
import xyz.risingthumb.navigator.util.Marker;

public class NavigationHandling implements AbstractGameEventListener {
	
	List<Marker> locations = new ArrayList<>();
	int currentIndex = -1;
	boolean active = false;
	
	public void play(List<Marker> locations) {
		active = true;
		// If stuff isn't set, startup
		if (currentIndex == -1) {
			startGoing(locations);
			return;
		}
		goToNext();
	}
	
	public void startGoing(List<Marker> locations) {
		//BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();
		this.locations = locations;
		currentIndex = 0;
		if (locations.size() <= 0)
			return;
		goToNext();
		
	}
	
	public void goToNext() {
		active = true;
		if (locations.size() == 0) {
			active = false;
			currentIndex = -1;
			Minecraft.getMinecraft().player.sendMessage(new TextComponentTranslation("chat.nyavigator.setlocations"));
			return;
		}
		if (currentIndex == locations.size()) {
			active = false;
			currentIndex = -1;
			Minecraft.getMinecraft().player.sendMessage(new TextComponentTranslation("chat.nyavigator.arrived"));
			return;
		}
		Marker m = locations.get(currentIndex);
		BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalBlock(m.getX(), m.getY(), m.getZ()));
		currentIndex += 1;
	}
	
	public void pause() {
		if (active) {
			active = false;
			currentIndex -= 1;
			BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();
		}
	}
	
	public void stop() {
		active = false;
		BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();
		currentIndex = -1;
	}

}
