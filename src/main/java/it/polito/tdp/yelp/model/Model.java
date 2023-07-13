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
	private List<Review> percorso;
		
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
	
	
	public List<Review> getPercorso(){
		
		this.percorso = new ArrayList<>();
		List<Review> parziale = new ArrayList<>();
		
		parziale.add(vertici.get(0));
		
		cerca(parziale);
		
		return percorso;
	}
	
	public void cerca(List<Review> parziale) {
		
		Review ultimo = parziale.get(parziale.size()-1);
		
		List<Review> vicini = Graphs.neighborListOf(this.grafo, ultimo);
		
		for(Review vicino : vicini) {
			if(!parziale.contains(vicino) && this.dao.getPunteggioRecensione(vicino)<=this.dao.getPunteggioRecensione(ultimo)) {
				
				parziale.add(vicino);
				
				cerca(parziale);
				
				parziale.remove(vicino);
			}
		}
		
		if(parziale.size() > percorso.size()) {
			this.percorso = new ArrayList<>(parziale);
		}
	}
	
	public String calcolaGiorni() {
		
		Review iniziale = this.percorso.get(0);
		
		Review finale = this.percorso.get(percorso.size()-1);
		
		double delta = ChronoUnit.DAYS.between(iniziale.getDate(), finale.getDate());
		
		delta = Math.abs(delta);
		
		return "Differenza tra prima e ultima recensioni: "+delta+" giorni.";
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