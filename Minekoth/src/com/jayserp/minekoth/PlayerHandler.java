package com.jayserp.minekoth;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.kitteh.tag.TagAPI;

public class PlayerHandler {
	
	private Minekoth plugin;
	
	public PlayerHandler(Minekoth plugin) {
		this.plugin = plugin;
	}
	
	public void addSpecPlayer (String playerName, int rank) {
		PlayerDataClass player = findPlayer(playerName);
		
		if (player == null) {		
			PlayerDataClass e = new PlayerDataClass();
			e.setName(playerName);
			e.setTeam("spec");
			e.setScore(0);
			e.setType(null);
			e.setRank(rank);
			plugin.getUserList().add(e);
			plugin.getGameManager().teleportToSpawn(plugin.getServer().getPlayer(playerName));
		} else {
			plugin.getUserList().get(plugin.getPlayerHandler().findPlayerId(playerName)).setRank(rank);
			plugin.getUserList().get(plugin.getPlayerHandler().findPlayerId(playerName)).setTeam("spec");
			plugin.getGameManager().teleportToSpawn(plugin.getServer().getPlayer(playerName));
		}
	}
	
	public void addSpecPlayer (String playerName) {
		PlayerDataClass player = findPlayer(playerName);
		
		if (player == null) {
			PlayerDataClass e = new PlayerDataClass();
			e.setName(playerName);
			e.setTeam("spec");
			e.setScore(0);
			e.setType(null);
			if (e.getRank() < 0) {
				e.setRank(0);
			}
			plugin.getUserList().add(e);
			plugin.getGameManager().teleportToSpawn(plugin.getServer().getPlayer(playerName));
		} else {
			plugin.getPlayerLocationListener()
			  .removePlayerFromPoint(player);
			plugin.getGameManager().teleportToSpawn(plugin.getServer().getPlayer(playerName));
			plugin.getServer().broadcastMessage(ChatColor.YELLOW + playerName + ChatColor.WHITE +
					" is now spectating.");
			plugin.getUserList().get(findPlayerId(playerName)).setTeam("spec");
			plugin.getUserList().get(findPlayerId(playerName)).setType(null);
		}
	}
	
	public void addRedPlayer(String playerName, String type) {
		//add a red player to the user list.	
		PlayerDataClass player = findPlayer(playerName);	
		if (player.getTeam() == "spec") {
			if (getRedPlayers().size() <= getBluePlayers().size()) {
				plugin.getUserList().get(findPlayerId(playerName)).setTeam("red");
				plugin.getUserList().get(findPlayerId(playerName)).setType(type);
				
		    	plugin.getServer().broadcastMessage(ChatColor.YELLOW + playerName + ChatColor.WHITE +
					" joined the " + ChatColor.RED + "RED" + ChatColor.WHITE + 
					" team.");
		    		
				if (plugin.getGameManager().hasGameStarted() == true) {
					spawnPlayer(findPlayer(playerName));
				}
			} else {
				plugin.getServer().getPlayer(playerName).sendMessage("The red team is full, " +
						"please join blue team.");
			}				
		} else {
			if (player.getTeam() == "blue" || !player.getType().equals(type)) {
				if (player.getTeam().equals("red")) {
					plugin.getPlayerLocationListener()
					  .removePlayerFromPoint(player);
					plugin.getUserList().get(findPlayerId(playerName)).setType(type);
					plugin.getServer().getPlayer(playerName).sendMessage("Changed class to: " + type);						
					if (plugin.getGameManager().hasGameStarted() == true) {
						spawnPlayer(findPlayer(playerName));
					}
				}

				if (getRedPlayers().size() < getBluePlayers().size()) {
					plugin.getPlayerLocationListener()
					  .removePlayerFromPoint(player);
					plugin.getUserList().get(findPlayerId(playerName)).setTeam("red");
					plugin.getUserList().get(findPlayerId(playerName)).setType(type);
			   		plugin.getServer().broadcastMessage(ChatColor.YELLOW + playerName + ChatColor.WHITE +
							" changed to the " + ChatColor.RED + "RED" + ChatColor.WHITE + 
							" team.");	 
		    		
					if (plugin.getGameManager().hasGameStarted() == true) {
						spawnPlayer(findPlayer(playerName));
					}
				} else {
					if (!player.getTeam().equals("red")) {
						plugin.getServer().getPlayer(playerName).sendMessage("Cannot change team: " +
								"the red team is full.");
					}
				}
			} else {
				plugin.getServer().getPlayer(playerName).sendMessage("You have already joined " +
						"the red team.");
			}
		}
		refreshTagsFor(plugin.getServer().getPlayer(playerName));
	}
	
	public void addBluePlayer(String playerName, String type) {
		//add a blue player to the user list.
		PlayerDataClass player = findPlayer(playerName);
		
		if (player.getTeam() == "spec") {

			if (getBluePlayers().size() <= getRedPlayers().size()) {
				/*giveArmor(playerName, "blue");
				giveWeapons(playerName);*/
				//plugin.getUserList().add(e);
				
				plugin.getUserList().get(findPlayerId(playerName)).setTeam("blue");
				plugin.getUserList().get(findPlayerId(playerName)).setType(type);
				
	    		plugin.getServer().broadcastMessage(ChatColor.YELLOW + playerName + ChatColor.WHITE +
						" joined the " + ChatColor.BLUE + "BLUE" + ChatColor.WHITE + 
						" team.");
				if (plugin.getGameManager().hasGameStarted() == true) {
					spawnPlayer(findPlayer(playerName));
				}
			} else {
				plugin.getServer().getPlayer(playerName).sendMessage("The blue team is full, " +
						"please join red team.");
			}

		} else {

			if (player.getTeam() == "red" || !player.getType().equals(type)) {
				if (player.getTeam().equals("blue")) {
					plugin.getPlayerLocationListener()
					  .removePlayerFromPoint(player);
					plugin.getUserList().get(findPlayerId(playerName)).setType(type);
					plugin.getServer().getPlayer(playerName).sendMessage("Changed class to: " + type);
					if (plugin.getGameManager().hasGameStarted() == true) {
						spawnPlayer(findPlayer(playerName));
					}
				}
				if (player.getTeam() == "red") {
					if (getBluePlayers().size() < getRedPlayers().size()) {
						/*giveArmor(playerName, "blue");
						giveWeapons(playerName);*/
						plugin.getPlayerLocationListener()
						  .removePlayerFromPoint(player);
						plugin.getUserList().get(findPlayerId(playerName)).setTeam("blue");
						plugin.getUserList().get(findPlayerId(playerName)).setType(type);
			    		plugin.getServer().broadcastMessage(ChatColor.YELLOW + playerName + ChatColor.WHITE +
							" changed to the " + ChatColor.BLUE + "BLUE" + ChatColor.WHITE + 
							" team.");	 
						if (plugin.getGameManager().hasGameStarted() == true) {
							spawnPlayer(findPlayer(playerName));
						}
					} else {
						if (!player.getTeam().equals("blue")) {
							plugin.getServer().getPlayer(playerName).sendMessage("Cannot change team: " +
									"the blue team is full.");
						}
					}
				}
			} else {
				plugin.getServer().getPlayer(playerName).sendMessage("You have already joined " +
						"the blue team.");
			}
		}
		refreshTagsFor(plugin.getServer().getPlayer(playerName));
	}
	
	public List<PlayerDataClass> getRedPlayers() {
		List<PlayerDataClass> returnList = new ArrayList<PlayerDataClass>();
		
		for (int i = 0; i < plugin.getUserList().size(); i++) {
			PlayerDataClass player = plugin.getUserList().get(i);
			if (player.getTeam() == "red") {					
				returnList.add(player);
			}		
		}
		return returnList;
	}
	
	public List<PlayerDataClass> getBluePlayers() {
		List<PlayerDataClass> returnList = new ArrayList<PlayerDataClass>();

		for (int i = 0; i < plugin.getUserList().size(); i++) {
			PlayerDataClass player = plugin.getUserList().get(i);
			if (player.getTeam() == "blue") {
				returnList.add(player);
			}
		}
		return returnList;
	}
	
	public void removePlayer(String playerName) {		
		for (int i = 0; i < plugin.getUserList().size(); i++) {
			PlayerDataClass temp = plugin.getUserList().get(i);
			
			if (temp.getName() == playerName) {
				plugin.getServer().broadcastMessage(playerName + " has left the game.");
				plugin.getServer().getPlayer(playerName).getInventory().setArmorContents(null);
				plugin.getServer().getPlayer(playerName).getInventory().clear();
				plugin.getUserList().remove(i);
				plugin.getCustomTab().updateTab();
			}
		}
	}
	
	public void clearInventories() {
		for (int i = 0; i < plugin.getServer().getOnlinePlayers().length; i++) {
			Player player = plugin.getServer().getOnlinePlayers()[i];
				player.getInventory().clear();
		}
	}
	
	public void removeAllPlayers() {		
		for (int i = 0; i < plugin.getUserList().size(); i++) {
				plugin.getUserList().remove(i);
		}
	}
	
	public PlayerDataClass findPlayer(String playerName) {
		if (plugin.getUserList() != null) {
    		for (int i = 0; i < plugin.getUserList().size(); i++) {
    			PlayerDataClass temp = plugin.getUserList().get(i);
    			
    			if (temp.getName() == playerName) {
    				return temp;
    			}
    		}
		} 
		return null;
	}
	
	public int findPlayerId(String playerName) {
		if (plugin.getUserList() != null) {
    		for (int i = 0; i < plugin.getUserList().size(); i++) {
    			PlayerDataClass temp = plugin.getUserList().get(i);
    			
    			if (temp.getName() == playerName) {
    				return i;
    			}
    		}
		} 
		return -1;
	}
	
	public void spawnPlayer(PlayerDataClass player) {
		if (player != null) {
		
			player.setDisguised(null);
			plugin.getPlayerListener().setUnscoped(plugin.getServer().getPlayer(player.getName()), player);
			
			if(player.getType().equals("scout")) {
				plugin.getServer().getPlayer(player.getName()).setWalkSpeed((float) 0.38);
			} else {
				plugin.getServer().getPlayer(player.getName()).setWalkSpeed((float) 0.25);
			}
			
			if (player.getTeam() == "red") {
				giveWeapons(player.getName(), player.getType());
				giveArmor(player.getName(), "red");
				teleport(player.getName(), "red");
			}
		
			if (player.getTeam() == "blue") {
				giveWeapons(player.getName(), player.getType());
				giveArmor(player.getName(), "blue");
				teleport(player.getName(), "blue");
			}
		}
	}
	
	public void teleport(String player, String team) {
		Location location = null;
		if (team == "red") {
			//location = new Location(plugin.getServer().getWorld("world"), 49,5,0);
			location = new Location(plugin.getServer().getWorld("world"), 1181,54,165);
		} 
		if (team == "blue") {
			//location = new Location(plugin.getServer().getWorld("world"), -49,5,0);
			location = new Location(plugin.getServer().getWorld("world"), 1182,54,252);
		}			
		plugin.getServer().getPlayer(player).teleport(location);
	}
	
	public void giveWeapons(String player, String playerType) {
		
		List<ItemStack> inv = new ArrayList<ItemStack>();
		if (playerType.equals("demo")) {					
			inv.add(new ItemStack(Material.IRON_SWORD, 1));
			inv.add(new ItemStack(Material.getMaterial(356), 1));
			inv.add(new ItemStack(Material.ARROW, 32));	
		} else {				
			inv.add(new ItemStack(Material.IRON_SWORD, 1));
			inv.add(new ItemStack(Material.BOW, 1));
			inv.add(new ItemStack(Material.ARROW, 32));	
		}
		
		ItemStack[] newStack = inv.toArray(new ItemStack[inv.size()]);
		
		plugin.getServer().getPlayer(player).getInventory().clear();
		plugin.getServer()
			.getPlayer(player)
			.getInventory()
			.addItem(newStack);
		plugin.getServer()
		.getPlayer(player)
		.updateInventory();	
	}
	
	public void giveArmor(String player, String team) {
		ItemStack helmet = new ItemStack(Material.LEATHER_HELMET, 1);
		LeatherArmorMeta lamh = (LeatherArmorMeta) helmet.getItemMeta();
		if (team == "red") {
			lamh.setColor(Color.RED);
		}
		if (team == "blue") {
			lamh.setColor(Color.BLUE);
		}
		helmet.setItemMeta(lamh);
		plugin.getServer().getPlayer(player).getInventory().setHelmet(helmet);
		
		ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
		LeatherArmorMeta lamc = (LeatherArmorMeta) chestplate.getItemMeta();
		if (team == "red") {
			lamc.setColor(Color.RED);
		}
		if (team == "blue") {
			lamc.setColor(Color.BLUE);
		}
		chestplate.setItemMeta(lamc);
		plugin.getServer().getPlayer(player).getInventory().setChestplate(chestplate);
		
		ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS, 1);
		LeatherArmorMeta laml = (LeatherArmorMeta) leggings.getItemMeta();
		if (team == "red") {
			laml.setColor(Color.RED);
		}
		if (team == "blue") {
			laml.setColor(Color.BLUE);
		}
		leggings.setItemMeta(laml);
		plugin.getServer().getPlayer(player).getInventory().setLeggings(leggings);
		
		ItemStack boots = new ItemStack(Material.LEATHER_BOOTS, 1);
		LeatherArmorMeta lamb = (LeatherArmorMeta) boots.getItemMeta();
		if (team == "red") {
			lamb.setColor(Color.RED);
		}
		if (team == "blue") {
			lamb.setColor(Color.BLUE);
		}
		boots.setItemMeta(lamb);
		plugin.getServer().getPlayer(player).getInventory().setBoots(boots);
	}
	
	public void refreshTagsFor(Player player) {
		for (int i = 0; i < plugin.getServer().getOnlinePlayers().length; i++) {
			Player updatedPlayer = plugin.getServer().getOnlinePlayers()[i];
			if (updatedPlayer != player) {
				TagAPI.refreshPlayer(updatedPlayer, player);
			}
		}
	}
	
}