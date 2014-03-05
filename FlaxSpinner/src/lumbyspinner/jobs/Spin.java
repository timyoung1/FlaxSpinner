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
import org.powerbot.script.wrappers.Tile;

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

		if (ctx.widgets.get(1370, 20).isVisible()) {
			if (ctx.widgets.get(1370, 20).isInViewport()) {
				Misc.s("Spinning Flax");
				ctx.widgets.get(1370, 20).interact("Make");

				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return ctx.widgets.get(1251, 11).isInViewport()
								|| ctx.players.local().getSpeed() == 0;
					}
				}, Random.nextInt(250, 2000), 10);
			}
		} else {
			if (wheel.isValid()) {
				if (wheel.getLocation().distanceTo(ctx.players.local()) <= 10) {
					if (!wheel.isInViewport()
							&& wheel.getLocation().distanceTo(
									ctx.players.local()) < 10) {
						Misc.s("Turning Camera to wheel");
						ctx.camera.turnTo(wheel);
					} else if (ctx.players.local().getSpeed() == 0) {
						Misc.s("Clicking wheel");
						wheel.interact("Spin", "Spinning wheel");

						Condition.wait(new Callable<Boolean>() {
							@Override
							public Boolean call() throws Exception {
								return ctx.players.local().getSpeed() == 0
										|| ctx.widgets.get(1370, 150)
												.isInViewport();
							}
						}, Random.nextInt(150, 1750), 10);
					}
				} else {
					Misc.s("Walking to wheel");
					ctx.movement.stepTowards(new Tile(3209, 3213, 1).randomize(
							1, 1));

					Condition.wait(new Callable<Boolean>() {
						@Override
						public Boolean call() throws Exception {
							return ctx.movement.getDistance(
									ctx.players.local(),
									ctx.movement.getDestination()) < 3;
						}
					}, Random.nextInt(150, 1250), 10);
				}
			} else {
				Misc.s("Cannot find Spinning wheel");
			}
		}
	}
}