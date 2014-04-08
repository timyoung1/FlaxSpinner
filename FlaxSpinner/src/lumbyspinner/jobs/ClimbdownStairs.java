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

public class ClimbdownStairs extends Job {
	private final Area Bankfloor;
	private final Area Spinwheelfloor;
	private final int Flax;
	private final int thirdfloorstairs;

	public ClimbdownStairs(ClientContext ctx) {
		super(ctx);
		this.Bankfloor = Areas.Bankfloor.getArea();
		this.Spinwheelfloor = Areas.Spinwheelfloor.getArea();
		this.Flax = Constantss.Flax.getId();
		this.thirdfloorstairs = Constantss.thirdfloorstairs.getId();
	}

	@Override
	public boolean activate() {
		return ctx.backpack.select().id(Flax).count() != 0
				&& Bankfloor.contains(ctx.players.local())
				&& !ctx.players.local().inMotion();
	}

	@Override
	public void execute() {
		GameObject stairs = ctx.objects.select().id(thirdfloorstairs).nearest()
				.poll();

		if (stairs.valid()) {
			if (stairs.tile().distanceTo(ctx.players.local()) <= 6) {
				if (!stairs.inViewport()
						&& stairs.tile().distanceTo(ctx.players.local()) < 6) {
					Misc.s("Turning Camera to Stairs");
					ctx.camera.turnTo(stairs);
				} else if (!ctx.players.local().inMotion()) {
					Misc.s("Climbing down Staircase");
					stairs.interact("Climb-down", "Staircase");

					Condition.wait(new Callable<Boolean>() {
						@Override
						public Boolean call() throws Exception {
							return Spinwheelfloor.contains(ctx.players.local())
									|| ctx.players.local().speed() == 0;
						}
					}, Random.nextInt(150, 1750), 10);
				}
			} else {
				Misc.s("Walking to Stairs");
				ctx.movement.step(stairs.tile());

				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return ctx.movement.distance(ctx.players.local(),
								ctx.movement.destination()) < 3;
					}
				}, Random.nextInt(150, 1250), 10);
			}
		} else {
			Misc.s("Cannot find Stairs to go down");
		}
	}
}