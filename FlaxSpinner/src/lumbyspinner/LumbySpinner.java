package lumbyspinner;

import java.awt.*;
import lumbyspinner.data.Misc;
import lumbyspinner.jobs.*;
import lumbyspinner.util.Job;
import lumbyspinner.util.JobContainer;
import org.powerbot.event.PaintListener;
import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;
import org.powerbot.script.util.Timer;

@SuppressWarnings("deprecation")
@Manifest(name = "LumbySpinner", description = "Spins flax in Lumbridge")
public class LumbySpinner extends PollingScript implements PaintListener {
	private JobContainer container;
	private static final Timer timer = new Timer(0);

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
		g.drawString("LumbySpinner " + "V 1.2", 5, 400);
		g.drawString("by timyoung", 5, 415);
		g.drawString("Time Running: " + timer.toElapsedString(), 5, 430);
		g.drawString("Status " + Misc.status, 5, 445);
	}

	@Override
	public void start() {
		this.container = new JobContainer(new Job[] { new PitchFix(ctx),new InventoryFix(ctx),
				new Banking(ctx), new ClimbdownStairs(ctx), new Spin(ctx),
				new ClimbupStairs(ctx), new failsafe(ctx) });
	}
}