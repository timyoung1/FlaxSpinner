package lumbyspinner;

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.atomic.AtomicInteger;

import lumbyspinner.data.Misc;
import lumbyspinner.jobs.Banking;
import lumbyspinner.jobs.ClimbdownStairs;
import lumbyspinner.jobs.ClimbupStairs;
import lumbyspinner.jobs.InventoryFix;
import lumbyspinner.jobs.PitchFix;
import lumbyspinner.jobs.Spin;
import lumbyspinner.jobs.failsafe;
import lumbyspinner.util.Job;
import lumbyspinner.util.JobContainer;

import org.powerbot.event.PaintListener;
import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;

@Manifest(name = "LumbySpinner", description = "Spins flax in Lumbridge")
public class LumbySpinner extends PollingScript implements PaintListener {
	private JobContainer container;
	private AtomicInteger timer = new AtomicInteger();

	@Override
	public int poll() {
		final Job job = container.get();
		if (job != null) {
			job.execute();
			return job.delay();
		}
		return 100;
	}

	public void repaint(Graphics g) {
		g.setColor(Color.GREEN);
		g.drawString("LumbySpinner " + "V 1.21", 5, 400);
		g.drawString("by timyoung", 5, 415);
		g.drawString("Time Running: " + timer.incrementAndGet(), 5, 430);
		g.drawString("Status " + Misc.status, 5, 445);
	}

	@Override
	public void start() {
		this.container = new JobContainer(new Job[] { new PitchFix(ctx),
				new InventoryFix(ctx), new Banking(ctx),
				new ClimbdownStairs(ctx), new Spin(ctx),
				new ClimbupStairs(ctx), new failsafe(ctx) });
	}
}