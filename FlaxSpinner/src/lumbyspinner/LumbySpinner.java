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

import org.powerbot.event.PaintListener;
import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;
import org.powerbot.script.methods.Bank;
import org.powerbot.script.methods.Menu;
import org.powerbot.script.util.Condition;
import org.powerbot.script.util.GeItem;
import org.powerbot.script.util.Random;
import org.powerbot.script.wrappers.GameObject;
import org.powerbot.script.wrappers.Player;
import org.powerbot.script.wrappers.Tile;

@Manifest(name = "LumbySpinner", description = "Spins flax in Lumbridge", topic = 1128107)
public class LumbySpinner extends PollingScript implements PaintListener {
	private JobContainer container;
	private final DecimalFormat df = new DecimalFormat("###,###,###.#");
	private final Tile banktile = new Tile(3208, 3219, 2);
	private int stringPrice;

	@Override
	public int poll() {
		if (ctx.game.isLoggedIn() && ctx.game.getClientState() == 11) {
			final Job job = container.get();
			if (job != null) {
				job.execute();
				return job.delay();
			}
		}
		return Random.nextInt(50, 350);
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
		g.drawString("LumbySpinner " + "V 1.32", 300, 410);
		g.drawString("by timyoung", 300, 425);
		g.drawLine(300, 426, 470, 426);
		g.drawString("Time Running: " + this.getRuntime(), 300, 445);
		g.drawString("Status: " + Misc.status, 300, 460);
		g.drawString(df.format(Misc.count) + " - bow strings made", 300, 480);
		g.drawString("Overall Wealth: " + df.format(wealth), 300, 510);
	}

	@Override
	public void start() {
		this.stringPrice = GeItem.getPrice(Constantss.BowString.getId());
		this.container = new JobContainer(new Job[] { new PitchFix(ctx),
				new InventoryFix(ctx), new OpenBank(ctx, this),
				new Deposit(ctx), new Withdraw(ctx, this), new CloseBank(ctx),
				new ClimbdownStairs(ctx), new Spin(ctx),
				new ClimbupStairs(ctx), new StairFailsafe(ctx),
				new BankFailsafe(ctx) });
	}

	@Override
	public void stop() {
		this.getController().stop();
	}

	public boolean open() {
		final GameObject t = getNearest();
		if (t == null) {
			return false;
		}

		if (ctx.players.local().getLocation().distanceTo(t) <= 3) {
			if (!t.isInViewport()
					&& t.getLocation().distanceTo(ctx.players.local()) < 3) {
				turnTo(t);
			} else if (!ctx.players.local().isInMotion()) {
				Misc.s("Opening Bank");
				t.interact(Menu.filter("Bank", t.getName()));

				Condition.wait(new Callable<Boolean>() {
					@Override
					public Boolean call() throws Exception {
						return isopened()
								|| ctx.players.local().getSpeed() == 0;
					}
				}, 750, 250);
			}
		} else {
			walk(banktile);
		}
		return isopened();
	}

	org.powerbot.script.wrappers.GameObject getNearest() {
		return ctx.objects.select().id(Bank.BANK_NPC_IDS, Bank.BANK_BOOTH_IDS)
				.shuffle().within(Areas.Bankfloor.getArea()).nearest().poll();
	}

	private void turnTo(GameObject t) {
		Misc.s("Turning to DepositBox");
		ctx.camera.turnTo(t, 5);
	}

	boolean isopened() {
		return ctx.widgets.get(11, 1).isInViewport();
	}

	void walk(Tile t) {
		Misc.s("Walking to Bank");
		if (ctx.movement.stepTowards(t.randomize(1, 1))
				&& Condition.wait(nextStep, 500, 1250)) {
		}
	}

	private final Callable<Boolean> nextStep = new Callable<Boolean>() {
		@Override
		public Boolean call() throws Exception {
			final Player player = ctx.players.local();
			return !player.isInMotion()
					|| ctx.movement.getDestination().distanceTo(player) <= Random
							.nextInt(2, 4);
		}
	};
}