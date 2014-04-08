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

public class StairFailsafe extends Job {
	private final Area GroundFloor;
	private final Area Bankfloor;
	private final int groundfloorstairs;

	public StairFailsafe(ClientContext ctx) {
		super(ctx);
		this.GroundFloor = Areas.GroundFloor.getArea();
		this.Bankfloor = Areas.Bankfloor.getArea();
		this.groundfloorstairs = Constantss.groundfloorstairs.getId();
	}

	@Override
	public boolean activate() {
		return GroundFloor.contains(ctx.players.local());
	}

	@Override
	public void execute() {
		GameObject stairs = ctx.objects.select().id(groundfloorstairs)
				.nearest().poll();

		if (stairs.valid()) {
			if (stairs.tile().distanceTo(ctx.players.local()) <= 6) {
				if (!stairs.inViewport()
						&& stairs.tile().distanceTo(ctx.players.local()) < 6) {
					Misc.s("Turning Camera to Stairs");
					ctx.camera.turnTo(stairs);
				} else if (!ctx.players.local().inMotion()) {
					Misc.s("Climbing up Staircase");
					stairs.interact("Climb-up", "Staircase");

					Condition.wait(new Callable<Boolean>() {
						@Override
						public Boolean call() throws Exception {
							return ctx.players.local().speed() == 0
									|| Bankfloor.contains(ctx.players.local());
						}
					}, Random.nextInt(250, 2000), 10);
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
				}, Random.nextInt(250, 2000), 10);
			}
		} else {
			Misc.s("Cannot find failsafe Stairs");
		}
	}
}