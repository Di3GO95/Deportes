package dao.jdbc;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import dao.DAOException;
import dao.UsuarioDAO;
import model.Partido;
import model.User;

public class JDBCUsuarioDAO implements UsuarioDAO {

	private DataSource ds;
	// [...]\glassfish\domains\domain1\config\img\noFotoPerfil.png
	private static final String URL_FOTO_POR_DEFECTO = System.getProperty("user.dir") + "\\img\\noFotoPerfil.png";
	
	public JDBCUsuarioDAO (DataSource ds) {
		this.ds = ds;
	}
	
	@Override
	public boolean crearUsuario(String usuario, String clave, String telefono, String mail) throws DAOException {
		try {
			Connection con = ds.getConnection();
			
			PreparedStatement ps = con.prepareStatement("SELECT * FROM usuario WHERE usuario = ?");
			ps.setString(1, usuario);
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) { // El usuario ya existe en la base de datos
				return false;
			}
			
			File fotoPerfil = new File(URL_FOTO_POR_DEFECTO);
			InputStream fotoPerfilIS = null;
			try {
				fotoPerfilIS = new FileInputStream(fotoPerfil);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			ps = con.prepareStatement("INSERT INTO usuario(usuario, clave, mail, telefono, fotoPerfil) VALUES(?,?,?,?,?)");
			ps.setString(1, usuario);
			ps.setString(2, clave);
			ps.setString(3, mail);
			ps.setString(4, telefono);
			ps.setBinaryStream(5, fotoPerfilIS, fotoPerfilIS.available());
			ps.executeUpdate();
			
			User user = new User();
			user.setUsuario(usuario);
			user.setClave(clave);
			user.setMail(mail);
			user.setTelefono(telefono);
			
			ps.close();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public void actualizarUsuario(String usuario, String clave, String telefono, String mail) throws DAOException {
		try {
			Connection con = ds.getConnection();
			
			Statement stm = con.createStatement();
			
			stm.executeUpdate("UPDATE USUARIO SET clave='" + clave + "' where usuario='" + usuario + "'");
			stm.executeUpdate("UPDATE USUARIO SET telefono='" + telefono + "' where usuario='" + usuario + "'");
			stm.executeUpdate("UPDATE USUARIO SET mail='" + mail + "' where usuario='" + usuario + "'");
			
			stm.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void actualizarUsuario(String usuario, String clave, String telefono, String mail, byte[] fotoPerfilArrayBytes) throws DAOException {
		try {
			Connection con = ds.getConnection();
			
			Statement stm = con.createStatement();
			
			stm.executeUpdate("UPDATE USUARIO SET clave='" + clave + "' where usuario='" + usuario + "'");
			stm.executeUpdate("UPDATE USUARIO SET telefono='" + telefono + "' where usuario='" + usuario + "'");
			stm.executeUpdate("UPDATE USUARIO SET mail='" + mail + "' where usuario='" + usuario + "'");
			stm.close();
			
			InputStream fotoPerfilIS = new ByteArrayInputStream(fotoPerfilArrayBytes);
			
			PreparedStatement ps = con.prepareStatement("UPDATE USUARIO SET fotoPerfil = ? where usuario = ?");
			ps.setBinaryStream(1, fotoPerfilIS, fotoPerfilIS.available());
			ps.setString(2, usuario);
			ps.executeUpdate();
			
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public User findUserByUsuario(String usuario) throws DAOException {
		try {
			Connection con = ds.getConnection();
			
			PreparedStatement ps = con.prepareStatement("SELECT * FROM usuario WHERE usuario = ?");
			ps.setString(1, usuario);
			
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				User user = new User();
				user.setUsuario(usuario);
				user.setClave(rs.getString("clave"));
				user.setTelefono(rs.getString("telefono"));
				user.setMail(rs.getString("mail"));
				user.setFotoPerfil(rs.getBytes("fotoPerfil"));
				user.setAdministrador(rs.getBoolean("administrador"));
				return user;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public List<Partido> getPartidosPendientesDeConfirmarPorUsuario(String usuario) throws DAOException {
		return null;
	}
	
	@Override
	public List<Partido> getPartidosPendientesDeCelebrar(String usuario) throws DAOException {
		return null;
	}

	@Override
	public List<String> getTodosLosUsuarios() throws DAOException {
		try {
			Connection con = ds.getConnection();
			
			PreparedStatement ps = con.prepareStatement("SELECT usuario FROM usuario");
			
			ResultSet rs = ps.executeQuery();
			
			List<String> listaUsuarios = new LinkedList<>();
			while (rs.next()) {
				String user = rs.getString("usuario");
				listaUsuarios.add(user);
			}
			return listaUsuarios;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
	public boolean validarUsuario(String usuario) throws DAOException {
		try {
			Connection con = ds.getConnection();
			
			Statement stm = con.createStatement();
			
			stm.executeUpdate("UPDATE USUARIO SET validado='" + true + "' where usuario='" + usuario + "'");
			
			stm.close();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public boolean isAdmin(String usuario) throws DAOException {
		try {
			Connection con = ds.getConnection();
			
			PreparedStatement ps = con.prepareStatement("SELECT administrador FROM usuario WHERE usuario = ?");
			ps.setString(1, usuario);
			
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				boolean isAdmin = rs.getBoolean("administrador");
				return isAdmin;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	@Override
	public List<Integer> getTemporadasDeUsuario(String usuario) throws DAOException {
		try {
			Connection con = ds.getConnection();
			
			PreparedStatement ps = con.prepareStatement("SELECT temporadas FROM usuario WHERE usuario = ?");
			ps.setString(1, usuario);
			
			ResultSet rs = ps.executeQuery();
			
			List<Integer> listaTemporadas = new LinkedList<>();
			while (rs.next()) {
				int temporada = rs.getInt("id");
				listaTemporadas.add(temporada);
			}
			return listaTemporadas;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
