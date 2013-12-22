package lumbyspinner.jobs;

import java.util.concurrent.Callable;

import lumbyspinner.data.Constants;
import lumbyspinner.data.Misc;
import lumbyspinner.util.Job;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Condition;
import org.powerbot.script.wrappers.GameObject;
import org.powerbot.script.wrappers.LocalPath;

public class failsafe extends Job {

	public failsafe(MethodContext ctx) {
		super(ctx);

	}

	@Override
	public boolean activate() {

		return Constants.GroundFloor.contains(ctx.players.local());
	}

	@Override
	public void execute() {

		GameObject stairs = ctx.objects.select()
				.id(Constants.groundfloorstairs).nearest().poll();

		if (stairs.isOnScreen()
				&& stairs.getLocation().distanceTo(ctx.players.local()) <= 6) {
            if (stairs.getLocation().distanceTo(ctx.players.local()) > 3) {
                Misc.s("Turning Camera to Stairs");
                ctx.camera.turnTo(stairs);
            }
			Misc.s("Climbing up Staircase");
			stairs.interact("Climb-up", "Staircase");
			Condition.wait(new Callable<Boolean>() {
				@Override
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