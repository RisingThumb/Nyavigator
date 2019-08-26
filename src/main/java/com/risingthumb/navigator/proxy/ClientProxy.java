package com.risingthumb.navigator.proxy;

import org.lwjgl.input.Keyboard;

import com.risingthumb.navigator.NavigatorMod;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy implements IProxy {
	
	public static KeyBinding[] keyBindings;
	
	public void preInit(FMLPreInitializationEvent event) {
	}
	
	public void init(FMLInitializationEvent event) {
		
		keyBindings = new KeyBinding[2];
		keyBindings[0] = new KeyBinding("key.hud.desc", Keyboard.KEY_H, "key.magicbeans.category");
		keyBindings[1] = new KeyBinding("key.hud.desc2", Keyboard.KEY_L, "key.magicbeans.category");
		
		for (int i = 0; i < keyBindings.length; i++) {
			ClientRegistry.registerKeyBinding(keyBindings[i]);
		}
		NavigatorMod.syncConfig();
		
	}
	
	public void postInit(FMLPostInitializationEvent event) {
	}

}
