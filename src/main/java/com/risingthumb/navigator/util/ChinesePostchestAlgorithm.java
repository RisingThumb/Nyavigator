package com.risingthumb.navigator.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.risingthumb.navigator.classes.Marker;

import net.minecraft.client.Minecraft;

public class ChinesePostchestAlgorithm {
	
	private static double distance2Points(double xPos, double yPos, double zPos, double xPos2, double yPos2, double zPos2) {
		return Math.sqrt(Math.pow(xPos-xPos2,2)+Math.pow(yPos-yPos2,2)+Math.pow(zPos-zPos2,2));
	}
	
	private static Marker calculateNextNode(double x, double y, double z,ArrayList<Marker> input, LinkedList<Marker> output) {
		double distance = Double.MAX_VALUE;
		Marker currentMark = new Marker("hi",0, 0, 0);
		for (Marker m: input) {
			if (distance > distance2Points(x,y,z,m.getX(),m.getY(),m.getZ())) {
				distance = distance2Points(x,y,z,m.getX(),m.getY(),m.getZ());
				currentMark = m;
			}
		}
		return currentMark;
		
	}
	
	public static LinkedList<Marker> calculateQueue(ArrayList<Marker> input) {
		LinkedList<Marker> output = new LinkedList<>();
		Marker currentMark;
		double xPos = Minecraft.getMinecraft().player.posX;
		double yPos = Minecraft.getMinecraft().player.posY;
		double zPos = Minecraft.getMinecraft().player.posZ;
		
		while(!input.isEmpty()) {
			currentMark = calculateNextNode(xPos,yPos,zPos,input, output);
			xPos = currentMark.getX();
			yPos = currentMark.getY();
			zPos = currentMark.getZ();
			input.remove(currentMark);
			output.add(currentMark);
		}
		return output;
	}

}
