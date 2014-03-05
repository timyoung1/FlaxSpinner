package lumbyspinner.jobs.Banking;

import lumbyspinner.LumbySpinner;
import lumbyspinner.data.Constantss;
import lumbyspinner.data.Misc;
import lumbyspinner.util.Job;

import org.powerbot.script.methods.MethodContext;

public class Withdraw extends Job {
	private final LumbySpinner script;
	private final int Flax;

	public Withdraw(MethodContext ctx, LumbySpinner script) {
		super(ctx);
		this.script = script;
		this.Flax = Constantss.Flax.getId();
	}

	@Override
	public boolean activate() {
		return ctx.bank.isOpen() && ctx.backpack.select().count() == 0;
	}

	@Override
	public void execute() {
		if (ctx.bank.select().id(Flax).isEmpty()) {
			Misc.s("Stopping Script");
			script.getController().stop();
		} else {
			Misc.s("Withdrawing Flax");
			ctx.bank.withdraw(Flax, 0);
		}
	}
}