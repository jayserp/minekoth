package com.jayserp.minekoth;

import java.util.List;

public class StatsHandler {
	
	private Minekoth plugin;
	private Database db;
	
	public StatsHandler(Minekoth plugin) {
		this.plugin = plugin;
		db = plugin.getSqlDb();
	}
	
	public void logGame(List<PlayerDataClass> data, String winner) {
		
		GamesDataClass game = new GamesDataClass();
		game.setWinner(winner);
		int gameId = db.insertGame(game);
		
		if (gameId != -1) {	
			for(int i = 0; i < data.size(); i++) {
				PlayerDataClass temp = data.get(i);
				UsersDataClass user = db.getUser(temp.getName());
				long difference = System.currentTimeMillis() - temp.getTime();
				plugin.getLogger().info("Processing stats for: " + temp.getName());
				if(user != null) {
					plugin.getLogger().info("User exists in database: " + user.getId());
					SessionsDataClass s = new SessionsDataClass();
					s.setGameId(gameId);
					s.setPlayerId(user.getId());
					s.setKills(temp.getKills());
					s.setDeaths(temp.getDeaths());
					s.setTime(difference);
					s.setPoints((temp.getKills() * 4) - temp.getDeaths());
					if (temp.getTeam() != null) {
						if (temp.getTeam() == winner) {
							s.setWins(1);
							s.setLosses(0);
						} else {
							s.setWins(0);
							s.setLosses(1);
						}
					}
					db.insertSession(s);				
				} else {
					UsersDataClass u = new UsersDataClass();
					u.setUsername(temp.getName());
					int userId = db.insertUser(u);					
					if (userId != -1) {	
						plugin.getLogger().info("Created user in database: " + userId);
						SessionsDataClass s = new SessionsDataClass();
						s.setGameId(gameId);
						s.setPlayerId(userId);
						s.setKills(temp.getKills());
						s.setDeaths(temp.getDeaths());
						s.setTime(difference);
						s.setPoints((temp.getKills() * 4) - temp.getDeaths());
						if (temp.getTeam() != null) {
							if (temp.getTeam() == winner) {
								s.setWins(1);
								s.setLosses(0);
							} else {
								s.setWins(0);
								s.setLosses(1);
							}
						}
						db.insertSession(s);
					} else {
						plugin.getLogger().info("Failed to insert SQL (user_id)");
					}
				}
			}
		} else {
			plugin.getLogger().info("Failed to insert SQL (game_id)");
		}
	}	
}