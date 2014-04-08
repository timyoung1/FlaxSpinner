package lumbyspinner.jobs.Banking;

import lumbyspinner.LumbySpinner;
import lumbyspinner.data.Constantss;
import lumbyspinner.data.Misc;
import lumbyspinner.util.Job;

import org.powerbot.script.rt6.ClientContext;

public class Withdraw extends Job {
	private final int Flax;

	public Withdraw(ClientContext ctx, LumbySpinner script) {
		super(ctx);
		this.Flax = Constantss.Flax.getId();
	}

	@Override
	public boolean activate() {
		return ctx.bank.opened() && ctx.backpack.select().count() == 0;
	}

	@Override
	public void execute() {
		if (ctx.bank.select().id(Flax).count() == 0) {
			Misc.s("Stopping Script");
			ctx.controller.stop();
		} else {
			Misc.s("Withdrawing Flax");
			ctx.bank.withdraw(Flax, 0);
		}
	}
}