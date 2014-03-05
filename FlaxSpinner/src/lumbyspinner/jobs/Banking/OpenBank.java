package lumbyspinner.jobs.Banking;

import lumbyspinner.LumbySpinner;
import lumbyspinner.data.Areas;
import lumbyspinner.data.Constantss;
import lumbyspinner.util.Job;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.Area;

public class OpenBank extends Job {
	private final LumbySpinner script;
	private final Area Bankfloor;
	private final int Flax;

	public OpenBank(MethodContext ctx, LumbySpinner script) {
		super(ctx);
		this.script = script;
		this.Bankfloor = Areas.Bankfloor.getArea();
		this.Flax = Constantss.Flax.getId();
	}

	@Override
	public boolean activate() {
		return !ctx.bank.isOpen() && Bankfloor.contains(ctx.players.local())
				&& !ctx.players.local().isInMotion()
				&& ctx.backpack.select().id(Flax).isEmpty();
	}

	@Override
	public void execute() {
		script.open();
	}
}