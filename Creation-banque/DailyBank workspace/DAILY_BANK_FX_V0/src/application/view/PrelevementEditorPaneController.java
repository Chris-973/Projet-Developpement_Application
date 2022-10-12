package application.view;

import java.net.URL;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

import application.DailyBankState;
import application.control.PrelevementManagement;
import application.tools.AlertUtilities;
import application.tools.ConstantesIHM;
import application.tools.EditionMode;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.CompteCourant;
import model.data.Operation;
import model.data.PrelevementAutomatique;
import model.orm.exception.DatabaseConnexionException;

/**
 * @author yann
 * controller relié à la classe PrelevementEditorPane pour la gestion d'un nouveau prélèvement
 */
public class PrelevementEditorPaneController implements Initializable{
	
	// Etat application
		private DailyBankState dbs;

		// Fenêtre physique
		private Stage primaryStage;

		// Données de la fenêtre
		private EditionMode em;
		private CompteCourant compte;
		private PrelevementAutomatique prelevementEdite;
		private PrelevementAutomatique prelevementResult;
		private boolean indiceOperation = false;
		
	// Manipulation de la fenêtre
	public void initContext(Stage _primaryStage, DailyBankState _dbstate) {
		this.primaryStage = _primaryStage;
		this.dbs = _dbstate;
		this.configure();
	}
	
	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));
		
		this.txtMontant.focusedProperty().addListener((t, o, n) -> this.focusMontant(t, o, n));
		this.txtDateRecurrente.focusedProperty().addListener((t, o, n) -> this.focusDate(t, o, n));
	}
	
	/*
	 * Configuration de la fenêtre d'édition d'un prelevement
	 * @param in c : le compte courant
	 * @param in prelevement : le prelevement
	 * @param in mode : le mode de modification
	 * return le resultat
	 */
	public PrelevementAutomatique displayDialog(CompteCourant c, PrelevementAutomatique prelevement, EditionMode mode) {
		this.compte = c;
		this.em = mode;
		if (prelevement == null) {
			this.prelevementEdite = new PrelevementAutomatique(0 ,10 , 5 , "N", this.compte.idNumCompte);

		} else {
			this.prelevementEdite = new PrelevementAutomatique(prelevement);
		}
		this.prelevementResult = null;
		switch (mode) {
		case CREATION:
			this.txtIdPrelevement.setDisable(true);
			this.txtIdNumCompte.setDisable(true);
			this.lblMessage.setText("Informations sur le nouveau prélèvement");
			this.btnOk.setText("Ajouter");
			this.btnCancel.setText("Annuler");
			break;
		case MODIFICATION:
			this.txtIdPrelevement.setDisable(true);
			this.txtIdNumCompte.setDisable(true);
			this.lblMessage.setText("Modification du compte");
			this.btnOk.setText("Modifier");
			this.btnCancel.setText("Annuler");
			break;
		case SUPPRESSION:
			//break;
		}

		// initialisation du contenu des champs
		this.txtIdPrelevement.setText("" + this.prelevementEdite.idPrelev);
		this.txtIdNumCompte.setText("" + this.prelevementEdite.idNumCompte);
		this.txtMontant.setText(String.format(Locale.ENGLISH, "%10.02f", this.prelevementEdite.montant));
		this.txtDateRecurrente.setText("" + this.prelevementEdite.dateRecurrente);
		this.txtBeneficiaire.setText("" + this.prelevementEdite.beneficiaire);

		this.prelevementResult = null;

		this.primaryStage.showAndWait();
		return this.prelevementResult;
	}
	
	// Gestion du stage
		private Object closeWindow(WindowEvent e) {
			this.doCancel();
			e.consume();
			return null;
		}
	
		/*
		 * permet de récupérer la valeur du montant saisi lors de la création d'un nouveau prélèvement
		 */
		private Object focusMontant(ObservableValue<? extends Boolean> txtField, boolean oldPropertyValue,
				boolean newPropertyValue) {
			if (oldPropertyValue) {
				try {
					double val;
					val = Double.parseDouble(this.txtMontant.getText().trim());
					if (val < 0) {
						throw new NumberFormatException();
					}
					this.prelevementEdite.montant = val;
				} catch (NumberFormatException nfe) {
					this.txtMontant.setText(String.format(Locale.ENGLISH, "%10.02f", this.prelevementEdite.montant));
				}
			}
			this.txtMontant.setText(String.format(Locale.ENGLISH, "%10.02f", this.prelevementEdite.montant));
			return null;
		}
		
		/*
		 * permet de récupérer la valeur de la date récurrente saisi lors de la création d'un nouveau prélèvement
		 */
		private Object focusDate(ObservableValue<? extends Boolean> txtField, boolean oldPropertyValue,
				boolean newPropertyValue) {
			if (oldPropertyValue) {
				try {
					int val;
					val = Integer.parseInt(this.txtDateRecurrente.getText().trim());
					if (val < 0) {
						throw new NumberFormatException();
					}
					this.prelevementEdite.dateRecurrente = val;
				} catch (NumberFormatException nfe) {
					this.txtDateRecurrente.setText("" + this.prelevementEdite.dateRecurrente);
				}
			}
			return null;
		}
		
		public boolean getOperation() {
			return this.indiceOperation;
		}
		
		public int getJourDate() {
			return Integer.parseInt(this.txtDateRecurrente.getText());
		}
		
		
	// Attributs de la scene + actions
	@FXML
	private Button btnOk;
	@FXML
	private Button btnCancel;
	@FXML
	private Label lblMessage;
	@FXML
	private TextField txtIdPrelevement;
	@FXML
	private TextField txtMontant;
	@FXML
	private TextField txtDateRecurrente;
	@FXML
	private TextField txtBeneficiaire;
	@FXML
	private TextField txtIdNumCompte;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}
	
	/*
	 * Permet de fermer une fenêtre au clique d'un bouton 
	 */
	@FXML
	private void doCancel() {
		this.prelevementResult = null;
		this.primaryStage.close();
	}
	
	/*
	 * Permet d'ajouter un prélèvement au clique d'un bouton
	 */
	@FXML
	private void doAjouter() throws DatabaseConnexionException, SQLException {
		switch (this.em) {
		case CREATION:
			
			if (this.isSaisieValide()) {
				indiceOperation = true;
				this.prelevementResult = this.prelevementEdite;
				this.primaryStage.close();
			}


			break;
		
		case MODIFICATION:
			if (this.isSaisieValide()) {
				this.prelevementResult = this.prelevementEdite;
				this.primaryStage.close();

			}
			break;
			
		case SUPPRESSION:
			this.primaryStage.close();
			break;
		}

	}
	
	/*
	 * Permet de vérifiez si les saisies sont valides : 
	 * renvoie une alerte si :
	 * - le montant n'est pas saisi
	 * - la date saisi est inférieur ou égal à 0 et supérieur à 28
	 * - le champ du bénéficaire ne doit pas être vide
	 */
	private boolean isSaisieValide() {
		this.prelevementEdite.beneficiaire = this.txtBeneficiaire.getText().trim();
		
		if (this.txtMontant.getText().equals("")) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Le montant ne doit pas être vide",
					AlertType.WARNING);
			this.txtMontant.requestFocus();
			return false;
		}
		
		int date;
		date = Integer.parseInt(this.txtDateRecurrente.getText());
		if(date <= 0 || date > 28) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "La date doit être supérieur à 0 et "
					+ "inférieur à 28",
					AlertType.WARNING);
			this.txtDateRecurrente.requestFocus();
			return false;
		}
		
		if (this.prelevementEdite.beneficiaire.isEmpty()) {
			AlertUtilities.showAlert(this.primaryStage, "Erreur de saisie", null, "Le Bénéficiaire ne doit pas être vide",
					AlertType.WARNING);
			this.txtBeneficiaire.requestFocus();
			return false;
		}

		return true;
	}

}