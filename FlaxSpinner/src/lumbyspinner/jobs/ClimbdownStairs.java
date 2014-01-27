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

public class ClimbdownStairs extends Job {
	private final Area Bankfloor;
	private final Area Spinwheelfloor;
	private final int Flax;
	private final int thirdfloorstairs;

	public ClimbdownStairs(MethodContext ctx) {
		super(ctx);
		this.Bankfloor = Areas.Bankfloor.getArea();
		this.Spinwheelfloor = Areas.Spinwheelfloor.getArea();
		this.Flax = Constantss.Flax.getId();
		this.thirdfloorstairs = Constantss.thirdfloorstairs.getId();
	}

	@Override
	public boolean activate() {
		return !ctx.backpack.select().id(Flax).isEmpty()
				&& Bankfloor.contains(ctx.players.local())
				&& !ctx.players.local().isInMotion();
	}

	@Override
	public void execute() {
		GameObject stairs = ctx.objects.select().id(thirdfloorstairs).nearest()
				.poll();
		if (stairs.isValid()) {
			if (stairs.isOnScreen()
					&& stairs.getLocation().distanceTo(ctx.players.local()) <= 10) {
				if (stairs.getLocation().distanceTo(ctx.players.local()) > 3) {
					Misc.s("Turning Camera to Stairs");
					ctx.camera.turnTo(stairs);
				}
				if (!ctx.players.local().isInMotion()) {
					Misc.s("Climbing down Staircase");
					stairs.interact("Climb-down", "Staircase");

					Condition.wait(new Callable<Boolean>() {
						@Override
						public Boolean call() throws Exception {
							return Spinwheelfloor.contains(ctx.players.local())
									|| ctx.players.local().getSpeed() == 0;
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
			Misc.s("Cannot find Stairs to go down");
		}
	}
}