package lumbyspinner.jobs;

import java.util.concurrent.Callable;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Condition;
import org.powerbot.script.wrappers.GameObject;
import lumbyspinner.data.Constants;
import lumbyspinner.data.Misc;
import lumbyspinner.util.Job;
import org.powerbot.script.wrappers.LocalPath;
import org.powerbot.script.wrappers.Tile;

public class Spin extends Job {

	public Spin(MethodContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return ctx.players.local().isIdle()
				&& ctx.backpack.select().id(Constants.Flax).count() != 0
				&& (Constants.Spinwheelfloor.contains(ctx.players.local()));
	}

	@Override
	public void execute() {
		GameObject wheel = ctx.objects.select().id(Constants.SpinningWheel)
				.nearest().poll();
		if (ctx.backpack.select().id(Constants.Flax).count() >= ctx.backpack
				.select().count()) {

			if (wheel.isOnScreen()
					&& wheel.getLocation().distanceTo(ctx.players.local()) <= 6) {
                    if (wheel.getLocation().distanceTo(ctx.players.local()) > 3) {
                        Misc.s("Turning Camera to Wheel");
                        ctx.camera.turnTo(wheel);
                    }
				if (ctx.widgets.get(1370, 20).isValid()) {
					if (ctx.widgets.get(1370, 20).isOnScreen()) {
						Misc.s("Spinning Flax");
						ctx.widgets.get(1370, 20).interact("Make");
						sleep(250, 750);
					}
				} else {
                    Misc.s("Clicking Wheel");
					wheel.interact("Spin", "Spinning wheel");
					Condition.wait(new Callable<Boolean>() {
						public Boolean call() throws Exception {
							return ctx.players.local().isIdle();
						}
					}, 2500, 3000);
				}
			} else {
				Misc.s("Walking to Wheel");
                ctx.movement.stepTowards(wheel.getLocation().randomize(1,1));

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
}