package controlador;

import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import controlador.ControladorRemote;
import jms.confirmaciones.EmisorCola;
import jms.mensajes.PublicadorMensajes;
import model.Alineacion;
import model.Color;
import model.Partido;
import model.Temporada;
import model.User;

public class Controlador {
	
	private static Controlador controlador;
	private ControladorRemote controladorRemote;
	
	public static Controlador getInstancia() {
		if (controlador == null)
			controlador = new Controlador();
		return controlador;
	}
	
	private Controlador() {
		try {
			controladorRemote = (ControladorRemote) new InitialContext().lookup("java:global/DeportesEJB/PracticaRemoto");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
	
	public boolean login(String usuario, String clave) {
		return controladorRemote.login(usuario, clave);
	}
	
	public boolean registrarUsuario(String usuario, String clave, String mail, String telefono) {
		return controladorRemote.registrarUsuario(usuario, clave, mail, telefono);
	}
	
	public void actualizarUsuario(String usuario, String clave, String mail, String telefono) {
		controladorRemote.actualizarUsuario(usuario, clave, mail, telefono);
	}
	
	public void actualizarUsuario(String usuario, String clave, String mail, String telefono, byte[] fotoPerfilArrayBytes) {
		controladorRemote.actualizarUsuario(usuario, clave, mail, telefono, fotoPerfilArrayBytes);
	}
	
	public User getUserByUsuario(String usuario) {
		return controladorRemote.getUserByUsuario(usuario);
	}
	
	public List<String> getTodosLosUsuarios(){
		return controladorRemote.getTodosLosUsuarios();
	}
	
	public List<Partido> getPartidosPendientesDeConfirmarPorUsuario(String usuario) {
		return controladorRemote.getPartidosPendientesDeConfirmarPorUsuario(usuario);
	}
	
	public int registrarPartido(int idTemporada, Date fecha) {
		int idPartido = controladorRemote.registrarPartido(idTemporada, fecha);
		
		/*
		//String tipo = "Normal";
		String texto = "Registrado partido el dia " + fecha.toString();
		try {
			PublicadorApartado.enviar(texto, idTemporada);
		} catch (Exception e) {
			System.out.println("Excepcion al registrar partido");
			e.printStackTrace();
		}*/
		
		String texto = "Registrado partido el dia " + fecha.toString();
		try {
			PublicadorMensajes.enviar(texto, idTemporada);
		} catch (Exception e) {
			System.out.println("Excepcion al registrar partido");
			e.printStackTrace();
		}
		
		return idPartido;
	}
	
	public Partido getPartido(int idPartido) {
		return controladorRemote.getPartido(idPartido);
	}
	
	public boolean confirmarAsistenciaAPartido(String nombreUsuario, int idPartido) {
		boolean confirmada = controladorRemote.confirmarAsistenciaAPartido(nombreUsuario, idPartido);
		
		if (confirmada) {
			try {
				EmisorCola.enviar("Usuario '" + nombreUsuario + "' confirma asistencia a partido con ID " + idPartido);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return confirmada;
	}
	
	public List<User> getUsuariosQueAsistenAPartido(int idPartido){
		return controladorRemote.getUsuariosQueAsistenAPartido(idPartido);
	}
	
	public List<Partido> getTodosLosPartidos(){
		return controladorRemote.getTodosLosPartidos();
	}
	
	public List<Partido> getPartidosEntreFechas(Date f1, Date f2){
		return controladorRemote.getPartidosEntreFechas(f1, f2);
	}
	
	public boolean registrarAlineacion(String nombre, Color color, int idPartido, List<String> listaUsuarios) {
		return controladorRemote.registrarAlineacion(nombre, color, idPartido, listaUsuarios);
	}
	
	public void introducirResultado(int idAlineacion, int resultado) {
		controladorRemote.introducirResultado(idAlineacion, resultado);
	}
	
	public int registrarTemporada(String nombre, String lugarCelebracion, int minParticipantesPorPartido) {
		return controladorRemote.registrarTemporada(nombre, lugarCelebracion, minParticipantesPorPartido);
	}
	
	public Temporada getTemporada(int idTemporada) {
		return controladorRemote.getTemporada(idTemporada);
	}
	
	public void addParticipanteATemporada(int idTemporada, String usuario) {
		controladorRemote.addParticipanteATemporada(idTemporada, usuario);
		
		System.out.println("Usuario " + usuario + ", quiere escuchar notificaciones");
		
		/*
		try {
			SuscriptorApartado sa = new SuscriptorApartado();
			sa.registrarApartado(idTemporada, usuario);
			//SuscriptorApartado.registrarApartado(idTemporada, usuario);
		} catch (Exception e) {
			System.out.println("excepcion al addparticipante");
			e.printStackTrace();
		}*/
		
		/*
		try {
			SubscriptorMensajes.registrarSubscriptor(usuario);
		} catch (Exception e) {
			System.out.println("Error al registrar subscriptor");
			e.printStackTrace();
		}*/
	}
	
	public void removeParticipanteATemporada(int idTemporada, String usuario) {
		controladorRemote.removeParticipanteATemporada(idTemporada, usuario);
		/*
		try {
			SuscriptorApartado.quitarApartado(usuario);
		} catch (NamingException | JMSException e) {
			System.out.println("excepcion al removeparticipante");
			e.printStackTrace();
		}*/
	}
	
	public boolean isUserParticipanteInTemporada(int idTemporada, String usuario) {
		return controladorRemote.isUserParticipanteInTemporada(idTemporada, usuario);
	}

	public List<Partido> getPartidosPendientesDeRegistrarAlineacion(int idTemporada) {
		return controladorRemote.getPartidosPendientesDeRegistrarAlineacion(idTemporada);
	}

	public List<Partido> getPartidosPendientesDeCelebrar(String usuario) {
		return controladorRemote.getPartidosPendientesDeCelebrar(usuario);
	}

	public List<Partido> filtrarPartidos(String nombreTemporada, Date filtroFechaInicio, Date filtroFechaFin, 
			String jugadorParticipante) {
		return controladorRemote.filtrarPartidos(nombreTemporada, filtroFechaInicio, filtroFechaFin, jugadorParticipante);
	}

	public List<Temporada> getTodasLasTemporadas() {
		return controladorRemote.getTodasLasTemporadas();
	}
	
	public List<String> getNombreDeTodasLasTemporadas(){
		return controladorRemote.getNombreDeTodasLasTemporadas();
	}

	public List<String> getParticipantesEnTemporada(String temporadaElegida) {
		return controladorRemote.getParticipantesEnTemporada(temporadaElegida);
	}
	
	public int getIdTemporadaFromNombre(String temporada) {
		return controladorRemote.getIdTemporadaFromNombre(temporada);
	}

	public List<Partido> getPartidosDeTemporada(int idTemporada) {
		return controladorRemote.getPartidosDeTemporada(idTemporada);
	}

	public List<String> getJugadoresDisponiblesEnPartido(int idPartido) {
		return controladorRemote.getJugadoresDisponiblesEnPartido(idPartido);
	}

	public List<Alineacion> getAlineacionesFromPartido(int idPartido) {
		return controladorRemote.getAlineacionesFromPartido(idPartido);
	}

	public boolean validarUsuario(String usuario) {
		return controladorRemote.validarUsuario(usuario);
	}

	public boolean isAdmin(String usuario) {
		return controladorRemote.isAdmin(usuario);
	}

	public List<Integer> getTemporadasDeUsuario(String usuario) {
		return controladorRemote.getTemporadasDeUsuario(usuario);
	}
	
	
}
