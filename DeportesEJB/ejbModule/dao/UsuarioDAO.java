package dao;

import java.util.List;

import model.Partido;
import model.User;

public interface UsuarioDAO {
	
	public boolean crearUsuario(String usuario, String clave, String telefono, String mail) throws DAOException;
	
	public void actualizarUsuario(String usuario, String clave, String telefono, String mail) throws DAOException;
	public void actualizarUsuario(String usuario, String clave, String telefono, String mail, byte[] fotoPerfilArrayBytes) throws DAOException;
	
	// Busca un Usuario por "usuario" (clave primaria). Si no lo encuentra devuelve null
	public User findUserByUsuario(String usuario) throws DAOException;

	public List<Partido> getPartidosPendientesDeConfirmarPorUsuario(String usuario) throws DAOException;

	public List<Partido> getPartidosPendientesDeCelebrar(String usuario) throws DAOException;

	public List<String> getTodosLosUsuarios() throws DAOException;

	public boolean validarUsuario(String usuario) throws DAOException;

	public boolean isAdmin(String usuario) throws DAOException;

	public List<Integer> getTemporadasDeUsuario(String usuario) throws DAOException;
	
}
