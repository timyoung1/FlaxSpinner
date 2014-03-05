package lumbyspinner.jobs.Banking;

import lumbyspinner.data.Constantss;
import lumbyspinner.data.Misc;
import lumbyspinner.util.Job;

import org.powerbot.script.methods.MethodContext;

public class CloseBank extends Job {
	private final int Flax;

	public CloseBank(MethodContext ctx) {
		super(ctx);
		this.Flax = Constantss.Flax.getId();
	}

	@Override
	public boolean activate() {
		return ctx.bank.isOpen()
				&& ctx.backpack.select().id(Flax).count() == 28;
	}

	@Override
	public void execute() {
		Misc.s("Closing Bank");
		ctx.bank.close();
	}
}