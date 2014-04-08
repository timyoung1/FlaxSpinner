package lumbyspinner;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.util.concurrent.Callable;
import lumbyspinner.data.Areas;
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
import org.powerbot.script.Condition;
import org.powerbot.script.PaintListener;
import org.powerbot.script.Script;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Random;
import org.powerbot.script.Tile;
import org.powerbot.script.rt6.Bank;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.GameObject;
import org.powerbot.script.rt6.GeItem;
import org.powerbot.script.rt6.Menu;
import org.powerbot.script.rt6.Player;

@Script.Manifest(name = "LumbySpinner", description = "Spins flax in Lumbridge", properties = "topic = 1128107;client=6;")
public class LumbySpinner extends PollingScript<ClientContext> implements
		PaintListener {
	private JobContainer container;
	private final DecimalFormat df = new DecimalFormat("###,###,###.#");

	String formated(long time) {
		final int sec = (int) (time / 1000), h = sec / 3600, m = sec / 60 % 60, s = sec % 60;
		return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":"
				+ (s < 10 ? "0" + s : s);
	}

	private final Tile banktile = new Tile(3208, 3219, 2);
	private int stringPrice;

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
				new InventoryFix(ctx), new OpenBank(ctx, this),
				new Deposit(ctx), new Withdraw(ctx, this), new CloseBank(ctx),
				new ClimbdownStairs(ctx), new Spin(ctx),
				new ClimbupStairs(ctx), new StairFailsafe(ctx),
				new BankFailsafe(ctx) });
	}

	@Override
	public void stop() {
		ctx.controller.stop();
	}

	public boolean open() {
		final GameObject t = getNearest();
		if (t == null) {
			return false;
		}

		if (ctx.players.local().tile().distanceTo(t) <= 3) {
			if (!t.inViewport() && t.tile().distanceTo(ctx.players.local()) < 3) {
				turnTo(t);
			} else if (!ctx.players.local().inMotion()) {
				Misc.s("Opening Bank");
				t.interact(Menu.filter("Bank", t.name()));

				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return isopened() || ctx.players.local().speed() == 0;
					}
				}, 200, 750);
			}
		} else {
			walk(banktile);
		}
		return isopened();
	}

	GameObject getNearest() {
		return ctx.objects.select().id(Bank.BANK_NPC_IDS, Bank.BANK_BOOTH_IDS)
				.shuffle().within(Areas.Bankfloor.getArea()).nearest().poll();
	}

	private void turnTo(GameObject t) {
		Misc.s("Turning to DepositBox");
		ctx.camera.turnTo(t, 5);
	}

	boolean isopened() {
		return ctx.widgets.component(11, 1).inViewport();
	}

	void walk(Tile t) {
		Misc.s("Walking to Bank");
		if (ctx.movement.step(t) && Condition.wait(nextStep, 500, 1250)) {
		}
	}

	private final Callable<Boolean> nextStep = new Callable<Boolean>() {
		@Override
		public Boolean call() throws Exception {
			final Player player = ctx.players.local();
			return !player.inMotion()
					|| ctx.movement.destination().distanceTo(player) <= Random
							.nextInt(2, 4);
		}
	};
}