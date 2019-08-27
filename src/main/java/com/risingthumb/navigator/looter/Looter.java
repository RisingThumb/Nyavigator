package com.risingthumb.navigator.looter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.risingthumb.navigator.classes.Marker;
import com.risingthumb.navigator.gui.GuiOptions;
import com.risingthumb.navigator.scheduling.ScheduledEvent;
import com.risingthumb.navigator.scheduling.Scheduler;
import com.risingthumb.navigator.util.CameraUtil;
import com.risingthumb.navigator.util.DijkstraAlgorithm;

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
	public static boolean waitForNextEvent = true;
	
	public static void readAllChestLocations() {
		for (Marker m: chests) {
			Minecraft.getMinecraft().player.sendMessage(new TextComponentString("X:"+m.getX()+"Y:"+m.getY()+"Z"+m.getZ()));
		}
	}
	
	public static void fillNewChestLocations() {
		
		List<TileEntity> tileEntities = Minecraft.getMinecraft().world.loadedTileEntityList;
		ArrayList<Marker> chestUnqueued = new ArrayList<>();
		
		for(TileEntity te : tileEntities) {
			if(te instanceof TileEntityChest) {
				Marker mark = new Marker(te.getPos().getX(),te.getPos().getY(),te.getPos().getZ());
				chestUnqueued.add(mark);
			}
		}
		chests = DijkstraAlgorithm.calculateQueue(chestUnqueued);
	}
	
	public static void continueLooting() {
		Marker ncl = chests.peek();
		if (ncl == null) {
			fillNewChestLocations();
			ncl = chests.peek();
			if (ncl==null) {
				GuiOptions.looting=false;
			}
		}
		
		if(GuiOptions.looting) {
			Looter.waitForNextEvent = true;
			BaritoneAPI.getProvider().getPrimaryBaritone().getCustomGoalProcess().setGoalAndPath(new GoalBlock(ncl.getX(), ncl.getY()+1, ncl.getZ()));
		}
		
	}

	@Override
	public void onPathEvent(PathEvent event) {
		if (event == PathEvent.CANCELED) {
			if(GuiOptions.looting && waitForNextEvent) {
				waitForNextEvent = false;
				Marker chestLoc = chests.remove();
				Minecraft.getMinecraft().player.sendMessage(new TextComponentString(""+event));
				// This is to stop annoying anticheat as you jump onto it and remove the block.
				new Scheduler(10, new ScheduledEvent() {
					@Override
					public void run() {
						Minecraft.getMinecraft().playerController.clickBlock(new BlockPos(chestLoc.getX(),chestLoc.getY(),chestLoc.getZ()), EnumFacing.UP);
						Minecraft.getMinecraft().player.swingArm(EnumHand.MAIN_HAND);
						CameraUtil.lookAtCoordinates(Minecraft.getMinecraft().player, chestLoc.getX(),chestLoc.getY(),chestLoc.getZ());
						//Minecraft.getMinecraft().player.rotationPitch=90f;
					}
				});
				
				new Scheduler(tickWaitTime, new ScheduledEvent() {
					@Override
					public void run() {
						Looter.waitForNextEvent = true;
						Minecraft.getMinecraft().player.sendMessage(new TextComponentString("[!] Continuing looting"));
						Looter.continueLooting();
					}
				});
				
			}
		}
	}
}
