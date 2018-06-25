package dao.jdbc;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import dao.DAOFactoria;
import dao.UsuarioDAO;

public class JDBCDAOFactoria extends DAOFactoria {

	private DataSource ds;
	
	public JDBCDAOFactoria() {
		try {
			Context ctx = new InitialContext();
			String nombre = "jdbc/aadd_jdbc";
			ds = (DataSource) ctx.lookup("java:comp/env/" + nombre);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public UsuarioDAO getUsuarioDAO() {
		return new JDBCUsuarioDAO(ds);
	}

}
