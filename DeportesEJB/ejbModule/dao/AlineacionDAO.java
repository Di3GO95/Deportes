package dao;

import java.util.List;

import model.Color;

public interface AlineacionDAO {
	boolean registrarAlineacion(String nombre, Color color, int idPartido, List<String> listaUsuarios) throws DAOException;

	void introducirResultado(int idAlineacion, int resultado) throws DAOException;
}
