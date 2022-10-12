package model.data;


/**
 * @author ruben
 * regroupe des informations nécesaires pour simuler un emprunt et une assurance d'emprunt
 */
public class Emprunt {
	//attributs
	public double capitalEmprunt;
	public int dureeEmprunt;
	public double tauxPretAnnuel;
	public double tauxAssurance;
	
	//constructeur de l'emprunt
	public Emprunt(double capital, int duree, double tauxPret) {
		this.capitalEmprunt = capital;
		this.dureeEmprunt = duree;
		this.tauxPretAnnuel = tauxPret;
	}
	
	//constructeur de l'assurance d'emprunt
	public Emprunt(double capital, double tauxAss) {
		this.capitalEmprunt = capital;
		this.tauxAssurance = tauxAss;
	}

	//methodes pour l'emprunt
	/**
	 * calcule le taux applicable d'un emprunt
	 * @return taux applicable
	 */
	public double getTauxApplicable() {
		return this.tauxPretAnnuel/100/12;
	}
	
	/**
	 * calcule le nombre de periodes (en nombre de mois) de la durée de l'emprunt
	 * @return le nombre de périodes
	 */
	public int getNbPeriode() {
		return this.dureeEmprunt*12;
	}
	
	
	/**
	 * calcule la mensualité de l'emprunt sans l'ajout de la mensuatlité d'une assurance
	 * @return mensualité de l'emprunt
	 */
	public double getMensualiteSansAss() {
		return this.capitalEmprunt*(this.getTauxApplicable()/ (1-Math.pow(1+this.getTauxApplicable(), -this.getNbPeriode())) );
	}
	
	
	/**
	 * calcule la somme des intêrets sur toute la durée de l'emprunt
	 * @return coût du crédit
	 */
	public double coutCredit() {
		double capitalRestant = this.capitalEmprunt;
		double res = 0;
		
		do {
			double interet = capitalRestant*this.getTauxApplicable(); //interets sur une periode
			double montantDuPricipal = this.getMensualiteSansAss() - interet; //montant du principal
			capitalRestant = capitalRestant - montantDuPricipal; //captial restant sur une periode
			res+=interet;
		}while(capitalRestant>0);
		
		
		return res;
	}
	
	//methodes pour l'assurance
	public double getMensualiteAssurance() {
		return this.tauxAssurance / 100 * (this.capitalEmprunt / 12);
	}
	
	
	
}
