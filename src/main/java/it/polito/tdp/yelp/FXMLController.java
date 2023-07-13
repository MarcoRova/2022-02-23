/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Migliori;
import it.polito.tdp.yelp.model.Model;
import it.polito.tdp.yelp.model.Review;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnMiglioramento"
    private Button btnMiglioramento; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="cmbLocale"
    private ComboBox<Business> cmbLocale; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML
    void doRiempiLocali(ActionEvent event) {
    	this.cmbLocale.getItems().clear();
    	String citta = this.cmbCitta.getValue();
    	if(citta != null) {
    		
    		List<Business> b = this.model.getBusinessByCity(citta);
    		
    		this.cmbLocale.getItems().addAll(b);
    		
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	this.txtResult.clear();
    	
    	String citta = this.cmbCitta.getValue();
    	
    	if(citta == null) {
    		this.txtResult.appendText("Selezionare una città per continuare");
    		return;
    	}
    	
    	
    	Business b = this.cmbLocale.getValue();
    	
    	if(b == null) {
    		this.txtResult.appendText("Selezionare un locale per continuare");
    		return;
    	}
    	
    	this.model.creaGrafo(b);
    	
    	this.txtResult.appendText(this.model.infoGrafo());
    	
    	List<Migliori> migliori = this.model.trovaMigliore();
    	
    	for(Migliori m : migliori) {
    		this.txtResult.appendText("\n\n"+m.getR().getReviewId()+" 		#archi uscenti: "+m.getUscenti());
    	}
    	
    	this.btnMiglioramento.setDisable(false);
    }

    @FXML
    void doTrovaMiglioramento(ActionEvent event) {
    	
    	this.txtResult.clear();
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnMiglioramento != null : "fx:id=\"btnMiglioramento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbLocale != null : "fx:id=\"cmbLocale\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.cmbCitta.getItems().addAll(this.model.getCitta());
    	this.btnMiglioramento.setDisable(true);
    }
}
