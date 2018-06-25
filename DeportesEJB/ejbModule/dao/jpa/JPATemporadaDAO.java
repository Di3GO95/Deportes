package dao.jpa;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import dao.DAOException;
import dao.TemporadaDAO;
import model.Partido;
import model.Temporada;
import model.User;

public class JPATemporadaDAO implements TemporadaDAO {
	private EntityManagerFactory emf;
	
	public JPATemporadaDAO(EntityManagerFactory emf) {
		this.emf = emf;
	}
	
	@Override
	public int registrarTemporada(String nombre, String lugarCelebracion, int minParticipantesPorPartido) throws DAOException {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		
		Temporada temporada = new Temporada(nombre, lugarCelebracion, minParticipantesPorPartido);
		EntityTransaction tx = em.getTransaction ();
		
		tx.begin();
		em.persist(temporada);
		tx.commit();
		em.close();
		
		return temporada.getId();
	}
	
	@Override
	public Temporada getTemporada(int idTemporada) throws DAOException {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		
		Temporada t = null;
		t = em.find(Temporada.class, idTemporada);
		
		em.close();
		
		return t;
	}

	@Override
	public boolean addParticipante(int idTemporada, String usuario) throws DAOException {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		
		Temporada temporada = em.find(Temporada.class, idTemporada);
		User user = em.find(User.class, usuario);
		
		EntityTransaction tx = em.getTransaction ();
		tx.begin();
		
		temporada.getUsuarios().add(user);
		user.getTemporadas().add(temporada);
		
		tx.commit();
		em.close();
		
		return true;
	}

	@Override
	public boolean removeParticipante(int idTemporada, String usuario) throws DAOException {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		
		Temporada temporada = em.find(Temporada.class, idTemporada);
		User user = em.find(User.class, usuario);
		
		EntityTransaction tx = em.getTransaction ();
		tx.begin();
		
		boolean usuarioEncontrado = false;
		int i = -1;
		while (i < temporada.getUsuarios().size() && !usuarioEncontrado) {
			i++;
			if (temporada.getUsuarios().get(i).getUsuario().equals(usuario))
				usuarioEncontrado = true;
		}
		
		if (!usuarioEncontrado) {
			tx.commit();
			em.close();
			return false;
		}
		
		temporada.getUsuarios().remove(i);
		
		i = -1;
		boolean temporadaEncontrada = false;
		while (i < user.getTemporadas().size() && !temporadaEncontrada){
			i++;
			if (user.getTemporadas().get(i).getId() == idTemporada)
				temporadaEncontrada = true;
		}
		
		if (!temporadaEncontrada) {
			tx.commit();
			em.close();
			
			return false;
		}
		
		user.getTemporadas().remove(i);
		
		tx.commit();
		em.close();
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isUserParticipanteInTemporada(int idTemporada, String usuario) throws DAOException {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		
		String jpwl = "SELECT t.usuarios "
					+ "FROM Temporada t JOIN t.usuarios u "
					+ "WHERE u.usuario = :idUsuario AND t.id = :idTemporada";
		Query query = em.createQuery(jpwl);
		query.setParameter("idUsuario", usuario);
		query.setParameter("idTemporada", idTemporada);
		
		List<User> listaUsuarios = null;
		listaUsuarios = query.getResultList(); 
		
		if (listaUsuarios.isEmpty())
			return false;
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Temporada> getTodasLasTemporadas() throws DAOException {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		
		String jpwl = "SELECT t "
					+ "FROM Temporada t";
		Query query = em.createQuery(jpwl);
		
		List<Temporada> listaTemporadas = null;
		listaTemporadas = query.getResultList(); 
		
		em.close();
		
		return listaTemporadas;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getParticipantesEnTemporada(String temporadaElegida) throws DAOException {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		
		String jpwl = "SELECT t.usuarios "
					+ "FROM Temporada t "
					+ "WHERE t.nombre = :temporadaElegida";
		Query query = em.createQuery(jpwl);
		query.setParameter("temporadaElegida", temporadaElegida);
		
		List<User> listaUsuarios;
		listaUsuarios = query.getResultList(); 
		
		em.close();
		
		List<String> listaUsuariosString = new LinkedList<>();
		for (User user : listaUsuarios) {
			if (user != null) {
				listaUsuariosString.add(user.getUsuario());
			}
		}
		
		return listaUsuariosString;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getNombreDeTodasLasTemporadas() throws DAOException {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		
		String jpwl = "SELECT t.nombre "
					+ "FROM Temporada t";
		Query query = em.createQuery(jpwl);
		
		List<String> listaTemporadas;
		listaTemporadas = query.getResultList(); 
		
		em.close();
		
		return listaTemporadas;
	}
	
	@Override
	public int getIdTemporadaFromNombre(String temporada) throws DAOException {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		
		String jpwl = "SELECT t.id "
					+ "FROM Temporada t "
					+ "WHERE t.nombre = :temporada";
		Query query = em.createQuery(jpwl);
		query.setParameter("temporada", temporada);
		
		int id = (int) query.getSingleResult();
		
		em.close();
		
		return id;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Partido> getPartidosDeTemporada(int idTemporada) throws DAOException {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		
		String jpwl = "SELECT t.partidos "
					+ "FROM Temporada t "
					+ "WHERE t.id = :idTemporada";
		Query query = em.createQuery(jpwl);
		query.setParameter("idTemporada", idTemporada);
		
		List<Partido> listaPartidos;
		listaPartidos = query.getResultList(); 
		
		em.close();
		
		List<Partido> listaADevolver = new LinkedList<>();
		for (Partido partido : listaPartidos) {
			if (partido != null)
				listaADevolver.add(partido);
		}
		
		return listaADevolver;
	}
	

}
