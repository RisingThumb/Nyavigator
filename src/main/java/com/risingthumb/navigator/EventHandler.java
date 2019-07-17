package com.risingthumb.navigator;

import org.lwjgl.input.Keyboard;

import com.risingthumb.navigator.gui.GuiMarkLocations;
import com.risingthumb.navigator.gui.GuiTutorial;
import com.risingthumb.navigator.proxy.ClientProxy;

import baritone.api.BaritoneAPI;
import baritone.api.pathing.goals.GoalBlock;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid=NavigatorMod.MODID)
public class EventHandler {
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public static void onEvent(KeyInputEvent event) {
		if (ClientProxy.keyBindings[0].isPressed()) {
			//Minecraft.getMinecraft().player.sendMessage(new TextComponentString("hi"));
			Minecraft.getMinecraft().displayGuiScreen(new GuiTutorial());
		}
		if (ClientProxy.keyBindings[1].isPressed()) {
			//Minecraft.getMinecraft().player.sendMessage(new TextComponentString("hi"));
			Minecraft.getMinecraft().displayGuiScreen(new GuiMarkLocations());
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			NavigatorMod.saveConfig();
		}
	}
	
	@SubscribeEvent
	public static void onClientTick(final TickEvent.ClientTickEvent event) {
		if (GuiTutorial.moveToLoc) {
			int x = GuiTutorial.marks[0];
			int y = GuiTutorial.marks[2];
			int z = GuiTutorial.marks[1];
			
			BaritoneAPI.getSettings().allowSprint.value = false;
			BaritoneAPI.getSettings().allowBreak.value = false;
			BaritoneAPI.getSettings().allowPlace.value = false;
			BaritoneAPI.getSettings().allowInventory.value = false;
			BaritoneAPI.getSettings().allowParkour.value = true;
			BaritoneAPI.getSettings().antiCheatCompatibility.value = true;
			
			BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalBlock(x, y, z));
			
			GuiTutorial.moveToLoc = false;
		}
		if (GuiTutorial.cancel) {
			BaritoneAPI.getProvider().getPrimaryBaritone().getPathingBehavior().cancelEverything();
			GuiTutorial.cancel = false;
		}
	}

}
