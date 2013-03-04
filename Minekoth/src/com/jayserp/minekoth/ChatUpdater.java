package com.jayserp.minekoth;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatUpdater implements Listener {
	
	private Minekoth plugin;
	private boolean newMessage;
	
	List<ChatDataClass> chatEntries;
	
	public ChatUpdater(Minekoth plugin) {
		this.plugin = plugin;
		chatEntries = new ArrayList<ChatDataClass>();
	}

	public void addMessage(String message) {
		
	}
	
	public void update() {
		for (int i = 0; i < 9; i++) {
			plugin.getServer().broadcastMessage("");
		}			
		plugin.getServer().broadcastMessage("_____________________________________________________");
		plugin.getServer().broadcastMessage("Jayserp's Minekoth");
		plugin.getServer().broadcastMessage("Game Details");			
		plugin.getServer().broadcastMessage("");
		plugin.getServer().broadcastMessage("");
		plugin.getServer().broadcastMessage("");
		plugin.getServer().broadcastMessage("");
		plugin.getServer().broadcastMessage("");
		plugin.getServer().broadcastMessage("");
		plugin.getServer().broadcastMessage("");
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	private void onChat(AsyncPlayerChatEvent evt) {
		evt.setCancelled(true);
		
		PlayerDataClass playerData = plugin.getPlayerHandler().findPlayer(evt.getPlayer().getDisplayName());
		if (playerData != null) {
			if (playerData.getTeam() == "red") {
				plugin.getServer().broadcastMessage(ChatColor.RED + "<" + playerData.getName() + 
						"> " + ChatColor.WHITE + evt.getMessage());
			}
			
			if (playerData.getTeam() == "blue") {
				plugin.getServer().broadcastMessage(ChatColor.BLUE + "<" + playerData.getName() + 
						"> "  + ChatColor.WHITE + evt.getMessage());
			}
		} else {
			plugin.getServer().broadcastMessage(ChatColor.GRAY + "<" + evt.getPlayer().getDisplayName() + 
					"> " + ChatColor.WHITE + evt.getMessage());
		}
	}
	
	public Minekoth getPlugin() {
		return plugin;
	}
	
	public void setNewMessage(boolean result) {
		newMessage = result;
	}
	
	public boolean getNewMessage() {
		return newMessage;
	}
}