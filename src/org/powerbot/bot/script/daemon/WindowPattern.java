package org.powerbot.bot.script.daemon;

import java.util.Arrays;

import org.powerbot.bot.script.Antipattern;
import org.powerbot.script.rs3.tools.Hud;
import org.powerbot.script.rs3.tools.MethodContext;
import org.powerbot.script.util.Random;

public class WindowPattern extends Antipattern {
	public WindowPattern(final MethodContext factory) {
		super(factory);
		freq.set(freq.get() / 2);
	}

	@Override
	public void run() {
		final boolean a = isAggressive();

		int d = 0;
		Hud.Window[] windows = Hud.Window.values();
		for (int i = 0; i < windows.length; i++) {
			final Hud.Window w = windows[i];
			if (!ctx.hud.isOpen(w) || ctx.hud.isVisible(w)) {
				continue;
			}
			if (w.getMenu() == Hud.Menu.NONE) {
				continue;
			}

			windows[d++] = w;
		}
		windows = Arrays.copyOf(windows, d);
		if (windows.length < 1) {
			return;
		}

		for (int i = a ? Random.nextInt(0, 3) : 2; i < 4; i++) {
			final Hud.Window w = windows[Random.nextInt(0, windows.length)];
			if (!ctx.hud.view(w)) {
				break;
			}

			if (a) {
				sleep(Random.nextInt(0, Random.nextInt(1000, 2000)));
			}
		}
	}
}