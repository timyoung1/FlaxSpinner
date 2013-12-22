package lumbyspinner.data;

import org.powerbot.script.wrappers.Area;
import org.powerbot.script.wrappers.Tile;

public class Constants {
	public static int Flax = 1779;
	public static int BowString = 1777;

	public static int SpinningWheel = 36970;
	public static int groundfloorstairs = 36773;
	public static int secondfloorstairs = 36774;
	public static int thirdfloorstairs = 36775;

	public static Area Spinwheelfloor = new Area(new Tile(3204, 3207, 1),
			new Tile(3204, 3230, 1), new Tile(3214, 3230, 1), new Tile(3214,
					3207, 1));

	public static Area Bankfloor = new Area(new Tile(3204, 3207, 2), new Tile(
			3204, 3230, 2), new Tile(3214, 3230, 2), new Tile(3214, 3207, 2));

	public static Area GroundFloor = new Area(new Tile(3204, 3207, 0),
			new Tile(3204, 3230, 0), new Tile(3214, 3230, 0), new Tile(3214,
					3207, 0));
}