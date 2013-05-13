package com.jayserp.minekoth;

import org.bukkit.entity.Item;

public class StickyDataClass {
	private Item sticky;
	private String owner;
	private String team;
	public Item getSticky() {
		return sticky;
	}
	public void setSticky(Item sticky) {
		this.sticky = sticky;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	
}