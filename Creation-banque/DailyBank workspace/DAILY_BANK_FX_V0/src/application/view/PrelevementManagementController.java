package application.view;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import application.DailyBankState;
import application.control.PrelevementManagement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.PrelevementAutomatique;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.ManagementRuleViolation;
import model.orm.exception.RowNotFoundOrTooManyRowsException;

/**
 * @author yann
 * controller de la classe PrelevementManagement
 */

public class PrelevementManagementController implements Initializable{
	
	// Etat application
		private DailyBankState dbs;
		private PrelevementManagement pm;
		
	// Fenêtre physique
	private Stage primaryStage;
	
	// Données de la fenêtre
		private Client clientDesComptes;
		private ObservableList<PrelevementAutomatique> olPrelevement;
	
		// Manipulation de la fenêtre
		public void initContext(Stage _primaryStage, PrelevementManagement _pm, DailyBankState _dbstate, Client client) {
			this.pm = _pm;
			this.primaryStage = _primaryStage;
			this.dbs = _dbstate;
			this.clientDesComptes = client;
			this.configure();
		}
		
		private void configure() {
			String info;

			this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));

			this.olPrelevement = FXCollections.observableArrayList();
			this.lvPrelevement.setItems(this.olPrelevement);
			this.lvPrelevement.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			this.lvPrelevement.getFocusModel().focus(-1);
			this.lvPrelevement.getSelectionModel().selectedItemProperty().addListener(e -> this.validateComponentState());
			this.validateComponentState();

			info = this.clientDesComptes.nom + "  " + this.clientDesComptes.prenom + "  (id : "
					+ this.clientDesComptes.idNumCli + ")";
			this.lblInfosClient.setText(info);

			this.loadListPrelev();
		}
		
		public void displayDialog() {
			this.primaryStage.showAndWait();
		}
		
		// Gestion du stage
		private Object closeWindow(WindowEvent e) {
			this.doCancel();
			e.consume();
			return null;
		}
		
		@FXML
		private Button btnModifierPrelevement;
		@FXML
		private Button btnNouveauPrelevement;
		@FXML
		private Button btnSupprimerPrelevement;
		@FXML
		private Label lblInfosClient;
		@FXML
		private ListView<PrelevementAutomatique> lvPrelevement;
		
		/*
		 * Permet de fermer la fenêtre au clique d'un bouton
		 */
		@FXML
		private void doCancel() {
			this.primaryStage.close();
		}
		
		/*
		 * Permet d'ajouter un nouveau prélèvement
		 */
		@FXML
		private void doNouveauPrelevement() throws SQLException {
			PrelevementAutomatique prelevement;
			prelevement = this.pm.creerPrelevement();
			if (prelevement != null) {
				this.olPrelevement.add(prelevement);
			}
		}
		
		/*
		 * Ajoute les prélèvements d'un compte dans une liste
		 */
		public void loadListPrelev () {
			ArrayList<PrelevementAutomatique> listeP;
			listeP = this.pm.getPrelevement();
			this.olPrelevement.clear();
			for (PrelevementAutomatique p : listeP) {
				this.olPrelevement.add(p);
			}
		}
		
		/*
		 * Permet de modifier les informations d'un prélèvement
		 */
		@FXML
		private void doModifierPrelevement() {
			int selectedIndice = this.lvPrelevement.getSelectionModel().getSelectedIndex();
			PrelevementAutomatique pMod = this.olPrelevement.get(selectedIndice);
			PrelevementAutomatique presult = this.pm.modifierPrelevement(pMod);
			if (presult != null) {
				this.olPrelevement.set(selectedIndice, presult);
			}
			this.loadListPrelev();
		}
		
		/*
		 * Permet de supprimer un prélèvement
		 * une boite de dialogue apparait nous demandant si on veut vraiment supprimer le prélèvement
		 * si oui le prélèvement se supprime
		 * si non on ne fait rien
		 */
		@FXML
		private void doSupprimerPrelevement() throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException, ManagementRuleViolation {
			int selectedIndice = this.lvPrelevement.getSelectionModel().getSelectedIndex();
			if (selectedIndice >= 0) {
				Alert dialog = new Alert(AlertType.INFORMATION);
				dialog.setTitle("Supprimer un prélèvement");
				dialog.setHeaderText("Voulez-vous vraiment supprimer le prélèvement ?");
				dialog.getButtonTypes().setAll(ButtonType.YES,ButtonType.NO);
				Optional<ButtonType> response = dialog.showAndWait();
			
				if(response.orElse(null) == ButtonType.YES) {
					PrelevementAutomatique pa = this.olPrelevement.get(selectedIndice);
					int indice = pa.idPrelev;
					this.pm.supprimerPrelevement(indice);
				}
				else {
					return;
				}
			}
			this.loadListPrelev();
		}

		@Override
		public void initialize(URL location, ResourceBundle resources) {
			this.lvPrelevement.setItems(this.olPrelevement);
		}
		
		/*
		 * Vérifie si les informations sont valide
		 */
		private void validateComponentState() {
			int selectedIndice = this.lvPrelevement.getSelectionModel().getSelectedIndex();

			//PrelevementAutomatique p = this.lvPrelevement.getSelectionModel().getSelectedItem();

			// Si un prélèvement est sélectionner
			if (selectedIndice >= 0) {
				this.btnModifierPrelevement.setDisable(true);
				this.btnSupprimerPrelevement.setDisable(false);
			} 

			
			// Si un prélèvement est sélectionner
			if (selectedIndice >= 0) {
					this.btnModifierPrelevement.setDisable(false);
					this.btnSupprimerPrelevement.setDisable(false);
			}
		}
}