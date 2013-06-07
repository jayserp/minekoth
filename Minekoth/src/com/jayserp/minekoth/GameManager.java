package com.jayserp.minekoth;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/*
 * This is the Game Manager. It deals with the game logic as well as
 * tracking user scores and keeping track of the control point state.
 */

public class GameManager {

	//check if game has started or not
	private boolean hasGameStarted = false;
	private boolean hasGameFinished = false;
	private boolean hasTeamWon = false;
	private boolean startGame = false;
	
	private int timeToWait = 5;
	private int timeToEndRed = 181; //181
	private int timeToEndBlue = 181; //181
	
	private int redScore = 0;
	private int blueScore = 0;
	private String winner;
		
	//see which team is capturing
	private boolean redIsCapturing = false;
	private boolean blueIsCapturing = false;	
	//detects who owns the plate.. 0 - noone, 1 - red, 2 - blue
	private int captureOwner = 0;
	//capture time before team can take point (seconds - 1)
	private int captureTime = 6;
	//capture timer tracks capture time
	private int captureTimer = 0;
	
	private Minekoth plugin;
	
	public GameManager(Minekoth plugin) {
		this.plugin = plugin;
		captureTimer = captureTime;
	}

	/**
	 * This functions deals with checking game logic every second to determine 
	 * what status
	 * 
	 * @param[in]	time	integer time value that should increment every second.
	 * @return 				returns true if game is in progress and false if game has ended.
	 */
	public boolean checkStatus(int time) {
		
		//check if game is still running
		if (hasGameFinished == false) {
			if (hasGameStarted == false && startGame == true) {
				timeToWait--;
				plugin.getServer().broadcastMessage("Game starting in: " + timeToWait);
				if (timeToWait <= 0) {
					hasGameStarted = true;
				}
			}
			
			if (hasGameStarted == true) {
				//timeToEndGame--;
				//plugin.getServer().broadcastMessage("Game ending in: " + timeToEndGame);
				
				//check capture condition for red & blue
				if (redIsCapturing == true && blueIsCapturing == false && captureOwner != 1) {
					captureTimer--;
					if (captureTimer > 0) {
						plugin.getServer().broadcastMessage("[" + ChatColor.RED + "RED CAPTURING" + 
								ChatColor.WHITE + "] " + captureTimer);
					}
					if (captureTimer <= 0) {
						captureOwner = 1;
						plugin.getServer().broadcastMessage("Red has captured the point!");
						plugin.getPlayerLocationListener().setPoint(DyeColor.RED);
						redIsCapturing = false;
						captureTimer = captureTime;
						givePoints(2, "red");
						playEffect();
					}
				}
				if (blueIsCapturing == true && redIsCapturing == false && captureOwner != 2) {
					captureTimer--;
					if (captureTimer > 0) {
						plugin.getServer().broadcastMessage("[" + ChatColor.BLUE + "BLUE CAPTURING" + 
								ChatColor.WHITE + "] " + captureTimer);
					}
					if (captureTimer <= 0) {
						captureOwner = 2;
						plugin.getServer().broadcastMessage("Blue has captured the point!");
						plugin.getPlayerLocationListener().setPoint(DyeColor.BLUE);					
						blueIsCapturing = false;
						captureTimer = captureTime;
						givePoints(2, "blue");
						playEffect();
					}
				}
				if (blueIsCapturing == true && redIsCapturing == true) {
					captureTimer = captureTime;
				}
				
				if (captureOwner == 1) {
					timeToEndRed--;
				}
				
				if (captureOwner == 2) {
					timeToEndBlue--;
				}

				if (timeToEndRed <= 0 || timeToEndBlue <= 0) {
					if (timeToEndRed > timeToEndBlue) {
						//blue wins
						winner = "blue";
						plugin.getServer().broadcastMessage(ChatColor.BLUE + "Blue has won the game!");
					}
					
					if (timeToEndBlue > timeToEndRed) {
						//red wins
						winner = "red";
						plugin.getServer().broadcastMessage(ChatColor.RED + "Red has won the game!");
						
					}
					
					hasGameFinished = true;
					
			    	plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			    		public void run() {
			    			plugin.getStatsHandler().logGame(plugin.getUserList(), winner);
							resetGame();
							startGame();
			    		}
			    	});
					

				}
				printTime(timeToEndRed, timeToEndBlue);	
			}
			return true;
		} else {
			return false;
		}		
	}
	
	private void printTime(int timeToEndRed, int timeToEndBlue) {
		if ((timeToEndRed == 180 || timeToEndBlue == 180 
				|| timeToEndRed == 90 || timeToEndBlue == 90
				|| timeToEndRed == 30 || timeToEndBlue == 30
				|| timeToEndRed <= 5 || timeToEndBlue <= 5) && hasGameFinished == false) {
			if (timeToEndRed != 0 || timeToEndBlue != 0) {

				plugin.getServer().broadcastMessage("Remaining Time - "
												  + ChatColor.RED + "Red: " + timeToEndRed + "s"
												  + ChatColor.GRAY + " | "
												  + ChatColor.BLUE + "Blue: " + timeToEndBlue + "s");
				/*if (timeToEndRed > 10 || timeToEndBlue > 10) {
					timeToEndRed--;
					timeToEndBlue--;
				}*/
			}
		}
	}
	
	public boolean hasTeamWon() {
		return hasTeamWon;
	}

	/**
	 * Gives collective points to a team
	 * 
	 * @param points number of points to give.
	 * @param team team to give it to.
	 */
	public void givePoints(int points, String team) {

	}
	
	public int getBlueScore() {
		return blueScore;
	}

	public void addBlueScore(int blueScore) {
		this.blueScore = this.blueScore + blueScore;
	}

	public int getRedScore() {
		return redScore;
	}

	public void setRedScore(int redScore) {
		this.redScore = redScore;
	}
	
	public void resetScores() {
		blueScore = 0;
		redScore = 0;
	}
	
	/**
	 * Registers a capture on the point from an external data source.
	 * 
	 * @param team the team who have triggered a capture.
	 */
	public void registerCapture(String team) {
		
		if (team == "red") {
			if (redIsCapturing != true) {
				redIsCapturing = true;
			}
		}
		
		if (team == "blue") {
			if (blueIsCapturing != true) {
				blueIsCapturing = true;
			}
		}
	}
	
	/**
	 * Deregisters a capture on the point from an external data source.
	 * 
	 * @param team the team who have left a capture.
	 */
	public void deregisterCapture(String team) {
		if (team == "red") {
			if (redIsCapturing != false) {
				redIsCapturing = false;
			}
		}
		
		if (team == "blue") {
			if (blueIsCapturing != false) {
				blueIsCapturing = false;
			}
		}
		captureTimer = captureTime;
	}
	
	public int getCaptureOwner() {
		return captureOwner;
	}
	
	/**
	 * Starts the game
	 */
	public void startGame() {
		startGame = true;
		onGameStart();
	}
	
	/**
	 * Triggered when game begins. Spawns players into the game.
	 */
	private void onGameStart() {
		List<PlayerDataClass> players = plugin.getUserList();
		for (int i = 0; i < players.size(); i++) {
			PlayerDataClass player = players.get(i);
			plugin.getPlayerHandler().spawnPlayer(player);
		}
		plugin.getServer().getWorld("world").setTime(0);
	}
	
	/**
	 * Resets the game so a new game can begin after the 
	 * old one has ended.
	 */
	public void resetGame() {
		hasGameStarted = false;
		hasGameFinished = false;
		hasTeamWon = false;
		startGame = false;
		
		timeToWait = 5;
		timeToEndRed = 181;
		timeToEndBlue = 181;
		
		redScore = 0;
		blueScore = 0;

		redIsCapturing = false;
		blueIsCapturing = false;	
		captureOwner = 0;
		captureTime = 6;
		captureTimer = 0;
			
		plugin.getPlayerLocationListener().removeAllPlayersFromPoint();
		plugin.getPlayerHandler().clearInventories();
		for (int i = 0; i < plugin.getPlayerHandler().getBluePlayers().size(); i++) {
			plugin.getPlayerHandler().getBluePlayers().get(i).resetScores();
		}
		for (int i = 0; i < plugin.getPlayerHandler().getRedPlayers().size(); i++) {
			plugin.getPlayerHandler().getRedPlayers().get(i).resetScores();
		}
		//plugin.getPlayerHandler().removeAllPlayers();
		captureTimer = captureTime;
	}
    
    public void playEffect() {
        plugin.getServer().getWorld("world").playEffect(new Location(plugin.getServer().getWorld("world"),  
        		0, 4, 0), 
        		Effect.CLICK2, 40);
    }
    
    public boolean hasGameStarted() {
    	return hasGameStarted;
    }
    
    public void teleportToSpawn(Player player) {
    	//player.teleport(new Location((plugin.getServer().getWorld("world")), 0, 5, -47));
    	player.teleport(new Location((plugin.getServer().getWorld("world")), 1220, 5, 179));
    }
}