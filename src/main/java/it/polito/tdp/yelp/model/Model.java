package it.polito.tdp.yelp.model;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	private YelpDao dao;
	private Graph<Review, DefaultWeightedEdge> grafo;
	private Map<String, Business> businessIdMap = new HashMap<>();
	private List<String> citta;
	private List<Review> vertici = new ArrayList<>();
		
	public Model() {
		this.dao = new YelpDao();
		this.citta = dao.getCitta();
		
		for(Business b : this.dao.getAllBusiness()) {
			this.businessIdMap.put(b.getBusinessId(), b);
		}
	}
	
	
	
	public void creaGrafo(Business b) {
		
		this.grafo = new SimpleDirectedWeightedGraph<Review, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		
		this.vertici = this.dao.getReviewsByBusiness(b);
		
		Graphs.addAllVertices(this.grafo, vertici);
		
		for(Review r1 : vertici) {
			for(Review r2 : vertici) {
				if(!r1.equals(r2) && r1.getDate().isBefore(r2.getDate())) {
					
					double peso = Math.abs(ChronoUnit.DAYS.between(r1.getDate(), r2.getDate()));
					
					if(peso != 0) {
						Graphs.addEdgeWithVertices(this.grafo, r1, r2, peso);
					}
				}
			}
		}
	}
	
	
	public List<Migliori> trovaMigliore(){
		
		List<Migliori> migliori = new ArrayList<>();
		
		int best = 0;
		
		for(Review r : vertici) {
			
			int uscenti = this.grafo.outgoingEdgesOf(r).size();
			
			if(uscenti > best) {
				best = uscenti;
				migliori.clear();
				migliori.add(new Migliori(r, uscenti));
			}
			else if(uscenti == best) {
				migliori.add(new Migliori(r, uscenti));
			}
		}		
		return migliori;
	}
	
	public String infoGrafo() {
		return "Grafo creato!\n#Vertici: "+this.grafo.vertexSet().size()+"\n#Archi: "+this.grafo.edgeSet().size();
	}
	



	public Graph<Review, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}



	public List<String> getCitta() {
		return citta;
	}
	
	public List<Business> getBusinessByCity(String city){
		return this.dao.getBusinessByCity(city);
	}
	
}
