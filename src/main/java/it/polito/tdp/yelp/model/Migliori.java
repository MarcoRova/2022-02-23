package it.polito.tdp.yelp.model;

public class Migliori {
	
	private Review r;
	private int uscenti;
	public Migliori(Review r, int uscenti) {
		super();
		this.r = r;
		this.uscenti = uscenti;
	}
	public Review getR() {
		return r;
	}
	public void setR(Review r) {
		this.r = r;
	}
	public int getUscenti() {
		return uscenti;
	}
	public void setUscenti(int uscenti) {
		this.uscenti = uscenti;
	}
	
	
	

}
