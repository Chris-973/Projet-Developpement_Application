package blackjack.model.view;

import java.util.Optional;

import blackjack.om.BlackBot;
import blackjack.om.EtatBlackBot;
import blackjack.om.MainBlackjack;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class BlackJackController {
	// solde des joueurs 
	private double soldeJ1 = 100;
	private double soldeJ2 = 100;
	
	private BlackBot bot = new BlackBot(2);
	// sauvegarde des mises des joueurs
	private int saveMiseJ1 = 0;
	private int saveMiseJ2 = 0;
	
	private MainBlackjack mainJ1 = new  MainBlackjack();
	private MainBlackjack mainJ2 = new MainBlackjack();
	private MainBlackjack mainCroupier = new MainBlackjack();

	@FXML
	private Button quitterButton;

	@FXML
	private Button rejouerButton;

	@FXML
	private Button distribuerButton;

	@FXML
	private Button clearButton;

	@FXML
	private Button tirerButtonJ1;

	@FXML
	private Button passerButtonJ1;

	@FXML
	private MenuItem mainButtonJ1;

	@FXML
	private MenuItem gainButtonJ1;

	@FXML
	private MenuItem soldeButtonJ1;

	@FXML
	private Button moinsButtonJ1;

	@FXML
	private Button moinsButtonJ2;

	@FXML
	private Button confirmeButtonJ1;

	@FXML
	private Button plusButtonJ1;

	@FXML
	private TextField miseJ1;

	@FXML
	private ListView<String> J1Panel;

	@FXML
	private ListView<String> J2Panel;

	@FXML
	private TextField miseJ2;

	@FXML
	private Button plusButtonJ2;

	@FXML
	private Button confirmeButtonJ2;

	@FXML
	private Button tirerButtonJ2;

	@FXML
	private Button passerButtonJ2;

	@FXML
	private MenuItem mainButtonJ2;

	@FXML
	private MenuItem gainButtonJ2;

	@FXML
	private MenuItem soldeButtonJ2;

	@FXML
	private ListView<String> infoPanel;

	@FXML
	private TextField etatPanel;

	@FXML
	private ListView<String> croupierPanel;
	
	/*
	 * permet d'augmenter la mise du joueur 1 de 10
	 * Utilisable uniquement en etat MISE 
	 * la mise ne peux pas être négatif ou dépasser le solde du joueur
	 */
	@FXML
	void augmenter1(ActionEvent event) {
	
		int nb = Integer.parseInt( this.miseJ1.getText() );
		if(nb < this.soldeJ1) {
			// le montant de la mise s'incrémente de 10
			this.miseJ1.setText(""+(nb+10));
		}
	}
	
	/*
	 * permet d'augmenter la mise du joueur 2 de 10
	 * Utilisable uniquement en etat MISE 
	 * la mise ne peux pas être négatif ou dépasser le solde du joueur
	 */
	@FXML
	void augmenterJ2(ActionEvent event) {
		
		int nb = Integer.parseInt( this.miseJ2.getText() );
		if(nb < this.soldeJ2) {
			// le montant de la mise s'incrémente de 10
			this.miseJ2.setText(""+(nb+10));
		}
	}
	
	/*
	 * Permet de supprimer les messages des ListView
	 */
	@FXML
	void clear(ActionEvent event) {
		this.croupierPanel.getItems().clear();
		this.infoPanel.getItems().clear();
		this.J1Panel.getItems().clear();
		this.J2Panel.getItems().clear();
	}
	
	/*
	 * Permet d'afficher les règles du jeu
	 */
	@FXML
    void afficherRegle(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Règle du BlackJack");
		//alert.setHeaderText("");
		alert.setContentText("RAPPROCHEZ-VOUS DU 21 SANS JAMAIS LE DÉPASSER\n\n"
				+ "Il consiste à battre la Banque, représentée par le Croupier, sans dépasser 21 sinon vous perdez votre mise. Si vous atteignez le Blackjack (soit une carte valant 10 + un As) votre mise est payée x2.5, si vous gagnez contre le Croupier, mais sans atteindre 21 points, vous remportez 2 fois votre mise.\n\n"
				+ "VALEUR DES CARTES\n\n"
				+ "- Du 2 au 9 : chaque carte a sa propre valeur nominale.\r\n"
				+ "- Les 10, Valets, Dames et Rois valent 10 et sont appelés les « Bûches ».\r\n"
				+ "- Les As équivalent à 1 ou à 11 selon le jeu du joueur Si votre main ne dépasse pas 21, l’as compte 11. Si au contraire elle le dépasse, l’As compte pour 1; la valeur de l’As étant toujours calculée à votre avantage.\r\n"
				+ "- La main appelée « Blackjack » est composée d’un As et d’une carte valant 10, pour un total de 21, reçues dès le début.");
		
		Optional<ButtonType> result = alert.showAndWait();
    }
	
	/*
	 * Permet de confirmer la mise du joueur 1 
	 * Utilisable uniquement en etat MISE
	 */
	@FXML
	void confirmerJ1(ActionEvent event) {
		String info = "";

		
		this.distribuerButton.setDisable(false);
		int nb = Integer.parseInt( this.miseJ1.getText() );
		// le montant de la mise remis à 0
		this.miseJ1.setText(""+0);
		bot.miser(0, nb);

		info += "la mise du joueur 1 est " + nb;
		this.saveMiseJ1 = nb;
		this.J1Panel.getItems().add(info);
	}

	/*
	 * Permet de confirmer la mise du joueur 2
	 * Utilisable uniquement en etat MISE
	 */
	@FXML
	void confirmerJ2(ActionEvent event) {	
		String info = "";

		
		this.distribuerButton.setDisable(false);
		int nb = Integer.parseInt( this.miseJ2.getText() );
		// le montant de la mise remis à 0
		this.miseJ2.setText(""+0);
		bot.miser(1, nb);

		info += "la mise du joueur 2 est " + nb;
		this.saveMiseJ2 = nb;
		this.J2Panel.getItems().add(info);
	}

	/*
	 * permet de diminuer la mise du joueur 1 de 10
	 * Utilisable uniquement en etat MISE 
	 * la mise ne peux pas être négatif
	 */
	@FXML
	void diminuerJ1(ActionEvent event) {
		int nb = Integer.parseInt( this.miseJ1.getText() );
		// check si le montant de la mise est inférieur à 1
		if(nb < 1) {
			this.miseJ1.setText(""+0);
		} else {
			this.miseJ1.setText(""+(nb-10));
		}
	}
	
	/*
	 * permet de diminuer la mise du joueur 2 de 10
	 * Utilisable uniquement en etat MISE 
	 * la mise ne peux pas être négatif
	 */
	@FXML
	void diminuerJ2(ActionEvent event) {
		int nb = Integer.parseInt( this.miseJ2.getText() );
		// check si le montant de la mise est inférieur à 1
		if(nb < 1) {
			this.miseJ2.setText(""+0);
		} else {
			this.miseJ2.setText(""+(nb-10));
		}
	}
	
	/*
	 * Permet de distribuer les carte (démarrer la partie)
	 * l'etat passe en JEU
	 */
	@FXML
	void distribuer(ActionEvent event) {
		String info = "";
		// check si un des joueur a miser
		if(this.bot.getMiseJoueurs(0) > 0 || this.bot.getMiseJoueurs(1) > 0) {
			this.bot.distribuer();
			
			this.plusButtonJ1.setDisable(true);
			this.plusButtonJ2.setDisable(true);
			this.moinsButtonJ1.setDisable(true);
			this.moinsButtonJ2.setDisable(true);
			this.confirmeButtonJ1.setDisable(true);
			this.confirmeButtonJ2.setDisable(true);

			this.tirerButtonJ1.setDisable(false);
			this.tirerButtonJ2.setDisable(false);
			this.passerButtonJ1.setDisable(false);
			this.passerButtonJ2.setDisable(false);
			this.gainButtonJ1.setDisable(false);
			this.gainButtonJ2.setDisable(false);
			this.mainButtonJ1.setDisable(false);
			this.mainButtonJ2.setDisable(false);


			this.miseAjourEtat();
			this.distribuerButton.setDisable(true);
			this.mainJ1 = this.bot.getMainJoueurs(0);
			this.mainJ2 = this.bot.getMainJoueurs(1);
			this.mainCroupier = this.bot.getMainBanque();

			info = "_____TOUR 1_____\n" + "Main joueur 1 : " + this.mainJ1.toString();
			this.J1Panel.getItems().add(info);

			info = "_____TOUR 1_____\n" + "Main joueur 2 : " + this.mainJ2.toString();
			this.J2Panel.getItems().add(info);

			info = "_____TOUR 1_____\n" + "Main Croupier : " + this.mainCroupier.toString();
			this.croupierPanel.getItems().add(info);

		}

	}
	
	/*
	 * Permet de mettre a jour le textField etat selon l'etat du jeu
	 */
	void miseAjourEtat() {
		// check si l'etat est MISE
		if(this.bot.getEtat() == EtatBlackBot.MISE) {
			this.etatPanel.setText("MISE");
			
			// Désactivation des boutons qui ne peuvent pas être utiliser
			this.plusButtonJ1.setDisable(false);
			this.plusButtonJ2.setDisable(false);
			this.moinsButtonJ1.setDisable(false);
			this.moinsButtonJ2.setDisable(false);
			this.confirmeButtonJ1.setDisable(false);
			this.confirmeButtonJ2.setDisable(false);
			// activation des boutons qui peuvent être utiliser
			this.rejouerButton.setDisable(true);
			this.gainButtonJ1.setDisable(true);
			this.gainButtonJ2.setDisable(true);
			this.mainButtonJ1.setDisable(true);
			this.mainButtonJ2.setDisable(true);
		}
		// check si l'etat est JEU
		if(this.bot.getEtat() == EtatBlackBot.JEU) {
			this.etatPanel.setText("JEU");
		}
		if(this.bot.getEtat() == EtatBlackBot.GAIN) {
			// check si l'etat est GAIN
			this.etatPanel.setText("GAIN");

			this.tirerButtonJ1.setDisable(true);
			this.tirerButtonJ2.setDisable(true);
			this.passerButtonJ1.setDisable(true);
			this.passerButtonJ2.setDisable(true);
			this.rejouerButton.setDisable(false);

		}
	}
	
	/*
	 * Permet au joueur 1 de ne pas demander de cartes au croupier
	 */
	@FXML
	void passerJ1(ActionEvent event) {
		String info = "";
		// check si le joueur a miser
		if(this.bot.getMiseJoueurs(0) == 0) {
			info = "Le Joueur 1 ne participe pas";
			this.infoPanel.getItems().add(info);
		} else {
			// check si le joueur a terminer son tour 
			if(this.bot.getFinJoueurs(0) == false) {
				this.bot.terminer(0);
				this.miseAjourEtat();
				info = "Le Joueur 1 passe son tour";
				this.infoPanel.getItems().add(info);
				info = "_____Joueur 1 passe_____\n" + this.bot.getMainBanque();
				this.croupierPanel.getItems().add(info);
			} else {
				info = "Le Joueur 1 a déjà terminer son tour";
				this.infoPanel.getItems().add(info);
			}

		}

	}
	
	/*
	 * Permet au joueur 2 de ne pas demander de cartes au croupier
	 */
	@FXML
	void passerJ2(ActionEvent event) {
		String info = "";
		// check si le joueur a miser
		if(this.bot.getMiseJoueurs(1) == 0) {
			info = "Le Joueur 2 ne participe pas";
			this.infoPanel.getItems().add(info);
		} else {
			// check si le joueur a terminer son tour
			if(this.bot.getFinJoueurs(1) == false) {
				this.bot.terminer(1);
				this.miseAjourEtat();
				info = "Le Joueur 2 passe son tour";
				this.infoPanel.getItems().add(info);
				info = "_____Joueur 2 passe_____\n" + this.bot.getMainBanque();
				this.croupierPanel.getItems().add(info);
			} else {
				info = "Le Joueur 2 a déjà terminer son tour";
				this.infoPanel.getItems().add(info);
			}

		}
	}
	
	/*
	 * Permet de quitter l'application
	 */
	@FXML
	void quitter(ActionEvent event) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Quitter le jeu");
		alert.setHeaderText("Voulez-vous vraiment quitter le jeu ?");
		alert.setContentText("Toutes progression non sauvegarder sera effacer");

		Optional<ButtonType> result = alert.showAndWait();
		if(result.isPresent() && result.get() == ButtonType.OK) {
			// mauvaise méthode ?
			System.exit(0);
		}
	}
	
	/*
	 * Permet de rejouer une partie 
	 * Utilisable uniquement en etat GAIN
	 * les gains/mises sont ajouter/retirer du solde des joueurs  
	 */
	@FXML
	void rejouer(ActionEvent event) {
		String info = "Nouvelle partie";		

		if(this.saveMiseJ1 > 0) {
			// check si le joueur a gagner
			if(this.bot.getGainJoueurs(0) > 0) {
				this.soldeJ1 += this.bot.getGainJoueurs(0);
				info = "Le joueur 1 a gagner cette partie, ses gains ont été déposés sur son compte";
				this.infoPanel.getItems().add(info);
			} else if(this.bot.getGainJoueurs(0) == 0) {
				this.soldeJ1 -= this.saveMiseJ1;
				info = "Le joueur 1 a perdu cette partie, sa mise a été retirer de son compte";
				this.infoPanel.getItems().add(info);
			}
		}
		
		if(this.saveMiseJ2 > 0) {
			if(this.bot.getGainJoueurs(1) > 0) {
				this.soldeJ2 += this.bot.getGainJoueurs(1);
				info = "Le joueur 2 a gagner cette partie, ses gains ont été déposés sur son compte";
				this.infoPanel.getItems().add(info);
			} else if(this.bot.getGainJoueurs(1) == 0) {
				this.soldeJ2 -= this.saveMiseJ2;
				info = "Le joueur 2 a perdu cette partie, sa mise a été retirer de son compte";
				this.infoPanel.getItems().add(info);
			}
		}
		
		this.bot.relancerPartie();
		this.miseAjourEtat();

	}
	
	/*
	 * Permet au joueur 1 de demander une carte au croupier
	 * Utilisable uniquement en etat JEU
	 */
	@FXML
	void tirerJ1(ActionEvent event) {
		String info = "";
		// check si le joueur a miser
		if(this.bot.getMiseJoueurs(0) == 0) {
			info = "Le joueur 1 ne participe pas";
			this.infoPanel.getItems().add(info);
		}
		// check su le joueur a fini son tour 
		if(this.bot.getFinJoueurs(0) == false && this.bot.getMiseJoueurs(0) > 0) {
			this.bot.tirer(0);
			this.miseAjourEtat();
			this.mainJ1 = this.bot.getMainJoueurs(0);
			this.mainCroupier = this.bot.getMainBanque();
			info = "_____Tirage Joueur 1_____"
					+ "\nMain joueur 1 : " + this.mainJ1.toString();
			this.J1Panel.getItems().add(info);		

			info = "_____Tirage Joueur 1_____\n"
					+ "Main du Croupier : " + this.mainCroupier.toString();
			this.croupierPanel.getItems().add(info);


		} else {
			if(this.bot.getMiseJoueurs(0) > 0) {
				info = "Le joueur 1 a soit perdu ou fini son tour";
				this.infoPanel.getItems().add(info);
			}
		}
	}
	
	/*
	 * Permet au joueur 2 de demander une carte au croupier
	 * Utilisable uniquement en etat JEU
	 */
	@FXML
	void tirerJ2(ActionEvent event) {
		String info = "";
		// check si le joueur a miser
		if(this.bot.getMiseJoueurs(1) == 0) {
			info = "Le joueur 2 ne participe pas";
			this.infoPanel.getItems().add(info);
		}
		// check si le joueur a fini son tour ou pas
		if(this.bot.getFinJoueurs(1) == false && this.bot.getMiseJoueurs(1) > 0) {
			this.bot.tirer(1);
			this.miseAjourEtat();
			this.mainJ2 = this.bot.getMainJoueurs(1);
			this.mainCroupier = this.bot.getMainBanque();
			info = "_____Tirage Joueur 2_____"
					+ "\nMain joueur 2 : " + this.mainJ2.toString();
			this.J2Panel.getItems().add(info);		

			info = "_____Tirage Joueur 2_____\n"
					+ "Main du Croupier : " + this.mainCroupier.toString();
			this.croupierPanel.getItems().add(info);


		} else {
			if(this.bot.getMiseJoueurs(1) > 0) {
				info = "Le joueur 2 a soit perdu ou fini son tour";
				this.infoPanel.getItems().add(info);
			}
		}
	}
	
	/*
	 * Afficher les gains du joueur 1 
	 */
	@FXML
	void voirGainJ1(ActionEvent event) {
		String info = "Gain du Joueur 1 : " + this.bot.getGainJoueurs(0);
		this.J1Panel.getItems().add(info);
	}
	
	/*
	 * Afficher les gains du joueur 2
	 */
	@FXML
	void voirGainJ2(ActionEvent event) {
		String info = "Gain du Joueur 2 : " + this.bot.getGainJoueurs(1);
		this.J2Panel.getItems().add(info);
	}
	
	/*
	 * Afficher la mains du joueur 1
	 */
	@FXML
	void voirMainJ1(ActionEvent event) {
		String info = "Main du Joueur 1 : " + this.bot.getMainJoueurs(0);
		this.J1Panel.getItems().add(info);
		info = "Main du Croupier : " + this.bot.getMainBanque();
		this.croupierPanel.getItems().add(info);
	}
	
	/*
	 * Affiche la mains du joueur 2
	 */
	@FXML
	void voirMainJ2(ActionEvent event) {
		String info = "Main du Joueur 2 : " + this.bot.getMainJoueurs(1);
		this.J2Panel.getItems().add(info);
		info = "Main du Croupier : " + this.bot.getMainBanque();
		this.croupierPanel.getItems().add(info);
	}
	
	/*
	 * Affiche le solde du joueur 1
	 */
	@FXML
	void voirSoldeJ1(ActionEvent event) {
		String info = "Le solde du joueur 1 est : " + this.soldeJ1;
		this.J1Panel.getItems().add(info);
	}
	
	/*
	 * Affiche le solde du joueur 2
	 */
	@FXML
	void voirSoldeJ2(ActionEvent event) {
		String info = "Le solde du joueur 2 est : " + this.soldeJ2;
		this.J2Panel.getItems().add(info);
	}

}
