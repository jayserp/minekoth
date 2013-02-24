package com.jayserp.minekoth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerLocationListener implements Listener {
	
	private Minekoth plugin;
	private ArrayList<String> inside;
	List<Location> cpBlocks = new ArrayList<Location>();
	
	//private List<String> playersInCaptureVicinity;  
	private ArrayList<String> redPlayersOnPoint;
	private ArrayList<String> bluePlayersOnPoint;

	private HashMap<Player, Object> inJump = new HashMap<Player, Object>();
	
	public PlayerLocationListener(Minekoth plugin) {
		this.plugin = plugin;
		inside = new ArrayList<String>();
		//captureListener = plugin.captureListener;
		
		//init playersInCaptureVicinity list
		//playersInCaptureVicinity = new ArrayList<String>();
		redPlayersOnPoint = new ArrayList<String>();
		bluePlayersOnPoint = new ArrayList<String>();
		
		//define capture point
		cpBlocks.add(new Location(plugin.getServer().getWorld("world"), -1, 3,  1));
		cpBlocks.add(new Location(plugin.getServer().getWorld("world"),  0, 3,  1));
		cpBlocks.add(new Location(plugin.getServer().getWorld("world"),  1, 3,  1));
		cpBlocks.add(new Location(plugin.getServer().getWorld("world"), -1, 3,  0));
		cpBlocks.add(new Location(plugin.getServer().getWorld("world"),  0, 3,  0));
		cpBlocks.add(new Location(plugin.getServer().getWorld("world"),  1, 3,  0));
		cpBlocks.add(new Location(plugin.getServer().getWorld("world"), -1, 3, -1));
		cpBlocks.add(new Location(plugin.getServer().getWorld("world"),  0, 3, -1));
		cpBlocks.add(new Location(plugin.getServer().getWorld("world"),  1, 3, -1));
	}
	
    @EventHandler
    public void onPlayerMoveEvent(PlayerMoveEvent event) {    	
    	Player player = event.getPlayer();	    	
    	PlayerDataClass playerGame = plugin.getPlayerHandler().findPlayer(player.getDisplayName());
    
        if(isInside(player.getLocation(), new Location(player.getWorld(), 4, 7, 3), 
        		new Location(player.getWorld(), -3, 2, -2)) == true) {
        	if(inside.contains(event.getPlayer().getName()) == false && 
        			plugin.getPlayerHandler().findPlayer(player.getDisplayName()) != null) {
        		
        		if (playerGame.getType().equals("spy") && playerGame.getDisguised() != null) {
        			return;
        		}
                addPlayerToPoint(playerGame);
            	//plugin.getServer().broadcastMessage(event.getPlayer().getName() + " entered the capture area.");
            	inside.add(event.getPlayer().getName());
           		//captureListener.updatePlayerVicinity(inside);            	
            	if (redPlayersOnPoint.size() > 0 && bluePlayersOnPoint.size() == 0) {
           			if (plugin.getGameManager().getCaptureOwner() != 1) {
           				plugin.getServer().broadcastMessage("The Control Point is being captured by " +
           					"RED.");
      					setPoint(DyeColor.RED);
           			}
          			plugin.getGameManager().registerCapture("red");
            	}  				
            	if (bluePlayersOnPoint.size() > 0 && redPlayersOnPoint.size() == 0) {
            		if (plugin.getGameManager().getCaptureOwner() != 2) {
            			plugin.getServer().broadcastMessage("The Control Point is being captured by " +
            				"BLUE.");
            			setPoint(DyeColor.BLUE);
            		}
            		plugin.getGameManager().registerCapture("blue");
            	}    				
            	if (bluePlayersOnPoint.size() == redPlayersOnPoint.size()) {
            		plugin.getServer().broadcastMessage("Defend the point!");
            	}	
            }             
        } else {       	
            if(inside.contains(event.getPlayer().getName())) {
            	//plugin.getServer().broadcastMessage(event.getPlayer().getName() + " left the capture area.");
            	removePlayerFromPoint(playerGame);
        				
        		if (plugin.getGameManager().getCaptureOwner() == 0) {
        			setPoint(DyeColor.GRAY);	
        		}	    				
        		if (plugin.getGameManager().getCaptureOwner() == 1) {
        			setPoint(DyeColor.RED);
        		}
        		if (plugin.getGameManager().getCaptureOwner() == 2) {
        	   		setPoint(DyeColor.BLUE);
        		}
            }           	
        }            
    	//plugin.getLogger().info("Number of people inside: " + String.valueOf(inside.size()));
    }
    
    public boolean isInside(Location pLoc, Location l1, Location l2) {
        int x1 = Math.min(l1.getBlockX(), l2.getBlockX());
        int y1 = Math.min(l1.getBlockY(), l2.getBlockY());
        int z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
        int x2 = Math.max(l1.getBlockX(), l2.getBlockX());
        int y2 = Math.max(l1.getBlockY(), l2.getBlockY());
        int z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
        l1 = new Location(l1.getWorld(), x1, y1, z1);
        l2 = new Location(l2.getWorld(), x2, y2, z2);
     
        return pLoc.getBlockX() >= l1.getBlockX()
            && pLoc.getBlockX() <= l2.getBlockX()
            && pLoc.getBlockY() >= l1.getBlockY()
            && pLoc.getBlockY() <= l2.getBlockY()
            && pLoc.getBlockZ() >= l1.getBlockZ()
            && pLoc.getBlockZ() <= l2.getBlockZ();
    }
  
    public void setPoint(DyeColor color) {
		for (int i = 0; i < cpBlocks.size(); i++) {
			Location location = cpBlocks.get(i);
			Block tempBlock = location.getBlock();
			tempBlock.setTypeId(35);
			tempBlock.setData(color.getData());
		}	
    }
    
    public List<String> getRedPlayersOnPoint() {
    	return redPlayersOnPoint;
    }
    
    public List<String> getBluePlayersOnPoint() {
    	return bluePlayersOnPoint;
    }
    
    public void addPlayerToPoint(PlayerDataClass player) {
    	if (player != null) {
			if (player.getTeam() == "red") {
				if (redPlayersOnPoint.size() == 0) {
					redPlayersOnPoint.add(player.getName());	
				} else {
					if (redPlayersOnPoint.contains(player.getName()) == false) {
						redPlayersOnPoint.add(player.getName());
					}
				}
			}
				
			if (player.getTeam() == "blue") {
				if (bluePlayersOnPoint.size() == 0) {
					bluePlayersOnPoint.add(player.getName());	
				} else {
					if (bluePlayersOnPoint.contains(player.getName()) == false) {
						bluePlayersOnPoint.add(player.getName());
					}
				}
			}
    	}
    }
    
    public void removePlayerFromPoint(PlayerDataClass player) {
    	if (player != null) {
    		//plugin.getServer().getLogger().info("removing player from point");
    		if (inside.contains(player.getName())) {
    			inside.remove(player.getName());
    		}
    		
    		if (redPlayersOnPoint.contains(player.getName()) || bluePlayersOnPoint.contains(player.getName())) {
    
				if (player.getTeam() == "red") {
					redPlayersOnPoint.remove(player.getName());
					if (redPlayersOnPoint.size() == 0) {
						plugin.getGameManager().deregisterCapture("red");
					}
				}
				
				if (player.getTeam() == "blue") {
					bluePlayersOnPoint.remove(player.getName());
					if (bluePlayersOnPoint.size() == 0) {
						plugin.getGameManager().deregisterCapture("blue");
					}
				}
    		}
    	}
    }   
    
    public void removeAllPlayersFromPoint() {   	
    	redPlayersOnPoint.clear();
    	bluePlayersOnPoint.clear();
		plugin.getGameManager().deregisterCapture("red");
		plugin.getGameManager().deregisterCapture("blue");
    	inside.clear();
    	setPoint(DyeColor.GRAY);	
    }
    
	public double getJumpFactor(Player player) {
		return 14;
	}
	
	public ArrayList<String> getInside() {
		return inside;
	}
}

