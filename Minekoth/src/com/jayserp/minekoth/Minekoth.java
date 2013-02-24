package com.jayserp.minekoth;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.kitteh.tag.TagAPI;

import com.jayserp.minekoth.PlayerDataClass;
import com.jayserp.minekoth.PlayerListeners;
import com.jayserp.minekoth.PlayerLocationListener;
import com.jayserp.minekoth.GameTimer;
import com.jayserp.spectate.SpectateAPI;
import com.jayserp.spectate.SpectateCommandExecutor;
import com.jayserp.spectate.SpectateListener;

/* DEV NOTES
 * 
 * TODO: 			teleporter + spawn points
 * 		 			spectate system
 * 		 			class select
 * 
 * IN PROGRESS:		GameManager (win/lose logic)
 * 					Team join listener
 * 					Capture listener (need to detect who is standing on the point)
 * 
 * 
 */

public final class Minekoth extends JavaPlugin {

	public int time;

	private Minekoth plugin = this;

	private PlayerListeners playerJoinListener;
	private PlayerLocationListener playerLocationListener;
	private BlockListener blockListener;	
	private PlayerHandler playerHandler;	
	private CustomTab customTab;
	
	public SpectateListener Listener;
	public SpectateCommandExecutor CommandExecutor;
	public FileConfiguration conf;	
	private GameTimer gameTimer;
	private GameManager gameManager;
	private Database sqlDb;
	private StatsHandler stats;
	private ItemDrops itemDrops;
	
	private List<PlayerDataClass> userList; //user list
	
    @Override
    public void onEnable(){
    	getLogger().info("Minekoth ENABLED");
    	    	
    	//register listeners
    	playerJoinListener = new PlayerListeners(this);
    	playerLocationListener = new PlayerLocationListener(this);
    	blockListener = new BlockListener(this);   	
    	Listener = new SpectateListener(this);
    	CommandExecutor = new SpectateCommandExecutor(this);
    	itemDrops = new ItemDrops(this);
    	
    	sqlDb = new Database(this);
    	stats = new StatsHandler(this);
    	
    	//setup gameManager
		gameManager = new GameManager(this);
		
		//setup playerHandler
		playerHandler = new PlayerHandler(this);
		
		//setup customTab
		customTab = new CustomTab(this);
    	
    	getServer().getPluginManager().registerEvents(playerJoinListener, this);
    	getServer().getPluginManager().registerEvents(playerLocationListener, this);
    	getServer().getPluginManager().registerEvents(blockListener, this);
		getServer().getPluginManager().registerEvents(Listener, this);
		getServer().getPluginManager().registerEvents(itemDrops, this);
    	
    	//setup user list
    	userList = new ArrayList<PlayerDataClass>();
    	//setup game timer
    	gameTimer = new GameTimer(this); 
    	
    	playerLocationListener.setPoint(DyeColor.GRAY);


		conf = getConfig();
		if (conf.get("canspectate Permission Enabled?") == null) {
			conf.set("canspectate Permission Enabled?", false);
		}
		if (conf.get("Disable commands while spectating?") == null) {
			conf.set("Disable commands while spectating?", false);
		}
		saveConfig();

		PluginDescriptionFile pdfFile = getDescription();
		System.out.println("[" + pdfFile.getName() + "] " + pdfFile.getName() + 
				" v" + pdfFile.getVersion() + " enabled!");
		Listener.updatePlayer();

		getCommand("spectate").setExecutor(CommandExecutor);
		getCommand("spec").setExecutor(CommandExecutor);
		
		playerHandler.clearInventories();
    }
   
    //setup commands
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
    	
    	String[] split = args;
    	
    	if(cmd.getName().equalsIgnoreCase("red")) {
    		if(sender instanceof Player == true) {
    			if (split.length > 1) {
    				sender.sendMessage("Too many arguments!");
    				return false;
    		    } 
    			if (split.length < 1) {
 		           sender.sendMessage("Select a class: /red <class>");
 		           return false;
 		        } 
    			if (split[0].equalsIgnoreCase("soldier") ||
    				split[0].equalsIgnoreCase("spy") ||
    				split[0].equalsIgnoreCase("sniper")) {
	       			getLogger().info(sender.getName() + " set to red " + split[0]);
	        		playerHandler.addRedPlayer(sender.getName(), split[0]);
	        		plugin.getCustomTab().updateTab();
	        		TagAPI.refreshPlayer((Player) sender);
    			} else {
    				sender.sendMessage("Classes available: soldier, spy, sniper");
    			}
			} else {
				sender.sendMessage("Sorry but this command is only for players");
			}
    		return true;
    	}
    	
    	if(cmd.getName().equalsIgnoreCase("blue")) { 
    		if(sender instanceof Player == true) {
    			if (split.length > 1) {
    				sender.sendMessage("Too many arguments!");
    				return false;
    		    } 
    			if (split.length < 1) {
 		           sender.sendMessage("Select a class: /blue <class>");
 		           return false;
 		        }
    			if (split[0].equalsIgnoreCase("soldier") ||
    				split[0].equalsIgnoreCase("spy") ||
    				split[0].equalsIgnoreCase("sniper")) {
	       			getLogger().info(sender.getName() + " set to blue " + split[0]);
	       			playerHandler.addBluePlayer(sender.getName(), split[0]);
	       			plugin.getCustomTab().updateTab();
	       			TagAPI.refreshPlayer((Player) sender);
    			} else {
    				sender.sendMessage("Classes available: soldier, spy, sniper");
    			}
    		} else {
    			sender.sendMessage("Sorry but this command is only for players");
    		}
    		return true;
    	}
    	
    	if(cmd.getName().equalsIgnoreCase("leave")) { 
    		if(sender instanceof Player == true) {
    			playerHandler.removePlayer(sender.getName());
    			gameManager.teleportToSpawn((Player) sender);
;    			plugin.getCustomTab().updateTab();
    		} else {
    			sender.sendMessage("Sorry but this command is only for players");
    		}
    		return true;
    	}
    	
    	if(cmd.getName().equalsIgnoreCase("start")) { 
    		gameManager.startGame();
    		return true;
    	}
    	
    	if(cmd.getName().equalsIgnoreCase("reset")) {
    			gameManager.resetGame();
    		return true;
    	}
    	
    	if(cmd.getName().equalsIgnoreCase("js")) {
    		//UsersDataClass data = database.getUser("jayserps");
			//plugin.getLogger().info(String.valueOf(data.getId()));
			return true;
    	}
    	
    	if(cmd.getName().equalsIgnoreCase("lp")) { 
    		String redList = null;
    		String blueList = null;
    		for (int i = 0; i < userList.size(); i++) {
    			PlayerDataClass e = userList.get(i);
    			if (e != null) {
    				if (e.getTeam() == "red") {
    					if (redList == null) {
    						redList = e.getName();
    					} else {
    						redList = redList + ", " + e.getName();
    					}
    				}
    				if (e.getTeam() == "blue") {
    					if (blueList == null) {
    						blueList = e.getName();
    					} else {
    						blueList = blueList + ", " + e.getName();
    					}
    				}
    			}
    		}
			plugin.getServer().broadcastMessage(ChatColor.RED + "Red Team : " + redList);
			plugin.getServer().broadcastMessage(ChatColor.BLUE + "Blue Team: " + blueList);
    		return true;
    	}
    	return false; 
    }
    
    @Override
    public void onDisable() {
    	getLogger().info("Minekoth DISABLED");
    	
    	sqlDb.closeConnection();
    	
		for (Player players : getServer().getOnlinePlayers()) {
			if (CommandExecutor.isSpectating.get(players.getName()) != null) {
				if (CommandExecutor.isSpectating.get(players.getName())) {
					players.sendMessage("You were forced to stop spectating because of a server reload.");
					if (CommandExecutor.isScanning.get(players.getName()) != null) {
						if (CommandExecutor.isScanning.get(players.getName())) {
							CommandExecutor.isScanning.put(players.getName(), false);
							getServer().getScheduler().cancelTask(CommandExecutor.taskId.get(players.getName()));
						}
					}
					SpectateAPI.spectateOff(players);
				}
			}
		}
		System.out.println("Spectate is disabled!");
    }
    
    public List<PlayerDataClass> getUserList() {
    	return userList;
    }
    
    public PlayerHandler getPlayerHandler() {
    	return playerHandler;
    }
    
    public CustomTab getCustomTab() {
    	return customTab;
    }
    
    public PlayerLocationListener getPlayerLocationListener() {
    	return playerLocationListener;
    }
    
    public SpectateAPI getSpectateApi() {
    	return CommandExecutor.spectateApi;
    }
    
    public GameManager getGameManager() {
    	return gameManager;
    }
    
    public GameTimer getGameTimer() {
    	return gameTimer;
    }
    
    public Database getSqlDb() {
    	return sqlDb;
    }
    
    public StatsHandler getStatsHandler() {
    	return stats;
    }
}