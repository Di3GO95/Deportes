package controlador;

import java.util.Date;
import java.util.List;

import javax.ejb.Remote;

import model.Alineacion;
import model.Color;
import model.Partido;
import model.Temporada;
import model.User;

@Remote
public interface ControladorRemote {
	public boolean login(String usuario, String clave);

	public boolean registrarUsuario(String usuario, String clave, String mail, String telefono);

	public void actualizarUsuario(String usuario, String clave, String mail, String telefono);

	public void actualizarUsuario(String usuario, String clave, String mail, String telefono, byte[] fotoPerfilArrayBytes);

	public User getUserByUsuario(String usuario);

	public List<String> getTodosLosUsuarios();

	public List<Partido> getPartidosPendientesDeConfirmarPorUsuario(String usuario);

	public int registrarPartido(int idTemporada, Date fecha);

	public Partido getPartido(int idPartido);

	public boolean confirmarAsistenciaAPartido(String nombreUsuario, int idPartido);

	public List<User> getUsuariosQueAsistenAPartido(int idPartido);

	public List<Partido> getTodosLosPartidos();

	public List<Partido> getPartidosEntreFechas(Date f1, Date f2);

	public boolean registrarAlineacion(String nombre, Color color, int idPartido, List<String> listaUsuarios);

	public void introducirResultado(int idAlineacion, int resultado);

	public int registrarTemporada(String nombre, String lugarCelebracion, int minParticipantesPorPartido);

	public Temporada getTemporada(int idTemporada);

	public void addParticipanteATemporada(int idTemporada, String usuario);

	public void removeParticipanteATemporada(int idTemporada, String usuario);

	public boolean isUserParticipanteInTemporada(int idTemporada, String usuario);

	public List<Partido> getPartidosPendientesDeRegistrarAlineacion(int idTemporada);

	public List<Partido> getPartidosPendientesDeCelebrar(String usuario);

	public List<Partido> filtrarPartidos(String nombreTemporada, Date filtroFechaInicio, Date filtroFechaFin,
			String jugadorParticipante);

	public List<Temporada> getTodasLasTemporadas();

	public List<String> getNombreDeTodasLasTemporadas();

	public List<String> getParticipantesEnTemporada(String temporadaElegida);

	public int getIdTemporadaFromNombre(String temporada);

	public List<Partido> getPartidosDeTemporada(int idTemporada);

	public List<String> getJugadoresDisponiblesEnPartido(int idPartido);

	public List<Alineacion> getAlineacionesFromPartido(int idPartido);

	public boolean validarUsuario(String usuario);
	
	public boolean isAdmin(String usuario);
	
	public List<Integer> getTemporadasDeUsuario(String usuario);
}
