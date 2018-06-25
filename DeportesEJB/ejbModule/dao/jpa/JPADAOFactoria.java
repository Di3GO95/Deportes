package dao.jpa;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import dao.AlineacionDAO;
import dao.DAOFactoria;
import dao.PartidoDAO;
import dao.TemporadaDAO;
import dao.UsuarioDAO;

public class JPADAOFactoria extends DAOFactoria {
	private EntityManagerFactory emf;

	public JPADAOFactoria() {
		String nombre = "DeportesEJB"; // Mismo nombre que el atributo name de persistence.xml
		emf = Persistence.createEntityManagerFactory(nombre); 
	}
	
	@Override
	public UsuarioDAO getUsuarioDAO() {
		return new JPAUsuarioDAO(emf);
	}
	
	@Override
	public PartidoDAO getPartidoDAO() {
		return new JPAPartidoDAO(emf);
	}
	
	@Override
	public AlineacionDAO getAlineacionDAO() {
		return new JPAAlineacionDAO(emf);
	}
	
	@Override
	public TemporadaDAO getTemporadaDAO() {
		return new JPATemporadaDAO(emf);
	}

}
