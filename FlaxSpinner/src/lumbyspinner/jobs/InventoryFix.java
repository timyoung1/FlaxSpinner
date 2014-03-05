package lumbyspinner.jobs;

import lumbyspinner.data.Areas;
import lumbyspinner.util.Job;

import org.powerbot.script.methods.Hud;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.wrappers.Area;

public class InventoryFix extends Job {
	private final Area Bankfloor;

	public InventoryFix(MethodContext ctx) {
		super(ctx);
		this.Bankfloor = Areas.Bankfloor.getArea();
	}

	@Override
	public boolean activate() {
		return ctx.game.isLoggedIn() && !ctx.hud.isVisible(Hud.Window.BACKPACK)
				&& !Bankfloor.contains(ctx.players.local());
	}

	@Override
	public void execute() {
		sleep(1000);
		if (!ctx.hud.isOpen(Hud.Window.BACKPACK)) {
			ctx.hud.open(Hud.Window.BACKPACK);
			sleep(500);
		} else if (!ctx.hud.isVisible(Hud.Window.BACKPACK)) {
			ctx.hud.view(Hud.Window.BACKPACK);
			sleep(500);
		}
	}
}