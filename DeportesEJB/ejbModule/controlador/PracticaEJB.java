package controlador;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;

import dao.AlineacionDAO;
import dao.DAOException;
import dao.DAOFactoria;
import dao.PartidoDAO;
import dao.TemporadaDAO;
import dao.UsuarioDAO;
import funciones.Utils;
import model.Alineacion;
import model.Color;
import model.Partido;
import model.Temporada;
import model.User;

@Stateful(name="PracticaRemoto")
public class PracticaEJB implements ControladorRemote {
	
	private String usuarioActual = null;
	
	public String getUsuarioActual() {
		return usuarioActual;
	}
	
	public void setUsuarioActual(String usuarioActual) {
		this.usuarioActual = usuarioActual;
	}
	
	@EJB(beanName="Contador")
	private ContadorEJB contador;
	
	@PostConstruct
	public void configuracionInicial() {
		try {
			DAOFactoria.setDAOFactoria(DAOFactoria.JPA);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean login(String usuario, String clave) {
		User usu = getUserByUsuario(usuario);
		if (usu == null || !usu.isValidado() || !usu.getClave().equals(clave))
			return false;
		usuarioActual = usuario;
		System.out.println("Login correctos por sesion: " +contador.siguienteValor());
		return true;
	}
	
	public boolean registrarUsuario(String usuario, String clave, String mail, String telefono) {
		UsuarioDAO usuarioDAO;
		try {
			usuarioDAO = DAOFactoria.getDAOFactoria().getUsuarioDAO();
			boolean registrado = usuarioDAO.crearUsuario(usuario, clave, telefono, mail);
			if (registrado) {
				Utils.enviarCorreo(mail, usuario);
			}
			return registrado;
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void actualizarUsuario(String usuario, String clave, String mail, String telefono) {
		UsuarioDAO usuarioDAO;
		try {
			usuarioDAO = DAOFactoria.getDAOFactoria().getUsuarioDAO();
			usuarioDAO.actualizarUsuario(usuario, clave, telefono, mail);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	public void actualizarUsuario(String usuario, String clave, String mail, String telefono, byte[] fotoPerfilArrayBytes) {
		UsuarioDAO usuarioDAO;
		try {
			usuarioDAO = DAOFactoria.getDAOFactoria().getUsuarioDAO();
			usuarioDAO.actualizarUsuario(usuario, clave, telefono, mail, fotoPerfilArrayBytes);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	public User getUserByUsuario(String usuario) {
		UsuarioDAO usuarioDAO;
		try {
			usuarioDAO = DAOFactoria.getDAOFactoria().getUsuarioDAO();
			return usuarioDAO.findUserByUsuario(usuario);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<String> getTodosLosUsuarios(){
		UsuarioDAO usuarioDAO;
		try {
			usuarioDAO = DAOFactoria.getDAOFactoria().getUsuarioDAO();
			return usuarioDAO.getTodosLosUsuarios();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Partido> getPartidosPendientesDeConfirmarPorUsuario(String usuario) {
		UsuarioDAO usuarioDAO;
		try {
			usuarioDAO = DAOFactoria.getDAOFactoria().getUsuarioDAO();
			return usuarioDAO.getPartidosPendientesDeConfirmarPorUsuario(usuario);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}	
	
	public int registrarPartido(int idTemporada, Date fecha) {
		PartidoDAO partidoDAO;
		try {
			partidoDAO = DAOFactoria.getDAOFactoria().getPartidoDAO();
			return partidoDAO.registrarPartido(idTemporada, fecha);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public Partido getPartido(int idPartido) {
		PartidoDAO partidoDAO;
		try {
			partidoDAO = DAOFactoria.getDAOFactoria().getPartidoDAO();
			return partidoDAO.getPartido(idPartido);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean confirmarAsistenciaAPartido(String nombreUsuario, int idPartido) {
		PartidoDAO partidoDAO;
		try {
			partidoDAO = DAOFactoria.getDAOFactoria().getPartidoDAO();
			return partidoDAO.confirmarAsistenciaAPartido(idPartido, nombreUsuario);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public List<User> getUsuariosQueAsistenAPartido(int idPartido){
		PartidoDAO partidoDAO;
		try {
			partidoDAO = DAOFactoria.getDAOFactoria().getPartidoDAO();
			return partidoDAO.getUsuariosQueAsistenAPartido(idPartido);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Partido> getTodosLosPartidos(){
		PartidoDAO partidoDAO;
		try {
			partidoDAO = DAOFactoria.getDAOFactoria().getPartidoDAO();
			return partidoDAO.getTodosLosPartidos();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<Partido> getPartidosEntreFechas(Date f1, Date f2){
		PartidoDAO partidoDAO;
		try {
			partidoDAO = DAOFactoria.getDAOFactoria().getPartidoDAO();
			return partidoDAO.findPartidoByFechas(f1, f2);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean registrarAlineacion(String nombre, Color color, int idPartido, List<String> listaUsuarios) {
		AlineacionDAO alineacionDAO;
		try {
			alineacionDAO = DAOFactoria.getDAOFactoria().getAlineacionDAO();
			return alineacionDAO.registrarAlineacion(nombre, color, idPartido, listaUsuarios);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void introducirResultado(int idAlineacion, int resultado) {
		AlineacionDAO alineacionDAO;
		try {
			alineacionDAO = DAOFactoria.getDAOFactoria().getAlineacionDAO();
			alineacionDAO.introducirResultado(idAlineacion, resultado);
		} catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	public int registrarTemporada(String nombre, String lugarCelebracion, int minParticipantesPorPartido) {
		TemporadaDAO temporadaDAO;
		try {
			temporadaDAO = DAOFactoria.getDAOFactoria().getTemporadaDAO();
			return temporadaDAO.registrarTemporada(nombre, lugarCelebracion, minParticipantesPorPartido);
		}catch (DAOException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	
	public Temporada getTemporada(int idTemporada) {
		TemporadaDAO temporadaDAO;
		try {
			temporadaDAO = DAOFactoria.getDAOFactoria().getTemporadaDAO();
			return temporadaDAO.getTemporada(idTemporada);
		}catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void addParticipanteATemporada(int idTemporada, String usuario) {
		TemporadaDAO temporadaDAO;
		try {
			temporadaDAO = DAOFactoria.getDAOFactoria().getTemporadaDAO();
			temporadaDAO.addParticipante(idTemporada, usuario);
		}catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	public void removeParticipanteATemporada(int idTemporada, String usuario) {
		TemporadaDAO temporadaDAO;
		try {
			temporadaDAO = DAOFactoria.getDAOFactoria().getTemporadaDAO();
			temporadaDAO.removeParticipante(idTemporada, usuario);
		}catch (DAOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isUserParticipanteInTemporada(int idTemporada, String usuario) {
		TemporadaDAO temporadaDAO;
		try {
			temporadaDAO = DAOFactoria.getDAOFactoria().getTemporadaDAO();
			return temporadaDAO.isUserParticipanteInTemporada(idTemporada, usuario);
		}catch (DAOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public List<Partido> getPartidosPendientesDeRegistrarAlineacion(int idTemporada) {
		PartidoDAO partidoDAO;
		try {
			partidoDAO = DAOFactoria.getDAOFactoria().getPartidoDAO();
			return partidoDAO.getPartidosPendientesDeRegistrarAlineacion(idTemporada);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Partido> getPartidosPendientesDeCelebrar(String usuario) {
		UsuarioDAO usuarioDAO;
		try {
			usuarioDAO = DAOFactoria.getDAOFactoria().getUsuarioDAO();
			return usuarioDAO.getPartidosPendientesDeCelebrar(usuario);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Partido> filtrarPartidos(String nombreTemporada, Date filtroFechaInicio, Date filtroFechaFin, 
			String jugadorParticipante) {
		PartidoDAO partidoDAO;
		
		try {
			partidoDAO = DAOFactoria.getDAOFactoria().getPartidoDAO();
			return partidoDAO.filtrarPartidos(nombreTemporada, filtroFechaInicio, filtroFechaFin, jugadorParticipante);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	public List<Temporada> getTodasLasTemporadas() {
		TemporadaDAO temporadaDAO;
		try {
			temporadaDAO = DAOFactoria.getDAOFactoria().getTemporadaDAO();
			return temporadaDAO.getTodasLasTemporadas();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<String> getNombreDeTodasLasTemporadas(){
		TemporadaDAO temporadaDAO;
		try {
			temporadaDAO = DAOFactoria.getDAOFactoria().getTemporadaDAO();
			return temporadaDAO.getNombreDeTodasLasTemporadas();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<String> getParticipantesEnTemporada(String temporadaElegida) {
		TemporadaDAO temporadaDAO;
		try {
			temporadaDAO = DAOFactoria.getDAOFactoria().getTemporadaDAO();
			return temporadaDAO.getParticipantesEnTemporada(temporadaElegida);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int getIdTemporadaFromNombre(String temporada) {
		TemporadaDAO temporadaDAO;
		try {
			temporadaDAO = DAOFactoria.getDAOFactoria().getTemporadaDAO();
			return temporadaDAO.getIdTemporadaFromNombre(temporada);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public List<Partido> getPartidosDeTemporada(int idTemporada) {
		TemporadaDAO temporadaDAO;
		try {
			temporadaDAO = DAOFactoria.getDAOFactoria().getTemporadaDAO();
			return temporadaDAO.getPartidosDeTemporada(idTemporada);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<String> getJugadoresDisponiblesEnPartido(int idPartido) {
		PartidoDAO partidoDAO;
		try {
			partidoDAO = DAOFactoria.getDAOFactoria().getPartidoDAO();
			return partidoDAO.getJugadoresDisponiblesEnPartido(idPartido);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Alineacion> getAlineacionesFromPartido(int idPartido) {
		PartidoDAO partidoDAO;
		try {
			partidoDAO = DAOFactoria.getDAOFactoria().getPartidoDAO();
			return partidoDAO.getAlineacionesFromPartido(idPartido);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean validarUsuario(String usuario) {
		UsuarioDAO usuarioDAO;
		try {
			usuarioDAO = DAOFactoria.getDAOFactoria().getUsuarioDAO();
			return usuarioDAO.validarUsuario(usuario);
		}catch (DAOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean isAdmin(String usuario) {
		UsuarioDAO usuarioDAO;
		try {
			usuarioDAO = DAOFactoria.getDAOFactoria().getUsuarioDAO();
			return usuarioDAO.isAdmin(usuario);
		}catch (DAOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public List<Integer> getTemporadasDeUsuario(String usuario) {
		UsuarioDAO usuarioDAO;
		try {
			usuarioDAO = DAOFactoria.getDAOFactoria().getUsuarioDAO();
			return usuarioDAO.getTemporadasDeUsuario(usuario);
		}catch (DAOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
