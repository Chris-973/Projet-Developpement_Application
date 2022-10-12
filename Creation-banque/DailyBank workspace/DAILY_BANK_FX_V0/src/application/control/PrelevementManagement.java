package application.control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import application.DailyBankApp;
import application.DailyBankState;
import application.tools.CategorieOperation;
import application.tools.EditionMode;
import application.tools.StageManagement;
import application.view.ComptesManagementController;
import application.view.OperationsManagementController;
import application.view.PrelevementManagementController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.data.Client;
import model.data.CompteCourant;
import model.data.Operation;
import model.data.PrelevementAutomatique;
import model.orm.AccessCompteCourant;
import model.orm.AccessOperation;
import model.orm.AccessPrelevement;
import model.orm.LogToDatabase;
import model.orm.exception.ApplicationException;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.ManagementRuleViolation;
import model.orm.exception.Order;
import model.orm.exception.RowNotFoundOrTooManyRowsException;
import model.orm.exception.Table;

/**
 * @author yann
 * classe dédié à la gestion de la fenêtre "gestion des prélèvements"
 */
public class PrelevementManagement {
	
	/**
	 * Attributs
	 */
	
	private Stage primaryStage; //la fenêtre principale
	private PrelevementManagementController pmc; //le controller relié au prélèvement
	private DailyBankState dbs;
	private CompteCourant compte; //un compte courant
	
	/**
	 * @param _parentStage
	 * @param _dbstate
	 * @param client
	 * @param compte
	 * représente la fenêtre "gestion des prélèvements" après avoir cliquer sur le bouton
	 * "prélèvement automatique" des opérations d'un compte d'un client
	 */
	public PrelevementManagement(Stage _parentStage, DailyBankState _dbstate, Client client, CompteCourant Compte) {
		this.compte = Compte;
		this.dbs = _dbstate;
		try {
			FXMLLoader loader = new FXMLLoader(PrelevementManagementController.class.getResource("prelevementmanagement.fxml"));
			BorderPane root = loader.load();

			Scene scene = new Scene(root, root.getPrefWidth()+50, root.getPrefHeight()+10);
			scene.getStylesheets().add(DailyBankApp.class.getResource("application.css").toExternalForm());

			this.primaryStage = new Stage();
			this.primaryStage.initModality(Modality.WINDOW_MODAL);
			this.primaryStage.initOwner(_parentStage);
			StageManagement.manageCenteringStage(_parentStage, this.primaryStage);
			this.primaryStage.setScene(scene);
			this.primaryStage.setTitle("Gestion des prélèvements");
			this.primaryStage.setResizable(false);

			this.pmc = loader.getController();
			this.pmc.initContext(this.primaryStage, this, _dbstate, client);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doPrelevementManagementDialog() {
		this.pmc.displayDialog();
	}
	
	/**
	 * @return le prélèvement automatique que l'on créer
	 * @throws SQLException 
	 */
	public PrelevementAutomatique creerPrelevement() throws SQLException {
		PrelevementAutomatique prelevement;
		PrelevementEditorPane pep = new PrelevementEditorPane(this.primaryStage, this.dbs);
		prelevement = pep.doPrelevementEditorDialog(this.compte, null, EditionMode.CREATION);
		if (prelevement != null) {
			try {
				
				Connection con = LogToDatabase.getConnexion(); //Connexion à la base de données
                
                String query = "INSERT INTO PRELEVEMENTAUTOMATIQUE VALUES (" + "seq_id_client.NEXTVAL" + ", " + "?" + ", " + "?" + ", " + "?" + ", " + "?" +")";
                
                PreparedStatement pst = con.prepareStatement(query);
                pst.setDouble(1, prelevement.montant);
                pst.setInt(2, prelevement.dateRecurrente);
                pst.setString(3, prelevement.beneficiaire);
                pst.setInt(4, prelevement.idNumCompte);
                
                System.err.println(query);
                
                int result = pst.executeUpdate();
                pst.close();
				
				 if (result != 1) {
                    con.rollback();
                    throw new RowNotFoundOrTooManyRowsException(Table.PrelevementAutomatique, Order.INSERT,
    						"Insert anormal (insert de moins ou plus d'une ligne)", null, result);
                }
				 
				 query = "SELECT seq_id_client.CURRVAL from DUAL";

					System.err.println(query);
					PreparedStatement pst2 = con.prepareStatement(query);

					ResultSet rs = pst2.executeQuery();
					rs.next();
					int numPrelev = rs.getInt(1);

					con.commit();
					rs.close();
					pst2.close();
					
					prelevement.idPrelev = numPrelev;
                
				// existe pour compiler les catchs dessous
				if (Math.random() < -1) {
					throw new ApplicationException(Table.PrelevementAutomatique, Order.INSERT, "todo : test exceptions", null);
				}
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dbs, e);
				ed.doExceptionDialog();
				this.primaryStage.close();
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dbs, ae);
				ed.doExceptionDialog();
			}
		}
		return prelevement;
	}
	
	/**
	 * Permet de modifier le prélèvement d'un compte
	 * @param prelevement : prélèvement du compte séléctionné
	 * @return prélèvement modifié
	 */
	public PrelevementAutomatique modifierPrelevement(PrelevementAutomatique p) {
		PrelevementEditorPane pep = new PrelevementEditorPane(this.primaryStage, this.dbs);
		PrelevementAutomatique result = pep.doPrelevementEditorDialog(this.compte, p, EditionMode.MODIFICATION);
		if(result != null) {
			try {
				AccessPrelevement acpv = new AccessPrelevement();
				acpv.updatePrelevement(result);
				
			}catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dbs, e);
				ed.doExceptionDialog();
				this.primaryStage.close();
				result = null;
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dbs, ae);
				ed.doExceptionDialog();
				result = null;
			}
		}
		
		return result;
	}
	
	/**
	 * Suppression d'un PrelevementAutomatique à partir de son id.
	 *
	 * @param idPrelev id du prélèvement (clé primaire)
	 * @throws RowNotFoundOrTooManyRowsException
	 * @throws DataAccessException
	 * @throws DatabaseConnexionException
	 * @throws ManagementRuleViolation
	 */
	public void supprimerPrelevement(int IdPrelev) throws RowNotFoundOrTooManyRowsException, DataAccessException,
	DatabaseConnexionException, ManagementRuleViolation {
		try {

			Connection con = LogToDatabase.getConnexion();

			String query = "DELETE FROM PrelevementAutomatique WHERE idPrelev = ?";

			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, IdPrelev);
			
			System.err.println(query);

			int result = pst.executeUpdate();
			pst.close();
			if (result != 1) {
				con.rollback();
				throw new RowNotFoundOrTooManyRowsException(Table.PrelevementAutomatique, Order.DELETE,
						"delete anormal (delete de moins ou plus d'une ligne)", null, result);
			}
			con.commit();
		} catch (SQLException e) {
			throw new DataAccessException(Table.PrelevementAutomatique, Order.DELETE, "Erreur accès", e);
		}
	}
	
	/**
	 * @return une liste de prélèvement en fonction du compte sélectionné
	 */
	public ArrayList<PrelevementAutomatique> getPrelevement() {
		ArrayList<PrelevementAutomatique> listeP = new ArrayList<>();

		try {
			AccessPrelevement ap = new AccessPrelevement();
			listeP = ap.getPrelevements(this.compte.idNumCompte);
		} catch (DatabaseConnexionException e) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dbs, e);
			ed.doExceptionDialog();
			this.primaryStage.close();
			listeP = new ArrayList<>();
		} catch (ApplicationException ae) {
			ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dbs, ae);
			ed.doExceptionDialog();
			listeP = new ArrayList<>();
		}
		return listeP;
	}
	
	/**
	 * @return une opération lorsqu'on effectue un prélèvement
	 */
	public Operation enregistrerPrelevement() {
		
		//int indiceErreur = 0;
		ArrayList<PrelevementAutomatique> numP = new ArrayList<PrelevementAutomatique>();
		OperationEditorPane oep = new OperationEditorPane(this.primaryStage, this.dbs);
		PrelevementEditorPane pep = new PrelevementEditorPane(this.primaryStage, this.dbs);
		Operation op = oep.doOperationEditorDialog(this.compte, CategorieOperation.PRELEVEMENT);
		if (op != null) {
			try {
				AccessPrelevement ap = new AccessPrelevement();
				numP = ap.getPrelevements(this.compte.idNumCompte);
				AccessOperation ao = new AccessOperation();
				for(int i=0;i<numP.size();i++) {
					if(pep.getPepc().getJourDate() == numP.get(i).dateRecurrente) {
						ao.insertDebit(this.compte.idNumCompte, op.montant, op.idTypeOp);
						//indiceErreur = 1;
					}
				}
			/*	if(indiceErreur == 0) {
					Alert dialog = new Alert(AlertType.INFORMATION);
					dialog.setTitle("Erreur numéro de compte");
					dialog.setHeaderText("Impossible de faire un virement vers un compte qui ne vous appartient pas !");
					dialog.showAndWait();
				}*/
			} catch (DatabaseConnexionException e) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dbs, e);
				ed.doExceptionDialog();
				this.primaryStage.close();
				op = null;
			} catch (ApplicationException ae) {
				ExceptionDialog ed = new ExceptionDialog(this.primaryStage, this.dbs, ae);
				ed.doExceptionDialog();
				op = null;
			}
		}
		return op;
	}

}
