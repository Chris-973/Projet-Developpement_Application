package application.view;

import java.awt.Desktop;	
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;	
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import application.DailyBankState;
import application.control.OperationsManagement;
import application.control.PrelevementEditorPane;
import application.control.PrelevementManagement;
import application.control.SimulationEmpruntPane;
import application.tools.NoSelectionModel;
import application.tools.PairsOfValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.data.Client;
import model.data.CompteCourant;
import model.data.Operation;

public class OperationsManagementController implements Initializable {

	// Etat application
	private DailyBankState dbs;
	private OperationsManagement om;

	// Fenêtre physique
	private Stage primaryStage;

	// Données de la fenêtre
	private Client clientDuCompte;
	private CompteCourant compteConcerne;
	private ObservableList<Operation> olOperation;
	private PrelevementManagement pm;
	private PrelevementEditorPane pep;

	// Manipulation de la fenêtre
	public void initContext(Stage _primaryStage, OperationsManagement _om, DailyBankState _dbstate, Client client, CompteCourant compte) {
		this.primaryStage = _primaryStage;
		this.dbs = _dbstate;
		this.om = _om;
		this.clientDuCompte = client;
		this.compteConcerne = compte;
		this.configure();
	}

	private void configure() {
		this.primaryStage.setOnCloseRequest(e -> this.closeWindow(e));
		this.olOperation = FXCollections.observableArrayList();
		this.lvOperations.setItems(this.olOperation);
		this.lvOperations.setSelectionModel(new NoSelectionModel<Operation>());
		this.updateInfoCompteClient();
		this.validateComponentState();
		//this.doPrelevement();
	}

	public void displayDialog() {
		this.primaryStage.showAndWait();
	}
	
	private void doPrelevement() {
		boolean verif = this.pep.getPepc().getOperation();
		if(verif == true) {
			Operation op = this.pm.enregistrerPrelevement();
			if (op != null) {
				this.updateInfoCompteClient();
				this.validateComponentState();
			}
		}
	}


	// Gestion du stage
	private Object closeWindow(WindowEvent e) {
		this.doCancel();
		e.consume();
		return null;
	}

	// Attributs de la scene + actions
	@FXML
	private Label lblInfosClient;
	@FXML
	private Label lblInfosCompte;
	@FXML
	private ListView<Operation> lvOperations;
	@FXML
	private Button btnDebit;
	@FXML
	private Button btnCredit;
	@FXML
	private Button btnVirement;
	@FXML
	private Button btnGenererPdf;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML
	private void doCancel() {
		this.primaryStage.close();
	}

	@FXML
	private void doDebit() {

		Operation op = this.om.enregistrerDebit();
		if (op != null) {
			this.updateInfoCompteClient();
			this.validateComponentState();
		}
	}

	@FXML
	private void doCredit() {
		Operation op = this.om.enregistrerCredit();
		if (op != null) {
			this.updateInfoCompteClient();
			this.validateComponentState();
		}
	}

	@FXML
	private void doVirement(){
		Operation op = this.om.enregistrerVirement();
		if (op != null) {
			this.updateInfoCompteClient();
			this.validateComponentState();
		}
	}

	@FXML
	private void doAutre() {
	}

	@FXML
	private void doGenererPdf() {
		int selectedIndice = this.lvOperations.getSelectionModel().getSelectedIndex();
		Operation op = this.lvOperations.getSelectionModel().getSelectedItem();


		Document doc = new Document();
		try {
			PdfWriter.getInstance(doc, new FileOutputStream("relevé-bancaire-mensuel.pdf"));
			doc.open();

			Font titleFont = FontFactory.getFont(FontFactory.TIMES,40f);
			Font subTitleFont = FontFactory.getFont(FontFactory.TIMES,20f);
			Font contentFont = FontFactory.getFont(FontFactory.TIMES,14f);

			String infoCli = "Nom du client : " + this.clientDuCompte.nom + 
					"\nPrénom du client : " + this.clientDuCompte.prenom +
					"\nAdresse postal du client : " + this.clientDuCompte.adressePostale +
					"\nNuméro de téléphone du client : " + this.clientDuCompte.telephone +
					"\nMail du client : " + this.clientDuCompte.email;

			String infoCompte = "Numéro du compte : " + this.compteConcerne.idNumCompte +
					"\nDécouvert autorisé : " + this.compteConcerne.debitAutorise +
					"\nSolde du compte : " + this.compteConcerne.solde;
			String infoOperation = "";


			//Crée des paragraphes sur le doc pdf
			Paragraph para = new Paragraph("Relevé bancaire : DailyBank", titleFont);
			Paragraph ligne = new Paragraph("______________________", titleFont);
			Paragraph content = new Paragraph(infoCli, contentFont);
			Paragraph content2 = new Paragraph(infoCompte, contentFont);

			// Aligner les paragraphes au centre
			ligne.setAlignment(Element.ALIGN_CENTER);
			para.setAlignment(Element.ALIGN_CENTER);


			para.setFont(titleFont);
			content.setFont(contentFont);

			doc.add(para);
			doc.add(ligne);
			doc.add(Chunk.NEWLINE);
			doc.add(new Paragraph("Informations sur le client :", subTitleFont));
			doc.add(Chunk.NEWLINE);
			doc.add(content);
			doc.add(Chunk.NEWLINE);
			doc.add(new Paragraph("Informations sur le compte bancaire :", subTitleFont));
			doc.add(Chunk.NEWLINE);
			doc.add(content2);
			doc.add(Chunk.NEWLINE);
			doc.add(new Paragraph("Informations sur les opération du compte :", subTitleFont));
			doc.add(Chunk.NEWLINE);
			// On récupère la liste des opération du compte
			for(int i = 0; i < this.olOperation.size(); i++) {
				System.out.println(this.olOperation.get(i));
				String leleu = this.olOperation.get(i).toString();
				Paragraph content3 = new Paragraph(leleu, contentFont);
				doc.add(content3);
			}



			doc.close();

			try {
				Desktop.getDesktop().open(new File("Relevé-bancaire-mensuel.pdf"));
			} catch (IOException e) {

				e.printStackTrace();
			}
            
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch(DocumentException d) {
			d.printStackTrace();
		}

		/*
		 String pdf = "bilanSAE2.pdf";

		Document document = new Document(PageSize.A4);

		PdfWriter.getInstance(document, new FileOutputStream(pdf));

		document.open();

		document.add(new Phrase("hello world"));

		document.close();
		System.out.println("Document : " + pdf + " generated");

		System.out.println("voulez-vous générer un PDF ?");
		 */
	}


	private void validateComponentState() {
		this.btnCredit.setDisable(false);
		this.btnDebit.setDisable(false);
		this.btnVirement.setDisable(false);
		if(this.dbs.isChefDAgence()) {
			this.btnGenererPdf.setDisable(false);
		}else {
			this.btnGenererPdf.setDisable(true);
		}
	}

	private void updateInfoCompteClient() {

		PairsOfValue<CompteCourant, ArrayList<Operation>> opesEtCompte;

		opesEtCompte = this.om.operationsEtSoldeDunCompte();

		ArrayList<Operation> listeOP;
		this.compteConcerne = opesEtCompte.getLeft();
		listeOP = opesEtCompte.getRight();

		String info;
		info = this.clientDuCompte.nom + "  " + this.clientDuCompte.prenom + "  (id : " + this.clientDuCompte.idNumCli
				+ ")";
		this.lblInfosClient.setText(info);

		info = "Cpt. : " + this.compteConcerne.idNumCompte + "  "
				+ String.format(Locale.ENGLISH, "%12.02f", this.compteConcerne.solde) + "  /  "
				+ String.format(Locale.ENGLISH, "%8d", this.compteConcerne.debitAutorise);
		this.lblInfosCompte.setText(info);

		this.olOperation.clear();
		for (Operation op : listeOP) {
			this.olOperation.add(op);
		}

		this.validateComponentState();
	}
}