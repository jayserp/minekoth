package com.jayserp.minekoth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.kitteh.tag.PlayerReceiveNameTagEvent;
import org.kitteh.tag.TagAPI;

public class PlayerListeners implements Listener {
	
	private Minekoth plugin;
	
	private HashMap<Player, Object> inJump = new HashMap<Player, Object>();
	
	public PlayerListeners(Minekoth plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent evt) {
		final Player p = evt.getPlayer();
		p.sendMessage("Welcome to jayserp's Minekoth. Type" + 
									" /red <class> or /blue <class> to join a team.");
		plugin.getGameManager().teleportToSpawn(p);
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				plugin.getCustomTab().updateTab();
			}
		}, 3);
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerQuit(PlayerQuitEvent evt) {	
		plugin.getPlayerLocationListener()
		  .removePlayerFromPoint(plugin.getPlayerHandler()
									   .findPlayer(evt.getPlayer()
											   			.getDisplayName()));
		
		if (plugin.getPlayerHandler().findPlayer(evt.getPlayer().getDisplayName()) != null) {
			plugin.getPlayerHandler().removePlayer(evt.getPlayer().getDisplayName());
			plugin.getLogger().info("removed " + evt.getPlayer().getDisplayName() + " from the game.");
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerKick(PlayerKickEvent evt) {
		plugin.getPlayerLocationListener()
		  .removePlayerFromPoint(plugin.getPlayerHandler()
									   .findPlayer(evt.getPlayer()
											   			.getDisplayName()));
		
		if (plugin.getPlayerHandler().findPlayer(evt.getPlayer().getDisplayName()) != null) {
			plugin.getPlayerHandler().removePlayer(evt.getPlayer().getDisplayName());
			plugin.getLogger().info("removed " + evt.getPlayer().getDisplayName() + " from the game.");
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onNameTag(PlayerReceiveNameTagEvent evt) {
		String target = evt.getNamedPlayer().getName();	
		String user = evt.getPlayer().getDisplayName();
		PlayerDataClass targetData = plugin.getPlayerHandler().findPlayer(target);
		PlayerDataClass userData = plugin.getPlayerHandler().findPlayer(user);
		
		if (targetData != null && userData != null) {
			if (targetData.getTeam() == userData.getTeam()) {
				if (userData.getTeam() == "red") {
					evt.setTag(ChatColor.RED + target);
				}
				if (userData.getTeam() == "blue") {
					evt.setTag(ChatColor.BLUE + target);
				}			
			}
			
			if (targetData.getTeam() != userData.getTeam()) {
				if (targetData.getDisguised() == null) {
					if (userData.getTeam() == "red") {
						evt.setTag("");
					}
					if (userData.getTeam() == "blue") {
						evt.setTag("");
					}
				}
				
				if (targetData.getDisguised() != null) {
					if (userData.getTeam() == "red") {
						evt.setTag(ChatColor.RED + targetData.getDisguised());
					}
					if (userData.getTeam() == "blue") {
						evt.setTag(ChatColor.BLUE + targetData.getDisguised());
					}
				}
			}	
		} else {
			evt.setTag("");
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onProjectileLaunch(ProjectileLaunchEvent evt){
	    if(evt.getEntity() instanceof Arrow){
	        Arrow arrow = (Arrow) evt.getEntity();
	        if(arrow.getShooter() instanceof Player){
	            Player shooter = (Player) arrow.getShooter();
	            PlayerDataClass shooterData = plugin.getPlayerHandler().findPlayer(shooter.getDisplayName());
	            if (shooterData != null) {	            	
	            	if (shooterData.getType().equalsIgnoreCase("sniper")) {
	            		evt.setCancelled(true);
	            		if (shooter.getInventory().contains(Material.ARROW)) {
	            			//shooter.getInventory().removeItem(new ItemStack (Material.ARROW, 1));
	            			//shooter.updateInventory();
	            			//ItemStack is = shooter.getInventory().getItem(Material.ARROW.getId());
	            			//is.setAmount(is.getAmount() - 1);
	            			//shooter.getInventory().setItem(Material.ARROW.getId(), is);   	
	        	            		//Location loc = shooter.getLocation(); 
	        	            		//loc.add(0,1,0);
	        	            Arrow sniperArrow = shooter.getWorld().spawnArrow(shooter.getEyeLocation(), 
	        	            shooter.getLocation().getDirection(), 0.6f, 1);
	        	            sniperArrow.setShooter(shooter);
	        	            sniperArrow.setBounce(false);
	        	            sniperArrow.setVelocity(shooter.getLocation().getDirection().normalize().multiply(8)); 
	            		}           		
	            	}
	            }
	        }
	    }
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInteract(PlayerInteractEvent event) {
	    final Player player = event.getPlayer();
	    PlayerDataClass playerData = plugin.getPlayerHandler().findPlayer(player.getDisplayName());
	    
	    if (playerData != null) {
		    if (player.getItemInHand().getType() == Material.BOW && 
		    		event.getAction() == Action.LEFT_CLICK_BLOCK &&
		    		(inJump.get(player) == null || inJump.get(player).equals(Boolean.FALSE)) &&
		    		playerData.getType().equalsIgnoreCase("soldier")) {
		    	
        		if (player.getInventory().contains(Material.ARROW)) {
        			player.getInventory().removeItem(new ItemStack (Material.ARROW, 1));
        			player.updateInventory();
			    	player.getLocation().getWorld().createExplosion(player.getLocation().getX(), 
			    													player.getLocation().getY(),
			    													player.getLocation().getZ(),
			    													0);
		
			    	double rotation = player.getLocation().getPitch();
			        	
			    	double jumpFactor = -(rotation / 20);
			    	double distanceFactor = -((90 - rotation) / 4);
	
					Vector newDirection = player.getVelocity();
					inJump.put(player, true);
					if (jumpFactor < -1) {
						newDirection.setY(newDirection.getY() * jumpFactor);
					}
					if (distanceFactor < -1) {
						newDirection.setX(newDirection.getX() * distanceFactor);
						newDirection.setZ(newDirection.getZ() * distanceFactor);
					}
					
					
					
					player.setVelocity(newDirection);
					player.damage(3);
		    		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
		    			public void run() {
		    				inJump.put(player, false);
		    			  }
		    			}, 10L);
        		}
		    }
		    
		    if (player.getItemInHand().getType() == Material.IRON_SWORD && 
		    		(event.getAction() == Action.RIGHT_CLICK_AIR ||
		    		event.getAction() == Action.RIGHT_CLICK_BLOCK) &&
		    		playerData.getType().equalsIgnoreCase("spy")) {
		
		        plugin.getServer().getWorld("world").playEffect(player.getLocation(), 
		        		Effect.EXTINGUISH, 40);
		    	
		    	if (playerData.getDisguised() == null) {
		    		if (playerData.getTeam() == "red") {
		    			if (plugin.getPlayerHandler().getBluePlayers().size() > 0) {
			    			plugin.getPlayerHandler().giveArmor(playerData.getName(), "blue");
		        			Random random = new Random();
		        			List<PlayerDataClass> temp = plugin.getPlayerHandler().getBluePlayers();
		        			int randomNumber = random.nextInt(temp.size());       			
			    			playerData.setDisguised(temp.get(randomNumber).getName());
			    			plugin.getPlayerLocationListener().getRedPlayersOnPoint().remove(playerData);
			    			TagAPI.refreshPlayer(plugin.getServer().getPlayer(playerData.getName()));
		    			} else {
			    			plugin.getPlayerHandler().giveArmor(playerData.getName(), "blue");
			    			playerData.setDisguised(playerData.getName());
			    			TagAPI.refreshPlayer(plugin.getServer().getPlayer(playerData.getName()));
		    			}
		    		}
		    		if (playerData.getTeam() == "blue") {
		    			if (plugin.getPlayerHandler().getRedPlayers().size() > 0) {
			    			plugin.getPlayerHandler().giveArmor(playerData.getName(), "red");
		        			Random random = new Random();
		        			List<PlayerDataClass> temp = plugin.getPlayerHandler().getRedPlayers();
		        			int randomNumber = random.nextInt(temp.size());       			
			    			playerData.setDisguised(temp.get(randomNumber).getName());
			    			TagAPI.refreshPlayer(plugin.getServer().getPlayer(playerData.getName()));
		    			} else {
			    			plugin.getPlayerHandler().giveArmor(playerData.getName(), "red");
			    			playerData.setDisguised(playerData.getName());
			    			TagAPI.refreshPlayer(plugin.getServer().getPlayer(playerData.getName()));
		    			}
		    		}
		    	} else {
		    		playerData.setDisguised(null);
		    		plugin.getPlayerHandler().giveArmor(playerData.getName(), playerData.getTeam());
		    		TagAPI.refreshPlayer(plugin.getServer().getPlayer(playerData.getName()));
		    	}
		    }
		    
		    if ((player.getItemInHand().getType() == Material.IRON_SWORD) && 
		    		(event.getAction() == Action.LEFT_CLICK_AIR ||
		    		event.getAction() == Action.LEFT_CLICK_BLOCK ||
		    		event.getAction() == Action.RIGHT_CLICK_AIR ||
		    		event.getAction() == Action.RIGHT_CLICK_BLOCK) &&
		    		playerData.getType().equalsIgnoreCase("spy") && 
		    		playerData.getDisguised() != null) {
	    		playerData.setDisguised(null);
	    		plugin.getPlayerHandler().giveArmor(playerData.getName(), playerData.getTeam());
	    		TagAPI.refreshPlayer(plugin.getServer().getPlayer(playerData.getName()));		    	
		    }
	    } else {
	    	return;
	    }
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent evt) {
		if (!(evt.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) evt.getEntity();
		PlayerDataClass pData = plugin.getPlayerHandler().findPlayer(p.getDisplayName());
		if (pData != null) {
			if (evt.getCause() == DamageCause.FALL) {
				Player player = (Player) evt.getEntity();
				if (plugin.getPlayerHandler().findPlayer(player.getDisplayName()) != null) {
					
					PlayerDataClass playerData = plugin.getPlayerHandler()
													   .findPlayer(player.getDisplayName());
	
					if (playerData.getType().equalsIgnoreCase("soldier")) {
						if(evt.getDamage() > 4) {
							evt.setDamage(4);
						}
					}
				}			
			}
		} else {
			evt.setCancelled(true);
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerDamage(EntityDamageByEntityEvent evt) {
		if (evt.getEntity() instanceof Player && evt.getDamager() instanceof Player) {
			Player player = (Player) evt.getEntity();
			PlayerDataClass playerData = plugin.getPlayerHandler()
											   .findPlayer(player.getDisplayName());
			Player attacker = (Player) evt.getDamager();
			PlayerDataClass attackerData = plugin.getPlayerHandler()
												 .findPlayer(attacker.getDisplayName());
			
			if (attackerData != null && playerData != null) {
				if (attackerData.getTeam() == playerData.getTeam()) {
					evt.setCancelled(true);
				}
			}
		}
		
		if (evt.getDamager() instanceof Arrow) {
			Arrow arrow = (Arrow) evt.getDamager();
			Player attacker = (Player) arrow.getShooter();
			PlayerDataClass attackerData = plugin.getPlayerHandler()
												 .findPlayer(attacker.getDisplayName());
			
			Player player = (Player) evt.getEntity();
			PlayerDataClass playerData = plugin.getPlayerHandler()
					   .findPlayer(player.getDisplayName());
			
			//plugin.getLogger().info(playerData.getName() + " " + attackerData.getName());
			if (attackerData != null && playerData != null) {
				if (attackerData.getTeam() == playerData.getTeam()) {
					evt.setCancelled(true);
				}
			}			
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerRespawn(final PlayerRespawnEvent evt) {		
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){			 
            public void run(){   
        		PlayerDataClass player = plugin.getPlayerHandler().findPlayer(evt.getPlayer().getDisplayName());
        		if (player != null) {
        			plugin.getLogger().info("Respawning: " + player.getName());
        			plugin.getPlayerHandler().spawnPlayer(player);
        		}
            }
        }, 3);
	}
	
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onDeath(PlayerDeathEvent evt) {
		evt.getDrops().clear();
		
		final Player player = evt.getEntity().getPlayer();
		final PlayerDataClass playerData = plugin.getPlayerHandler().findPlayer(player.getDisplayName());
		
		if (playerData != null) {		
			plugin.getPlayerLocationListener()
			  .removePlayerFromPoint(playerData);
			
			if (evt.getEntity().getKiller() != null) {	
				Player killer = evt.getEntity().getKiller();
				PlayerDataClass killerData = plugin.getPlayerHandler().findPlayer(killer.getDisplayName());
				//List<PlayerDataClass> redPlayers = plugin.getPlayerHandler().getRedPlayers();
				//List<PlayerDataClass> bluePlayers = plugin.getPlayerHandler().getBluePlayers();
	
		
				playerData.setDeaths(playerData.getDeaths() + 1);		
				if (killer != null) {
					killerData.setKills(killerData.getKills() + 1);
				}
				plugin.getLogger().info(player.getDisplayName() + " K/D: "
						+ playerData.getKills() + "/" + playerData.getDeaths());
			}
			if (playerData.getType().equals("spy") && playerData.getDisguised() != null) {
				playerData.setDisguised(null);
			}
			
	        if (player.isDead()) {
	            player.setHealth(20);
	        }
	        	
	    	plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
	    		public void run() {
	    			if (plugin.getPlayerHandler().findPlayer(player.getDisplayName()) != null) {
	    				plugin.getLogger().info("Respawning: " + player.getName());
	    				plugin.getPlayerHandler().spawnPlayer(playerData);
	    				TagAPI.refreshPlayer(player);
	    			}
	    		}
	    	}, 5L);
		} else {
			plugin.getGameManager().teleportToSpawn(player);
		}
	}       	
}
