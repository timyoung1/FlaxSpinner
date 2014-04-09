package lumbyspinner.jobs.Banking;

import lumbyspinner.data.Areas;
import lumbyspinner.data.Constantss;
import lumbyspinner.util.Job;

import org.powerbot.script.Area;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.ClientContext;

public class OpenBank extends Job {
	private final Area Bankfloor;
	private final int Flax;
	private final Tile banktile = new Tile(3208, 3219, 2);

	public OpenBank(ClientContext ctx) {
		super(ctx);
		this.Bankfloor = Areas.Bankfloor.getArea();
		this.Flax = Constantss.Flax.getId();
	}

	@Override
	public boolean activate() {
		return !ctx.bank.opened() && Bankfloor.contains(ctx.players.local())
				&& !ctx.players.local().inMotion()
				&& ctx.backpack.select().id(Flax).count() == 0;
	}

	@Override
	public void execute() {
		if (ctx.bank.inViewport()) {
			ctx.bank.open();
		} else {
			ctx.movement.step(banktile);
		}
	}
}