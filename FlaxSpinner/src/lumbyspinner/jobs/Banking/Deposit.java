package lumbyspinner.jobs.Banking;

import lumbyspinner.data.Constantss;
import lumbyspinner.data.Misc;
import lumbyspinner.util.Job;

import org.powerbot.script.methods.MethodContext;

public class Deposit extends Job {
	private final int Flax;

	public Deposit(MethodContext ctx) {
		super(ctx);
		this.Flax = Constantss.Flax.getId();
	}

	@Override
	public boolean activate() {
		return ctx.bank.isOpen() && ctx.backpack.select().id(Flax).isEmpty()
				&& ctx.backpack.select().count() != 0;
	}

	@Override
	public void execute() {
		Misc.s("Depositing Backpack");
		ctx.bank.depositInventory();
	}
}