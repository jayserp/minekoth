package com.jayserp.minekoth;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerHandler implements Listener {
	
	private Minekoth plugin;
	
	public ServerHandler(Minekoth plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	private void displayPlayers(ServerListPingEvent evt) {
		evt.setMaxPlayers(24);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	private void checkUser(AsyncPlayerPreLoginEvent evt) {
		if (plugin.getServer().getOnlinePlayers().length >= 24) {
			plugin.getLogger().info("people on server >= 24, retrieving user: " + evt.getName());
			UsersDataClass user = plugin.getSqlDb().getUser(evt.getName());
			if (user != null) {
				plugin.getLogger().info("rank: " + user.getRank());
				if (user.getRank() > 0) {
					plugin.getLogger().info("allowing: " + evt.getName() + " " + user.getRank());
					return;
				} else {
					plugin.getLogger().info("disallowing: " + evt.getName() + " " + user.getRank());
					evt.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "Server Full. To ensure a guaranteed slot, " +
							"donate at www.minekoth.com");
					return;
				}
			} else {
				plugin.getLogger().info("disallowing: " + evt.getName());
				evt.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "Server Full. To ensure a guaranteed slot, " +
						"donate at www.minekoth.com");
			}
			
		} else {
			return;
		}
	}
	
	public Minekoth getPlugin() {
		return plugin;
	}
}