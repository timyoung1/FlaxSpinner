package lumbyspinner;

import java.awt.Color;
import java.awt.Graphics;
import lumbyspinner.data.Misc;
import lumbyspinner.jobs.BankFailsafe;
import lumbyspinner.jobs.Banking;
import lumbyspinner.jobs.ClimbdownStairs;
import lumbyspinner.jobs.ClimbupStairs;
import lumbyspinner.jobs.InventoryFix;
import lumbyspinner.jobs.PitchFix;
import lumbyspinner.jobs.Spin;
import lumbyspinner.jobs.StairFailsafe;
import lumbyspinner.util.Job;
import lumbyspinner.util.JobContainer;
import org.powerbot.event.PaintListener;
import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;
import org.powerbot.script.util.Random;
import org.powerbot.script.util.Timer;

@SuppressWarnings("deprecation")
@Manifest(name = "LumbySpinner", description = "Spins flax in Lumbridge")
public class LumbySpinner extends PollingScript implements PaintListener {
	private JobContainer container;

	@Override
	public int poll() {
		if (ctx.game.isLoggedIn() && ctx.game.getClientState() == 11) {
			final Job job = container.get();
			if (ctx.game.isLoggedIn()) {
				if (job != null) {
					job.execute();
					return job.delay();
				}
			}
		}
		return Random.nextInt(200, 350);
	}

	@Override
	public void repaint(Graphics g) {
		String runTime = Timer.format(getRuntime());
		g.setColor(Color.BLACK);
		g.drawString("LumbySpinner " + "V 1.23", 300, 550);
		g.drawString("by timyoung", 300, 560);
		g.drawString("Time Running: " + (runTime), 300, 570);
		g.drawString("Status " + Misc.status, 300, 580);
		g.drawString(Misc.count + "bow strings made", 300, 590);
	}

	@Override
	public void start() {
		this.container = new JobContainer(new Job[] { new PitchFix(ctx),
				new InventoryFix(ctx), new BankFailsafe(ctx), new Banking(ctx),
				new ClimbdownStairs(ctx), new Spin(ctx),
				new ClimbupStairs(ctx), new StairFailsafe(ctx) });
	}
}