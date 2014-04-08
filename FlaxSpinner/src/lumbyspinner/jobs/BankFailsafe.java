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

public class BankFailsafe extends Job {
	private final Area Bankroof;
	private final Area Bankfloor;
	private final int roofladder;

	public BankFailsafe(ClientContext ctx) {
		super(ctx);
		this.Bankroof = Areas.Bankroof.getArea();
		this.Bankfloor = Areas.Bankfloor.getArea();
		this.roofladder = Constantss.bankladder.getId();
	}

	@Override
	public boolean activate() {
		return Bankroof.contains(ctx.players.local());
	}

	@Override
	public void execute() {
		GameObject stairs = ctx.objects.select().id(roofladder).nearest()
				.poll();

		if (stairs.valid()) {
			if (stairs.tile().distanceTo(ctx.players.local()) <= 6) {
				if (!stairs.inViewport()
						&& stairs.tile().distanceTo(ctx.players.local()) < 6) {
					Misc.s("Turning to Ladder");
					ctx.camera.turnTo(stairs);
				} else if (!ctx.players.local().inMotion()) {
					Misc.s("Climbing down Ladder");
					stairs.interact("Climb-up", "Staircase");

					Condition.wait(new Callable<Boolean>() {
						@Override
						public Boolean call() throws Exception {
							return ctx.players.local().speed() == 0
									|| Bankfloor.contains(ctx.players.local());
						}
					}, Random.nextInt(150, 1750), 10);
				}
			} else {
				Misc.s("Walking to Ladder");
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
			Misc.s("Cannot find Ladder");
		}
	}
}