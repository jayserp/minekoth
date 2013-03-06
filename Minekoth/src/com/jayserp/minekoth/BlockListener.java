package com.jayserp.minekoth;

import java.util.List;

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
		
		List<PlayerDataClass> userList = plugin.getUserList();
		//iterate through player list (userList) and check if player is in game		
		if (userList != null) {
			for (int i = 0; i < userList.size(); i++) {
				PlayerDataClass tempPlayerData = userList.get(i);
				if (tempPlayerData.getName() == e.getPlayer().getDisplayName()) {
					//don't allow to break blocks
					e.setCancelled(true);
				} else {
					//allow them to break blocks
				}
			}			
		}
	}
}