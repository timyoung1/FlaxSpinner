package lumbyspinner;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;

import lumbyspinner.data.Constantss;
import lumbyspinner.data.Misc;
import lumbyspinner.jobs.BankFailsafe;
import lumbyspinner.jobs.ClimbdownStairs;
import lumbyspinner.jobs.ClimbupStairs;
import lumbyspinner.jobs.InventoryFix;
import lumbyspinner.jobs.PitchFix;
import lumbyspinner.jobs.Spin;
import lumbyspinner.jobs.StairFailsafe;
import lumbyspinner.jobs.Banking.CloseBank;
import lumbyspinner.jobs.Banking.Deposit;
import lumbyspinner.jobs.Banking.OpenBank;
import lumbyspinner.jobs.Banking.Withdraw;
import lumbyspinner.util.Job;
import lumbyspinner.util.JobContainer;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GeItem;

@Script.Manifest(name = "LumbySpinner", description = "Spins flax in Lumbridge", properties = "topic = 1128107;client=6;")
public class LumbySpinner extends PollingScript<ClientContext> implements
		PaintListener {
	private JobContainer container;
	private final DecimalFormat df = new DecimalFormat("###,###,###.#");
	private int stringPrice;

	// Dad's time formatting method
	String formated(long time) {
		final int sec = (int) (time / 1000), h = sec / 3600, m = sec / 60 % 60, s = sec % 60;
		return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":"
				+ (s < 10 ? "0" + s : s);
	}

	@Override
	public void poll() {
		if (ctx.game.loggedIn() && ctx.game.clientState() == 11) {
			final Job job = container.get();
			if (job != null) {
				job.execute();
				return;
			}
		}
		return;
	}

	@Override
	public void repaint(Graphics g) {
		final Color color1 = new Color(47, 79, 79, 200);
		int wealth = stringPrice * Misc.count;
		g.setColor(color1);
		g.fillRoundRect(290, 395, 200, 120, 5, 5);
		g.setColor(Color.DARK_GRAY);
		g.drawRoundRect(290, 395, 200, 120, 5, 5);
		g.setColor(Color.ORANGE);
		g.drawString("LumbySpinner " + "V 1.33", 300, 410);
		g.drawString("by timyoung", 300, 425);
		g.drawLine(300, 426, 470, 426);
		g.drawString("Time Running: " + formated(this.getRuntime()), 300, 445);
		g.drawString("Status: " + Misc.status, 300, 460);
		g.drawString(df.format(Misc.count) + " - bow strings made", 300, 480);
		g.drawString("Overall Wealth: " + df.format(wealth), 300, 510);
	}

	@Override
	public void start() {
		this.stringPrice = GeItem.price(Constantss.BowString.getId());
		this.container = new JobContainer(new Job[] { new PitchFix(ctx),
				new InventoryFix(ctx), new OpenBank(ctx), new Deposit(ctx),
				new Withdraw(ctx, this), new CloseBank(ctx),
				new ClimbdownStairs(ctx), new Spin(ctx),
				new ClimbupStairs(ctx), new StairFailsafe(ctx),
				new BankFailsafe(ctx) });
	}

	@Override
	public void stop() {
		ctx.controller.stop();
	}
}