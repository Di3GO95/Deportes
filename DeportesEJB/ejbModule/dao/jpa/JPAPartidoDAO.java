package dao.jpa;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import dao.DAOException;
import dao.PartidoDAO;
import model.Alineacion;
import model.Partido;
import model.Temporada;
import model.User;

public class JPAPartidoDAO implements PartidoDAO {
	private EntityManagerFactory emf;
	
	public JPAPartidoDAO(EntityManagerFactory emf) {
		this.emf = emf;
	}
	
	@Override
	public int registrarPartido(int idTemporada, Date fecha) throws DAOException {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		
		Partido p = new Partido();
		p.setFecha(fecha);
		
		EntityTransaction tx = em.getTransaction ();
		tx.begin();
		
		em.persist(p);
		
		Temporada t = em.find(Temporada.class, idTemporada);
		t.getPartidos().add(p);
		p.setTemporada(t);
		
		tx.commit();
		em.close();
		
		return p.getId();
	}
	
	@Override
	public Partido getPartido(int idPartido) throws DAOException {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		
		Partido p = null;
		p = em.find(Partido.class, idPartido);
		
		em.close();
		return p;
	}
	
	@Override
	public boolean confirmarAsistenciaAPartido(int idPartido, String nombreUsuario) throws DAOException {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		
		EntityTransaction tx = em.getTransaction ();
		tx.begin();
		
		Partido partido = em.find(Partido.class, idPartido);
		User usuario = em.find(User.class, nombreUsuario);
		
		partido.getAsistentes().add(usuario);
		usuario.getPartidos().add(partido);
		
		tx.commit();
		em.close();
		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUsuariosQueAsistenAPartido(int idPartido) throws DAOException {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		
		String jpwl = "SELECT p.asistentes "
					+ "FROM Partido p "
					+ "WHERE p.id = :id";
		Query query = em.createQuery(jpwl);
		query.setParameter("id", idPartido);
		
		List<User> listaUsuarios = null;
		listaUsuarios = query.getResultList(); 
		
		em.close();
		
		return listaUsuarios;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Partido> getTodosLosPartidos() throws DAOException {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		
		String jpwl = "SELECT p "
					+ "FROM Partido p";
		Query query = em.createQuery(jpwl);
		
		List<Partido> listaPartidos = null;
		listaPartidos = query.getResultList(); 
		
		em.close();
		
		return listaPartidos;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Partido> findPartidoByFechas(Date f1, Date f2) {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		
		Query query = em.createNamedQuery("findPartidoByFechas");
		query.setParameter("f1", f1);
		query.setParameter("f2", f2);
		
		List<Partido> listaPartidos;
		listaPartidos = query.getResultList(); 
		
		em.close();
		
		return listaPartidos;
	}
	
	@SuppressWarnings("unchecked")
	public List<Partido> findPartidoByFechasCRIT(Date f1, Date f2) {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Partido> criteria = builder.createQuery(Partido.class);
		Root<Partido> p = criteria.from(Partido.class);
		criteria.select(p).where(builder.between(p.get("fecha"), f1, f2));
		
		Query query = em.createQuery(criteria);
		List<Partido> listaPartidos;
		listaPartidos = query.getResultList(); 
		
		em.close();
		
		return listaPartidos;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getJugadoresDisponiblesEnPartido(int idPartido) throws DAOException {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}

		String jpwl = "SELECT p.asistentes " 
					+ "FROM Partido p "
					+ "WHERE p.id = :idPartido";
		Query query = em.createQuery(jpwl);
		query.setParameter("idPartido", idPartido);

		List<User> listaUsuarios = null;
		listaUsuarios = query.getResultList(); /* Jugadores que asisten al partido */

		em.close();

		List<String> listaJugadoresDisponibles = new LinkedList<>();
		for (User user : listaUsuarios) {
			if (user != null) {
				boolean anadir = true;
				List<Alineacion> alineacionesDeJugador = user.getAlineaciones();
				for (Alineacion alineacion : alineacionesDeJugador) {
					if (alineacion.getPartido().getId() == idPartido)
						anadir = false;
				}
				if (anadir)
					listaJugadoresDisponibles.add(user.getUsuario());
			}
		}
		
		return listaJugadoresDisponibles;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Alineacion> getAlineacionesFromPartido(int idPartido) throws DAOException {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		String jpwl = "SELECT p.alineaciones " 
					+ "FROM Partido p "
					+ "WHERE p.id = :idPartido";
		Query query = em.createQuery(jpwl);
		query.setParameter("idPartido", idPartido);

		List<Alineacion> listaAlineaciones = null;
		listaAlineaciones = query.getResultList(); 
		
		em.close();

		List<Alineacion> listaADevolver = new LinkedList<>();
		for (Alineacion a : listaAlineaciones) {
			if (a != null) {
				listaADevolver.add(a);
			}
		}
		
		return listaADevolver;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Partido> filtrarPartidos(String nombreTemporada, Date filtroFechaInicio, Date filtroFechaFin, String jugadorParticipante) throws DAOException {
		
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		
		String jpwl = "SELECT p "
					+ "FROM Partido p ";
		
		if (jugadorParticipante != null)
			jpwl += "JOIN p.asistentes u ";
		jpwl += "WHERE ";
		
		int whereCompuesto = 0; // WHERE _clausula_ AND _clausula_
		if (filtroFechaFin != null && filtroFechaInicio != null) {
			jpwl += "p.fecha >= :fechaInicio AND p.fecha <= :fechaFin";
			whereCompuesto = 1;
		}
		
		if (nombreTemporada != null) {
			if (whereCompuesto == 1) {
				jpwl += " AND ";
			}
			jpwl += "p.temporada.nombre = :nombreTemporada";
			whereCompuesto = 1;
		}
		
		
		if (jugadorParticipante != null) {
			if (whereCompuesto == 1) {
				jpwl += " AND ";
			}
			jpwl += "u.usuario = :nombreUsuario";
			whereCompuesto = 1;
		}
		
		Query query = em.createQuery(jpwl);
		if (nombreTemporada != null)
			query.setParameter("nombreTemporada", nombreTemporada);
		if (jugadorParticipante != null)
			query.setParameter("nombreUsuario", jugadorParticipante);
		if (filtroFechaFin != null && filtroFechaInicio != null) {
			query.setParameter("fechaInicio", filtroFechaInicio);
			query.setParameter("fechaFin", filtroFechaFin);
		}
		
		List<Partido> listaPartidos = null;
		listaPartidos = query.getResultList();
		
		/*
		 * Instanciamos la relación LAZY antes de enviarlo, si no, al acceder más tarde
		 * a las alineaciones de uno de los partidos, saltará excepción:
		 * 
		 * Exception Description: An attempt was made to traverse a relationship using indirection that had a null Session. 
		 * This often occurs when an entity with an uninstantiated LAZY relationship is serialized and that lazy relationship is traversed after serialization. 
		 * To avoid this issue, instantiate the LAZY relationship prior to serialization.
		 * 
		 */
		for (Partido partido : listaPartidos) {
			partido.getAlineaciones().size();
			
			for (Alineacion a : partido.getAlineaciones()) {
				a.getJugadores().size();
			}
		}
		
		return listaPartidos;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Partido> getPartidosPendientesDeRegistrarAlineacion(int idTemporada) throws DAOException {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}

		String jpwl = "SELECT p "
					+ "FROM Partido p "
					+ "WHERE p.temporada.id = :id";
		
		Query query = em.createQuery(jpwl);
		query.setParameter("id", idTemporada);
		
		List<Partido> listaPartidos = null;
		listaPartidos = query.getResultList();
		
		List<Partido> listaPartidosPendientes = new LinkedList<Partido>();
		
		for (Partido p : listaPartidos) {
			int minPartidosTemporada = p.getTemporada().getMinParticipantesPorPartido();
			if (p.getAsistentes().size() >= minPartidosTemporada) {
				listaPartidosPendientes.add(p);
			}
		}
		
		return listaPartidosPendientes;
	}
	
	
	
	
	
	
	
	

}
