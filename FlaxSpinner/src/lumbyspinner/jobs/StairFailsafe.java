package lumbyspinner.jobs;

import java.util.concurrent.Callable;

import lumbyspinner.data.Areas;
import lumbyspinner.data.Constantss;
import lumbyspinner.data.Misc;
import lumbyspinner.util.Job;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Condition;
import org.powerbot.script.wrappers.Area;
import org.powerbot.script.wrappers.GameObject;

public class StairFailsafe extends Job {
	private final Area GroundFloor;
	private final Area Bankfloor;
	private final int groundfloorstairs;

	public StairFailsafe(MethodContext ctx) {
		super(ctx);
		this.GroundFloor = Areas.GroundFloor.getArea();
		this.Bankfloor = Areas.Bankfloor.getArea();
		this.groundfloorstairs = Constantss.groundfloorstairs.getId();
	}

	@Override
	public boolean activate() {

		return GroundFloor.contains(ctx.players.local());
	}

	@Override
	public void execute() {

		GameObject stairs = ctx.objects.select().id(groundfloorstairs)
				.nearest().poll();
		if (stairs.isValid()) {
			if (stairs.isOnScreen()
					&& stairs.getLocation().distanceTo(ctx.players.local()) <= 10) {
				if (stairs.getLocation().distanceTo(ctx.players.local()) > 3) {
					Misc.s("Turning Camera to Stairs");
					ctx.camera.turnTo(stairs);
				}
				if (!ctx.players.local().isInMotion()) {
					Misc.s("Climbing up Staircase");
					stairs.interact("Climb-up", "Staircase");

					Condition.wait(new Callable<Boolean>() {
						@Override
						public Boolean call() throws Exception {
							return ctx.players.local().getSpeed() == 0
									|| Bankfloor.contains(ctx.players.local());
						}
					}, 2000, 3000);
				}
			} else {
				Misc.s("Walking to Stairs");
				ctx.movement.stepTowards(stairs.getLocation().randomize(1, 1));

				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return ctx.movement.getDistance(ctx.players.local(),
								ctx.movement.getDestination()) < 4;
					}
				}, 750, 20);
			}
		} else {
			Misc.s("Cannot find failsafe Stairs");
		}
	}
}