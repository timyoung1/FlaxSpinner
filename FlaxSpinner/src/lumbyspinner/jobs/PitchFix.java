package lumbyspinner.jobs;

import lumbyspinner.util.Job;

import org.powerbot.script.rt6.ClientContext;

public class PitchFix extends Job {
	public PitchFix(ClientContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return ctx.camera.pitch() < 55;
	}

	@Override
	public void execute() {
		ctx.camera.pitch(true);
	}
}