package lumbyspinner.jobs.Banking;

import java.util.concurrent.Callable;

import lumbyspinner.data.Constantss;
import lumbyspinner.data.Misc;
import lumbyspinner.util.Job;

import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.util.Condition;

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

		Condition.wait(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				return !ctx.bank.isOpen();
			}
		}, 750, 50);
		sleep(550, 100);
	}
}