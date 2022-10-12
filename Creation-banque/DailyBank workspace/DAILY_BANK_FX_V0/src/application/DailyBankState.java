package application;

import application.tools.ConstantesIHM;
import model.data.AgenceBancaire;
import model.data.Employe;

public class DailyBankState {
	private Employe empAct;
	private AgenceBancaire agAct;
	private boolean isChefDAgence;
	
	/**
	 * Permet d'obtenir l'employé actif
	 * @return l'employé actif
	 */
	public Employe getEmpAct() {
		return this.empAct;
	}
	
	/**
	 * Permet de définir l'employé actif
	 * @param employeActif : l'employé qui se connecte
	 */
	public void setEmpAct(Employe employeActif) {
		this.empAct = employeActif;
	}

	/**
	 * Permet d'obtenir l'agence active
	 * @return l'agence connectée
	 */
	public AgenceBancaire getAgAct() {
		return this.agAct;
	}

	/**
	 * Définit l'agence active
	 * @param agenceActive : l'agence connectée
	 */
	public void setAgAct(AgenceBancaire agenceActive) {
		this.agAct = agenceActive;
	}

	/**
	 * Renvoie si l'employé actif est le chef de l'agence
	 * @return un booléen qui vérifie si c'est le chef d'agence ou non
	 */
	public boolean isChefDAgence() {
		return this.isChefDAgence;
	}

	/**
	 * Permet de définir un employé en tant que chef d'agence si celui-ci est bien chef d'agence
	 * @param isChefDAgence : booléen qui permet de vérifier si la personne connectée est un chef d'agence 
	 */
	public void setChefDAgence(boolean isChefDAgence) {
		this.isChefDAgence = isChefDAgence;
	}

	/**
	 * Permet de définir un employé en tant que chef d'agence selon si il possède certains droits d'accès
	 * @param droitsAccesss : Chaîne de caractères correspondant à un droit d'accès particulier
	 */
	public void setChefDAgence(String droitsAccess) {
		this.isChefDAgence = false;

		if (droitsAccess.equals(ConstantesIHM.AGENCE_CHEF)) {
			this.isChefDAgence = true;
		}
	}
}
