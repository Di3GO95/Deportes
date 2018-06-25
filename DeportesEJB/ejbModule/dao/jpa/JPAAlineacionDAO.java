package dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import dao.AlineacionDAO;
import dao.DAOException;
import model.Alineacion;
import model.Color;
import model.Partido;
import model.User;

public class JPAAlineacionDAO implements AlineacionDAO {
	private EntityManagerFactory emf;
	
	public JPAAlineacionDAO(EntityManagerFactory emf) {
		this.emf = emf;
	}
	
	@Override
	public boolean registrarAlineacion(String nombre, Color color, int idPartido, List<String> listaUsuarios) throws DAOException {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		
		Alineacion alineacion = new Alineacion();
		alineacion.setNombre(nombre);
		alineacion.setColor(color);
		
		EntityTransaction tx = em.getTransaction ();
		tx.begin();
		
		Partido partido = em.find(Partido.class, idPartido);
		
		partido.getAlineaciones().add(alineacion);
		alineacion.setPartido(partido);
		
		for (String usuarioString : listaUsuarios) {
			User user = em.find(User.class, usuarioString);
			user.getAlineaciones().add(alineacion);
			
			alineacion.getUsuarios().add(user);
		}
		
		em.persist(alineacion);
		tx.commit();
		em.close();
		
		return true;
	}
	
	@Override
	public void introducirResultado(int idAlineacion, int resultado) throws DAOException {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		EntityTransaction tx = em.getTransaction ();
		tx.begin();
		
		Alineacion alineacion = em.find(Alineacion.class, idAlineacion);
		alineacion.setTanteo(resultado);
		
		tx.commit();
		em.close();
	}


}
