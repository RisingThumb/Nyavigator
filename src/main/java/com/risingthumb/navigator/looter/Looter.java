package com.risingthumb.navigator.looter;

import java.util.LinkedList;
import java.util.List;

import com.risingthumb.navigator.EventHandler;
import com.risingthumb.navigator.classes.Marker;
import com.risingthumb.navigator.gui.GuiTutorial;
import com.risingthumb.navigator.timing.Timer;
import com.risingthumb.navigator.timing.TimerEvent;

import baritone.api.BaritoneAPI;
import baritone.api.event.events.PathEvent;
import baritone.api.event.listener.AbstractGameEventListener;
import baritone.api.pathing.goals.GoalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class Looter implements AbstractGameEventListener {
	
	public static LinkedList<Marker> chests = new LinkedList<>();
	public static boolean firstLoot = true;
	public static int tickWaitTime = 80;
	
	public static void readAllChestLocations() {
		for (Marker m: chests) {
			Minecraft.getMinecraft().player.sendMessage(new TextComponentString("X:"+m.getX()+"Y:"+m.getY()+"Z"+m.getZ()));
		}
	}
	
	private static void fillNewChestLocations() {
		List<TileEntity> tileEntities = Minecraft.getMinecraft().world.loadedTileEntityList;
		for(TileEntity te : tileEntities) {
			if(te instanceof TileEntityChest) {
				Marker mark = new Marker("",te.getPos().getX(),te.getPos().getY(),te.getPos().getZ());
				Looter.chests.add(mark);
			}
		}
	}
	
	public static void continueLooting() {
		Marker ncl = chests.peek();
		if (ncl == null) {
			fillNewChestLocations();
			Marker mark2 = chests.peek();
			if (mark2==null) {
				GuiTutorial.looting=false;
			}
		}
		else {
			BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalBlock(ncl.getX(), ncl.getY()+1, ncl.getZ()));
		}
		
	}

	@Override
	public void onPathEvent(PathEvent event) {
		if (event == PathEvent.CANCELED) {
			if(GuiTutorial.looting && EventHandler.currentTick > tickWaitTime) {
				if (!firstLoot) {
					Marker chestLoc = chests.remove();
					Minecraft.getMinecraft().player.sendMessage(new TextComponentString(""+event));
					Minecraft.getMinecraft().playerController.clickBlock(new BlockPos(chestLoc.getX(),chestLoc.getY(),chestLoc.getZ()), EnumFacing.UP);
					Minecraft.getMinecraft().player.swingArm(EnumHand.MAIN_HAND);
					Minecraft.getMinecraft().player.rotationPitch=90f;
				}
				else {
					firstLoot = false;
				}
				EventHandler.currentTick=0;
				
			}
		}
	}
}
