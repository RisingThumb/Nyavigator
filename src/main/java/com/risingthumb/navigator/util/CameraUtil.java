package com.risingthumb.navigator.util;

import net.minecraft.entity.player.EntityPlayer;

public class CameraUtil {
	
	public static void lookAtCoordinates(EntityPlayer player, double lookX, double lookY, double lookZ) {
		double atX = player.posX;
		double atY = player.posY;
		double atZ = player.posZ;
		// Pitch is vertical angle
		// Yaw is horizontal angle
		
		// To be implemented. Requires trigonometry. Math.atan may be useful
		
		player.rotationYaw=0f; // Range is from +180 to -180
		player.rotationPitch=0f; // Range is from +90 to -90
	}

}
