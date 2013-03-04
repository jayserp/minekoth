package com.jayserp.minekoth;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;
import org.bukkit.util.Vector;

public class ItemDrops implements Listener {
	
	private Minekoth plugin;
	private List<Location> ammoPacks;
	
	public ItemDrops(Minekoth plugin) {
		this.plugin = plugin;
		ammoPacks = new ArrayList<Location>();
		ammoPacks.add(new Location(plugin.getServer().getWorld("world"), 28, 5, -1));
		ammoPacks.add(new Location(plugin.getServer().getWorld("world"), -28, 5, 2));
		
		for (int i = 0; i < ammoPacks.size(); i++) {
			if (ammoPacks.get(i) != null) {
				spawnAmmoPack(ammoPacks.get(i));
			}
		}		
	}
	
	/*@EventHandler(priority = EventPriority.HIGHEST)
	public void noDespawn(ItemDespawnEvent evt) {
		if (evt.getEntity().hasMetadata("mkammo")) {
			evt.getEntity().setTicksLived(1);
			evt.setCancelled(true);
		}
	}*/
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void noPickup(PlayerPickupItemEvent evt){
        if (evt.isCancelled()) {
            return;
        }
        
        if (evt.getItem().hasMetadata("mkhealth")) {
            if (evt.getPlayer().getHealth() == 20) {
         	   evt.setCancelled(true);
     		}
        }
       
        if (evt.getItem().hasMetadata("mkammo")) { 
        	Location ammoLoc = evt.getItem().getLocation();
        	if (evt.getPlayer().getInventory().contains(Material.ARROW)) {
        		ItemStack playerItems[] = evt.getPlayer().getInventory().getContents();
        		for (int i = 0; i < playerItems.length; i++){
        			ItemStack item = playerItems[i];
        			if (item != null) {
	        			if (item.getType() == Material.ARROW &&
	        					item.getAmount() < 25) {
	        				evt.getItem().remove();  
	        				evt.setCancelled(true);
	        				spawnAmmoPack(ammoLoc, 5);
	        				//plugin.getLogger().info("matched arrows on: " + String.valueOf(i));
	        				if (item.getAmount() < 25) { 
	        					item.setAmount(25); 					        					   					
	        				}
	        				return;
	        			}
        			} else {
        				evt.setCancelled(true);
        				return;
        			}
        		}
        		//evt.getPlayer().updateInventory();
        		//evt.setCancelled(true);
        		//evt.getItem().remove();
        		
        	} else {
        		ItemStack ammo = new ItemStack(Material.ARROW, 25);
        		evt.getPlayer().getInventory().addItem(ammo);
        		evt.getPlayer().updateInventory();
        		evt.setCancelled(true);
        		evt.getItem().remove();
        		spawnAmmoPack(ammoLoc, 5);
        		return;
        	}
        }
    }
	
	public void spawnHealthPack(Location loc) {		
		Potion potionType = new Potion(PotionType.INSTANT_HEAL, 1, false, false);
		ItemStack potion = potionType.toItemStack(1);
		Item item = plugin.getServer().getWorld("world").dropItem(loc, potion);
		item.setMetadata("mkhealth", new FixedMetadataValue(plugin, true));
	}
	
	public void spawnAmmoPack(final Location loc, int time) {
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				spawnAmmoPack(loc);
			}
		}, time*20);
	}
	
	public void spawnAmmoPack(Location loc) {
		ItemStack ammo = new ItemStack(Material.IRON_BLOCK, 1);
		Item item = plugin.getServer()
						  .getWorld("world")
						  .dropItem(loc.add(+0.5, +1, +0.5), ammo);
		item.setVelocity(new Vector(0, 0, 0));
		item.setMetadata("mkammo", new FixedMetadataValue(plugin, true));
	}
}