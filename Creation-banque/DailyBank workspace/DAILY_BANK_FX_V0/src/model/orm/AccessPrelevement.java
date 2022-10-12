package model.orm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.data.CompteCourant;
import model.data.PrelevementAutomatique;
import model.orm.exception.DataAccessException;
import model.orm.exception.DatabaseConnexionException;
import model.orm.exception.ManagementRuleViolation;
import model.orm.exception.Order;
import model.orm.exception.RowNotFoundOrTooManyRowsException;
import model.orm.exception.Table;

public class AccessPrelevement {
	
	public AccessPrelevement() {
	}
	
	
	/**
	 * Recherche des prélèvements d'un compte à partir de son id.
	 *
	 * @param idNumCompte id du compte dont on cherche les prélèvements
	 * @return Tous les PrelevementAutomatique de idNumCOmpte (ou liste vide)
	 * @throws DataAccessException
	 * @throws DatabaseConnexionException
	 */
	public ArrayList<PrelevementAutomatique> getPrelevements(int idNumCompte)
			throws DataAccessException, DatabaseConnexionException {
		ArrayList<PrelevementAutomatique> alResult = new ArrayList<>();

		try {
			Connection con = LogToDatabase.getConnexion();
			String query = "SELECT * FROM PrelevementAutomatique where idNumCompte = ?";
			query += " ORDER BY idPrelev";

			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, idNumCompte);
			System.err.println(query);

			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				int idPrelev = rs.getInt("idPrelev");
				double montant = rs.getDouble("montant");
				int dateRecurrente = rs.getInt("dateRecurrente");
				String beneficiaire = rs.getString("beneficiaire");
				int idNumCompteTROUVEE = rs.getInt("idNumCompte");

				alResult.add(new PrelevementAutomatique(idPrelev, montant, dateRecurrente, beneficiaire, idNumCompteTROUVEE));
			}
			rs.close();
			pst.close();
		} catch (SQLException e) {
			throw new DataAccessException(Table.PrelevementAutomatique, Order.SELECT, "Erreur accès", e);
		}

		return alResult;
	}
	
	
	/**
	 * Recherche d'un PrelevementAutomatique à partir de son id (idPrelev).
	 *
	 * @param idPrelev id du prélèvement (clé primaire)
	 * @return Le prélèvement ou null si non trouvé
	 * @throws RowNotFoundOrTooManyRowsException
	 * @throws DataAccessException
	 * @throws DatabaseConnexionException
	 */
	public PrelevementAutomatique getPrelevemenT(int idPrelev)
			throws RowNotFoundOrTooManyRowsException, DataAccessException, DatabaseConnexionException {
		try {
			PrelevementAutomatique pa;

			Connection con = LogToDatabase.getConnexion();

			String query = "SELECT * FROM PrelevementAutomatique where" + " idPrelev = ?";

			PreparedStatement pst = con.prepareStatement(query);
			pst.setInt(1, idPrelev);

			System.err.println(query);

			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				int idPrelevTROUVE = rs.getInt("idPrelev");
				double montant = rs.getDouble("montant");
				int dateRecurrente = rs.getInt("dateRecurrente");
				String beneficiaire = rs.getString("beneficiaire");
				int idNumCompteTROUVE = rs.getInt("idNumCompte");

				pa = new PrelevementAutomatique(idPrelevTROUVE, montant, dateRecurrente, beneficiaire, idNumCompteTROUVE);
			} else {
				rs.close();
				pst.close();
				return null;
			}

			if (rs.next()) {
				throw new RowNotFoundOrTooManyRowsException(Table.PrelevementAutomatique, Order.SELECT,
						"Recherche anormale (en trouve au moins 2)", null, 2);
			}
			rs.close();
			pst.close();
			return pa;
		} catch (SQLException e) {
			throw new DataAccessException(Table.PrelevementAutomatique, Order.SELECT, "Erreur accès", e);
		}
	}
	
	
	/**
	 * Mise à jour d'un PrelevementAutomatique.
	 *
	 * pa.IdPrelev (clé primaire) doit exister seul 
	 * pa.montant est mis à jour
	 * pa.dateRecurrente est mis à jour
	 * pa.beneficiaire mis à jour
	 * pa.idPrelev non mis à jour (un pa ne change pas d'identifiant)
	 * cc.idNumCompte non mis à jour (un pa ne change pas de compte)
	 *
	 * @param pa IN pa.idPrelev (clé primaire) doit exister seul
	 * @throws RowNotFoundOrTooManyRowsException
	 * @throws DataAccessException
	 * @throws DatabaseConnexionException
	 * @throws ManagementRuleViolation
	 */
	public void updatePrelevement(PrelevementAutomatique pa) 
			throws RowNotFoundOrTooManyRowsException, DataAccessException,
	DatabaseConnexionException, ManagementRuleViolation {
		try {

			Connection con = LogToDatabase.getConnexion();

			String query = "UPDATE PrelevementAutomatique SET " + "montant = ? , " + "dateRecurrente = " + "? , " 
					+ "beneficiaire = " + "?" + " " + "WHERE idPrelev = ?";

			PreparedStatement pst = con.prepareStatement(query);
			pst.setDouble(1, pa.montant);
			pst.setInt(2, pa.dateRecurrente);
			pst.setString(3, pa.beneficiaire);
			pst.setInt(4, pa.idPrelev);

			System.err.println(query);

			int result = pst.executeUpdate();
			pst.close();
			if (result != 1) {
				con.rollback();
				throw new RowNotFoundOrTooManyRowsException(Table.PrelevementAutomatique, Order.UPDATE,
						"Update anormal (update de moins ou plus d'une ligne)", null, result);
			}
			con.commit();
		} catch (SQLException e) {
			throw new DataAccessException(Table.PrelevementAutomatique, Order.UPDATE, "Erreur accès", e);
		}
	}
	
	

}
