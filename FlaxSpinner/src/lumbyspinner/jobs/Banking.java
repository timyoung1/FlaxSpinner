package lumbyspinner.jobs;

import java.util.concurrent.Callable;
import lumbyspinner.data.Constants;
import lumbyspinner.data.Misc;
import lumbyspinner.util.Job;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Condition;
import org.powerbot.script.util.Random;
import org.powerbot.script.wrappers.LocalPath;
import org.powerbot.script.wrappers.Tile;

public class Banking extends Job {
    private final Tile banktile =  new Tile(3208,3219,2);
	public Banking(MethodContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return ctx.backpack.select().id(Constants.Flax).count() == 0
				&& Constants.Bankfloor.contains(ctx.players.local());
	}

	@Override
	public void execute() {
		if (ctx.bank.isPresent()) {
			if (ctx.bank.isOpen()) {
				ctx.bank.depositInventory();
				// condition, backpack must be greater than 0
				/*Condition.wait(new Callable<Boolean>() {
					public Boolean call() throws Exception {
						return ctx.backpack.count() > 0;
					}
				}, 750, 20);*/
				if (ctx.backpack.select().id(Constants.Flax).count() == 0) {
					ctx.bank.withdraw(Constants.Flax, 0);
					// condition, backpack does not contain any flax
					Condition.wait(new Callable<Boolean>() {
						public Boolean call() throws Exception {
							return !ctx.backpack.select().id(Constants.Flax)
									.isEmpty();
						}
					}, 750, 20);
				}
				ctx.bank.close();
				sleep(Random.nextInt(250, 750));
			} else if (ctx.bank.isOnScreen()
					&& ctx.bank.getNearest().getLocation()
							.distanceTo(ctx.players.local()) <= 6) {
				Misc.s("Opening Bank");
				ctx.bank.open();
			} else {
				Misc.s("Walking to Bank");
                ctx.movement.stepTowards(banktile.randomize(1,1));

				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return ctx.movement.getDistance(ctx.players.local(),
								ctx.movement.getDestination()) < 4;
					}
				}, 750, 20);
			}
		} else {
			Misc.s("Cannot Find Bank");
		}
	}
}