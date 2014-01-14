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
import org.powerbot.script.wrappers.Tile;

public class Banking extends Job {
	private final Tile banktile = new Tile(3208, 3219, 2);
	private final Area Bankfloor;
	private final int Flax;

	public Banking(MethodContext ctx) {
		super(ctx);
		this.Bankfloor = Areas.Bankfloor.getArea();
		this.Flax = Constantss.Flax.getId();
	}

	@Override
	public boolean activate() {
		return ctx.backpack.select().id(Flax).count() == 0
				&& Bankfloor.contains(ctx.players.local());
	}

	@Override
	public void execute() {
		if (ctx.bank.isOpen()) {

			ctx.bank.depositInventory();
			if (ctx.backpack.select().id(Flax).count() == 0) {
				if (ctx.bank.select().id(Flax).count() != 0) {
					ctx.bank.withdraw(Flax, 0);
				} else {
					ctx.getBot().isStopping();
				}
			}
			sleep(Random.nextInt(250, 750));
		} else if (ctx.bank.isOnScreen()
				&& ctx.bank.getNearest().getLocation()
						.distanceTo(ctx.players.local()) < 10) {
			if (ctx.bank.getNearest().getLocation()
					.distanceTo(ctx.players.local()) > 3) {
				ctx.camera.turnTo(ctx.bank.getNearest());
			}
			Misc.s("Opening Bank");
			ctx.bank.open();

		} else {
			Misc.s("Walking to Bank");
			ctx.movement.stepTowards(banktile.randomize(1, 1));

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