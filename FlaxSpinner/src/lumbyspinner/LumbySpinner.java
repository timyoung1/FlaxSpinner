package lumbyspinner;

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.Callable;

import lumbyspinner.data.Areas;
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
import org.powerbot.script.util.Random;
import org.powerbot.script.util.Timer;
import org.powerbot.script.wrappers.GameObject;
import org.powerbot.script.wrappers.Player;
import org.powerbot.script.wrappers.Tile;

@SuppressWarnings("deprecation")
@Manifest(name = "LumbySpinner", description = "Spins flax in Lumbridge", topic = 1128107)
public class LumbySpinner extends PollingScript implements PaintListener {
	private JobContainer container;
	private final Tile banktile = new Tile(3208, 3219, 2);

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
		return 200;
	}

	@Override
	public void repaint(Graphics g) {
		String runTime = Timer.format(getRuntime());
		g.setColor(Color.darkGray);
		g.drawRoundRect(285, 490, 150, 80, 5, 5);
		g.setColor(Color.ORANGE);
		g.drawString("LumbySpinner " + "V 1.26", 300, 500);
		g.drawString("by timyoung", 300, 515);
		g.drawString("Time Running: " + (runTime), 300, 530);
		g.drawString("Status " + Misc.status, 300, 545);
		g.drawString(Misc.count + " - bow strings made", 300, 560);
	}

	@Override
	public void start() {
		this.container = new JobContainer(new Job[] { new PitchFix(ctx),
				new InventoryFix(ctx), new OpenBank(ctx, this),
				new Deposit(ctx), new Withdraw(ctx, this), new CloseBank(ctx),
				new ClimbdownStairs(ctx), new Spin(ctx),
				new ClimbupStairs(ctx), new StairFailsafe(ctx),
				new BankFailsafe(ctx) });
	}

	public boolean open() {
		final GameObject t = getNearest();
		if (t == null) {
			return false;
		}

		if (t.isOnScreen()
				&& ctx.players.local().getLocation().distanceTo(t) <= 10) {
			if (!t.isOnScreen()
					&& t.getLocation().distanceTo(ctx.players.local()) <= 3) {
				turnTo(t);
			}
			Misc.s("Opening Bank");
			t.interact(Menu.filter("Bank", t.getName()));

			Condition.wait(new Callable<Boolean>() {
				@Override
				public Boolean call() throws Exception {
					return isopened() || ctx.players.local().getSpeed() == 0;
				}
			}, 750, 250);
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
		return ctx.widgets.get(11, 1).isOnScreen();
	}

	void walk(Tile t) {
		Misc.s("Walking to Bank");
		if (ctx.movement.stepTowards(t) && Condition.wait(nextStep, 500, 1250)) {
		}
	}

	private final Callable<Boolean> nextStep = new Callable<Boolean>() {
		@Override
		public Boolean call() throws Exception {
			final Player player = ctx.players.local();
			return !player.isInMotion()
					|| ctx.movement.getDestination().distanceTo(player) <= Random
							.nextInt(2, 6);
		}
	};

}