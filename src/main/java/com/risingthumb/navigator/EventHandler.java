package com.risingthumb.navigator;

import org.lwjgl.input.Keyboard;

import com.risingthumb.navigator.gui.GuiMarkLocations;
import com.risingthumb.navigator.gui.GuiOptions;
import com.risingthumb.navigator.looter.Looter;
import com.risingthumb.navigator.proxy.ClientProxy;

import baritone.api.BaritoneAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid=NavigatorMod.MODID)
public class EventHandler {
	
	public static int currentTick = 0;
	
	private static boolean hasInitalisedShit = false;
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public static void onEvent(KeyInputEvent event) {
		if (ClientProxy.keyBindings[0].isPressed()) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiOptions());
		}
		if (ClientProxy.keyBindings[1].isPressed()) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiMarkLocations());
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			// This is to save config if you exit out of a menu the improper way
			NavigatorMod.saveConfig();
		}
	}
	
	@SubscribeEvent
	public static void onClientTick(final TickEvent.ClientTickEvent event) {
		// Initialise any event listeners for the Baritone
		if (!hasInitalisedShit) {
			hasInitalisedShit = true;
			Looter looter = new Looter();
			BaritoneAPI.getProvider().getPrimaryBaritone().getGameEventHandler().registerEventListener(looter);
			
			// These are config settings. We set them once. Do not change these unless it's been discussed and has been reasoned as a good change
			// Alternatively, we could change it to be toggleable in a seperate GUI
			// There's more if you check the BaritoneAPI documentation
			
			BaritoneAPI.getSettings().allowSprint.value = false;
			BaritoneAPI.getSettings().allowBreak.value = false;
			BaritoneAPI.getSettings().allowPlace.value = false;
			BaritoneAPI.getSettings().allowInventory.value = false;
			BaritoneAPI.getSettings().allowParkour.value = true;
			BaritoneAPI.getSettings().antiCheatCompatibility.value = true;
		}
		
		// This is a shitty timer for how long to wait before continuing looting after hitting a chest.
		if (GuiOptions.looting == true && currentTick == Looter.tickWaitTime) {
			Minecraft.getMinecraft().player.sendMessage(new TextComponentString("Continuing looting"));
			Looter.continueLooting();
		}
		
		currentTick++;
	}

}
