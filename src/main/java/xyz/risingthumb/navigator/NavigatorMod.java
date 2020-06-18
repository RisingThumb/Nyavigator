package xyz.risingthumb.navigator;

import org.apache.logging.log4j.Logger;

import baritone.api.BaritoneAPI;

import org.apache.logging.log4j.LogManager;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import xyz.risingthumb.navigator.gui.ButtonFunctions;
import xyz.risingthumb.navigator.gui.GuiNavigatorScreen;
import xyz.risingthumb.navigator.navigation.NavigationEvents;
import xyz.risingthumb.navigator.navigation.NavigationHandling;
import xyz.risingthumb.navigator.proxy.IProxy;
import xyz.risingthumb.navigator.util.Route;

@Mod(modid=NavigatorMod.MODID, name=NavigatorMod.NAME, version= NavigatorMod.VERSION, acceptedMinecraftVersions=NavigatorMod.MC_VERSION)
public class NavigatorMod {
	public static NavigationHandling navigator;
	public static ButtonFunctions functions;
	
	public static final String MODID="thumbler";
	public static final String NAME="Nyavigator";
	public static final String VERSION="2.0";
	public static final String MC_VERSION="[1.12.2]";
	
	public static Configuration CONFIG;

	
	public static final Logger LOGGER = LogManager.getLogger(NavigatorMod.MODID);
	
	public static final String CLIENT="xyz.risingthumb.navigator.proxy.ClientProxy";
	public static final String SERVER="xyz.risingthumb.navigator.proxy.ServerProxy";
	
	@SidedProxy(clientSide=NavigatorMod.CLIENT, serverSide=NavigatorMod.SERVER)
	public static IProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
		CONFIG = new Configuration(event.getSuggestedConfigurationFile());
		syncConfig();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
		settings();
	}
	
	private void settings() {
		functions = new ButtonFunctions();
		navigator = new NavigationHandling();
	}
	
	public static void syncConfig() {
		try {
			CONFIG.load();
			// Loading markers
			Property routes = CONFIG.get(Configuration.CATEGORY_GENERAL,
					"routes", new String[] {
							"Aspermont;-2850,64,-2140"
					});
			String[] elements = routes.getStringList();
			GuiNavigatorScreen.routes.clear();
			for (String s: elements) {
				GuiNavigatorScreen.routes.add(new Route(s, 1));
			}
			
		} catch(Exception e) {
			
		} finally {
			if (CONFIG.hasChanged()) {
				CONFIG.save();
			}
		}
	}
	
	public static void saveConfig() {
		try {
			CONFIG.load();
			Property routes = CONFIG.get(Configuration.CATEGORY_GENERAL,
					"routes", new String[] {});
			String[] newRoutes = new String[GuiNavigatorScreen.routes.size()];
			for (int i=0; i<newRoutes.length; i++) {
				newRoutes[i] = GuiNavigatorScreen.routes.get(i).stringify();
			}
			routes.set(newRoutes);
			
		} catch(Exception e) {
			
		} finally {
			if (CONFIG.hasChanged()) {
				CONFIG.save();
			}
		}
	}

}
