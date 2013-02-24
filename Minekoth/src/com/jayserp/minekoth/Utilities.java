package com.jayserp.minekoth;

import org.bukkit.inventory.ItemStack;

//this contains utility functions that are shared among the different classes
public class Utilities {
	
	private Minekoth plugin;
	
	private int countdownTimer;
	private int countdownTaskID;
	
	public Utilities(Minekoth plugin) {
		this.plugin = plugin;
	}
	
	/*
	 * This function creates a countdown timer
	 * Accepts:
	 * string, int, int
	 * text to display, time to countdown from, time to countdown to
	 */
	public void spawnCountdown(final String message, final int fromTime, final int toTime) {
		
		countdownTimer = fromTime;
		
		countdownTaskID = plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable() {
			public void run() {
								
				plugin.getServer().broadcastMessage(message + countdownTimer);
				countdownTimer--;
				
				if (countdownTimer == toTime) {
					plugin.getServer().getScheduler().cancelTask(countdownTaskID);
				}
			}
		}, 20L, 20L);
	}
	

}