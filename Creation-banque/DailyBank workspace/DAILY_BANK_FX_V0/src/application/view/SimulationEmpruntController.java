package application.view;

import java.net.URL;
import java.util.ResourceBundle;

import application.tools.AlertUtilities;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Emprunt;

/**
 * @author ruben
 *
 */
public class SimulationEmpruntController implements Initializable{
	
	// Etat application
		private Emprunt emprunt;
		//private AssuranceEmprunt assurance;
	
	// Fenêtre physique
	private Stage primaryStage;
	
	// Données de la fenêtre
	
	
	// Manipulation de la fenêtre
	public void initContext(Stage _primaryStage) {
		this.primaryStage = _primaryStage;
		this.configure();
	}
		
	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));
		this.capital.requestFocus();

		this.capital.setPromptText("Veuillez remplir ce champ");
		this.duree.setPromptText("Veuillez remplir ce champ");
		this.tauxAnnuel.setPromptText("Veuillez remplir ce champ");
		this.tauxAssurance.setPromptText("Veuillez remplir ce champ");
		this.dureePretAssurance.setPromptText("Veuillez remplir ce champ");
		this.fraisDossier.setPromptText("Veuillez remplir ce champ");


		this.validateComponent();
	}
	

	// Gestion du stage
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}
	
	
	// Attributs de la scene + actions
	@FXML
	private TextField capital;
	@FXML
	private TextField duree;
	@FXML
	private TextField tauxAnnuel;
	@FXML
	private TextField tauxApplicable;
	@FXML
	private TextField nbPeriodes;
	@FXML
	private TextField dureePretAssurance;
	@FXML
	private TextField mensualiteSansA;
	@FXML
	private TextField coutCredit;
	@FXML
	private TextField fraisDossier;
	@FXML
	private TextField total;
	@FXML
	private TextField tauxAssurance;
	@FXML
	private TextField mensualiteAssurance;
	@FXML
	private TextField coutAssurance;
	@FXML
	private Button simulerEmprunt;
	@FXML
	private Button simulerAssurance;
	@FXML
	private Button butTotal;
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	/*
	 * Permet de fermer la fenêtre au clique d'un bouton
	 */
	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}
	
	
	/**
	 * applique au TextField correspondant la valeur du taux applicable de l'emprunt
	 * @param txt
	 * @param em
	 */
	private void setTauxApplicable(TextField txt, Emprunt em) {
		String res = String.valueOf(em.getTauxApplicable());
		txt.setText(res);
	}
	
	/**
	 * applique au TextField correspondant la valeur du nombre de periodes de l'emprunt
	 * @param txt
	 * @param em
	 */
	private void setPeriode(TextField txt, Emprunt em) {
		String res = String.valueOf(em.getNbPeriode());
		txt.setText(res);
	}
	
	/**
	 * applique au TextField correspondant la valeur de la mensualite de l'emprunt
	 * @param txt
	 * @param em
	 */
	private void setMensualiteSansAssurance(TextField txt, Emprunt em) {
		String res = String.valueOf(em.getMensualiteSansAss());
		txt.setText(res);
	}
	
	/**
	 * applique au TextField correspondant la valeur de la mensualite de l'assurance d'emprunt
	 * @param txt
	 * @param em
	 */
	private void setMensualiteAssurance(TextField txt, Emprunt em) {
		String res = String.valueOf(em.getMensualiteAssurance());
		txt.setText(res);
	}
	
	/**
	 * applique au TextField correspondant la valeur du coût de l'emprunt
	 * @param txt
	 * @param em
	 */
	private void setCoutCredit(TextField txt, Emprunt em) {
		String res = String.valueOf(em.coutCredit());
		txt.setText(res);
	}
	
	/**
	 * applique au TextField correspondant la valeur du coût de l'assurance d'emprunt
	 * @param txt
	 * @param em
	 */
	private void setCoutAssurance(TextField txt, Emprunt em) {
		String res = String.valueOf(toDouble(this.dureePretAssurance.getText().trim())*em.getMensualiteAssurance());
		txt.setText(res);
	}
	
	/**
	 * applique au TextField correspondant la valeur du coût total de l'emprunt avec l'assurance d'emprunt
	 * @param txt
	 * @param em
	 */
	private void setTotal(TextField txt, Emprunt em) {
		String res = String.valueOf(toDouble(this.coutAssurance.getText().trim())
				+ toDouble(this.coutCredit.getText().trim()) + toDouble(this.fraisDossier.getText().trim()));
		txt.setText(res);
	}
	
	/**
	 * si la saisie de champs nécessaires est valide, lance les méthodes pour affecter les valeurs aux champs concernés pour l'emprunt
	 */
	@FXML
	private void displayEmprunt() {
		if (this.isSaisieValideEmprunt()) {
			setTauxApplicable(tauxApplicable,emprunt);
			setPeriode(nbPeriodes,emprunt);
			setMensualiteSansAssurance(mensualiteSansA,emprunt);
			setCoutCredit(coutCredit,emprunt);
		}
	}
	
	/**
	 * si la saisie de champs nécessaires est valide, lance les méthodes pour affecter les valeurs aux champs concernés pour l'assurance d'emprunt
	 */
	@FXML
	private void displayAssuranceEmprunt() {
		if(this.isSaisieValideAssurance()) {
			setMensualiteAssurance(mensualiteAssurance,emprunt);
			setCoutAssurance(coutAssurance,emprunt);
		}
	}
	
	/**
	 * si la saisie de champs nécessaires est valide, lance les méthodes pour affecter la valeur du total au champ concerné
	 */
	@FXML
	private void displayTotal() {
		if(isSaisieValideEmprunt() && isSaisieValideAssurance() && isSaisieValideTotal())
			setTotal(total,emprunt);
	}
	
	
	/**
	 * Permet de mettre un string correspondant à un chiffre en double
	 * @param String number
	 * @return un double basé sur le string ou -1
	 */
	private double toDouble(String number) {
		try {
			return Double.parseDouble(number);
		} catch (Exception e) {
			return -1;
		}
	}
	
	/** Vérifie si les entrées sont valides pour l''emprunt
     * @return Vrai si les entrées sont valides, Faux sinon
     */
    private boolean isSaisieValideEmprunt() {
    	//initialisation de la variable Emprunt depuis les textfield
    	emprunt = new Emprunt( toDouble(this.capital.getText().trim()), (int) toDouble(this.duree.getText().trim()), toDouble(this.tauxAnnuel.getText().trim()));
        
        //reinitialisation du css pour afficher le champ avec erreur
        this.capital.getStyleClass().remove("borderred");
        this.duree.getStyleClass().remove("borderred");
        this.tauxAnnuel.getStyleClass().remove("borderred");
        this.fraisDossier.getStyleClass().remove("borderred");
        
        //verification de la valitdité des champs
		try {
			if (emprunt.capitalEmprunt < 5000) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException nbF) {
			this.capital.getStyleClass().add("borderred");
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Capital inférieur à 5 000 euros",AlertType.WARNING);
			this.capital.requestFocus();
			return false;
		}

		try {
			if (emprunt.dureeEmprunt < 1) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException nbF) {
			this.duree.getStyleClass().add("borderred");
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Durée infrérieur à 1 ans",AlertType.WARNING);
			this.duree.requestFocus();
			return false;
		}
        
		try {
			if (emprunt.tauxPretAnnuel < 0) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException nbF) {
			this.tauxAnnuel.getStyleClass().add("borderred");
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Taux de prêt annuel négatif",AlertType.WARNING);
			this.tauxAnnuel.requestFocus();
			return false;
		}

        return true;
    } 
    
    
    /** Vérifie si les entrées sont valides pour l'assurance d'emprunt
     * @return Vrai si les entrées sont valides, Faux sinon
     */
    private boolean isSaisieValideAssurance() {
    	//initialisation de la variable Emprunt depuis les textfield
    	emprunt = new Emprunt( toDouble(this.capital.getText().trim()), toDouble(this.tauxAssurance.getText().trim()) );

    			
        //reinitialisation du css pour afficher le champ avec erreur
        this.capital.getStyleClass().remove("borderred");
        this.tauxAssurance.getStyleClass().remove("borderred");
        
        //verification de la valitdité des champs
		try {
			if (emprunt.capitalEmprunt < 5000) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException nbF) {
			this.capital.getStyleClass().add("borderred");
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Capital inférieur à 5 000 euros",AlertType.WARNING);
			this.capital.requestFocus();
			return false;
		}
		
		try {
			if (emprunt.tauxAssurance < 0) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException nbF) {
			this.tauxAssurance.getStyleClass().add("borderred");
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Taux de l'assurance négatif",AlertType.WARNING);
			this.tauxAssurance.requestFocus();
			return false;
		}
		
		try {
			if (toDouble(this.dureePretAssurance.getText().trim()) < 12) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException nbF) {
			this.tauxAssurance.getStyleClass().add("borderred");
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Durée de prêt inférieur à 12 mois",AlertType.WARNING);
			this.dureePretAssurance.requestFocus();
			return false;
		}

        return true;
    } 
	
	/**
	 * Vérifie si les entrées sont valides pour le total
	 * 
	 * @return Vrai si les entrées sont valides, Faux sinon
	 */
	private boolean isSaisieValideTotal() {
		// reinitialisation du css pour afficher le champ avec erreur
		this.coutCredit.getStyleClass().remove("borderred");
		this.coutAssurance.getStyleClass().remove("borderred");
		this.fraisDossier.getStyleClass().remove("borderred");

		// verification de la valitdité des champs
		try {
			if (toDouble(this.coutCredit.getText().trim()) < 0) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException nbF) {
			this.coutCredit.getStyleClass().add("borderred");
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Coût du crédit négatif",
					AlertType.WARNING);
			return false;
		}

		try {
			if (toDouble(this.coutAssurance.getText().trim()) < 0) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException nbF) {
			this.coutAssurance.getStyleClass().add("borderred");
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Coût de l'assurance négatif",
					AlertType.WARNING);
			return false;
		}

		try {
			if (toDouble(this.fraisDossier.getText().trim()) < 0) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException nbF) {
			this.fraisDossier.getStyleClass().add("borderred");
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Frais de dossier négatif",
					AlertType.WARNING);
			this.fraisDossier.requestFocus();
			return false;
		}

		return true;
	}
    
	private void validateComponent	() {
		this.tauxApplicable.setEditable(false);
		this.nbPeriodes.setEditable(false);
		this.coutCredit.setEditable(false);
		this.mensualiteAssurance.setEditable(false);
		this.mensualiteSansA.setEditable(false);
		this.coutAssurance.setEditable(false);
		this.total.setEditable(false);
	}

}
