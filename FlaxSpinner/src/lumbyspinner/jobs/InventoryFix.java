package lumbyspinner.jobs;

import lumbyspinner.data.Areas;
import lumbyspinner.util.Job;

import org.powerbot.script.Area;
import org.powerbot.script.rt6.ClientContext;
import org.powerbot.script.rt6.Hud;

public class InventoryFix extends Job {
	private final Area Bankfloor;

	public InventoryFix(ClientContext ctx) {
		super(ctx);
		this.Bankfloor = Areas.Bankfloor.getArea();
	}

	@Override
	public boolean activate() {
		return ctx.game.loggedIn() && !ctx.hud.opened(Hud.Window.BACKPACK)
				&& !Bankfloor.contains(ctx.players.local());
	}

	@Override
	public void execute() {
		if (!ctx.hud.open(Hud.Window.BACKPACK)) {
			ctx.hud.open(Hud.Window.BACKPACK);
		} else if (!ctx.hud.opened(Hud.Window.BACKPACK)) {
			ctx.hud.open(Hud.Window.BACKPACK);
		}
	}
}