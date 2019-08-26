package com.risingthumb.navigator;

import org.apache.logging.log4j.Logger;

import com.risingthumb.navigator.classes.Marker;
import com.risingthumb.navigator.gui.GuiTutorial;
import com.risingthumb.navigator.proxy.IProxy;

import org.apache.logging.log4j.LogManager;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=NavigatorMod.MODID, name=NavigatorMod.NAME, version= NavigatorMod.VERSION, acceptedMinecraftVersions=NavigatorMod.MC_VERSION)
public class NavigatorMod {
	public static final String MODID="piclient";
	public static final String NAME="PiClient";
	public static final String VERSION="1.1";
	public static final String MC_VERSION="[1.12.2]";
	
	public static Configuration CONFIG;

	
	public static final Logger LOGGER = LogManager.getLogger(NavigatorMod.MODID);
	
	public static final String CLIENT="com.risingthumb.navigator.proxy.ClientProxy";
	public static final String SERVER="com.risingthumb.navigator.proxy.ServerProxy";
	
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
		LOGGER.info(NavigatorMod.NAME + "says hi!");
		proxy.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
		
	}
	
	public static void syncConfig() {
		try {
			// What mods are loaded
			CONFIG.load();
			Property isEnabled = CONFIG.get(Configuration.CATEGORY_GENERAL,
					"isEnabled", new boolean[] {
							false,//MoveToLoc
							false,//cancel
							});
			
			boolean[] values = isEnabled.getBooleanList();
			GuiTutorial.moveToLoc = values[0];
			GuiTutorial.cancel = values[1];
			
			// Loading markers
			Property markers = CONFIG.get(Configuration.CATEGORY_GENERAL,
					"markers", new String[] {
							"Aspermont:-2850:64:-2140"
					});
			String[] waypoints = markers.getStringList();
			GuiTutorial.waypoints.clear();
			for(String s:waypoints) {
				String[] parts = s.split(":");
				GuiTutorial.waypoints.add(new Marker(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3])));
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
			Property isEnabled = CONFIG.get(Configuration.CATEGORY_GENERAL,
					"isEnabled", new boolean[] {
							false,//MoveToLoc
							false,//cancel
							});
			
			boolean[] values = {
					false,//MoveToLoc
					false,//Cancel
					};
			
			isEnabled.set(values);
			
			Property markers = CONFIG.get(Configuration.CATEGORY_GENERAL,
					"markers", new String[] {
							"Aspermont:-2850:64:-2140"
					});
			
			String[] waypoints = new String[GuiTutorial.waypoints.size()];
			for (int i=0; i<waypoints.length; i++) {
				Marker m = GuiTutorial.waypoints.get(i);
				String input = String.join(":",m.getString(),""+m.getX(),""+m.getY(),""+m.getZ());
				waypoints[i]=input;
			}
			
			markers.set(waypoints);
			
		} catch(Exception e) {
			
		} finally {
			if (CONFIG.hasChanged()) {
				CONFIG.save();
			}
		}
	}

}
