package dao;

import java.util.List;

import model.Partido;
import model.Temporada;

public interface TemporadaDAO {
	public int registrarTemporada(String nombre, String lugarCelebracion, int minParticipantesPorPartido) throws DAOException;
	
	public boolean addParticipante(int idTemporada, String usuario) throws DAOException;
	
	public boolean removeParticipante(int idTemporada, String usuario) throws DAOException;

	public Temporada getTemporada(int idTemporada) throws DAOException;

	public boolean isUserParticipanteInTemporada(int idTemporada, String usuario) throws DAOException;

	public List<Temporada> getTodasLasTemporadas() throws DAOException;

	public List<String> getParticipantesEnTemporada(String temporadaElegida) throws DAOException;

	public List<String> getNombreDeTodasLasTemporadas() throws DAOException;

	public int getIdTemporadaFromNombre(String temporada) throws DAOException;

	public List<Partido> getPartidosDeTemporada(int idTemporada) throws DAOException;
}
