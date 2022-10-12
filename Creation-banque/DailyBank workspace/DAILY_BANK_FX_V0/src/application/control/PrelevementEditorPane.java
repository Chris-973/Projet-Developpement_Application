package application.control;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.CompteEditorPaneController;
import application.view.OperationEditorPaneController;
import application.view.PrelevementEditorPaneController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Client;
import model.data.CompteCourant;
import model.data.PrelevementAutomatique;

/**
 * @author yann
 * classe dédié à la gestion des informations d'un nouveau prélèvement
 */
public class PrelevementEditorPane {
	
	/**
	 * Attributs
	 */
	
	private Stage primaryStage; //fenêtre principale
	private PrelevementEditorPaneController pepc; //controller relié à la création d'un nouveau prélèvement
	
	/**
	 * @param _parentStage
	 * @param _dbstate
	 * gère la fenêtre de "nouveau prélèvement" dans la gestion des prélèvements
	 */
	public PrelevementEditorPane(Stage _parentStage, DailyBankState _dbstate) {

		try {
			FXMLLoader loader = new FXMLLoader(PrelevementEditorPaneController.class.getResource("prelevementeditorpane.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth()+20, root.getPrefHeight()+10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Gestion d'un prélèvement");
			this.primaryStage.setResizable(false);

			this.pepc = loader.getController();
			this.pepc.initContext(this.primaryStage, _dbstate);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param compte
	 * @param prelevement
	 * @param em
	 * @return 
	 */
	public PrelevementAutomatique doPrelevementEditorDialog(CompteCourant compte, PrelevementAutomatique prelevement, EditionMode em) {
		return this.pepc.displayDialog(compte, prelevement, em);
	}
	
	/**
	 * @return le controller de la classe : PrelevementEditorPane
	 */
	public PrelevementEditorPaneController getPepc() {
		return pepc;
	}

}
