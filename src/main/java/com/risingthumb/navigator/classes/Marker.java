package com.risingthumb.navigator.classes;

import java.awt.Point;

public class Marker
{
	private String name;
	private int x;
	private int y;
	private int z;

	public Point.Double screenPos = new Point.Double(0, 0);

	public Marker(String name,int x, int y, int z)
	{
		this.name = name;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public String getString()
	{
		return name;
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

	// arraylist.contains was producing unexpected results in some situations
	// rather than figure out why i'll just control how two markers are compared
	@Override
	public boolean equals(final Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o instanceof Marker)
		{
			Marker m = (Marker) o;
			return (this.x == m.x) && (this.y == m.y) && (this.z == m.z);
		}
		return false;
	}
}