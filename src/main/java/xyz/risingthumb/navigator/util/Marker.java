package xyz.risingthumb.navigator.util;

import java.awt.Point;

public class Marker
{
	private int x;
	private int y;
	private int z;

	public Point.Double screenPos = new Point.Double(0, 0);

	public Marker(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getZ() {
		return z;
	}
	public String getCoords() {
		return String.format("X:%6d Y:%6d Z:%6d",x, y, z);
	}
	public String getCoordsSaveFormat() {
		return String.format("%d,%d,%d", x, y, z);
	}
}