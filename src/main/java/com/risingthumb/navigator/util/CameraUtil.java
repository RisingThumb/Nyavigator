package com.risingthumb.navigator.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public class CameraUtil {
	
	public static void lookAtCoordinates(EntityPlayer player, Vec3d target) {
		// This centres it on the block.
		// As a result it's probably not the most useful for entities
		target.add(new Vec3d(0.5,0.5,0.5));

		Vec3d w = target.subtract(player.getPositionEyes(0)).normalize();

		float yaw = (float) Math.toDegrees(Math.atan2(w.x, w.z)) * -1;
		float pitch = (float) -Math.toDegrees(Math.atan2(w.y, Math.sqrt(w.z * w.z + w.x * w.x)));

		player.setPositionAndRotation(player.posX, player.posY, player.posZ, yaw, pitch);
	}

}
