package org.powerbot.gui;

import java.awt.CheckboxMenuItem;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JCheckBoxMenuItem;

import org.powerbot.bot.AbstractBot;
import org.powerbot.bot.EventDispatcher;
import org.powerbot.bot.MessageLogger;
import org.powerbot.bot.TMousePosition;
import org.powerbot.bot.ViewMouse;
import org.powerbot.bot.ViewMouseTrails;
import org.powerbot.bot.rt6.DrawAbilities;
import org.powerbot.bot.rt6.DrawBoundaries;
import org.powerbot.bot.rt6.DrawGroundItems;
import org.powerbot.bot.rt6.DrawItems;
import org.powerbot.bot.rt6.DrawMobs;
import org.powerbot.bot.rt6.DrawObjects;
import org.powerbot.bot.rt6.DrawPlayers;
import org.powerbot.bot.rt6.DrawProjectiles;
import org.powerbot.bot.rt6.TCamera;
import org.powerbot.bot.rt6.TClientState;
import org.powerbot.bot.rt6.TDestination;
import org.powerbot.bot.rt6.TLocation;
import org.powerbot.bot.rt6.TMapBase;
import org.powerbot.bot.rt6.TMenu;
import org.powerbot.bot.rt6.TPlane;
import org.powerbot.bot.rt6.TPlayer;

final class RT6BotMenuView implements ActionListener {
	private final Map<String, Class<? extends EventListener>> map;
	private final BotChrome chrome;

	public RT6BotMenuView(final BotChrome chrome, final Menu menu) {
		this.chrome = chrome;
		final AbstractBot b = chrome.bot.get();

		final MenuItem widgetExplorer = new MenuItem(BotLocale.UTIL_WIDGET);
		widgetExplorer.addActionListener(this);
		menu.add(widgetExplorer);
		final MenuItem settingExplorer = new MenuItem(BotLocale.UTIL_VARPBITS);
		settingExplorer.addActionListener(this);
		menu.add(settingExplorer);

		final MenuItem boundingUtility = new MenuItem(BotLocale.UTIL_MODELING);
		boundingUtility.addActionListener(this);
		menu.add(boundingUtility);

		menu.addSeparator();

		map = new LinkedHashMap<String, Class<? extends EventListener>>();
		map.put(BotLocale.VIEW_LANDSCAPE, DrawBoundaries.class);
		map.put(BotLocale.VIEW_OBJECTS, DrawObjects.class);
		map.put(BotLocale.VIEW_PLAYERS, DrawPlayers.class);
		map.put(BotLocale.VIEW_PLAYER, TPlayer.class);
		map.put(BotLocale.VIEW_NPCS, DrawMobs.class);
		map.put(BotLocale.VIEW_PROJECTILES, DrawProjectiles.class);
		map.put(BotLocale.VIEW_GROUND_ITEMS, DrawGroundItems.class);
		map.put(BotLocale.VIEW_CLIENT_STATE, TClientState.class);
		map.put(BotLocale.VIEW_MENU, TMenu.class);
		map.put(BotLocale.VIEW_PLANE, TPlane.class);
		map.put(BotLocale.VIEW_MAP_BASE, TMapBase.class);
		map.put(BotLocale.VIEW_LOCATION, TLocation.class);
		map.put(BotLocale.VIEW_DESTINATION, TDestination.class);
		map.put(BotLocale.VIEW_MOUSE, ViewMouse.class);
		map.put(BotLocale.VIEW_MOUSE_TRAILS, ViewMouseTrails.class);
		map.put(BotLocale.VIEW_ITEMS, DrawItems.class);
		map.put(BotLocale.VIEW_ABILITIES, DrawAbilities.class);
		map.put(BotLocale.VIEW_MOUSE_POSITION, TMousePosition.class);
		map.put(BotLocale.VIEW_MESSAGES, MessageLogger.class);
		map.put(BotLocale.VIEW_CAMERA, TCamera.class);

		final List<String> items = new ArrayList<String>(map.size());
		items.add(BotLocale.VIEW_MOUSE);
		items.add(BotLocale.VIEW_MOUSE_TRAILS);
		items.add(BotLocale.VIEW_PLAYERS);
		items.add(BotLocale.VIEW_PLAYER);
		items.add(BotLocale.VIEW_NPCS);
		items.add(BotLocale.VIEW_GROUND_ITEMS);
		items.add(BotLocale.VIEW_PROJECTILES);
		items.add(BotLocale.VIEW_OBJECTS);
		items.add(BotLocale.VIEW_LANDSCAPE);
		items.add(BotLocale.VIEW_ITEMS);
		items.add(BotLocale.VIEW_ABILITIES);
		items.add(BotLocale.SEPARATOR);
		items.add(BotLocale.VIEW_CLIENT_STATE);
		items.add(BotLocale.VIEW_CAMERA);
		items.add(BotLocale.VIEW_MENU);
		items.add(BotLocale.VIEW_PLANE);
		items.add(BotLocale.VIEW_MAP_BASE);
		items.add(BotLocale.VIEW_LOCATION);
		items.add(BotLocale.VIEW_DESTINATION);
		items.add(BotLocale.VIEW_MOUSE_POSITION);
		items.add(BotLocale.SEPARATOR);
		items.add(BotLocale.VIEW_MESSAGES);

		final EventDispatcher d = b.dispatcher;

		boolean selectedAll = true;

		for (final String key : items) {
			if (key.equals(BotLocale.SEPARATOR)) {
				continue;
			}
			if (!d.contains(map.get(key))) {
				selectedAll = false;
				break;
			}
		}

		final CheckboxMenuItem all = new CheckboxMenuItem(BotLocale.VIEW_ALL, selectedAll);
		all.addActionListener(this);
		menu.add(all);
		menu.addSeparator();

		for (final String key : items) {
			if (key.equals(BotLocale.SEPARATOR)) {
				menu.addSeparator();
				continue;
			}
			final CheckboxMenuItem item = new CheckboxMenuItem(key, d.contains(map.get(key)));
			item.addActionListener(this);
			menu.add(item);
		}
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		setView(DrawGroundItems.class, true);
		final String s = e.getActionCommand();
		if (s.equals(BotLocale.UTIL_WIDGET)) {
			RT6WidgetExplorer.getInstance(chrome).display();
		} else if (s.equals(BotLocale.UTIL_VARPBITS)) {
			BotSettingExplorer.getInstance(chrome).display();
		} else if (s.equals(BotLocale.UTIL_MODELING)) {
			RT6BotBoundingUtility.getInstance(chrome).setVisible(true);
		} else {
			final JCheckBoxMenuItem item = (JCheckBoxMenuItem) e.getSource();
			item.setSelected(!item.isSelected());
			if (item.getText().equals(BotLocale.VIEW_ALL)) {
				for (final Entry<String, Class<? extends EventListener>> entry : map.entrySet()) {
					setView(entry.getValue(), item.isSelected());
				}
			} else {
				setView(map.get(item.getText()), item.isSelected());
			}
		}
	}

	private void setView(final Class<? extends EventListener> e, final boolean s) {
		final AbstractBot b = chrome.bot.get();

		if (b == null) {
			return;
		}

		final EventDispatcher d = b.dispatcher;
		final boolean c = d.contains(e);

		if (s && !c) {
			EventListener l = null;
			final Object a = b.ctx;

			for (final Class<?> clazz : new Class[]{a.getClass(), a.getClass().getSuperclass()}) {
				try {
					l = e.getDeclaredConstructor(clazz).newInstance(a);
				} catch (final Exception ignored) {
				}
			}

			if (l != null) {
				d.add(l);
			}
		} else if (!s && c) {
			for (final EventListener l : d) {
				if (l.getClass().isAssignableFrom(e)) {
					d.remove(l);
				}
			}
		}
	}
}
