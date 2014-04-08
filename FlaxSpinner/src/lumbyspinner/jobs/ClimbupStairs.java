package lumbyspinner.jobs;

import java.util.concurrent.Callable;

import lumbyspinner.data.Areas;
import lumbyspinner.data.Constantss;
import lumbyspinner.data.Misc;
import lumbyspinner.util.Job;

import org.powerbot.script.Area;
import org.powerbot.script.Condition;
import org.powerbot.script.Random;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;

public class ClimbupStairs extends Job {
	private final Area Spinwheelfloor;
	private final Area Bankfloor;
	private final int Flax;
	private final int secondfloorstairs;

	public ClimbupStairs(ClientContext ctx) {
		super(ctx);
		this.Spinwheelfloor = Areas.Spinwheelfloor.getArea();
		this.Bankfloor = Areas.Bankfloor.getArea();
		this.Flax = Constantss.Flax.getId();
		this.secondfloorstairs = Constantss.secondfloorstairs.getId();
	}

	@Override
	public boolean activate() {
		return ctx.backpack.select().id(Flax).count() == 0
				&& Spinwheelfloor.contains(ctx.players.local())
				&& !ctx.players.local().inMotion();
	}

	@Override
	public void execute() {
		GameObject stairs = ctx.objects.select().id(secondfloorstairs)
				.nearest().poll();

		if (stairs.valid()) {
			if (stairs.tile().distanceTo(ctx.players.local()) <= 6) {
				if (!stairs.inViewport()
						&& stairs.tile().distanceTo(ctx.players.local()) < 6) {
					Misc.s("Turning Camera to Stairs");
					ctx.camera.turnTo(stairs);
				} else if (!ctx.players.local().inMotion()) {
					Misc.s("Climbing up Staircase");
					if (stairs.interact("Climb-up", "Staircase")) {
						Misc.count += ctx.backpack.select()
								.id(Constantss.BowString.getId()).count();
					}

					Condition.wait(new Callable<Boolean>() {
						@Override
						public Boolean call() throws Exception {
							return ctx.players.local().speed() == 0
									|| Bankfloor.contains(ctx.players.local());
						}
					}, Random.nextInt(150, 1750), 10);
				}
			} else {
				Misc.s("Walking to Stairs");
				ctx.movement.step(stairs);

				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return ctx.movement.distance(ctx.players.local(),
								ctx.movement.destination()) < 3;
					}
				}, Random.nextInt(150, 1250), 10);
			}
		} else {
			Misc.s("Cannot find Stairs to go up");
		}
	}
}