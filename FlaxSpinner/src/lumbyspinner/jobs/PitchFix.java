package lumbyspinner.jobs;

import lumbyspinner.util.Job;

import org.powerbot.script.methods.MethodContext;

public class PitchFix extends Job {
	public PitchFix(MethodContext ctx) {
		super(ctx);
	}

	@Override
	public boolean activate() {
		return ctx.camera.getPitch() < 55;
	}

	@Override
	public void execute() {
		ctx.camera.setPitch(true);
		sleep(500);
	}
}