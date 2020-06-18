package xyz.risingthumb.navigator.util;

import java.util.ArrayList;
import java.util.List;

public class Route {
	
	private String name;
	private List<Marker> locations = new ArrayList<>();
	
	public Route(String name) {
		this.name = name;
	}
	public Route(String name, int stringify) {
		// This is an extra method. The stringify parameter isn't used
		// This is for converting the string data in the config into object information
		String[] parts = name.split(";");
		this.name = parts[0];
		for (int i=1; i<parts.length; i++) {
			String coordPart = parts[i];
			if (coordPart.length() > 0) {
				String[] coords = coordPart.split(",");
				int x = Integer.valueOf(coords[0]);
				int y = Integer.valueOf(coords[1]);
				int z = Integer.valueOf(coords[2]);
				this.addLocation(x, y, z);
			}
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public void addLocation(int x, int y, int z) {
		locations.add(new Marker(x, y, z));
	}
	
	public String stringify() {
		StringBuilder sb = new StringBuilder(this.name);
		for (Marker m: locations) {
			sb.append(";");
			sb.append(m.getX());
			sb.append(",");
			sb.append(m.getY());
			sb.append(",");
			sb.append(m.getZ());
		}
		return sb.toString();
	}
	public List<Marker> getLocations(){
		return this.locations;
	}
	

}
