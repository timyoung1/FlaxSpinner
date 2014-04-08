package lumbyspinner.jobs.Banking;

import lumbyspinner.data.Constantss;
import lumbyspinner.data.Misc;
import lumbyspinner.util.Job;

import org.powerbot.script.rt6.ClientContext;

public class Deposit extends Job {
	private final int Flax;

	public Deposit(ClientContext ctx) {
		super(ctx);
		this.Flax = Constantss.Flax.getId();
	}

	@Override
	public boolean activate() {
		return ctx.bank.opened() && ctx.backpack.select().id(Flax).count() == 0
				&& ctx.backpack.select().count() != 0;
	}

	@Override
	public void execute() {
		Misc.s("Depositing Backpack");
		ctx.bank.depositInventory();
	}
}