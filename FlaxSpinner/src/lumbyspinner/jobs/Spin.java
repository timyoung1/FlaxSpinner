package lumbyspinner.jobs;

import java.util.concurrent.Callable;

import lumbyspinner.data.Areas;
import lumbyspinner.data.Constantss;
import lumbyspinner.data.Misc;
import lumbyspinner.util.Job;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Condition;
import org.powerbot.script.util.Random;
import org.powerbot.script.wrappers.Area;
import org.powerbot.script.wrappers.GameObject;

public class Spin extends Job {
	private final Area Spinwheelfloor;
	private final int Flax;
	private final int SpinningWheel;

	public Spin(MethodContext ctx) {
		super(ctx);
		this.Spinwheelfloor = Areas.Spinwheelfloor.getArea();
		this.Flax = Constantss.Flax.getId();
		this.SpinningWheel = Constantss.SpinningWheel.getId();
	}

	@Override
	public boolean activate() {
		return ctx.players.local().isIdle()
				&& ctx.backpack.select().id(Flax).count() != 0
				&& (Spinwheelfloor.contains(ctx.players.local()) && !ctx.widgets
						.get(1251, 11).isVisible());
	}

	@Override
	public void execute() {
		GameObject wheel = ctx.objects.select().id(SpinningWheel).nearest()
				.poll();

		if (ctx.widgets.get(1370, 20).isValid()) {
			if (ctx.widgets.get(1370, 20).isOnScreen()) {
				Misc.s("Spinning Flax");
				ctx.widgets.get(1370, 20).interact("Make");

				Condition.wait(new Callable<Boolean>() {
					public Boolean call() throws Exception {
						return ctx.players.local().isIdle()
								&& ctx.widgets.get(1251, 11).isValid();
					}
				}, 2500, 3000);
			}
		} else {
			if (wheel.isValid()) {
				if (wheel.isOnScreen()
						&& wheel.getLocation().distanceTo(ctx.players.local()) <= 6) {
					if (wheel.getLocation().distanceTo(ctx.players.local()) > 3) {
						Misc.s("Turning Camera to wheel");
						ctx.camera.turnTo(wheel);
						sleep(Random.nextInt(250, 750));
					}
					Misc.s("Clicking wheel");
					wheel.interact("Spin", "Spinning wheel");

					Condition.wait(new Callable<Boolean>() {
						public Boolean call() throws Exception {
							return ctx.players.local().isIdle()
									&& ctx.widgets.get(1370, 20).isValid();
						}
					}, 2500, 3000);
				} else {
					Misc.s("Walking to wheel");
					ctx.movement.stepTowards(wheel.getLocation()
							.randomize(1, 1));

					Condition.wait(new Callable<Boolean>() {
						@Override
						public Boolean call() throws Exception {
							return ctx.movement.getDistance(
									ctx.players.local(),
									ctx.movement.getDestination()) < 4;
						}
					}, 750, 20);
				}
			} else {
				Misc.s("Cannot find Spinnin wheel");
			}
		}
	}
}