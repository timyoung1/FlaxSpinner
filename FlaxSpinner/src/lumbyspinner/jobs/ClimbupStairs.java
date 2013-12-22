package lumbyspinner.jobs;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Condition;
import org.powerbot.script.wrappers.GameObject;
import lumbyspinner.data.Constants;
import lumbyspinner.data.Misc;
import lumbyspinner.util.Job;

import java.util.concurrent.Callable;

public class ClimbupStairs extends Job {

	public ClimbupStairs(MethodContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return ctx.backpack.select().id(Constants.Flax).count() == 0
				&& Constants.Spinwheelfloor.contains(ctx.players.local());
	}

	@Override
	public void execute() {
		GameObject stairs = ctx.objects.select()
				.id(Constants.secondfloorstairs).nearest().poll();

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
							|| Constants.Bankfloor.contains(ctx.players.local());
				}
			}, 2000, 3000);
		} else {
			Misc.s("Walking to Stairs");
            ctx.movement.stepTowards(stairs.getLocation().randomize(1,1));

			Condition.wait(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return ctx.movement.getDistance(ctx.players.local(),
							ctx.movement.getDestination()) < 4;
				}
			}, 750, 20);
		}
	}
}