package lumbyspinner.util;

import org.powerbot.script.rt6.ClientAccessor;
import org.powerbot.script.rt6.ClientContext;

public abstract class Job extends ClientAccessor {
	public Job(ClientContext ctx) {
		super(ctx);
	}

	/* returns the priority of the job. higher priority = executed first */
	public int priority() {
		return 0;
	}

	public abstract boolean activate();

	public abstract void execute();
}