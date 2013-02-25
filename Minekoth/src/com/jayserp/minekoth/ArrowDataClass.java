package com.jayserp.minekoth;

import org.bukkit.entity.Arrow;
import org.bukkit.util.Vector;

public class ArrowDataClass {
	
	private Arrow arrow;
	private Vector vector;
	
	public Arrow getArrow() {
		return arrow;
	}
	public void setArrow(Arrow arrow) {
		this.arrow = arrow;
	}
	public Vector getVector() {
		return vector;
	}
	public void setVector(Vector vector) {
		this.vector = vector;
	}
}