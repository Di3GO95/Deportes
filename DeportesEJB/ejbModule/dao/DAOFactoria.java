package dao;

import dao.jdbc.JDBCDAOFactoria;
import dao.jpa.JPADAOFactoria;


public class DAOFactoria {
	// Metodos factoria
	public UsuarioDAO getUsuarioDAO() {return null;}
	public PartidoDAO getPartidoDAO() {return null;}
	public AlineacionDAO getAlineacionDAO() {return null;}
	public TemporadaDAO getTemporadaDAO() {return null;}

	// get temporada, partido, alineacion DAO
	// Declaracion como constantes de los tipos de factoria
	public final static int JDBC = 1;
	public final static int JPA = 2;

	private static DAOFactoria unicaInstancia;

	protected DAOFactoria() {}

	public static DAOFactoria getDAOFactoria() {
		if (unicaInstancia == null)
			unicaInstancia = new DAOFactoria();
		return unicaInstancia;
	}

	public static void setDAOFactoria(int tipo) throws DAOException {
		switch (tipo) {
			case JDBC: {
				try {
					unicaInstancia = new JDBCDAOFactoria();
					break;
				} catch (Exception e) {
					throw new DAOException(e.getMessage());
				}
			}
			case JPA: {
				try {
					unicaInstancia = new JPADAOFactoria();
					break;
				} catch (Exception e) {
					throw new DAOException(e.getMessage());
				}
			}
		}
	}
}