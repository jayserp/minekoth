package com.jayserp.minekoth;

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
				//System.out.println("Time: " + String.valueOf(time));
				//System.out.println(String.valueOf(plugin.userList.size()));
			}
		}, 20L, 20L);
	}
	
	public int getGameTimerId() {
		return id;
	}

}