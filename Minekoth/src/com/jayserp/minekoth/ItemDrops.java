package com.jayserp.minekoth;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class ItemDrops implements Listener {
	
	private Minekoth plugin;
	
	public ItemDrops(Minekoth plugin) {
		Location ammoLocation = new Location(plugin.getServer().getWorld("world"), 28, 4, 0);
		this.plugin = plugin;
		//spawnAmmoPack(ammoLocation);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void noPickup(PlayerPickupItemEvent evt){
        if (evt.isCancelled()) {
            return;
        }
        
        /*if (evt.getItem().hasMetadata("mkhealth")) {
            if (evt.getPlayer().getHealth() == 20) {
         	   evt.setCancelled(true);
     		}
        }
       
        if (evt.getItem().hasMetadata("mkammo")) { 
        	if (evt.getPlayer().getInventory().contains(Material.ARROW)) {
        		ItemStack playerItems[] = evt.getPlayer().getInventory().getContents();
        		for (int i = 0; i < playerItems.length; i++){
        			plugin.getLogger().info("iterating: "+ String.valueOf(i) + "/" +
        					String.valueOf(playerItems.length));
        			if (playerItems[i].getType() == Material.ARROW &&
        					playerItems[i].getAmount() < 25) {
        				plugin.getLogger().info("matched arrows on: " + String.valueOf(i));
        				if (playerItems[i].getAmount() < 25) { 
        					playerItems[i].setAmount(25);
        					return;
        				}
        			}
        		}
        		evt.getPlayer().updateInventory();
        		evt.setCancelled(true);
        		evt.getItem().remove();
        		
        	} else {
        		ItemStack ammo = new ItemStack(Material.ARROW, 25);
        		evt.getPlayer().getInventory().addItem(ammo);
        		evt.getPlayer().updateInventory();
        		evt.setCancelled(true);
        		evt.getItem().remove();
        	}
        }*/
        
        /*if (evt.getItem().getItemStack().getType() == Material.ARROW) {
    		ItemStack playerItems[] = evt.getPlayer().getInventory().getContents();
    		for (int i = 0; i < playerItems.length; i++){
    			if(playerItems[i].getType() == Material.ARROW)
    				playerItems[i].setAmount(25);
    		}	
        }*/
    }
	
	public void spawnHealthPack(Location loc) {
		
		Potion potionType = new Potion(PotionType.INSTANT_HEAL, 1, false, false);
		ItemStack potion = potionType.toItemStack(1);
		Item item = plugin.getServer().getWorld("world").dropItem(loc, potion);
		item.setTicksLived(1000000);
		item.setMetadata("mkhealth", new FixedMetadataValue(plugin, true));
	}
	
	public void spawnAmmoPack(Location loc) {
		ItemStack ammo = new ItemStack(Material.IRON_BLOCK, 1);
		Item item = plugin.getServer().getWorld("world").dropItem(loc, ammo);
		item.setTicksLived(1000000);
		item.setMetadata("mkammo", new FixedMetadataValue(plugin, true));
	}
}