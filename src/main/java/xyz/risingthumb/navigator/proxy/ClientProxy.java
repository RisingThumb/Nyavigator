package xyz.risingthumb.navigator.proxy;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xyz.risingthumb.navigator.NavigatorMod;

public class ClientProxy implements IProxy {
	
	public static KeyBinding[] keyBindings;
	
	public void preInit(FMLPreInitializationEvent event) {
	}
	
	public void init(FMLInitializationEvent event) {
		keyBindings = new KeyBinding[1];
		keyBindings[0] = new KeyBinding("key.nyavigator.desc", Keyboard.KEY_L, "key.nyavigator.category");
		
		for (int i = 0; i < keyBindings.length; i++) {
			ClientRegistry.registerKeyBinding(keyBindings[i]);
		}
		NavigatorMod.syncConfig();
		
	}

	public void postInit(FMLPostInitializationEvent event) {}

}
