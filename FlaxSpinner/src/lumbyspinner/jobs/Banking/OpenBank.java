package lumbyspinner.jobs.Banking;

import lumbyspinner.LumbySpinner;
import lumbyspinner.data.Areas;
import lumbyspinner.data.Constantss;
import lumbyspinner.util.Job;

import org.powerbot.script.Area;
import org.powerbot.script.rt6.ClientContext;

public class OpenBank extends Job {
	private final LumbySpinner script;
	private final Area Bankfloor;
	private final int Flax;

	public OpenBank(ClientContext ctx, LumbySpinner script) {
		super(ctx);
		this.script = script;
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
		script.open();
	}
}