package dao.jpa;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import org.apache.commons.io.IOUtils;

import dao.DAOException;
import dao.UsuarioDAO;
import model.Partido;
import model.Temporada;
import model.User;

public class JPAUsuarioDAO implements UsuarioDAO {
	private EntityManagerFactory emf;
	// [...]\glassfish\domains\domain1\config\img\noFotoPerfil.png
	private static final String URL_FOTO_POR_DEFECTO = System.getProperty("user.dir") + "\\img\\noFotoPerfil.png";

	public JPAUsuarioDAO(EntityManagerFactory emf) {
		this.emf = emf;
	}

	private byte[] inputStreamToByteArray(InputStream is) {
		byte[] res = null;
		try {
			res = IOUtils.toByteArray(is);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return res;
	}

	private User crearUsuarioInterno(String usuario, String clave, String telefono, String mail) {
		
		User usu = new User();
		usu.setUsuario(usuario);
		usu.setMail(mail);
		usu.setClave(clave);
		usu.setTelefono(telefono);

		
		File fotoPerfil = new File(URL_FOTO_POR_DEFECTO);
		InputStream fotoPerfilIS = null;
		try {
			fotoPerfilIS = new FileInputStream(fotoPerfil);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		byte[] fotoPerfilArrayBytes = inputStreamToByteArray(fotoPerfilIS);
		
		usu.setFotoPerfil(fotoPerfilArrayBytes);
		
		try {
			fotoPerfilIS.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return usu;
	}

	@Override
	public boolean crearUsuario(String usuario, String clave, String telefono, String mail)
			throws DAOException {

		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}

		User usu = crearUsuarioInterno(usuario, clave, telefono, mail);
		if (usu == null)
			return false;

		EntityTransaction tx = em.getTransaction();
		tx.begin();

		em.persist(usu);
		tx.commit();
		em.close();

		return true;
	}

	private void actualizarUsuarioInterno(User user, String usuario, String clave, String telefono, String mail) {
		user.setClave(clave);
		user.setTelefono(telefono);
		user.setMail(mail);
	}

	private void actualizarUsuarioInterno(User user, String usuario, String clave, String telefono, String mail,
			byte[] fotoPerfilArrayBytes) {
		user.setClave(clave);
		user.setTelefono(telefono);
		user.setMail(mail);

		user.setFotoPerfil(fotoPerfilArrayBytes);
	}

	@Override
	public void actualizarUsuario(String usuario, String clave, String telefono, String mail) throws DAOException {
		User user = null;

		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		user = em.find(User.class, usuario);

		actualizarUsuarioInterno(user, usuario, clave, telefono, mail);

		tx.commit();
		em.close();
	}

	@Override
	public void actualizarUsuario(String usuario, String clave, String telefono, String mail, byte[] fotoPerfilIS)
			throws DAOException {
		User user = null;

		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		user = em.find(User.class, usuario);

		actualizarUsuarioInterno(user, usuario, clave, telefono, mail, fotoPerfilIS);

		tx.commit();
		em.close();
	}
	

	@Override
	public User findUserByUsuario(String usuario) throws DAOException {
		User user = null;

		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}

		user = em.find(User.class, usuario);
		em.close();

		return user;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Partido> getPartidosPendientesDeConfirmarPorUsuario(String usuario) throws DAOException {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}

		String jpwl = "SELECT t.partidos " 
					+ "FROM Temporada t JOIN t.usuarios u " 
					+ "WHERE u.usuario = :usuario";

		Query query = em.createQuery(jpwl);
		query.setParameter("usuario", usuario);

		List<Partido> partidosTotales = null;
		partidosTotales = query.getResultList();

		User user = em.find(User.class, usuario);

		em.close();

		List<Partido> partidosPendientes = new LinkedList<Partido>();
		for (Partido p : partidosTotales) {
			if (p != null) {
				if (p.getFecha().after(new Date())) {
					boolean seAñade = true;
					for (Partido p2 : user.getPartidos()) {
						if (p.getId() == p2.getId())
							seAñade = false;
					}
					if (seAñade) {
						partidosPendientes.add(p);
					}
				}

			}
		}
		System.out.println("jpaUsuariodaofinnn");
		return partidosPendientes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Partido> getPartidosPendientesDeCelebrar(String usuario) throws DAOException {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}

		String jpwl = "SELECT u.partidos " 
					+ "FROM User u "
					+ "WHERE u.usuario = :usuario";
		Query query = em.createQuery(jpwl);
		query.setParameter("usuario", usuario);

		List<Partido> partidosConfirmados = null;
		partidosConfirmados = query.getResultList();
		
		em.close();
		
		List<Partido> partidosPendientesDeCelebrar = new LinkedList<>();
		
		for (Partido partido : partidosConfirmados) {
			if (partido != null) {
				if (partido.getFecha().after(new Date()))
					partidosPendientesDeCelebrar.add(partido);
			}
		}
		
		return partidosPendientesDeCelebrar;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getTodosLosUsuarios() throws DAOException {
		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		String jpwl = "SELECT u.usuario " + "FROM User u";
		Query query = em.createQuery(jpwl);

		List<String> listaUsuarios = null;
		listaUsuarios = query.getResultList();

		em.close();

		return listaUsuarios;
	}
	
	
	@Override
	public boolean validarUsuario(String usuario) throws DAOException {
		User user = null;

		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		user = em.find(User.class, usuario);

		user.setValidado(true);

		tx.commit();
		em.close();
		
		return true;
	}

	
	@Override
	public boolean isAdmin(String usuario) throws DAOException {
		User user = null;

		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		user = em.find(User.class, usuario);

		boolean isAdmin = user.isAdministrador();

		tx.commit();
		em.close();
		
		return isAdmin;
	}
	
	@Override
	public List<Integer> getTemporadasDeUsuario(String usuario) throws DAOException {
		User user = null;

		EntityManager em = null;
		synchronized (emf) {
			em = emf.createEntityManager();
		}
		EntityTransaction tx = em.getTransaction();
		tx.begin();

		user = em.find(User.class, usuario);

		tx.commit();
		em.close();
		
		List<Integer> listaTemporadas = new LinkedList<>();
		
		for (Temporada t : user.getTemporadas()) {
			listaTemporadas.add(t.getId());
		}
		
		return listaTemporadas;
	}
	
}
