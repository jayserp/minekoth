package com.jayserp.minekoth;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.jayserp.minekoth.PlayerDataClass;

public class BlockListener implements Listener {
	
	private Minekoth plugin;
	
	public BlockListener(Minekoth plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e){
		
		PlayerDataClass playerData = plugin.getPlayerHandler().findPlayer(e.getPlayer().getDisplayName());
		
		if (playerData == null) {
			e.setCancelled(true);
		} else {
			if (playerData.getTeam().equals("red") || playerData.getTeam().equals("blue")) {
				e.setCancelled(true);
				return;
			}
			if (playerData.getRank() > 90 && playerData.getTeam().equals("spec")) {
				plugin.getLogger().info("admin breaking");
				return;
			}
			e.setCancelled(true);
		}
	}
}