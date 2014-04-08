package lumbyspinner.jobs;

import java.util.concurrent.Callable;

import lumbyspinner.data.Areas;
import lumbyspinner.data.Constantss;
import lumbyspinner.data.Misc;
import lumbyspinner.util.Job;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

public class Spin extends Job {
	private final Area Spinwheelfloor;
	private final int Flax;
	private final int SpinningWheel;

	public Spin(ClientContext ctx) {
		super(ctx);
		this.Spinwheelfloor = Areas.Spinwheelfloor.getArea();
		this.Flax = Constantss.Flax.getId();
		this.SpinningWheel = Constantss.SpinningWheel.getId();
	}

	@Override
	public boolean activate() {
		return ctx.players.local().idle()
				&& ctx.backpack.select().id(Flax).count() != 0
				&& (Spinwheelfloor.contains(ctx.players.local()) && !ctx.widgets
						.component(1251, 11).visible());
	}

	@Override
	public void execute() {
		GameObject wheel = ctx.objects.select().id(SpinningWheel).nearest()
				.poll();

		if (ctx.widgets.component(1370, 20).visible()) {
			if (ctx.widgets.component(1370, 20).inViewport()) {
				Misc.s("Spinning Flax");
				ctx.widgets.component(1370, 20).interact("Make");

				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return ctx.widgets.component(1251, 11).inViewport()
								|| ctx.players.local().speed() == 0;
					}
				}, Random.nextInt(250, 2000), 10);
			}
		} else {
			if (wheel.valid()) {
				if (wheel.tile().distanceTo(ctx.players.local()) <= 6) {
					if (!wheel.inViewport()
							&& wheel.tile().distanceTo(ctx.players.local()) < 6) {
						Misc.s("Turning Camera to wheel");
						ctx.camera.turnTo(wheel);
					} else if (ctx.players.local().speed() == 0) {
						Misc.s("Clicking wheel");
						wheel.interact("Spin", "Spinning wheel");

						Condition.wait(new Callable<Boolean>() {
							@Override
							public Boolean call() throws Exception {
								return ctx.players.local().speed() == 0
										|| ctx.widgets.component(1370, 150)
												.inViewport();
							}
						}, Random.nextInt(200, 1750), 10);
					}
				} else {
					Misc.s("Walking to wheel");
					ctx.movement.step(new Tile(3209, 3213, 1));

					Condition.wait(new Callable<Boolean>() {
						@Override
						public Boolean call() throws Exception {
							return ctx.movement.distance(ctx.players.local(),
									ctx.movement.destination()) < 3;
						}
					}, Random.nextInt(200, 1250), 10);
				}
			} else {
				Misc.s("Cannot find Spinning wheel");
			}
		}
	}
}