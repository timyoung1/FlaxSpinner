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

public class ClimbupStairs extends Job {
	private final Area Spinwheelfloor;
	private final Area Bankfloor;
	private final int Flax;
	private final int secondfloorstairs;

	public ClimbupStairs(MethodContext ctx) {
		super(ctx);
		this.Spinwheelfloor = Areas.Spinwheelfloor.getArea();
		this.Bankfloor = Areas.Bankfloor.getArea();
		this.Flax = Constantss.Flax.getId();
		this.secondfloorstairs = Constantss.secondfloorstairs.getId();
	}

	@Override
	public boolean activate() {
		return ctx.backpack.select().id(Flax).count() == 0
				&& Spinwheelfloor.contains(ctx.players.local());
	}

	@Override
	public void execute() {
		GameObject stairs = ctx.objects.select().id(secondfloorstairs)
				.nearest().poll();
		if (stairs.isValid()) {
			if (stairs.isOnScreen()
					&& stairs.getLocation().distanceTo(ctx.players.local()) <= 6) {
				if (stairs.getLocation().distanceTo(ctx.players.local()) > 3) {
					Misc.s("Turning Camera to Stairs");
					ctx.camera.turnTo(stairs);
				}
				Misc.s("Climbing up Staircase");
				stairs.interact("Climb-up", "Staircase");

				Condition.wait(new Callable<Boolean>() {
					public Boolean call() throws Exception {
						return ctx.players.local().isIdle()
								|| Bankfloor.contains(ctx.players.local());
					}
				}, 2000, 3000);
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
			Misc.s("Cannot find Stairs to go up");
		}
	}
}