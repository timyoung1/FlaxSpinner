package lumbyspinner.data;

import org.powerbot.script.wrappers.Area;
import org.powerbot.script.wrappers.Tile;

public enum Areas {
	Spinwheelfloor(new Area(new Tile(3204, 3207, 1), new Tile(3204, 3230, 1),
			new Tile(3214, 3230, 1), new Tile(3214, 3207, 1))),

	Bankfloor(new Area(new Tile(3204, 3207, 2), new Tile(3204, 3230, 2),
			new Tile(3214, 3230, 2), new Tile(3214, 3207, 2))),

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