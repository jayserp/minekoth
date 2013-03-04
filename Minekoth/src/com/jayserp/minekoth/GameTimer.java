package com.jayserp.minekoth;

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTimer {

	private Minekoth plugin;
	public int id;
	
	public int time;
	
	public GameTimer(final Minekoth plugin) {
		this.plugin = plugin;
		
		id = plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable() {
			public void run() {
				time++;
			
				//Poll gameManager for game management
				//return true if everything is fine
				//pass time to allow capture tracking
				plugin.getGameManager().checkStatus(time);
				//plugin.getChatUpdater().update();
				//System.out.println("Time: " + String.valueOf(time));
				//System.out.println(String.valueOf(plugin.userList.size()));
			}
		}, 20L, 20L);
		
		plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin,
			new BukkitRunnable() {
				public void run() {
					for (ArrowDataClass e : plugin.getArrowsFired()) {
						//e.setVelocity(e.getVelocity().setY(e.getVelocity().getY()));
						/*plugin.getLogger().info("x/y/z: " + String.valueOf(e.getVelocity().getX()) + "/" +
															String.valueOf(e.getVelocity().getY()) + "/" +
															String.valueOf(e.getVelocity().getZ()));*/
						
						e.getArrow().setVelocity(e.getVector());
															

					}
				}		
		}, 0, 1);
	}
	
	public int getGameTimerId() {
		return id;
	}

}