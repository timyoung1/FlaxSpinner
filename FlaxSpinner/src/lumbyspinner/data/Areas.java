package lumbyspinner.data;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;

public enum Areas {
	Spinwheelfloor(new Area(new Tile(3204, 3207, 1), new Tile(3204, 3230, 1),
			new Tile(3214, 3230, 1), new Tile(3214, 3207, 1))),

	Bankfloor(new Area(new Tile(3204, 3207, 2), new Tile(3204, 3230, 2),
			new Tile(3214, 3230, 2), new Tile(3214, 3207, 2))),

	Bankroof(new Area(new Tile(3206, 3222, 3), new Tile(3210, 3222, 3),
			new Tile(3210, 3215, 3), new Tile(3206, 3215, 3))),

	GroundFloor(new Area(new Tile(3204, 3207, 0), new Tile(3204, 3230, 0),
			new Tile(3214, 3230, 0), new Tile(3214, 3207, 0)));

	private final Area area;

	Areas(Area area) {
		this.area = area;
	}

	public Area getArea() {
		return area;
	}
}