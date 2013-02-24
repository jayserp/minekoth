package com.jayserp.minekoth;

import java.sql.Timestamp;
import java.util.Date;

class PlayerDataClass {

	private String name;
    private String team;
    private String type;
    private int score;
    private String disguised;
    private int kills = 0;
    private int deaths = 0;
    private int points = 0;
    private Timestamp timeStarted;
   
    public PlayerDataClass() {
    	Date date = new Date();
    	timeStarted = new Timestamp(date.getTime());
    }
    
    public String getTeam() {
	    return team;
    }
    public void setTeam(String team) {
	    this.team = team;
    }
    public int getScore() {
 	    return score;
    }
    public void setScore(int score) {
    	this.score = score;
    }
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDisguised() {
		return disguised;
	}
	public void setDisguised(String disguised) {
		this.disguised = disguised;
	}
	public int getKills() {
		return kills;
	}
	public void setKills(int kills) {
		this.kills = kills;
	}
	public int getDeaths() {
		return deaths;
	}
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public Timestamp getTime() {
		return timeStarted;
	}
	public void setTime(Timestamp time) {
		this.timeStarted = time;
	}
	public void resetScores() {
	    this.score = 0;
	    this.disguised = null;
	    this.kills = 0;
	    this.deaths = 0;
	    this.points = 0;
	    Date date = new Date();
	    this.timeStarted = new Timestamp(date.getTime());
	}
}