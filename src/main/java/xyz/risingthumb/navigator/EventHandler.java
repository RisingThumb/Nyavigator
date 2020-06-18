package xyz.risingthumb.navigator;

import baritone.api.BaritoneAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.realms.RealmsBridge;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xyz.risingthumb.navigator.gui.GuiIngameMenuFix;
import xyz.risingthumb.navigator.gui.GuiNavigatorScreen;
import xyz.risingthumb.navigator.navigation.NavigationEvents;
import xyz.risingthumb.navigator.proxy.ClientProxy;

@EventBusSubscriber(modid=NavigatorMod.MODID)
public class EventHandler {
	
	private static boolean hasInitialised = false;
	public static boolean saveAndQuit = false;
	public static int ticksToWait = 0;
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public static void onEvent(KeyInputEvent event) {
		if (ClientProxy.keyBindings[0].isPressed())
			Minecraft.getMinecraft().displayGuiScreen(new GuiNavigatorScreen());
	}
	
	private static void saveAndQuit() {
		Minecraft mc = Minecraft.getMinecraft();
		boolean flag = mc.isIntegratedServerRunning();
        boolean flag1 = mc.isConnectedToRealms();
        mc.world.sendQuittingDisconnectingPacket();
        mc.loadWorld((WorldClient)null);
        if (flag)
            mc.displayGuiScreen(new GuiMainMenu());
        else if (flag1) {
            RealmsBridge realmsbridge = new RealmsBridge();
            realmsbridge.switchToRealms(new GuiMainMenu());
        }
        else
            mc.displayGuiScreen(new GuiMultiplayer(new GuiMainMenu()));
	}
	
	
	@SubscribeEvent
	public static void onClientTick(final TickEvent.ClientTickEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.currentScreen != null && mc.currentScreen.getClass().equals(GuiIngameMenu.class))
            mc.displayGuiScreen(new GuiIngameMenuFix());
		if (saveAndQuit)
			ticksToWait += 1;
		if (ticksToWait >= 5) {
			ticksToWait = 0;
			saveAndQuit = false;
			saveAndQuit();
		}
		if (!hasInitialised) {
			hasInitialised = true;
			//BaritoneAPI.getProvider().getPrimaryBaritone().getGameEventHandler().registerEventListener(looter);
			BaritoneAPI.getProvider().getPrimaryBaritone().getGameEventHandler().registerEventListener(new NavigationEvents());
			
			BaritoneAPI.getSettings().allowSprint.value = false;
			BaritoneAPI.getSettings().allowBreak.value = false;
			BaritoneAPI.getSettings().allowPlace.value = false;
			BaritoneAPI.getSettings().allowInventory.value = false;
			BaritoneAPI.getSettings().allowParkour.value = true;
			BaritoneAPI.getSettings().antiCheatCompatibility.value = true;
			BaritoneAPI.getSettings().chatControl.value = false;
			BaritoneAPI.getSettings().chatDebug.value = false;
		}
	}

}
