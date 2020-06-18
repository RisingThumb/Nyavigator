package xyz.risingthumb.navigator.navigation;

import baritone.api.event.events.PathEvent;
import baritone.api.event.listener.AbstractGameEventListener;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentTranslation;
import xyz.risingthumb.navigator.NavigatorMod;

public class NavigationEvents implements AbstractGameEventListener {
	
	@Override
	public void onPathEvent(PathEvent event) {
		// Baritone's AT_GOAL event doesn't fire correctly.
		// When it reaches a goal, it fires the Cancelled event
		if (event == PathEvent.CANCELED && NavigatorMod.navigator.active) {
			Minecraft.getMinecraft().player.sendMessage(new TextComponentTranslation("chat.nyavigator.location"));
			NavigatorMod.navigator.goToNext();
		}
	}

}
