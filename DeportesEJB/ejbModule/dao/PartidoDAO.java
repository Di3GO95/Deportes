package dao;

import java.util.Date;
import java.util.List;

import model.Alineacion;
import model.Partido;
import model.User;

public interface PartidoDAO {
	int registrarPartido(int idTemporada, Date fecha) throws DAOException;
	
	List<Partido> findPartidoByFechas(Date f1, Date f2) throws DAOException;

	boolean confirmarAsistenciaAPartido(int idPartido, String nombreUsuario) throws DAOException;

	List<Partido> getTodosLosPartidos() throws DAOException;

	List<User> getUsuariosQueAsistenAPartido(int idPartido) throws DAOException;

	Partido getPartido(int idPartido) throws DAOException;

	List<String> getJugadoresDisponiblesEnPartido(int idPartido) throws DAOException;

	List<Alineacion> getAlineacionesFromPartido(int idPartido) throws DAOException;

	List<Partido> filtrarPartidos(String nombreTemporada, Date filtroFechaInicio, Date filtroFechaFin,
			String jugadorParticipante) throws DAOException;

	List<Partido> getPartidosPendientesDeRegistrarAlineacion(int idTemporada) throws DAOException;
}
