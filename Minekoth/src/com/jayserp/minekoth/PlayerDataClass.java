package com.jayserp.minekoth;

class PlayerDataClass {

	private String name;
    private String team;
    private String type;
    private int score;
    private String disguised;
    private int kills = 0;
    private int deaths = 0;
    private int points = 0;
    private long timeStarted;
    private int rank = -1;
    private boolean scoped = false;
   
    public PlayerDataClass() {
    	timeStarted = System.currentTimeMillis();
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
	public long getTime() {
		return timeStarted;
	}
	public void setTime(long time) {
		this.timeStarted = time;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	public void resetScores() {
	    this.score = 0;
	    this.disguised = null;
	    this.kills = 0;
	    this.deaths = 0;
	    this.points = 0;
	    this.timeStarted = System.currentTimeMillis();
	}

	public boolean isScoped() {
		return scoped;
	}

	public void setScoped(boolean scoped) {
		this.scoped = scoped;
	}
}